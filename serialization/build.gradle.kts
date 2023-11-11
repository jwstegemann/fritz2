plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm()
    js().browser { }
    sourceSets {
        val jsMain by getting {
            dependencies {
                api(project(":core"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:_")
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")