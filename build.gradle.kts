plugins {
    kotlin("multiplatform") version "1.4.30" apply false
    kotlin("plugin.serialization") version "1.4.30" apply false
    id("org.jetbrains.dokka") version "1.4.10.2"
    id("maven-publish")
    signing
}

ext {
    // Dependencies
    set("coroutinesVersion", "1.4.2")
    set("kotlinpoetVersion", "1.6.0")
    set("stylisVersion", "4.0.2")
    set("murmurhashVersion", "1.0.0")
    set("logbackVersion", "1.2.1")
    set("ktorVersion", "1.4.0") // upgrade to 1.5 produces test errors
    set("serializationVersion", "1.1.0-RC")
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
    version = "0.10"
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(rootDir.resolve("api"))
}
