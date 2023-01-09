plugins {
    application
    kotlin("jvm")
    id("com.github.psxpaul.execfork") version "0.1.15"
}

application {
    mainClass.set("dev.fritz2.ServerKt")
}

repositories {
    mavenLocal()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-core:${rootProject.extra["ktorVersion"]}")
    implementation("io.ktor:ktor-server-netty:${rootProject.extra["ktorVersion"]}")
    implementation("io.ktor:ktor-server-call-logging-jvm:${rootProject.extra["ktorVersion"]}")
    implementation("io.ktor:ktor-server-auth-jvm:${rootProject.extra["ktorVersion"]}")
    implementation("io.ktor:ktor-server-content-negotiation:${rootProject.extra["ktorVersion"]}")
    implementation("io.ktor:ktor-serialization-jackson:${rootProject.extra["ktorVersion"]}")
    implementation("io.ktor:ktor-server-websockets-jvm:${rootProject.extra["ktorVersion"]}")
    implementation("ch.qos.logback:logback-classic:${rootProject.extra["logbackVersion"]}")
}

tasks.register<com.github.psxpaul.task.JavaExecFork>("start") {
    classpath = sourceSets.main.map { it.runtimeClasspath }.get()
    main = application.mainClass.get()
    workingDir = buildDir
    standardOutput = "$buildDir/server.log"
    errorOutput = "$buildDir/error.log"
    stopAfter = project(":core").tasks["check"]
    waitForPort = 3000
}