plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm()
    js(IR).browser { }
    sourceSets {
        val jsMain by getting {
            dependencies {
                api(project(":core"))
                api(KotlinX.serialization.json)
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")