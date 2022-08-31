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

// Fixes webpack-cli incompatibility by pinning the newest version.
// https://youtrack.jetbrains.com/issue/KTIJ-22030
rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
    versions.webpackCli.version = "4.10.0"
}

/**
 * KSP support - end
 */