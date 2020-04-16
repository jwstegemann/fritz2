plugins {
    kotlin("js")
}

kotlin {
    target {
        browser {
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-js"))
    testImplementation(kotlin("test-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.4")
    implementation(project(":core"))
    // libary for handling dates
    implementation("com.soywiz.korlibs.klock:klock-js:1.10.5")
}
