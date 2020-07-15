plugins {
    kotlin("multiplatform")
    id("maven-publish")
    id("org.jetbrains.dokka")
}

kotlin {
    jvm()
    js().browser {
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
/*            runTask {
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
                implementation(kotlin("stdlib"))
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
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.7")
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.7")
            }
        }
    }
}


publishing {
    repositories {
        maven {
            name = "bintray"
            val bintrayUsername = "jwstegemann"
            val bintrayRepoName = "fritz2"
            val bintrayPackageName = "core"
            setUrl(
                "https://api.bintray.com/maven/" +
                        "$bintrayUsername/$bintrayRepoName/$bintrayPackageName/;" +
                        "publish=0;" + // Never auto-publish to allow override.
                        "override=1"
            )
            credentials {
                username = "jwstegemann"
                password = System.getenv("BINTRAY_API_KEY")
            }
        }
    }
}


tasks {
    val dokka by getting(org.jetbrains.dokka.gradle.DokkaTask::class) {
        impliedPlatforms = mutableListOf("common")
        outputFormat = "markdown"
        outputDirectory = "$projectDir/api/dokka"
        multiplatform {
            val js by creating {
                impliedPlatforms = mutableListOf("js")
            }
            val jvm by creating {
                impliedPlatforms = mutableListOf("jvm")
            }
        }
    }
}
