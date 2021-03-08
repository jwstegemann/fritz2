plugins {
    kotlin("multiplatform")
//    kotlin("kapt")
    id("maven-publish")
}

kotlin {
    jvm()
    js(BOTH).nodejs()

    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
        val jvmMain by getting {
            dependencies {
                api(kotlin("stdlib"))
                api(project(":core"))
                api(kotlin("stdlib-jdk8"))
                api("com.squareup:kotlinpoet:${rootProject.ext["kotlinpoetVersion"]}")
                api("com.squareup:kotlinpoet-classinspector-elements:${rootProject.ext["kotlinpoetVersion"]}")
                api("com.squareup:kotlinpoet-metadata:${rootProject.ext["kotlinpoetVersion"]}")
                api("com.squareup:kotlinpoet-metadata-specs:${rootProject.ext["kotlinpoetVersion"]}")
//                    compileOnly("net.ltgt.gradle.incap:incap:${incap_version}")
//                    configurations.get("kapt").dependencies.add(compileOnly("net.ltgt.gradle.incap:incap-processor:${incap_version}"))
//                    implementation(kotlin("compiler-embeddable"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("org.assertj:assertj-core:3.19.0")
                implementation("com.github.tschuchortdev:kotlin-compile-testing:1.3.6")
            }
        }
    }
}

publishing {
    repositories {
        maven {
            name = "bintray"
            val releaseUrl = "https://api.bintray.com/maven/jwstegemann/fritz2/${project.name}/;" +
                    "publish=0;" + // Never auto-publish to allow override.
                    "override=1"
            val snapshotUrl = "https://oss.jfrog.org/artifactory/oss-snapshot-local"
            val isRelease = System.getenv("GITHUB_EVENT_NAME").equals("release", true)

            url = uri(if (isRelease && !version.toString().endsWith("SNAPSHOT")) releaseUrl else snapshotUrl)

            credentials {
                username = "jwstegemann"
                password = System.getenv("BINTRAY_API_KEY")
            }
        }
    }
}
