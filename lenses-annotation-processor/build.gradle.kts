plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp") version "1.5.31-1.0.0"
}

ksp {
    arg("autoserviceKsp.verify", "true")
    arg("autoserviceKsp.verbose", "true")
}

kotlin {
    jvm()

    sourceSets {
        val jvmMain by getting {
            dependencies {
                api(kotlin("stdlib"))
                api(project(":core"))
                implementation("com.squareup:kotlinpoet:${rootProject.ext["kotlinpoetVersion"]}")
                implementation("com.squareup:kotlinpoet-ksp:${rootProject.ext["kotlinpoetVersion"]}")
                implementation("com.google.devtools.ksp:symbol-processing-api:1.5.31-1.0.0")

                /*
                api("com.squareup:kotlinpoet-classinspector-elements:${rootProject.ext["kotlinpoetVersion"]}")
                api("com.squareup:kotlinpoet-metadata:${rootProject.ext["kotlinpoetVersion"]}")
                api("com.squareup:kotlinpoet-metadata-specs:${rootProject.ext["kotlinpoetVersion"]}")

                 */
                api(kotlin("reflect:1.5.0"))
                api(kotlin("script-runtime:1.5.0"))

                implementation("com.google.auto.service:auto-service-annotations:1.0")


            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("org.assertj:assertj-core:3.19.0")
                implementation("com.github.tschuchortdev:kotlin-compile-testing:${rootProject.ext["compileTestingVersion"]}")
                implementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:${rootProject.ext["compileTestingVersion"]}")

                //testImplementation("junit:junit:4.13.2")
            }
        }
    }
}

apply(from = "$rootDir/publishing.gradle.kts")