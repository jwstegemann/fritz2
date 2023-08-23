plugins {
    kotlin("multiplatform")
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

