buildscript {
    repositories {
        mavenLocal()
//        google()
//        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath("io.fritz2.optics:plugin:0.2")
    }
}

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    js().browser()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(project(":core"))
                implementation("io.fritz2.optics:core:0.2")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.4")
            }
        }
    }
}

apply(plugin = "io.fritz2.optics")