plugins {
    kotlin("multiplatform") version "1.4.10" apply false
}

allprojects {
    //manage common setting and dependencies
    repositories {
        mavenLocal()
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
