plugins {
    kotlin("multiplatform")
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
                optIn("kotlin.ExperimentalStdlibApi")
                //optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                //optIn("kotlinx.coroutines.FlowPreview")
            }
        }
        val commonMain by getting {
            dependencies {
                api(project(":core"))
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")
