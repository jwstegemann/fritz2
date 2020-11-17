plugins {
    kotlin("multiplatform") version "1.4.10" apply false
    id("org.jetbrains.dokka") version "0.10.1" apply false
}

allprojects {
    //manage common setting and dependencies
    repositories {
        mavenCentral()
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

subprojects {
    group = "dev.fritz2"
    version = "0.8-SNAPSHOT"
}
