package dev.fritz2.lens

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeVariableName
import dev.fritz2.core.Lens
import dev.fritz2.core.Lenses
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

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
 * Have a look at the unit tests in `dev.fritz2.lens.LensesProcessorTests` to get examples of the generated code.
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

    private data class MemberName(val packageName: String, val simpleName: String) {
        val fullyQualifiedName: String by lazy { "$packageName.$simpleName" }
    }

    private class LensSourceBuilder(
        val header: String,
        val main: StringBuilder = StringBuilder(),
        val imports: MutableSet<MemberName> = mutableSetOf(
            MemberName("dev.fritz2.core", "Lens"),
        )
    ) {
        fun toSource(): String = buildString {
            appendLine(header)
            imports.forEach { member ->
                appendLine("import ${member.fullyQualifiedName}")
            }
            appendLine()
            append(main.toString())
        }.trimEnd()
    }

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val packageName = classDeclaration.packageName.asString()
        val companionObject = extractCompanionObject(classDeclaration)
        val checker = AggregatingRequirementCheckerDecorator(
            LensesPropertyNamesAreAvailable.forDefaultLensFactories(companionObject),
            CompanionObjectFound(companionObject)
        )

        when (classDeclaration.isTypeVariant()) {
            TypeVariant.DataClass -> generateLenses(
                classDeclaration,
                companionObject,
                packageName,
                determineLensablePropertiesInConstructor,
                checker
                    .with(thereAreLenseableProperties)
                    .with(
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
                determineLensablePropertiesInWholeType,
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
                determineLensablePropertiesInWholeType,
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
        addLensCode: (MemberName, KSClassDeclaration, LensSourceBuilder) -> Unit
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
        addLensCode: (MemberName, KSClassDeclaration, LensSourceBuilder) -> Unit
    ) {
        val result = LensSourceBuilder(
            buildString {
                appendLine("// GENERATED by fritz2 - NEVER CHANGE CONTENT MANUALLY!")
                //lensableProps.firstOrNull()?.let { prop -> appendLine("package ${prop.packageName.asString()}") }
                appendLine("package $packageName")
            }
        )

        result.main.apply {
            val isGeneric = classDeclaration.typeParameters.isNotEmpty()
            lensableProps.forEach { prop ->
                val attributeName = MemberName(prop.packageName.asString(), prop.simpleName.getShortName())
                createLensFactoryCode(result, prop, isGeneric, classDeclaration, compObj, addLensCode, attributeName)
                appendLine()
                appendLensChainingCode(result, prop, classDeclaration, attributeName)
                appendLine()
            }
            when (classDeclaration.isTypeVariant()) {
                TypeVariant.SealedInterface, TypeVariant.SealedDataClass -> {
                    createUpTypingLensFactoryCodesForSealedBase(isGeneric, classDeclaration, compObj, result)
                }

                TypeVariant.DataClass -> {
                    createDownTypingLensFactoryCodeForSealedChild(isGeneric, classDeclaration, compObj)
                }

                else -> Unit
            }
        }

        result.imports.removeIf { it.packageName == packageName }

        val fileName = classDeclaration.simpleName.asString() + "Lenses"
        writeTo(
            codeGenerator = codeGenerator,
            Dependencies(false),
            packageName,
            fileName,
            result.toSource()
        )
    }

    private fun writeTo(
        codeGenerator: CodeGenerator,
        dependencies: Dependencies,
        packageName: String,
        fileName: String,
        generatedCode: String
    ) {
        val file = codeGenerator.createNewFile(dependencies, packageName, fileName)
        // Don't use writeTo(file) because that tries to handle directories under the hood
        val writer = OutputStreamWriter(file, StandardCharsets.UTF_8)
        writer.write(generatedCode)
        writer.close()
    }


    private fun createLensFactoryCode(
        lensSourceBuilder: LensSourceBuilder,
        prop: KSPropertyDeclaration,
        isGeneric: Boolean,
        classDeclaration: KSClassDeclaration,
        compObj: KSClassDeclaration,
        addLensCode: (MemberName, KSClassDeclaration, LensSourceBuilder) -> Unit,
        attributeName: MemberName
    ) {
        lensSourceBuilder.main.apply {
            lensSourceBuilder.imports.add(
                MemberName("dev.fritz2.core", "lensOf"),
            )
            val member = prop.type.resolve().declaration.let { declaration ->
                MemberName(
                    declaration.packageName.asString(),
                    declaration.simpleName.asString(),
                )
            }
            lensSourceBuilder.imports.add(member)
            append("public fun ")
            append(genericTagOf(classDeclaration).let { tag -> if (tag.isNotEmpty()) "$tag " else tag })
            append(classDeclaration.simpleName.getShortName())
            append(".")
            append(compObj.asType(emptyList()).toClassName().simpleName)
            append(".")
            append(prop.simpleName.getShortName())
            append("(): ")
            append(Lens::class.java.simpleName)
            append("<")
            append(genericClassNameOf(classDeclaration))
            append(", ")
            append(prop.type.resolve().toString())
            //append(genericTagOf(prop))
            append(">")
            append(" = ")
            addLensCode(attributeName, classDeclaration, lensSourceBuilder)
        }
    }

    private fun createUpTypingLensFactoryCodesForSealedBase(
        isGeneric: Boolean,
        classDeclaration: KSClassDeclaration,
        compObj: KSClassDeclaration,
        lensSourceBuilder: LensSourceBuilder
    ) {
        lensSourceBuilder.imports.add(
            MemberName("dev.fritz2.core", "lensForUpcasting")
        )
        val children = classDeclaration.getSealedSubclasses()
        lensSourceBuilder.main.apply {
            children.forEach { child ->
                append("public fun ${classDeclaration.toClassName().simpleName}.Companion.")
                append("${child.simpleName.getShortName().lowerCamelCased()}(): ")
                append("Lens<${classDeclaration.toClassName().simpleName}, ${child.simpleName.getShortName()}> ")
                append("= lensForUpcasting<")
                appendLine("${classDeclaration.toClassName().simpleName}, ${child.simpleName.getShortName()}>()")
                appendLine()
            }
        }
    }

    private fun StringBuilder.createDownTypingLensFactoryCodeForSealedChild(
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
            /*
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

             */
        }
    }

    private fun appendLensChainingCode(
        lensSourceBuilder: LensSourceBuilder,
        prop: KSPropertyDeclaration,
        classDeclaration: KSClassDeclaration,
        attributeName: MemberName
    ) {
        val destTypeName = prop.type.resolve().toString()
        lensSourceBuilder.main.apply {
            append("public fun <PARENT${genericPartOf(classDeclaration)}> ")
            append("Lens<PARENT, ${genericClassNameOf(classDeclaration)}>")
            append(".${attributeName.simpleName}(): Lens<PARENT, $destTypeName>")
            append(" = this + ${classDeclaration.simpleName.getShortName()}.${attributeName.simpleName}()")
            appendLine()
        }
    }

    private val createLens: (MemberName, KSClassDeclaration, LensSourceBuilder) -> Unit = { attributeName, _, builder ->
        builder.main.apply {
            appendLine("lensOf(")
            appendLine("    \"${attributeName.simpleName}\",")
            appendLine("    { it.${attributeName.simpleName} },")
            appendLine("    { p, v -> p.copy(${attributeName.simpleName} = v)}")
            appendLine(")")
        }
    }

    private val createDelegatingLens: (MemberName, KSClassDeclaration, LensSourceBuilder) -> Unit =
        { attributeName, classDeclaration, builder ->
            val children = classDeclaration.getSealedSubclasses()
            builder.main.apply {
                appendLine("lensOf(")
                appendLine("    \"${attributeName.simpleName}\",")
                appendLine("    { parent ->")
                appendLine("        when(parent) {")
                children.forEach { child ->
                    append("            is ${child.toClassName().simpleName}")
                    append(" -> parent.${attributeName.simpleName}")
                    appendLine()
                }
                appendLine("        }")
                appendLine("    },")
                appendLine("    { parent, value ->")
                appendLine("        when(parent) {")
                children.forEach { child ->
                    append("            is ${child.toClassName().simpleName}")
                    append(" -> parent.copy(${attributeName.simpleName} = value)")
                    appendLine()
                }
                appendLine("        }")
                appendLine("    }")
                appendLine(")")
            }
        }

    private fun genericClassNameOf(classDeclaration: KSClassDeclaration): String =
        "${classDeclaration.simpleName.getShortName()}${genericTagOf(classDeclaration)}"

    private fun genericPartOf(classDeclaration: KSClassDeclaration): String =
        if (classDeclaration.typeParameters.isNotEmpty()) classDeclaration.typeParameters
            .map { it.toTypeVariableName() }
            .joinToString(separator = ", ", prefix = ", ")
        else ""

    private fun genericTagOf(classDeclaration: KSClassDeclaration): String =
        if (classDeclaration.typeParameters.isNotEmpty()) classDeclaration.typeParameters
            .map { it.toTypeVariableName() }
            .joinToString(separator = ", ", prefix = "<", postfix = ">")
        else ""
}

