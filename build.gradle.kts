buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.70"))
    }
}

plugins {
    id("org.jetbrains.dokka") version "0.10.0"
}

repositories {
    jcenter() // or maven { url 'https://dl.bintray.com/kotlin/dokka' }
}

allprojects {
    //TODO: manage common setting and dependencies
}

subprojects {
    group = "io.fritz2"
    version = "0.2"

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }
}

tasks {
    val dokka by getting(org.jetbrains.dokka.gradle.DokkaTask::class) {
        outputFormat = "markdown"
        outputDirectory = "$projectDir/api/dokka"
        subProjects = listOf("core")
        configuration {
            platform = "JS"
            sourceLink {
                path = "src/main/kotlin" // or simply "./"
                url = "https://github.com/jwstegemann/fritz2/blob/master/src/main/kotlin" //remove src/main/kotlin if you use "./" above
                lineSuffix = "#L"
            }
        }
    }
}
