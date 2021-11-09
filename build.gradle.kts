plugins {
    kotlin("multiplatform") version "1.5.31" apply false
    kotlin("plugin.serialization") version "1.5.31" apply false
    id("org.jetbrains.dokka") version "1.4.32"
    id("maven-publish")
    signing
}

ext {
    // Dependencies
    set("kotlinVersion", "1.5.31")
    set("coroutinesVersion", "1.5.2")
    set("kotlinpoetVersion", "1.9.0")
    set("compileTestingVersion", "1.4.5")
    set("stylisVersion", "4.0.2")
    set("murmurhashVersion", "2.0.0")
    set("logbackVersion", "1.2.1")
    set("ktorVersion", "1.4.0") // upgrade to 1.5 produces test errors
    set("serializationVersion", "1.3.0")
}

allprojects {
    //manage common setting and dependencies
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = "dev.fritz2"
    version = "0.14-SNAPSHOT"
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(rootDir.resolve("api"))
}
