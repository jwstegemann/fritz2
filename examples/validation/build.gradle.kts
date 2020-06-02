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
                implementation("com.soywiz.korlibs.klock:klock:1.10.5")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.4")
            }
        }
    }
}