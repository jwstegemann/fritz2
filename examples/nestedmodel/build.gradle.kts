buildscript {
    repositories {
        mavenLocal()
        google()
        jcenter()
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
    kotlin {
        jvm()
        js().browser()

        sourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(kotlin("stdlib"))
                }
            }
            val jsMain by getting {
                dependencies {
                    implementation("io.fritz2.optics:core-js:0.2")
                }
            }
        }
    }
}

apply(plugin = "io.fritz2.optics")


