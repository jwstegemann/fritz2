plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp")
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
                implementation("com.google.devtools.ksp:symbol-processing-api:${rootProject.ext["kspVersion"]}")
                api(kotlin("reflect:${rootProject.ext["kotlinVersion"]}"))
                api(kotlin("script-runtime:${rootProject.ext["kotlinVersion"]}"))
                implementation("com.google.auto.service:auto-service-annotations:${rootProject.ext["autoServiceVersion"]}")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter-params:${rootProject.ext["junitJupiterParamsVersion"]}")
                implementation("org.assertj:assertj-core:${rootProject.ext["assertJVersion"]}")
                implementation("com.github.tschuchortdev:kotlin-compile-testing:${rootProject.ext["compileTestingVersion"]}")
                implementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:${rootProject.ext["compileTestingVersion"]}")
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")