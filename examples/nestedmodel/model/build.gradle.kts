
buildscript {
    repositories {
        mavenLocal()
        google()
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath("io.fritz2.optics:plugin:0.1")
    }
}


plugins {
    kotlin("js")
}

kotlin {
    target {
        browser {
        }
    }
}

apply(plugin = "io.fritz2.optics")

dependencies {
    implementation(kotlin("stdlib-js"))
    api("io.fritz2.optics:core-js:0.1")
}

