import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("js")
}

//TODO: add DCE and closure-compiler
kotlin {
    target {
        browser {
            runTask {
                devServer = KotlinWebpackConfig.DevServer(
                    port = 9000,
                    contentBase = listOf("$projectDir/src/main/web")
                )
            }
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-js"))
    testImplementation(kotlin("test-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.3")
    implementation(project(":core"))
    implementation(project(":examples:nestedmodel:model"))
}
