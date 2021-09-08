plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    js(BOTH).browser {
        testTask {
            useKarma {
                useChromeHeadless()
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

        val commonMain by getting {
            dependencies {
                api(project(":core"))
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
                implementation(npm("stylis", rootProject.ext["stylisVersion"] as String))
                implementation(npm("murmurhash", rootProject.ext["murmurhashVersion"] as String))
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