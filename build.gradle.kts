import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin.Companion.kotlinNodeJsEnvSpec

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

    // We use a fixed NodeJS LTS version since there are NPM dependencies that are not yet
    // compatible with newer node versions. In our case, this is cliui.
    // Have a look at https://nodejs.org/en/about/previous-releases for the available node
    // versions. 
    // NodeJS v20 is supported until April 2026.
    plugins.withType<NodeJsPlugin> {
        kotlinNodeJsEnvSpec.apply {
            version = "20.19.4"
            download = true
        }
    }
}

subprojects {
    group = "dev.fritz2"
    version = "1.0-SNAPSHOT"
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
            "serialization"
        )
    )
}
