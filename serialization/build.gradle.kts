plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
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