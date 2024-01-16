plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
}

kotlin {
    jvm()
    js(IR).browser {
        testTask {
            //running test-server in background
            dependsOn(":test-server:start")
            // see "karma.config.d" folder for customizing karma
        }
    }
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.ExperimentalStdlibApi")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.coroutines.FlowPreview")
            }
        }
        commonMain {
            dependencies {
                api(KotlinX.coroutines.core)
            }
        }
        commonTest {
            dependencies {
                implementation(Kotlin.test)
                implementation(Kotlin.test.common)
                implementation(Kotlin.test.junit)
                implementation(Kotlin.test.annotationsCommon)
            }
        }
        jsMain {
            dependencies {
                api(KotlinX.coroutines.core)
            }
        }
        jsTest {
            dependencies {
                implementation(Kotlin.test.js)
                implementation(KotlinX.serialization.json)
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")