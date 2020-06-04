plugins {
    kotlin("multiplatform")
    id("maven-publish")
}

kotlin {
    kotlin {
        jvm()
        js().nodejs()

        sourceSets {
            val commonMain by getting {
                dependencies {
                }
            }
            val jvmMain by getting {
                dependencies {
                    implementation(kotlin("stdlib"))
                    implementation(project(":core"))
                    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
                    implementation("com.squareup:kotlinpoet:1.3.0")
                }
            }

        }
    }
}

/*
publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["java"])
            artifact(tasks.kotlinSourcesJar.get()) {
                classifier = "sources"
            }
        }
    }
    repositories {
        maven {
            url = uri("$buildDir/repository")
        }
    }
}

 */