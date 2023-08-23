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
                implementation(npm("tailwindcss", rootProject.extra["tailwindcss"] as String))
                implementation(npm("@tailwindcss/forms", rootProject.extra["tailwindcss/forms"] as String))

                // webpack
                implementation(devNpm("postcss", rootProject.extra["postcss"] as String))
                implementation(devNpm("postcss-loader", rootProject.extra["postcss-loader"] as String))
                implementation(devNpm("autoprefixer", rootProject.extra["autoprefixer"] as String))
                implementation(devNpm("css-loader", rootProject.extra["css-loader"] as String))
                implementation(devNpm("style-loader", rootProject.extra["style-loader"] as String))
                implementation(devNpm("cssnano", rootProject.extra["cssnano"] as String))
            }
        }
    }
}

/**
 * KSP support - start
 */
dependencies {
    add("kspCommonMainMetadata",  project(":lenses-annotation-processor"))
}
kotlin.sourceSets.commonMain { kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin") }

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
    if (name != "kspCommonMainKotlinMetadata") dependsOn("kspCommonMainKotlinMetadata")
}
/**
 * KSP support - end
 */
