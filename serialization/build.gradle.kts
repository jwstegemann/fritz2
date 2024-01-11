plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
}

kotlin {
    jvm()
    js(IR).browser { }
    sourceSets {
        all {
            languageSettings {
                languageVersion = "2.0"
                optIn("kotlin.ExperimentalStdlibApi")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }

        jsMain {
            dependencies {
                api(project(":core"))
                api(KotlinX.serialization.json)
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")