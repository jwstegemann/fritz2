plugins {
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization")  apply false
    id("com.google.devtools.ksp") apply false
    id("org.jetbrains.dokka")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("maven-publish")
    signing
}

allprojects {
    //manage common setting and dependencies
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = "dev.fritz2"
    version = "1.0-RC19.1"
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(rootDir.resolve("api"))
}

tasks.register("metadataToWww") {
    doLast {
        File(rootDir.resolve("www/src/_data"), "fritz2.json").writeText(
            """
                {
                    "version": "${subprojects.find { it.name == "core" }?.version ?: ""}"
                }
            """.trimIndent()
        )
    }
}

apiValidation {
    ignoredProjects.addAll(
        listOf(
            "lenses-annotation-processor",
            "test-server",
            "headless-demo",
            "snippets",
            "examples",
            "gettingstarted",
            "nestedmodel",
            "performance",
            "remote",
            "masterdetail",
            "routing",
            "tictactoe",
            "todomvc",
            "validation",
            "webcomponent",
        )
    )
}
