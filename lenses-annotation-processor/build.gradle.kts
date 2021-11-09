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
                api("com.squareup:kotlinpoet:${rootProject.ext["kotlinpoetVersion"]}")
                api("com.squareup:kotlinpoet-classinspector-elements:${rootProject.ext["kotlinpoetVersion"]}")
                api("com.squareup:kotlinpoet-metadata:${rootProject.ext["kotlinpoetVersion"]}")
                api("com.squareup:kotlinpoet-metadata-specs:${rootProject.ext["kotlinpoetVersion"]}")
                api(kotlin("reflect:1.5.0"))
                api(kotlin("script-runtime:1.5.0"))
//                    compileOnly("net.ltgt.gradle.incap:incap:${incap_version}")
//                    configurations.get("kapt").dependencies.add(compileOnly("net.ltgt.gradle.incap:incap-processor:${incap_version}"))
//                    implementation(kotlin("compiler-embeddable"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("org.assertj:assertj-core:3.19.0")
                implementation("com.github.tschuchortdev:kotlin-compile-testing:${rootProject.ext["compileTestingVersion"]}")
            }
        }
    }
}

if (JavaVersion.current() >= JavaVersion.VERSION_16) {
    tasks.withType<Test>().all {
        jvmArgs(
            "--add-opens=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.jvm=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
        )
    }
}

apply(from = "$rootDir/publishing.gradle.kts")