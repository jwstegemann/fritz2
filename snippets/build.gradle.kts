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

