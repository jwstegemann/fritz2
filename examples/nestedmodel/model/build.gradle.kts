
buildscript {
    repositories {
        mavenLocal()
        google()
        jcenter()
        mavenCentral()
        maven {
            name = "bintray"
            url = uri("https://dl.bintray.com/jwstegemann/fritz2/")
        }
    }

    dependencies {
        classpath("io.fritz2.optics:plugin:0.1")
    }
}


plugins {
    kotlin("js")
}

kotlin {
    target {
        browser {
        }
    }

tasks.register("compileGenerated", org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile::class) {
        sourceSets {
            create("generated") {
                kotlin.srcDir("${buildDir}/src/generated/kotlin/")
                dependsOn(sourceSets.getByName("main"))
            }
         }
    }
}



//task("build").finalizedBy("compileGenerated")

apply(plugin = "io.fritz2.optics")

dependencies {
    implementation(kotlin("stdlib-js"))
    api("io.fritz2.optics:core-js:0.1")
}



