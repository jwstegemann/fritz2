plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm()
    js(BOTH).browser { }
    sourceSets {
        val jsMain by getting {
            dependencies {
                api(project(":core"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:${rootProject.extra["serializationVersion"]}")
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")