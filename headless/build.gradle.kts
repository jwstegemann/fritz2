plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
}

kotlin {
    jvm()
    js(IR).browser {
        testTask {
            useKarma {
//                useSafari()
//                useFirefox()
//                useChrome()
                useChromeHeadless()
//                usePhantomJS()
            }
        }
        // just to have a place to copy it from...
        /*
        runTask {
            devServer?.apply {
                port = 9000
                proxy?.apply {
                    put("/members", "http://localhost:8080")
                    put("/chat", mapOf(
                        "target" to "ws://localhost:8080",
                        "ws" to true
                    ))
                }
            }
        }
        */
    }
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.ExperimentalStdlibApi")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
        val commonMain by getting {
            dependencies {
                api(project(":core"))
            }
        }
        val jsMain by getting {
            dependencies {
                api(npm("@floating-ui/dom","_"))
                api(npm("scroll-into-view-if-needed", "_"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(Kotlin.test.js)
            }
        }
    }
}

// Generates a javadoc jar from dokka html sources
val dokkaJavadocJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles java doc to jar"
    archiveClassifier.set("javadoc")
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
}

apply(from = "$rootDir/publishing.gradle.kts")