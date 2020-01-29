import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

buildscript {

    repositories {
        mavenLocal()
        google()
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath("io.fritz2.optics:optics-plugin:1.0.0")

    }

}


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

apply(plugin = "io.fritz2.optics")

dependencies {
    implementation(kotlin("stdlib-js"))
    testImplementation(kotlin("test-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.2")
    implementation(project(":core"))
    implementation("io.fritz2.optics:core-js:0.0.1")
//    implementation(project(":optics:core", configuration = "jsRuntimeElements"))

}

