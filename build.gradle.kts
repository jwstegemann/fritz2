plugins {
    kotlin("multiplatform") version "1.7.0" apply false
    kotlin("plugin.serialization") version "1.7.0" apply false
    id("com.google.devtools.ksp") version "1.7.0-1.0.6" apply false
    id("org.jetbrains.dokka") version "1.6.21"
    id("maven-publish")
    signing
}

// needed to work on Apple Silicon. Should be fixed by 1.6.20 (https://youtrack.jetbrains.com/issue/KT-49109#focus=Comments-27-5259190.0-0)
rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>().nodeVersion = "16.0.0"
}

// consider moving to idiomatic solution of gradle for dependency sharing once it is ready:
// https://docs.gradle.org/current/userguide/platforms.html
extra.apply {
    // Dependencies
    set("kotlinVersion", "1.7.0")
    set("coroutinesVersion", "1.6.1") // upgrade to 1.6.2 causes issues with atomicfu?
    set("kotlinpoetVersion", "1.12.0")
    set("compileTestingVersion", "1.4.9")
    set("logbackVersion", "1.2.11")
    set("ktorVersion", "1.6.8") // TODO investigate update to 2.x
    set("serializationVersion", "1.3.3")
    set("kspVersion", "1.7.0-1.0.6")
    set("autoServiceVersion", "1.0.1")
    set("junitJupiterParamsVersion", "5.8.2")
    set("assertJVersion", "3.23.1")
    // npm
    set("popperjs", "2.10.1")
    set("scroll-into-view-if-needed", "2.2.28")

    // tailwind
    set("tailwindcss", "3.0.19")
    set("tailwindcss/forms", "0.4.0")

    // webpack
    set("postcss", "8.4.6")
    set("postcss-loader", "6.2.1")
    set("autoprefixer", "10.4.2")
    set("css-loader", "6.6.0")
    set("style-loader", "3.3.1")
    set("cssnano", "5.0.17")
}

allprojects {
    //manage common setting and dependencies
    repositories {
        mavenCentral()
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