import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
}

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js(IR) {
        browser()
    }.binaries.executable()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core"))
                implementation(KotlinX.serialization.json)
            }
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
        jvmMain {
            dependencies {
            }
        }
        jsMain {
            dependencies {
            }
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", project(":lenses-annotation-processor"))
}

project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
    if(name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
