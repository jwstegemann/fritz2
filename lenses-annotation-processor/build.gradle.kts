plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    js(BOTH).browser()

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
    }
}

apply(from = "$rootDir/publishing.gradle.kts")