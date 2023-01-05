plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm()
    js(BOTH).browser {
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
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-junit"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jsMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${rootProject.extra["coroutinesVersion"]}")
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${rootProject.extra["coroutinesVersion"]}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${rootProject.extra["serializationVersion"]}")
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")
