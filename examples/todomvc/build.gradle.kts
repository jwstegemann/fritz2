
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
}

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    jvm() // needed for kspCommonMainMetadata
    js(IR) {
        browser()
    }.binaries.executable()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core"))
            }
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
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
