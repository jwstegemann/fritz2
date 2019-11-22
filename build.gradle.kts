import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "de.oevbs"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.3.60"
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.60"))
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
    compile("org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.12")
}

repositories {
    mavenCentral()
    jcenter()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}