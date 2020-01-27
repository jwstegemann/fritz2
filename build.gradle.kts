import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.ir.backend.js.compile

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.61"))
    }
}

allprojects {
    //TODO: manage common setting and dependencies

    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    group = "io.fritz2"
    version = "0.1"
}