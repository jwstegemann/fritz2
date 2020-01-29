
plugins {
    kotlin("multiplatform")
    `maven-publish`
}

group = "io.fritz2.optics"
version = "0.0.1"

kotlin {
    jvm()
    js()

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }

    }
}

configurations {
}

tasks {
    build {
        finalizedBy(publishToMavenLocal)
    }
}