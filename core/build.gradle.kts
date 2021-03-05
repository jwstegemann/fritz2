plugins {
    kotlin("multiplatform")
    id("maven-publish")
    id("org.jetbrains.dokka")
    kotlin("plugin.serialization")
    signing
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

signing {
    sign((extensions.getByName("publishing") as PublishingExtension).publications)
}

publishing {
    repositories {
        maven {
            name = "sonatype"

            val releaseUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            val isRelease = System.getenv("GITHUB_EVENT_NAME").equals("release", true)

            url = uri(if (isRelease && !version.toString().endsWith("SNAPSHOT")) releaseUrl else snapshotUrl)

            credentials {
                username = System.getenv("OSSRH_USERNAME")
                password = System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}

