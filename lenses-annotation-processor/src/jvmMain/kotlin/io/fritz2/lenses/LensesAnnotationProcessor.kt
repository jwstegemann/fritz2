package io.fritz2.lenses

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@SupportedAnnotationTypes("io.fritz2.lenses.Lenses")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class LensesAnnotationProcessor() : AbstractProcessor() {
    private final val kaptKotlinGeneratedDir = "src/commonGenerated/kotlin"

    override fun getSupportedAnnotationTypes(): MutableSet<String> = mutableSetOf(Lenses::class.java.name)

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {

        val annotatedElements = roundEnv?.getElementsAnnotatedWith(Lenses::class.java)
        if (annotatedElements == null || annotatedElements.isEmpty()) return false

        annotatedElements.groupBy {
            processingEnv.elementUtils.getPackageOf(it).qualifiedName.toString()
        }.mapValues { (pack, elements) ->
            elements.filter { it.kind == ElementKind.CLASS }
        }.forEach { (pack, classes) ->
            val fileSpecBuilder = FileSpec.builder(pack, "Lenses")
                .addComment("GENERATED by fritz2")
                .addComment(" - NEVER CHANGE CONTENT MANUALLY!")
                .addImport("io.fritz2.lenses", "buildLens")

            val lensesObject = TypeSpec.objectBuilder("Lenses")
            classes.forEach {
                lensesObject.addType(handleDataClass(it))
            }
            fileSpecBuilder.addType(lensesObject.build())

            fileSpecBuilder
                .build()
                .writeTo(
                    File(kaptKotlinGeneratedDir).apply {
                        parentFile.mkdirs()
                    }
                )

        }

        return true
    }

    private fun handleDataClass(classElement: Element): TypeSpec {
        val classSpec = TypeSpec.objectBuilder(classElement.simpleName.toString())

        classElement.enclosedElements.filter { it.kind == ElementKind.FIELD }.forEach {
            classSpec.addProperty(handleField(classElement, it))
        }


        return classSpec.build()
    }

    private fun stringToClassName(name: String): TypeName {
        val hasParam = name.contains('<')
        val typeString = if (hasParam) name.substringBefore('<') else name

        val typeList = typeString.split(".")
        val classString = typeList.last()
        val packageString = typeList.dropLast(1).joinToString(".")
        val className = ClassName(packageString, classString)

        return if (hasParam) {
            //FIXME: handle more type parameters and nested ones
            val parameterString = name.substringAfter('<').substringBefore('>')
            className.plusParameter(stringToClassName(parameterString))
        } else {
            className
        }
    }

    private fun handleField(classElement: Element, fieldElement: Element): PropertySpec {

        val outerTypeName = stringToClassName(classElement.asType().toString())
        val innerTypeName = stringToClassName(fieldElement.asType().toString())

        val attributeName = fieldElement.simpleName.toString()

        val lensTypeName = ClassName("io.fritz2.lenses", "Lens")
            //.plusParameter(ClassName("kotlin","String"))
            .plusParameter(outerTypeName)
            .plusParameter(innerTypeName)

        return PropertySpec.builder(attributeName, lensTypeName)
            .initializer("buildLens(%S, { it.$attributeName },{ p, v -> p.copy($attributeName = v)})", attributeName)
            .addKdoc(fieldElement.asType().toString())
            .build()
    }


}
/*
        annotatedElements.mapNotNull {
            when (it) {
                is ClassElement -> it as ClassElement
                else -> null
            }.groupBy {
                it.javaClass.`package`.name
            }
                .mapValues
            { (key, list) ->
                val fileSpecBuilder = FileSpec.builder(key, "Lenses")
                    .addComment("GENERATED by fritz2")
                    .addComment("NEVER CHANGE CONTENT MANUALLY!")
                    .addImport("io.fritz2.lenses", "buildLens")
                    .addImport("kotlin.reflect", "KProperty1")

                list.forEach { clazz ->
                    buildFunctionsForClass(clazz.toTypeElementOrNull()).forEach {
                        fileSpecBuilder.addFunction(it)
                    }
                }

                fileSpecBuilder.build()
            }.forEach
            { (key, fileSpec) ->
                fileSpec.writeTo(File("$kaptKotlinGeneratedDir/${key.replace(".", "/")}"))
            }

                 */


/*        File(kaptKotlinGeneratedDir, "Lenses.kt").apply {
            parentFile.mkdirs()
            writeText("""fun hugo(): String = "peter" """)
        }
  */



//operator fun KProperty1<Outer, Inner>.not(): Lens<Outer, Inner> = buildLens(this) { p, v -> p.copy(inner = v)}
/*
private fun buildFunctionsForClass(classDescriptor: ClassDescriptor): List<FunSpec> =
    classDescriptor.constructors.first().valueParameters.map {
        buildExtensionFunction(classDescriptor, it)
    }


fun createClassName(fq: FqNameUnsafe): ClassName {
    val shortName = fq.shortNameOrSpecial().toString()
    val pack = fq.asString().substringBefore(shortName).trim('.')
    return ClassName(pack,shortName)
}

private fun stringToClassName(name: String): TypeName {
    val hasParam = name.contains('<')
    val typeString = if (hasParam) name.substringBefore('<') else name

    val typeList = typeString.split(".")
    val classString = typeList.last()
    val packageString = typeList.dropLast(1).joinToString(".")
    val className = ClassName(packageString, classString)

    return if (hasParam) {
        //FIXME: handle more type parameters and nested ones
        val parameterString = name.substringAfter('<').substringBefore('>')
        className.plusParameter(stringToClassName(parameterString))
    } else {
        className
    }
}

private fun buildExtensionFunction(classDescriptor: ClassDescriptor, parameterDescriptor: ValueParameterDescriptor): FunSpec {

    val outerTypeName = stringToClassName(classDescriptor.defaultType.getJetTypeFqName(false))
    val innerTypeName = stringToClassName(parameterDescriptor.type.getJetTypeFqName(true))

    val attributeName = parameterDescriptor.name

    val receiverType = ClassName("kotlin.reflect","KProperty1")
        .plusParameter(outerTypeName)
        .plusParameter(innerTypeName)

    return FunSpec.builder("not")
        .addModifiers(KModifier.OPERATOR)
        .receiver(receiverType)
        .addStatement("return buildLens(this) {p,v -> p.copy($attributeName = v)}")
        .build()
}

 */

