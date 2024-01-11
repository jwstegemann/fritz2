plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    js(IR) {
        browser()
    }.binaries.executable()

    sourceSets {
        all {
            languageSettings {
                languageVersion = "2.0"
                optIn("kotlin.ExperimentalStdlibApi")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }

        commonMain {
            dependencies {
                implementation(project(":core"))
            }
        }
        jsMain {
            dependencies {
                implementation(npm("@mat3e-ux/stars", "0.2.5"))
            }
        }
    }
}