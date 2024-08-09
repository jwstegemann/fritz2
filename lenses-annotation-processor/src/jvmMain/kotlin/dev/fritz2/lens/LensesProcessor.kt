package dev.fritz2.lens

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.*
import dev.fritz2.core.Lens
import dev.fritz2.core.Lenses
import dev.fritz2.lens.CheckResult.Companion.collectMessages

/**
 * This Processor generates automatically functions of lenses for all public properties of a data class, a sealed class
 * or a sealed interface within theirs the companion objects.
 *
 * Those functions are created during compile process within separate source files as extension functions of the
 * companion object. That's why the user must provide a companion object within the `@Lenses` annotated data class
 * of his own. The processor will detect a missing definition and throw an error. The naming schema  of the generated
 * file is based upon the name of the data class with the appended suffix `Lenses`.
 *
 * We decided to model those generated lenses as functions, as the call to `lensOf` has only a small impact to the
 * overall rendering performance compared to other aspects but enables the support for *generic* data classes.
 * So if a client suffers from a bad performance *because of this approach*, feel free to manually implement a lens
 * with better performance.
 *
 * Have a look at the unit tests in [dev.fritz2.lens.LensesProcessorTests] to get examples of the generated code.
 */
class LensesProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    private val checkRelevantPorpertiesAreValid: (KSNode?, KSNode) -> Boolean = { _, node ->
        when (node) {
            is KSClassDeclaration -> {
                when (node.classKind) {
                    ClassKind.INTERFACE -> {
                        if (node.modifiers.contains(Modifier.SEALED)) {
                            node.getAllProperties().all { it.validate() }
                        } else {
                            false
                        }
                    }

                    ClassKind.CLASS -> node.primaryConstructor?.validate() ?: false
                    else -> false
                }
            }

            else -> false
        }
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val lensesAnnotated = resolver.getSymbolsWithAnnotation(Lenses::class.qualifiedName!!)

        val unableToProcess = lensesAnnotated.filterNot { it.validate(checkRelevantPorpertiesAreValid) }

        lensesAnnotated
            .filter { it is KSClassDeclaration && it.validate(checkRelevantPorpertiesAreValid) }
            .forEach { it.accept(LensesVisitor(codeGenerator, logger, resolver), Unit) }

        return unableToProcess.toList()
    }
}

private class LensesVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val resolver: Resolver
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val packageName = classDeclaration.packageName.asString()
        val companionObject = extractCompanionObject(classDeclaration)
        val checker = AggregatingRequirementCheckerDecorator(
            thereAreLenseableProperties,
            LensesPropertyNamesAreAvailable.forDefaultLensFactories(companionObject),
            CompanionObjectFound(companionObject)
        )

        when (classDeclaration.isTypeVariant()) {
            TypeVariant.DataClass -> generateLenses(
                classDeclaration,
                companionObject,
                packageName,
                determineLensablePropertiesInConstructor,
                checker.with(
                    LensesPropertyNamesAreAvailable.forDownTypingLensesInSealedChild(
                        classDeclaration,
                        resolver,
                        companionObject
                    )
                ),
                createLens
            )

            TypeVariant.SealedDataClass -> generateLenses(
                classDeclaration,
                companionObject,
                packageName,
                determineLensablePropertiesInBody,
                checker
                    .with(allImplementationsOfSealedTypeAreDataClasses)
                    .with(thereIsAtLeastOneImplementationOfSealedType)
                    .with(
                        LensesPropertyNamesAreAvailable.forUpTypingLensesInSealedBase(
                            classDeclaration,
                            companionObject
                        )
                    ),
                createDelegatingLens
            )

            TypeVariant.SealedInterface -> generateLenses(
                classDeclaration,
                companionObject,
                packageName,
                determineLensablePropertiesInBody,
                checker
                    .with(allImplementationsOfSealedTypeAreDataClasses)
                    .with(thereIsAtLeastOneImplementationOfSealedType)
                    .with(
                        LensesPropertyNamesAreAvailable.forUpTypingLensesInSealedBase(
                            classDeclaration,
                            companionObject
                        )
                    ),
                createDelegatingLens
            )

            else -> logger.error("$classDeclaration is not a data class, sealed class or sealed interface!")
        }
    }

    private fun extractCompanionObject(classDeclaration: KSClassDeclaration) = classDeclaration.declarations
        .filterIsInstance<KSClassDeclaration>()
        .filter { it.isCompanionObject }
        .firstOrNull()

    private fun generateLenses(
        classDeclaration: KSClassDeclaration,
        companionObject: KSClassDeclaration?,
        packageName: String,
        determineLensablePropertiesStrategy: DetermineLenseableProperties,
        checker: RequirementChecker,
        addLensCode: FunSpec.Builder.(MemberName, KSClassDeclaration) -> FunSpec.Builder
    ) {
        val lensableProps = determineLensablePropertiesStrategy.determine(classDeclaration)

        if (allRequirementsAreFulfilled(checker, classDeclaration, lensableProps)) {
            companionObject?.let { companion ->
                generateLensesCode(
                    codeGenerator,
                    packageName,
                    classDeclaration,
                    lensableProps,
                    companion,
                    addLensCode
                )
            }
        }
    }

    private fun allRequirementsAreFulfilled(
        checker: RequirementChecker,
        classDeclaration: KSClassDeclaration,
        lensableProps: List<KSPropertyDeclaration>
    ): Boolean = when (val result = checker.checked(classDeclaration, lensableProps)) {
        is CheckResult.Failure -> {
            result.errors.forEach(logger::log)
            false
        }

        is CheckResult.Warning -> {
            result.warnings.forEach(logger::log)
            false
        }

        is CheckResult.Success -> true
    }

    private fun generateLensesCode(
        codeGenerator: CodeGenerator,
        packageName: String,
        classDeclaration: KSClassDeclaration,
        lensableProps: List<KSPropertyDeclaration>,
        compObj: KSClassDeclaration,
        addLensCode: FunSpec.Builder.(MemberName, KSClassDeclaration) -> FunSpec.Builder
    ) {
        val fileSpec = FileSpec.builder(
            packageName = packageName,
            fileName = classDeclaration.simpleName.asString() + "Lenses"
        ).apply {
            addFileComment("GENERATED by fritz2 - NEVER CHANGE CONTENT MANUALLY!")
            val isGeneric = classDeclaration.typeParameters.isNotEmpty()
            lensableProps.forEach { prop ->
                val attributeName = MemberName("", prop.simpleName.getShortName())
                createLensFactoryCode(prop, isGeneric, classDeclaration, compObj, addLensCode, attributeName)
                createLensChainingCode(prop, isGeneric, classDeclaration, attributeName)
            }
            when (classDeclaration.isTypeVariant()) {
                TypeVariant.SealedInterface, TypeVariant.SealedDataClass -> {
                    createUpTypingLensFactoryCodesForSealedBase(isGeneric, classDeclaration, compObj)
                }

                TypeVariant.DataClass -> {
                    createDownTypingLensFactoryCodeForSealedChild(isGeneric, classDeclaration, compObj)
                }

                else -> Unit
            }
        }.build()

        fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = false)
    }

    private fun FileSpec.Builder.createLensFactoryCode(
        prop: KSPropertyDeclaration,
        isGeneric: Boolean,
        classDeclaration: KSClassDeclaration,
        compObj: KSClassDeclaration,
        addLensCode: FunSpec.Builder.(MemberName, KSClassDeclaration) -> FunSpec.Builder,
        attributeName: MemberName
    ) {
        addFunction(
            FunSpec.builder(
                prop.simpleName.getShortName()
            ).returns(
                Lens::class.asClassName().parameterizedBy(
                    if (isGeneric) classDeclaration.toClassName()
                        .parameterizedBy(classDeclaration.typeParameters.map { it.toTypeVariableName() })
                    else classDeclaration.toClassName(),
                    prop.type.toTypeName(classDeclaration.typeParameters.toTypeParameterResolver())
                )
            ).addTypeVariables(classDeclaration.typeParameters.map { it.toTypeVariableName() })
                .receiver(compObj.asType(emptyList()).toTypeName())
                .addLensCode(attributeName, classDeclaration)
                .build()
        )
    }

    private fun FileSpec.Builder.createUpTypingLensFactoryCodesForSealedBase(
        isGeneric: Boolean,
        classDeclaration: KSClassDeclaration,
        compObj: KSClassDeclaration,
    ) {
        val children = classDeclaration.getSealedSubclasses()
        children.forEach { child ->
            addFunction(
                FunSpec.builder(
                    child.simpleName.getShortName().lowerCamelCased()
                ).returns(
                    Lens::class.asClassName().parameterizedBy(
                        if (isGeneric) classDeclaration.toClassName()
                            .parameterizedBy(classDeclaration.typeParameters.map { it.toTypeVariableName() })
                        else classDeclaration.toClassName(),
                        child.toClassName()
                    )
                ).addTypeVariables(classDeclaration.typeParameters.map { it.toTypeVariableName() })
                    .receiver(compObj.asType(emptyList()).toTypeName())
                    .apply {
                        addCode(
                            """ 
                            |return %M(
                            |    "",
                            |    { it as %T },
                            |    { _, v -> v }
                            |)
                            """.trimMargin(),
                            MemberName("dev.fritz2.core", "lensOf"),
                            child.toClassName(),
                        )
                    }
                    .build()
            )
        }
    }

    private fun FileSpec.Builder.createDownTypingLensFactoryCodeForSealedChild(
        isGeneric: Boolean,
        classDeclaration: KSClassDeclaration,
        compObj: KSClassDeclaration,
    ) {
        val parents = classDeclaration.superTypes
            .map { "${classDeclaration.packageName.asString()}.$it" }
            .mapNotNull { name -> resolver.getClassDeclarationByName(name) }
            .filter { it.isTypeVariant() in setOf(TypeVariant.SealedDataClass, TypeVariant.SealedInterface) }
            .toList()

        parents.forEach { parent ->
            addFunction(
                FunSpec.builder(
                    parent.simpleName.getShortName().lowerCamelCased()
                ).returns(
                    Lens::class.asClassName().parameterizedBy(
                        if (isGeneric) classDeclaration.toClassName()
                            .parameterizedBy(classDeclaration.typeParameters.map { it.toTypeVariableName() })
                        else classDeclaration.toClassName(),
                        parent.toClassName()
                    )
                ).addTypeVariables(classDeclaration.typeParameters.map { it.toTypeVariableName() })
                    .receiver(compObj.asType(emptyList()).toTypeName())
                    .apply {
                        addCode(
                            """ 
                            |return %M(
                            |    "",
                            |    { it },
                            |    { _, v -> v as %T }
                            |)
                            """.trimMargin(),
                            MemberName("dev.fritz2.core", "lensOf"),
                            classDeclaration.toClassName(),
                        )
                    }
                    .build()
            )
        }
    }

    private fun FileSpec.Builder.createLensChainingCode(
        prop: KSPropertyDeclaration,
        isGeneric: Boolean,
        classDeclaration: KSClassDeclaration,
        attributeName: MemberName
    ) {
        val parentType = TypeVariableName("PARENT")

        addFunction(
            FunSpec.builder(prop.simpleName.getShortName())
                .addTypeVariable(parentType)
                .receiver(
                    Lens::class.asClassName().parameterizedBy(
                        parentType,
                        if (isGeneric) classDeclaration.toClassName()
                            .parameterizedBy(classDeclaration.typeParameters.map { it.toTypeVariableName() })
                        else classDeclaration.toClassName()
                    )
                )
                .returns(
                    Lens::class.asClassName().parameterizedBy(
                        parentType,
                        prop.type.toTypeName(classDeclaration.typeParameters.toTypeParameterResolver())
                    )
                ).addTypeVariables(classDeclaration.typeParameters.map { it.toTypeVariableName() })
                .addCode(
                    "return this + %T.%L()",
                    classDeclaration.toClassName(),
                    attributeName
                )
                .build()
        )
    }

    private val createLens: FunSpec.Builder.(MemberName, KSClassDeclaration) -> FunSpec.Builder = { attributeName, _ ->
        addCode(
            """ 
        |return %M(
        |    "%L",
        |    { it.%M },
        |    { p, v -> p.copy(%M = v)}
        |  )
        """.trimMargin(),
            MemberName("dev.fritz2.core", "lensOf"),
            attributeName,
            attributeName,
            attributeName
        )
    }

    private val createDelegatingLens: FunSpec.Builder.(MemberName, KSClassDeclaration) -> FunSpec.Builder =
        { attributeName, classDeclaration ->
            val children = classDeclaration.getSealedSubclasses()

            addCode(
                """ 
            |return %M(
            |    "%L",
            |    { parent ->
            |        when(parent) {
            """.trimMargin(),
                MemberName("dev.fritz2.core", "lensOf"),
                attributeName
            )
            addStatement("")
            children.forEach { child ->
                addStatement(
                    """ 
                |            is %T -> parent.%M
                """.trimMargin(),
                    child.toClassName(),
                    attributeName
                )
            }
            addCode(
                """
            |        }
            |    },
            |    { parent, value ->
            |        when(parent) {
            """.trimMargin(),
            )
            addStatement("")
            children.forEach { child ->
                addStatement(
                    """ 
                |            is %T -> parent.copy(%M = value)
                """.trimMargin(),
                    child.toClassName(),
                    attributeName
                )
            }
            addCode(
                """
            |        }
            |    }
            |)
            """.trimMargin(),
            )
        }
}

private fun String.lowerCamelCased() = "${first().lowercase()}${drop(1)}"

private enum class TypeVariant {
    Invalid, DataClass, SealedDataClass, SealedInterface
}

private fun KSClassDeclaration.isTypeVariant(): TypeVariant = when (classKind) {
    ClassKind.INTERFACE ->
        if (modifiers.contains(Modifier.SEALED)) TypeVariant.SealedInterface
        else TypeVariant.Invalid

    ClassKind.CLASS ->
        if (modifiers.contains(Modifier.DATA)) TypeVariant.DataClass
        else if (modifiers.contains(Modifier.SEALED)) TypeVariant.SealedDataClass
        else TypeVariant.Invalid

    else -> TypeVariant.Invalid
}

private fun interface DetermineLenseableProperties {
    fun determine(classDeclaration: KSClassDeclaration): List<KSPropertyDeclaration>
}

private val determineLensablePropertiesInConstructor = DetermineLenseableProperties { classDeclaration ->
    val allPublicCtorProps = classDeclaration.primaryConstructor!!.parameters.filter { it.isVal }.map { it.name }
    classDeclaration.getDeclaredProperties()
        .filter { it.isPublic() && allPublicCtorProps.contains(it.simpleName) }.toList()
}

private val determineLensablePropertiesInBody = DetermineLenseableProperties { classDeclaration ->
    classDeclaration.getDeclaredProperties().filter { it.isPublic() }.toList()
}

enum class Severity {
    Warning, Error
}

data class Message(val severity: Severity, val message: String)

fun KSPLogger.log(message: Message) {
    when (message.severity) {
        Severity.Warning -> this.warn(message.message)
        Severity.Error -> this.error(message.message)
    }
}

private sealed interface CheckResult {
    object Success : CheckResult
    data class Warning(val warnings: List<Message>) : CheckResult {
        constructor(warning: Message) : this(listOf(warning))
    }

    data class Failure(val errors: List<Message>) : CheckResult {
        constructor(error: Message) : this(listOf(error))
    }

    companion object {
        fun collectMessages(vararg results: List<CheckResult>): List<Message> =
            results.flatMap { chunk ->
                chunk.flatMap { result ->
                    when (result) {
                        is Warning -> result.warnings
                        is Failure -> result.errors
                        else -> emptyList()
                    }
                }
            }
    }
}

private fun interface RequirementChecker {
    fun checked(
        classDeclaration: KSClassDeclaration,
        lensableProps: List<KSPropertyDeclaration>
    ): CheckResult
}

private class AggregatingRequirementCheckerDecorator(private val checkers: List<RequirementChecker>) :
    RequirementChecker {

    constructor(vararg checker: RequirementChecker) : this(listOf(*checker))

    fun with(checker: RequirementChecker): AggregatingRequirementCheckerDecorator =
        AggregatingRequirementCheckerDecorator(checkers + checker)

    override fun checked(
        classDeclaration: KSClassDeclaration,
        lensableProps: List<KSPropertyDeclaration>
    ): CheckResult {
        val (errors, warnings) = checkers
            .map { checker -> checker.checked(classDeclaration, lensableProps) }
            .filterNot { it is CheckResult.Success }
            .partition { it is CheckResult.Failure }
        return when {
            errors.isEmpty() && warnings.isNotEmpty() -> CheckResult.Warning(collectMessages(warnings))
            errors.isNotEmpty() -> CheckResult.Failure(collectMessages(warnings, errors))
            else -> CheckResult.Success
        }
    }
}

@JvmInline
private value class DetermineReservedNames(val get: (List<KSPropertyDeclaration>) -> Set<String>)

private data class LensesPropertyNamesAreAvailable(
    private val determineReservedNames: DetermineReservedNames,
    private val companionObject: KSClassDeclaration?
) : RequirementChecker {
    override fun checked(
        classDeclaration: KSClassDeclaration,
        lensableProps: List<KSPropertyDeclaration>
    ): CheckResult {
        val usedNames = companionObject?.let { companion ->
            (companion.getDeclaredFunctions() + companion.getDeclaredProperties()).map { it.simpleName.getShortName() }
        }?.toSet() ?: emptySet()

        val conflictingNames = usedNames intersect determineReservedNames.get(lensableProps)

        return if (conflictingNames.isEmpty()) {
            CheckResult.Success
        } else {
            CheckResult.Failure(
                Message(
                    Severity.Error,
                    buildString {
                        append(
                            "The companion object of $classDeclaration already defines the following" +
                                    " functions / properties: "
                        )
                        append(" {${conflictingNames.joinToString("; ")}} ")
                        append(
                            " -> Those names must not be defined! They are used for the automatic lenses generation. "
                                    + "Please rename those existing function(s) to bypass this problem!"
                        )
                    }
                )
            )
        }
    }

    companion object {
        fun forDefaultLensFactories(companionObject: KSClassDeclaration?): LensesPropertyNamesAreAvailable =
            LensesPropertyNamesAreAvailable(
                DetermineReservedNames { lensableProps -> lensableProps.map { it.simpleName.getShortName() }.toSet() },
                companionObject
            )

        fun forDownTypingLensesInSealedChild(
            classDeclaration: KSClassDeclaration,
            resolver: Resolver,
            companionObject: KSClassDeclaration?
        ): LensesPropertyNamesAreAvailable =
            LensesPropertyNamesAreAvailable(
                DetermineReservedNames { _ ->
                    val res = classDeclaration.superTypes
                        .map { "${classDeclaration.packageName.asString()}.$it" }
                        .mapNotNull { name -> resolver.getClassDeclarationByName(name) }
                        .filter {
                            it.isTypeVariant() in setOf(
                                TypeVariant.SealedDataClass,
                                TypeVariant.SealedInterface
                            )
                        }
                        .map { it.simpleName.getShortName().lowerCamelCased() }
                        .toSet()
                    res
                },
                companionObject
            )

        fun forUpTypingLensesInSealedBase(
            classDeclaration: KSClassDeclaration,
            companionObject: KSClassDeclaration?
        ): LensesPropertyNamesAreAvailable =
            LensesPropertyNamesAreAvailable(
                DetermineReservedNames {
                    val res = classDeclaration.getSealedSubclasses()
                        .map { it.simpleName.getShortName().lowerCamelCased() }
                        .toSet()
                    res
                },
                companionObject
            )
    }
}

private val thereAreLenseableProperties = RequirementChecker { classDeclaration, lensableProps ->
    if (lensableProps.isEmpty()) {
        CheckResult.Warning(
            Message(
                Severity.Warning,
                buildString {
                    append("@Lenses annotated data class, sealed class or sealed interface $classDeclaration found,")
                    if (classDeclaration.isTypeVariant() == TypeVariant.DataClass)
                        append(" but it has no public properties defined in constructor ")
                    else
                        append(" but it has no public properties defined in ist body ")
                    append("-> can not create any lenses though...")
                }
            )
        )
    } else {
        CheckResult.Success
    }
}

private val allImplementationsOfSealedTypeAreDataClasses =
    RequirementChecker { classDeclaration, _ ->
        val noneDataClassChildren =
            classDeclaration.getSealedSubclasses().filter { it.isTypeVariant() != TypeVariant.DataClass }.toList()
        if (noneDataClassChildren.isEmpty()) CheckResult.Success else CheckResult.Failure(
            Message(
                Severity.Error,
                buildString {
                    appendLine(
                        "All implementing children of '$classDeclaration' need to be data classes." +
                                " The following children are no data classes:"
                    )
                    noneDataClassChildren.forEach { appendLine("  - ${it.qualifiedName}") }
                }
            )
        )
    }

private val thereIsAtLeastOneImplementationOfSealedType =
    RequirementChecker { classDeclaration, _ ->
        if (classDeclaration.getSealedSubclasses().toList().isNotEmpty()) CheckResult.Success
        else CheckResult.Warning(
            Message(
                Severity.Warning,
                "There are no subclasses for '$classDeclaration'. -> can not create any lenses though..."
            )
        )
    }

private class CompanionObjectFound(private val companionObject: KSClassDeclaration?) : RequirementChecker {
    override fun checked(
        classDeclaration: KSClassDeclaration,
        lensableProps: List<KSPropertyDeclaration>
    ): CheckResult =
        if (companionObject == null) CheckResult.Failure(
            Message(
                Severity.Error,
                "The companion object for $classDeclaration is missing!  Please define it to bypass this error."
            )
        )
        else CheckResult.Success
}
