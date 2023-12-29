plugins {
    application
    kotlin("jvm")
    id("com.github.psxpaul.execfork")
}

application {
    mainClass.set("dev.fritz2.ServerKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(Kotlin.stdlib)
    implementation(Kotlin.stdlib.jdk8)
    implementation(Ktor.server.core)
    implementation(Ktor.server.netty)
    implementation(Ktor.server.callLogging)
    implementation(Ktor.server.auth)
    implementation(Ktor.server.contentNegotiation)
    implementation(Ktor.plugins.serialization.jackson)
    implementation(Ktor.server.websockets)
    implementation("ch.qos.logback:logback-classic:_")
}

tasks.register<com.github.psxpaul.task.JavaExecFork>("start") {

    classpath = sourceSets.main.map { it.runtimeClasspath }.get()
    main = application.mainClass.get()
    workingDir = layout.buildDirectory.asFile.get()
    standardOutput = "$workingDir/server.log"
    errorOutput = "$workingDir/error.log"
    stopAfter = project(":core").tasks["check"]
    waitForPort = 3000
}