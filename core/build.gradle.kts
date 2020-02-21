plugins {
    kotlin("js")
    `maven-publish`
    id("org.jetbrains.dokka")
}

repositories {
    jcenter() // or maven { url 'https://dl.bintray.com/kotlin/dokka' }
}

//TODO: add DCE and closure-compiler
kotlin {
    target {
        browser {
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "fritz2-core-js"
            from(components["kotlin"])
        }
        //FIXME: transitive dependencies
        //TODO: jars for source and docs
    }
}

dependencies {
    implementation(kotlin("stdlib-js"))
    testImplementation(kotlin("test-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.3")
    api("io.fritz2.optics:core-js:0.1")
}

tasks {
    test {
        testTasks.forEach {
            it.testLogging.showExceptions = true
            it.testLogging.showStandardStreams = true
            it.testLogging.minGranularity = 3
            it.addTestListener(object : TestListener {
                override fun beforeTest(testDescriptor: TestDescriptor?) {
                }

                override fun beforeSuite(suite: TestDescriptor?) {
                }

                override fun afterTest(testDescriptor: TestDescriptor?, result: TestResult?) {
                }

                override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                    if (suite.parent == null) { // root suite
                        logger.lifecycle("----")
                        logger.lifecycle("Test result: ${result.resultType}")
                        logger.lifecycle("Test summary: ${result.testCount} tests, " +
                                "${result.successfulTestCount} succeeded, " +
                                "${result.failedTestCount} failed, " +
                                "${result.skippedTestCount} skipped")
                    }
                }
            })
        }
    }
}

