
buildscript {
    repositories {
        mavenLocal()
        google()
        jcenter()
        mavenCentral()
        maven {
            name = "bintray"
            url = uri("https://dl.bintray.com/jwstegemann/fritz2/")
        }
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
    compile("io.fritz2.optics:core-js:0.1")
}


