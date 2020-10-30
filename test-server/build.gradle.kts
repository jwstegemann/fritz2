plugins {
    application
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "6.0.0"
    id("com.github.hesch.execfork") version "0.1.14"
}

group = "dev.fritz2"
version = "0.0.1-SNAPSHOT"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

val logback_version = "1.2.1"
val ktor_version = "1.4.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
}

tasks.register<com.github.psxpaul.task.JavaExecFork>("start") {
    classpath = sourceSets.main.get().runtimeClasspath
    main = application.mainClassName
//    jvmArgs = [ '-Xmx500m', '-Djava.awt.headless=true' ]
    workingDir = buildDir
    standardOutput = "$buildDir/server.log"
    errorOutput = "$buildDir/error.log"
    stopAfter = project(":core").tasks["jsTest"]
    waitForPort = 3000
}

tasks {
    // This task will generate your fat JAR and put it in the ./build/libs/ directory
    shadowJar {
        manifest {
            attributes("Main-Class" to application.mainClassName)
        }
    }
}
