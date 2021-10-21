plugins {
    kotlin("multiplatform") version "1.5.30" apply false
    kotlin("plugin.serialization") version "1.5.30" apply false
    id("org.jetbrains.dokka") version "1.4.32"
    id("maven-publish")
    signing
}

ext {
    // Dependencies
    set("kotlinVersion", "1.5.30")
    set("coroutinesVersion", "1.5.1")
    set("kotlinpoetVersion", "1.8.0")
    set("compileTestingVersion", "1.4.1")
    set("stylisVersion", "4.0.2")
    set("murmurhashVersion", "2.0.0")
    set("logbackVersion", "1.2.1")
    set("ktorVersion", "1.4.0") // upgrade to 1.5 produces test errors
    set("serializationVersion", "1.2.2")
}

allprojects {
    //manage common setting and dependencies
    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    group = "dev.fritz2"
    version = "0.14-SNAPSHOT"
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(rootDir.resolve("api"))
}
