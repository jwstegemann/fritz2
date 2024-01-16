plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp")
    id("org.jetbrains.dokka")
}

ksp {
    arg("autoserviceKsp.verify", "true")
    arg("autoserviceKsp.verbose", "true")
}

kotlin {
    js(IR) {
        browser()
    }.binaries.executable()
    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        jvmMain {
            dependencies {
                api(kotlin("stdlib"))
                api(project(":core"))
                implementation(Square.kotlinPoet)
                implementation("com.squareup:kotlinpoet-ksp:_")
                implementation("com.google.devtools.ksp:symbol-processing-api:_")
                api(kotlin("reflect:_"))
                api(kotlin("script-runtime:_"))
                implementation("com.google.auto.service:auto-service-annotations:_")
            }
        }

        jvmTest {
            dependencies {
                implementation(Kotlin.test.junit5)
                implementation(Testing.junit.jupiter.params)
                implementation(Testing.assertj.core)
                implementation("com.github.tschuchortdev:kotlin-compile-testing:_")
                implementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:_")
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")