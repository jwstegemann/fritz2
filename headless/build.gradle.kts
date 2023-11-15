plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    js(IR).browser {
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
            }
        }
        val commonMain by getting {
            dependencies {
                api(project(":core"))
            }
        }
        val jsMain by getting {
            dependencies {
                api(npm("@floating-ui/dom","_"))
                api(npm("scroll-into-view-if-needed", "_"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(Kotlin.test.js)
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")
