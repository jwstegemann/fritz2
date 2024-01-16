import com.google.devtools.ksp.gradle.KspTask
import com.google.devtools.ksp.gradle.KspTaskMetadata
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
}

repositories {
    mavenCentral()
}

kotlin {
    jvm() // needed for kspCommonMainMetadata
    js(IR) {
        browser {
            webpackTask(Action {
                cssSupport {
                    enabled.set(true)
                }
            })
        }
    }.binaries.executable()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core"))
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
    }
}

/**
 * KSP support - start
 */
dependencies.kspCommonMainMetadata(project(":lenses-annotation-processor"))
kotlin.sourceSets.commonMain { tasks.withType<KspTaskMetadata> { kotlin.srcDir(destinationDirectory) } }
tasks.withType<KotlinCompilationTask<*>> { if (this !is KspTask) dependsOn(tasks.withType<KspTask>()) }
tasks.withType<AbstractArchiveTask> { dependsOn(tasks.withType<KspTask>()) }
/**
 * KSP support - end
 */
