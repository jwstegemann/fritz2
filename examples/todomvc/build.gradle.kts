import com.google.devtools.ksp.gradle.KspTask
import com.google.devtools.ksp.gradle.KspTaskMetadata
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
}

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    jvm() // needed for kspCommonMainMetadata
    js(IR) {
        browser()
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

// KSP support for Lens generation
dependencies.kspCommonMainMetadata(project(":lenses-annotation-processor"))
kotlin.sourceSets.commonMain { tasks.withType<KspTaskMetadata> { kotlin.srcDir(destinationDirectory) } }