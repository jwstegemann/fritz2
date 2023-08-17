plugins {
    kotlin("multiplatform") version "1.8.22" apply false
    kotlin("plugin.serialization") version "1.8.22" apply false
    id("com.google.devtools.ksp") version "1.8.22-1.0.11" apply false
    id("org.jetbrains.dokka") version "1.8.20"
    id("maven-publish")
    signing
}

// consider moving to idiomatic solution of gradle for dependency sharing once it is ready:
// https://docs.gradle.org/current/userguide/platforms.html
extra.apply {
    // core
    set("kotlinVersion", "1.8.22")
    set("kspVersion", "1.8.22-1.0.11")
    set("coroutinesVersion", "1.7.3")
    set("serializationVersion", "1.5.1")
    set("dateTimeVersion", "0.4.0")

    // test-server
    set("ktorVersion", "2.3.3")
    set("logbackVersion", "1.4.11")

    // lenses-annotation-processor
    set("kotlinpoetVersion", "1.14.2")
    set("compileTestingVersion", "1.5.0")
    set("autoServiceVersion", "1.1.1")
    set("junitJupiterParamsVersion", "5.10.0")
    set("assertJVersion", "3.24.2")

    // npm
    set("popperjs", "2.11.8")
    set("scroll-into-view-if-needed", "2.2.31")

    // tailwind
    set("tailwindcss", "3.3.3")
    set("tailwindcss/forms", "0.5.4")

    // webpack
    set("postcss", "8.4.27")
    set("postcss-loader", "7.3.0")
    set("autoprefixer", "10.4.13")
    set("css-loader", "6.7.2")
    set("style-loader", "3.3.2")
    set("cssnano", "5.1.14")
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
