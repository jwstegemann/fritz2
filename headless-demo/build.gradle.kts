plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }.binaries.executable()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.ExperimentalStdlibApi")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(project(":headless"))
                // tailwind
                implementation(npm("postcss", "8.3.5"))
                implementation(npm("postcss-loader", "4.2.0")) // 5.0.0 seems not to work
                implementation(npm("autoprefixer", "10.2.6"))
                implementation(npm("tailwindcss", "3.0.7"))
                implementation(npm("@tailwindcss/forms", "0.4.0"))

            }
        }
    }
}

dependencies {
    add("kspMetadata", project(":lenses-annotation-processor"))
}

// Generate common code with ksp instead of per-platform, hopefully this won't be needed in the future.
// https://github.com/google/ksp/issues/567
kotlin.sourceSets.commonMain {
    kotlin.srcDir("build/generated/ksp/commonMain/kotlin")
}
tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
    if (name != "kspKotlinMetadata") {
        dependsOn("kspKotlinMetadata")
    }
}
