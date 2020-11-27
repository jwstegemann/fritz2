plugins {
    kotlin("multiplatform")
    id("maven-publish")
    id("org.jetbrains.dokka")
}

kotlin {
//    jvm()
    js(LEGACY).browser {
        testTask {
            useKarma {
//                useSafari()
//                useFirefox()
//                useChrome()
                useChromeHeadless()
//                usePhantomJS()
            }
            //running test-server in background
//            dependsOn(":test-server:start")
        }
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
                implementation(project(":core"))
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
                implementation(project(":styling"))
                implementation(project(":components"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}


publishing {
    repositories {
        maven {
            name = "bintray"
            val releaseUrl = "https://api.bintray.com/maven/jwstegemann/fritz2/${project.name}/;" +
                    "publish=0;" + // Never auto-publish to allow override.
                    "override=1"
            val snapshotUrl = "https://oss.jfrog.org/artifactory/oss-snapshot-local"
            val isRelease = System.getenv("GITHUB_EVENT_NAME").equals("release", true)

            url = uri(if (isRelease && !version.toString().endsWith("SNAPSHOT")) releaseUrl else snapshotUrl)

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