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

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val checkOnlyCtor: (KSNode?, KSNode) -> Boolean = { _, node ->
            when (node) {
                is KSClassDeclaration -> node.primaryConstructor?.validate() ?: false
                else -> false
            }
        }

        val lensesAnnotated = resolver.getSymbolsWithAnnotation(Lenses::class.qualifiedName!!)

        val unableToProcess = lensesAnnotated.filterNot { it.validate(checkOnlyCtor) }

        lensesAnnotated.filter { it is KSClassDeclaration && it.validate(checkOnlyCtor) }
            .forEach { it.accept(LensesVisitor(), Unit) }

        return unableToProcess.toList()
    }

    private inner class LensesVisitor : KSVisitorVoid() {

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val packageName = classDeclaration.packageName.asString()

            if (classDeclaration.isDataClass()) {
                val companionObject = extractCompanionObject(classDeclaration)

                if (companionObject != null) {
                    val lensableProps = determineLensableProperties(classDeclaration)
                    if (!assertLensesPropertyNamesAreAvailable(companionObject, lensableProps, classDeclaration)) return

                    if (lensableProps.isNotEmpty()) {
                        generateLensesCode(packageName, classDeclaration, lensableProps, companionObject)
                    } else {
                        logger.warn(
                            "@Lenses annotated data class $classDeclaration found, but it has no public"
                                    + " properties defined in constructor -> can not create any lenses though..."
                        )
                    }
                } else {
                    logger.error(
                        "The companion object for data class $classDeclaration is missing!"
                                + " Please define it to bypass this error."
                    )
                }
            } else {
                logger.error("$classDeclaration is not a data class!")
            }
        }
    }

    private fun KSClassDeclaration.isDataClass() =
        modifiers.contains(Modifier.DATA)

    private fun extractCompanionObject(classDeclaration: KSClassDeclaration) = classDeclaration.declarations
        .filterIsInstance<KSClassDeclaration>()
        .filter { it.isCompanionObject }
        .firstOrNull()

    private fun determineLensableProperties(classDeclaration: KSClassDeclaration): List<KSPropertyDeclaration> {
        val allPublicCtorProps = classDeclaration.primaryConstructor!!.parameters.filter { it.isVal }.map { it.name }
        return classDeclaration.getDeclaredProperties()
            .filter { it.isPublic() && allPublicCtorProps.contains(it.simpleName) }.toList()
    }

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

        return if (neededNamesAlreadyInUse.isNotEmpty()) {
            logger.error(
                "The companion object of $classDeclaration already defines the following functions / properties: "
                        + neededNamesAlreadyInUse.joinToString("; ")
                        + " -> Those names must not be defined! They are used for the automatic lenses generation. "
                        + "Please rename those existing function(s) to bypass this problem!"
            )
            false
        } else true
    }


    private fun generateLensesCode(
        packageName: String,
        classDeclaration: KSClassDeclaration,
        lensableProps: List<KSPropertyDeclaration>,
        compObj: KSClassDeclaration
    ) {
        val fileSpec = FileSpec.builder(
            packageName = packageName,
            fileName = classDeclaration.simpleName.asString() + "Lenses"
        ).apply {
            addFileComment("GENERATED by fritz2 - NEVER CHANGE CONTENT MANUALLY!")
            lensableProps.forEach { prop ->
                val attributeName = MemberName("", prop.simpleName.getShortName())
                val isGeneric = classDeclaration.typeParameters.isNotEmpty()
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
                        .addCode(
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
                        ).build()
                )
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
        }.build()

        fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = false)
    }
}