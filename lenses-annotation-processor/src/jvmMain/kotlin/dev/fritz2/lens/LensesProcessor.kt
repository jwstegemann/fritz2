package dev.fritz2.lens

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.*
import dev.fritz2.core.GetterLens
import dev.fritz2.core.Lens
import dev.fritz2.core.Lenses

/**
 * This Processor generates automatically functions of lenses for all public properties of a data class within the
 * companion object of the data class.
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
            .forEach { it.accept(LensesVisitor(codeGenerator, logger), Unit) }

        return unableToProcess.toList()
    }
}

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

private val determineLensablePropertiesForClasses = DetermineLenseableProperties { classDeclaration ->
    val allPublicCtorProps = classDeclaration.primaryConstructor!!.parameters.filter { it.isVal }.map { it.name }
    classDeclaration.getDeclaredProperties()
        .filter { it.isPublic() && allPublicCtorProps.contains(it.simpleName) }.toList()
}

private val determineLensablePropertiesForInterfaces = DetermineLenseableProperties { classDeclaration ->
    classDeclaration.getDeclaredProperties().filter { it.isPublic() }.toList()
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

private val createGetterLens: FunSpec.Builder.(MemberName, KSClassDeclaration) -> FunSpec.Builder =
    { attributeName, classDeclaration ->
        addCode(
            """ 
            |return %M(
            |    "%L",
            |    %T::%M
            |  )
            """.trimMargin(),
            MemberName("dev.fritz2.core", "lensOf"),
            attributeName,
            classDeclaration.toClassName(),
            attributeName
        )
    }

private class LensesVisitor(private val codeGenerator: CodeGenerator, private val logger: KSPLogger) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val packageName = classDeclaration.packageName.asString()

        when (classDeclaration.isTypeVariant()) {
            TypeVariant.DataClass -> generateLenses(
                classDeclaration,
                packageName,
                determineLensablePropertiesForClasses,
                { Lens::class.asClassName() },
                createLens
            )

            TypeVariant.SealedDataClass -> generateLenses(
                classDeclaration,
                packageName,
                determineLensablePropertiesForClasses,
                { GetterLens::class.asClassName() },
                createGetterLens
            )

            TypeVariant.SealedInterface -> generateLenses(
                classDeclaration,
                packageName,
                determineLensablePropertiesForInterfaces,
                { GetterLens::class.asClassName() },
                createGetterLens
            )

            else -> logger.error("$classDeclaration is not a data class, sealed class or sealed interface!")
        }
    }

    private fun generateLenses(
        classDeclaration: KSClassDeclaration,
        packageName: String,
        determineLensablePropertiesStrategy: DetermineLenseableProperties,
        lensClassName: () -> ClassName,
        addLensCode: FunSpec.Builder.(MemberName, KSClassDeclaration) -> FunSpec.Builder
    ) {
        val companionObject = extractCompanionObject(classDeclaration)

        if (companionObject != null) {
            val lensableProps = determineLensablePropertiesStrategy.determine(classDeclaration)
            if (!assertLensesPropertyNamesAreAvailable(companionObject, lensableProps, classDeclaration)) return

            if (lensableProps.isNotEmpty()) {
                generateLensesCode(
                    codeGenerator,
                    packageName,
                    classDeclaration,
                    lensableProps,
                    companionObject,
                    lensClassName,
                    addLensCode
                )
            } else {
                logger.warn(
                    "@Lenses annotated data class $classDeclaration found, but it has no public"
                            + " properties defined in constructor -> can not create any lenses though..."
                )
            }
        } else {
            logger.error(
                "The companion object for $classDeclaration is missing!"
                        + " Please define it to bypass this error."
            )
        }
    }

    private fun extractCompanionObject(classDeclaration: KSClassDeclaration) = classDeclaration.declarations
        .filterIsInstance<KSClassDeclaration>()
        .filter { it.isCompanionObject }
        .firstOrNull()

    private fun assertLensesPropertyNamesAreAvailable(
        compObj: KSClassDeclaration,
        lensableProps: List<KSPropertyDeclaration>,
        classDeclaration: KSClassDeclaration
    ): Boolean {
        val neededNamesAlreadyInUse = (compObj.getDeclaredFunctions() + compObj.getDeclaredProperties())
            .filter { declaredEntity ->
                lensableProps.any {
                    it.simpleName.getShortName() == declaredEntity.simpleName.getShortName()
                }
            }
            .toList()

        return (neededNamesAlreadyInUse.isEmpty()).also { namesAreAvailable ->
            if (!namesAreAvailable) logger.error(
                "The companion object of $classDeclaration already defines the following functions / properties: "
                        + neededNamesAlreadyInUse.joinToString("; ")
                        + " -> Those names must not be defined! They are used for the automatic lenses generation. "
                        + "Please rename those existing function(s) to bypass this problem!"
            )
        }
    }

    private fun generateLensesCode(
        codeGenerator: CodeGenerator,
        packageName: String,
        classDeclaration: KSClassDeclaration,
        lensableProps: List<KSPropertyDeclaration>,
        compObj: KSClassDeclaration,
        lensClassName: () -> ClassName,
        addLensCode: FunSpec.Builder.(MemberName, KSClassDeclaration) -> FunSpec.Builder
    ) {
        val fileSpec = FileSpec.builder(
            packageName = packageName,
            fileName = classDeclaration.simpleName.asString() + "Lenses"
        ).apply {
            addFileComment("GENERATED by fritz2 - NEVER CHANGE CONTENT MANUALLY!")
            lensableProps.forEach { prop ->
                val attributeName = MemberName("", prop.simpleName.getShortName())
                val isGeneric = classDeclaration.typeParameters.isNotEmpty()
                createLensFactoryCode(
                    prop,
                    lensClassName,
                    isGeneric,
                    classDeclaration,
                    compObj,
                    addLensCode,
                    attributeName
                )
                createLensChainingCode(prop, lensClassName, isGeneric, classDeclaration, attributeName)
            }
        }.build()

        fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = false)
    }

    private fun FileSpec.Builder.createLensFactoryCode(
        prop: KSPropertyDeclaration,
        lensClassName: () -> ClassName,
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
                lensClassName().parameterizedBy(
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

    private fun FileSpec.Builder.createLensChainingCode(
        prop: KSPropertyDeclaration,
        lensClassName: () -> ClassName,
        isGeneric: Boolean,
        classDeclaration: KSClassDeclaration,
        attributeName: MemberName
    ) {
        val parentType = TypeVariableName("PARENT")

        addFunction(
            FunSpec.builder(prop.simpleName.getShortName())
                .addTypeVariable(parentType)
                .receiver(
                    lensClassName().parameterizedBy(
                        parentType,
                        if (isGeneric) classDeclaration.toClassName()
                            .parameterizedBy(classDeclaration.typeParameters.map { it.toTypeVariableName() })
                        else classDeclaration.toClassName()
                    )
                )
                .returns(
                    lensClassName().parameterizedBy(
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
}
