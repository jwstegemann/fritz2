buildscript {
    repositories {
        mavenLocal()
    }

    dependencies {
        classpath("io.fritz2:fritz2-gradle:0.4")
    }
}

//apply(plugin = "fritz2-gradle") // activates automatic Lenses-genration

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
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation(project(":core"))
            }
        }

    }
}

