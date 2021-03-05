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
        /* runTask {
                devServer = DevServer(
                    port = 9000,
                    contentBase = listOf("$projectDir/src/jsMain/resources")
                ).copy(
                    proxy = mapOf(
                        "/get" to "http://postman-echo.com"
                    )
                )
            }
        */
    }
    sourceSets {
        all {
            languageSettings.apply {
                enableLanguageFeature("InlineClasses") // language feature name
                useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes") // annotation FQ-name
                useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
                useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
                useExperimentalAnnotation("kotlinx.coroutines.InternalCoroutinesApi")
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
