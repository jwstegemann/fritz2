plugins {
    kotlin("multiplatform")
    id("maven-publish")
}

val kotlinpoet_version = "1.6.0"

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
                    implementation(kotlin("stdlib-jdk8"))
                    implementation("com.squareup:kotlinpoet:$kotlinpoet_version")
                    implementation("com.squareup:kotlinpoet-classinspector-elements:$kotlinpoet_version")
                    implementation("com.squareup:kotlinpoet-metadata:$kotlinpoet_version")
                    implementation("com.squareup:kotlinpoet-metadata-specs:$kotlinpoet_version")
                    implementation(kotlin("compiler-embeddable"))
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