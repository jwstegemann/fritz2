package dev.fritz2.lenses

//import net.ltgt.gradle.incap.IncrementalAnnotationProcessor
//import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import com.squareup.kotlinpoet.classinspector.elements.ElementsClassInspector
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import com.squareup.kotlinpoet.metadata.specs.toTypeSpec
import dev.fritz2.lenses.LensesAnnotationProcessor.Companion.KAPT_KOTLIN_GENERATED_OPTION_NAME
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic.Kind.ERROR


@KotlinPoetMetadataPreview
@SupportedAnnotationTypes("dev.fritz2.lenses.Lenses")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(KAPT_KOTLIN_GENERATED_OPTION_NAME)
//@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.AGGREGATING)
class LensesAnnotationProcessor() : AbstractProcessor() {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: run {
            processingEnv.messager.printMessage(ERROR, "Can't find the target directory for generated Kotlin files.")
            return false
        }

//        val classInspector = ElementsClassInspector.create(processingEnv.elementUtils, processingEnv.typeUtils)

        val annotatedElements = roundEnv?.getElementsAnnotatedWith(Lenses::class.java)
        if (annotatedElements == null || annotatedElements.isEmpty()) return false

        annotatedElements
            .filter { it.kind == ElementKind.CLASS }
            .map { it as TypeElement }
            .associateWith { it.toTypeSpec() }
            .toList()
            .groupBy { (element, _) ->
                processingEnv.elementUtils.getPackageOf(element).qualifiedName.toString()
            }
            .forEach { (pack, classes) ->
                val fileSpecBuilder = FileSpec.builder(pack, "GeneratedLenses")
                    .addComment("GENERATED by fritz2")
                    .addComment(" - NEVER CHANGE CONTENT MANUALLY!")
                    .addImport("dev.fritz2.lenses", "buildLens")

                val lensesObject = TypeSpec.objectBuilder("L")
                classes.forEach { (element, classData) ->
                    lensesObject.addType(handleDataClass(element, classData))
                }
                fileSpecBuilder.addType(lensesObject.build())

                fileSpecBuilder
                    .build()
                    .writeTo(File(kaptKotlinGeneratedDir).apply {
                        mkdir()
                    })
            }

        return true
    }

    private fun handleDataClass(element: TypeElement, classData: TypeSpec): TypeSpec {
        val classSpec = TypeSpec.objectBuilder(classData.name ?: "unknown")

        classData.propertySpecs.forEach { propertyData ->
            //FIXME: replace deprecated function call
            classSpec.addProperty((handleField(element.asClassName(), propertyData)))
        }

        return classSpec.build()
    }

    private fun handleField(className: ClassName, propertyData: PropertySpec): PropertySpec {

        val attributeName = propertyData.name

        val lensTypeName = ClassName("dev.fritz2.lenses", "Lens")
            //.plusParameter(ClassName("kotlin","String"))
            .plusParameter(className)
            .plusParameter(propertyData.type)

        return PropertySpec.builder(attributeName, lensTypeName)
            .initializer("buildLens(%S, { it.$attributeName },{ p, v -> p.copy($attributeName = v)})", attributeName)
            .build()
    }

}
