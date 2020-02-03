
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.61"))
    }
}

allprojects {
    //TODO: manage common setting and dependencies
}

subprojects {
    group = "io.fritz2"
    version = "0.1"

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        maven {
            name = "bintray"
            url = uri("https://dl.bintray.com/jwstegemann/fritz2/")
        }
    }
}