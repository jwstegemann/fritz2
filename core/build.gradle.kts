plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm()
    js(BOTH).browser {
        testTask {
            useKarma {
//                useSafari()
//                useFirefox()
//                useChrome()
                useChromeHeadless()
//                usePhantomJS()
            }
            //running test-server in background
            dependsOn(":test-server:start")
        }
        // just to have a place to copy it from...
        /*
        runTask {
            devServer = devServer?.copy(
                port = 9000,
                proxy = mapOf(
                    "/members" to "http://localhost:8080",
                    "/chat" to mapOf(
                        "target" to "ws://localhost:8080",
                        "ws" to true
                    )
                )
            )
        }
        */
    }
    sourceSets {
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
                useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
                useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
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
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${rootProject.ext["coroutinesVersion"]}")
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${rootProject.ext["coroutinesVersion"]}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${rootProject.ext["serializationVersion"]}")
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")
