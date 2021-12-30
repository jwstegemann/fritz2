package dev.fritz2.lenses

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.*

/**
 * This Processor generates automatically properties with lenses for all public properties of a data class within the
 * companion object of the data class.
 *
 * Those properties are created during compile process within separate source files as extension properties of the
 * companion object. That's why the user must provide a companion object within the `@Lenses` annotated data class
 * of his own. The processor will detect a missing definition and throw an error. The naming schema is based upon the
 * name of the data class with the appended suffix `Lenses`.
 *
 * We decided to model those generated lenses as properties, as the call to `buildLens` is always only performed once
 * in the lifetime of running program. So the client has not to deal with performance impacts and lifetime creation
 * considerations. (Think of calling a lens within a loop!)
 *
 * The former aspect is the main advantage compared to the other possible solution to model the lens generation as
 * functions!
 *
 * But this decision comes with a drawback:
 * As properties within a companion object cannot handle generic type parameters in Kotlin, there is currently no
 * support for generic type parameters!
 *
 * We believe that the need for a generic data class is rather a corner case and does therefore not outweigh the
 * performance advantage due to automatic lifetime optimization of the property based approach!
 *
 * We also discovered that one of the leading projects dealing with lenses [Arrow Optics](https://arrow-kt.io/docs/optics/)
 * "suffers" from the same problem: [Issue 2552](https://github.com/arrow-kt/arrow/issues/2552)
 * As this bug has been reported quite recently and does not gain much attraction, our considerations are further
 * reinforced.
 *
 * Nevertheless, if you really have some generic data class, you can use this template to create a working lens
 * factory method:
 * ```
 * data class Answer<T>(val value: T, val done: Boolean) {
 *     companion object {
 *         fun <T> value(): Lens<Answer<T>, T> = buildLens("value", { it.value }, { p, v -> p.copy(value = v)})
 *
 *         // also the "not" dependent property need to be modeled as `fun` as the first parameter of
 *         // `Lens` remains generic!
 *         fun <T> done(): Lens<Answer<T>, Boolean> = buildLens("done", { it.done }, { p, v -> p.copy(done = v)})
 *     }
 * }
 * ```
 * But have in mind that every call to those factories will create a new instance of a `Lens`, which is bad for
 * performance and temporarily memory consumptions! So we propose to create those lenses only once during lifetime
 * and store them appropriately.
 */
class LensesProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val lensesAnnotated = resolver.getSymbolsWithAnnotation(Lenses::class.qualifiedName!!)
        val unableToProcess = lensesAnnotated.filterNot { it.validate() }

        lensesAnnotated.filter { it is KSClassDeclaration && it.validate() }
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

    @OptIn(KotlinPoetKspPreview::class)
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
            addComment("GENERATED by fritz2 - NEVER CHANGE CONTENT MANUALLY!")
            lensableProps.forEach { prop ->
                val attributeName = MemberName("", prop.simpleName.getShortName())
                val isGeneric = prop.type.resolve().declaration is KSTypeParameter
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
                            MemberName("dev.fritz2.lenses", "buildLens"),
                            attributeName,
                            attributeName,
                            attributeName
                        ).build()
                )
            }
        }.build()

        fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = false)
    }
}