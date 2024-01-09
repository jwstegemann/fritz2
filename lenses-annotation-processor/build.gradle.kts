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
                implementation(Square.kotlinPoet)
                implementation("com.squareup:kotlinpoet-ksp:_")
                implementation("com.google.devtools.ksp:symbol-processing-api:_")
                api(kotlin("reflect:_"))
                api(kotlin("script-runtime:_"))
                implementation("com.google.auto.service:auto-service-annotations:_")
            }
        }

        val jvmTest by getting {
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

// Generates a javadoc jar from dokka html sources
val dokkaJavadocJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles java doc to jar"
    archiveClassifier.set("javadoc")
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
}

apply(from = "$rootDir/publishing.gradle.kts")