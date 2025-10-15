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
        commonMain {
            dependencies {
                implementation(project(":headless"))
            }
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
        jsMain {
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

dependencies {
    add("kspCommonMainMetadata", project(":lenses-annotation-processor"))
}

project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
    if(name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
