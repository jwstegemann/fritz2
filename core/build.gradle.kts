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
        // just to have a place to copy it from...
        /*
        runTask {
            devServer?.apply {
                port = 9000
                proxy?.apply {
                    put("/members", "http://localhost:8080")
                    put("/chat", mapOf(
                        "target" to "ws://localhost:8080",
                        "ws" to true
                    ))
                }
            }
        }
        */
    }
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.ExperimentalStdlibApi")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.coroutines.FlowPreview")
            }
        }
        val commonMain by getting {
            dependencies {
                api(KotlinX.coroutines.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Kotlin.test)
                implementation(Kotlin.test.common)
                implementation(Kotlin.test.junit)
                implementation(Kotlin.test.annotationsCommon)
            }
        }
        val jsMain by getting {
            dependencies {
                api(KotlinX.coroutines.core)
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(Kotlin.test.js)
                implementation(KotlinX.serialization.json)
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")