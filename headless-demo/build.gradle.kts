import com.google.devtools.ksp.gradle.KspTask
import com.google.devtools.ksp.gradle.KspTaskMetadata
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp")
}

kotlin {
    jvm() // needed for kspCommonMainMetadata
    js(IR) {
        browser()
    }.binaries.executable()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.ExperimentalStdlibApi")
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(project(":headless"))
            }
        }
        val jsMain by getting {
            dependencies {
                // tailwind
                implementation(npm("tailwindcss", "_"))
                implementation(npm("@tailwindcss/forms", "_"))

                // webpack
                implementation(devNpm("postcss", "_"))
                implementation(devNpm("postcss-loader", "_"))
                implementation(devNpm("autoprefixer", "_"))
                implementation(devNpm("css-loader", "_"))
                implementation(devNpm("style-loader", "_"))
                implementation(devNpm("cssnano", "_"))
            }
        }
    }
}

// KSP support for Lens generation
dependencies.kspCommonMainMetadata(project(":lenses-annotation-processor"))
kotlin.sourceSets.commonMain { tasks.withType<KspTaskMetadata> { kotlin.srcDir(destinationDirectory) } }