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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.2")
    api("io.fritz2.optics:core-js:0.1")
}

