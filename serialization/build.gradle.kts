plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
}

kotlin {
    jvm()
    js(IR).browser { }
    sourceSets {
        val jsMain by getting {
            dependencies {
                api(project(":core"))
                api(KotlinX.serialization.json)
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