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
                implementation("com.squareup:kotlinpoet:${rootProject.extra["kotlinpoetVersion"]}")
                implementation("com.squareup:kotlinpoet-ksp:${rootProject.extra["kotlinpoetVersion"]}")
                implementation("com.google.devtools.ksp:symbol-processing-api:${rootProject.extra["kspVersion"]}")
                api(kotlin("reflect:${rootProject.extra["kotlinVersion"]}"))
                api(kotlin("script-runtime:${rootProject.extra["kotlinVersion"]}"))
                implementation("com.google.auto.service:auto-service-annotations:${rootProject.extra["autoServiceVersion"]}")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter-params:${rootProject.extra["junitJupiterParamsVersion"]}")
                implementation("org.assertj:assertj-core:${rootProject.extra["assertJVersion"]}")
                implementation("com.github.tschuchortdev:kotlin-compile-testing:${rootProject.extra["compileTestingVersion"]}")
                implementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:${rootProject.extra["compileTestingVersion"]}")
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")