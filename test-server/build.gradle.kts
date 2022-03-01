plugins {
    application
    kotlin("jvm")
    id("com.github.psxpaul.execfork") version "0.1.15"
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenLocal()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-netty:${rootProject.extra["ktorVersion"]}")
    implementation("ch.qos.logback:logback-classic:${rootProject.extra["logbackVersion"]}")
    implementation("io.ktor:ktor-server-core:${rootProject.extra["ktorVersion"]}")
    implementation("io.ktor:ktor-auth:${rootProject.extra["ktorVersion"]}")
    implementation("io.ktor:ktor-jackson:${rootProject.extra["ktorVersion"]}")
    implementation("io.ktor:ktor-websockets:${rootProject.extra["ktorVersion"]}")
    testImplementation("io.ktor:ktor-server-tests:${rootProject.extra["ktorVersion"]}")
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