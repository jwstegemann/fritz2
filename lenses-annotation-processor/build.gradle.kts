plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
}

ksp {
    arg("autoserviceKsp.verify", "true")
    arg("autoserviceKsp.verbose", "true")
}

kotlin {
    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        browser()
    }.binaries.executable()
    sourceSets {
        val jvmMain by getting {
            dependencies {
                api(kotlin("stdlib"))
                api(project(":core"))
                implementation("com.squareup:kotlinpoet:${rootProject.ext["kotlinpoetVersion"]}")
                implementation("com.squareup:kotlinpoet-ksp:${rootProject.ext["kotlinpoetVersion"]}")
                implementation("com.google.devtools.ksp:symbol-processing-api:1.6.10-1.0.2")
                api(kotlin("reflect:1.6.10"))
                api(kotlin("script-runtime:1.6.10"))
                implementation("com.google.auto.service:auto-service-annotations:1.0.1")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
                implementation("org.assertj:assertj-core:3.19.0")
                implementation("com.github.tschuchortdev:kotlin-compile-testing:${rootProject.ext["compileTestingVersion"]}")
                implementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:${rootProject.ext["compileTestingVersion"]}")
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")