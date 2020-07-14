buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.72"))
    }
}

plugins {
    id("org.jetbrains.dokka") version "0.10.1" apply false
    id("io.gitlab.arturbosch.detekt") version "1.10.0" apply false
}

repositories {
    jcenter() // or maven { url 'https://dl.bintray.com/kotlin/dokka' }
}

allprojects {
    //manage common setting and dependencies
}

subprojects {
    group = "dev.fritz2"
    version = "0.7"

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }
}

