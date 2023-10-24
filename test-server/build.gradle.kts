plugins {
    application
    kotlin("jvm")
    id("com.github.psxpaul.execfork")
}

application {
    mainClass.set("dev.fritz2.ServerKt")
}

repositories {
    mavenLocal()
//    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-core:_")
    implementation("io.ktor:ktor-server-netty:_")
    implementation("io.ktor:ktor-server-call-logging-jvm:_")
    implementation("io.ktor:ktor-server-auth-jvm:_")
    implementation("io.ktor:ktor-server-content-negotiation:_")
    implementation("io.ktor:ktor-serialization-jackson:_")
    implementation("io.ktor:ktor-server-websockets-jvm:_")
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