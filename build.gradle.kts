plugins {
    kotlin("multiplatform") version "1.7.20" apply false
    kotlin("plugin.serialization") version "1.7.20" apply false
    id("com.google.devtools.ksp") version "1.7.20-1.0.6" apply false
    id("org.jetbrains.dokka") version "1.7.20"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.13.2"
    id("maven-publish")
    signing
}

// consider moving to idiomatic solution of gradle for dependency sharing once it is ready:
// https://docs.gradle.org/current/userguide/platforms.html
extra.apply {
    // core
    set("kotlinVersion", "1.7.20")
    set("kspVersion", "1.7.20-1.0.6")
    set("coroutinesVersion", "1.6.4")
    set("serializationVersion", "1.4.0")

    // test-server
    set("ktorVersion", "2.2.2")
    set("logbackVersion", "1.4.5")

    // lenses-annotation-processor
    set("kotlinpoetVersion", "1.12.0")
    set("compileTestingVersion", "1.4.9")
    set("autoServiceVersion", "1.0.1")
    set("junitJupiterParamsVersion", "5.8.2")
    set("assertJVersion", "3.23.1")

    // npm
    set("floatingui", "1.5.1")
    set("scroll-into-view-if-needed", "2.2.29")

    // tailwind
    set("tailwindcss", "3.2.1")
    set("tailwindcss/forms", "0.5.3")

    // webpack
    set("postcss", "8.4.17")
    set("postcss-loader", "7.0.1")
    set("autoprefixer", "10.4.12")
    set("css-loader", "6.7.1")
    set("style-loader", "3.3.1")
    set("cssnano", "5.1.13")
}

allprojects {
    //manage common setting and dependencies
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = "dev.fritz2"
    version = "1.0-RC8"
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
