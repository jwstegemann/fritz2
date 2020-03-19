plugins {
    kotlin("js")
    `maven-publish`
    id("org.jetbrains.dokka")
}

repositories {
    jcenter()
}

kotlin {
    target {
        browser {
        }
    }
}

publishing {
    repositories {
        maven {
            name = "bintray"
            val bintrayUsername = "jwstegemann"
            val bintrayRepoName = "fritz2"
            val bintrayPackageName = "fritz2-core"
            setUrl(
                "https://api.bintray.com/maven/" +
                        "$bintrayUsername/$bintrayRepoName/$bintrayPackageName/;" +
                        "publish=0;" + // Never auto-publish to allow override.
                        "override=1"            )
            credentials {
                username = "jwstegemann"
                password = System.getProperty("BINTRAY_API_KEY")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            artifactId = "fritz2-core-js"
            from(components["kotlin"])
            artifact(tasks.kotlinSourcesJar.get()) {
                classifier = "sources"
            }
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-js"))
    testImplementation(kotlin("test-js"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.4")
    api("io.fritz2.optics:core-js:0.1")
}

tasks {
    test {
        testTasks.forEach {
            it.testLogging.showExceptions = true
            it.testLogging.showStandardStreams = true
            it.testLogging.minGranularity = 3
            it.testLogging.error {
                this.events(org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED)
            }
            it.addTestListener(object : TestListener {
                override fun beforeTest(testDescriptor: TestDescriptor?) {
                }

                override fun beforeSuite(suite: TestDescriptor?) {
                }

                override fun afterTest(testDescriptor: TestDescriptor?, result: TestResult?) {
                }

                override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                    if (suite.parent == null) { // root suite
                        val summary = "| Test summary: ${result.testCount} tests, " +
                                "${result.successfulTestCount} succeeded, " +
                                "${result.failedTestCount} failed, " +
                                "${result.skippedTestCount} skipped"
                        val line = "+".padEnd(summary.length,'-') + "-+"
                        logger.lifecycle(line)
                        logger.lifecycle("| Test result: ${result.resultType}".padEnd(summary.length, ' ') + " |")
                        logger.lifecycle(summary + " |")
                        logger.lifecycle(line)
                    }
                }
            })
        }
    }
}
