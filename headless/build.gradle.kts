plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
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
    }
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.ExperimentalStdlibApi")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
        commonMain {
            dependencies {
                api(project(":core"))
            }
        }
        jsMain {
            dependencies {
                api(npm("@floating-ui/dom","_"))
                api(npm("scroll-into-view-if-needed", "_"))
            }
        }
        jsTest {
            dependencies {
                implementation(Kotlin.test.js)
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")