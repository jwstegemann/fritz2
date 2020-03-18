import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("js")
}

kotlin {
    target {
        browser {
            runTask {
                devServer = KotlinWebpackConfig.DevServer(
                    port = 9000,
                    contentBase = listOf("$projectDir/src/main/web")
                ).copy(proxy = mapOf(
                    "/get" to "http://postman-echo.com"
                ))
            }
        }
    }
}


dependencies {
    implementation(kotlin("stdlib-js"))
    testImplementation(kotlin("test-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.4")
    implementation(project(":core"))
}

