package io.fritz2.optics


import com.squareup.kotlinpoet.*
import de.jensklingenberg.mpapt.common.*
import de.jensklingenberg.mpapt.model.AbstractProcessor
import de.jensklingenberg.mpapt.model.Element
import de.jensklingenberg.mpapt.model.RoundEnvironment
import de.jensklingenberg.mpapt.utils.KonanTargetValues
import de.jensklingenberg.mpapt.utils.KotlinPlatformValues
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.platform.TargetPlatform
import org.jetbrains.kotlin.resolve.annotations.argumentValue
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import java.io.File
import io.fritz2.optics.Optics

class MpAptTestProcessor() : AbstractProcessor() {
    val TAG = "MyAnnotationProcessor222"

    val optics = Optics::class.java.name
//    val optics = mutableListOf<FunctionDescriptor>()

    override fun process(roundEnvironment: RoundEnvironment) {

        roundEnvironment.getElementsAnnotatedWith(optics).forEach { element ->
            when (element) {
                is Element.ClassElement -> {
                    //getFunctions.add(element.func)
                    log("##### GEFUNDEN: " + element)
                }
            }
        }


    }

    override fun isTargetPlatformSupported(platform: TargetPlatform): Boolean {
        val targetName = platform.first().platformName

        return when (targetName) {
            KotlinPlatformValues.JS -> true
            KotlinPlatformValues.JVM -> false
            else -> {
                log(targetName)
                false
            }
        }

    }

    override fun getSupportedAnnotationTypes(): Set<String> = setOf(optics)

    override fun processingOver() {
        log("$TAG***Processor over ***")
    }

}
