plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp")
}

kotlin {
    js(IR) {
        browser()
    }.binaries.executable()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.ExperimentalStdlibApi")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(project(":core"))
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
ksp {

}

kotlin.sourceSets.commonMain { kotlin.srcDir("build/generated/ksp/commonMain/kotlin") }
// needed to work on Apple Silicon. Should be fixed by 1.6.20 (https://youtrack.jetbrains.com/issue/KT-49109#focus=Comments-27-5259190.0-0)
rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>().nodeVersion = "16.0.0"
}
/**
 * KSP support - end
 */
