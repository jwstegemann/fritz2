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
        determineLensablePropertiesStrategy: LenseablePropertiesDeterminer,
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

