---
layout: layouts/docsWithContentNav.njk
title: Project Setup
permalink: /docs/setup/
eleventyNavigation:
    key: setup
    parent: documentation
    title: Project Setup
    order: 20
---

## Create a Project

To use fritz2, you have to set up a Kotlin multiplatform-project. To do so you can either
* [clone our template from github](https://github.com/jwstegemann/fritz2-template)
* clone the [fritz2-examples](https://github.com/jamowei/fritz2-examples) and copy from one of the sub-projects
* have a look a the [official documentation](https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#setting-up-a-multiplatform-project) and include the following plugin:

## Setup Gradle Build

```kotlin
plugins {
    kotlin("multiplatform") version "1.6.10"
    // KSP support
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
}

repositories {
    mavenLocal()
    mavenCentral()
}

val fritz2Version = "0.14"

//group = "my.fritz2.app"
//version = "0.0.1-SNAPSHOT"

kotlin {
    jvm()
    js(IR) {
        browser()
    }.binaries.executable()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("dev.fritz2:core:$fritz2Version")
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
    }
}

/**
 * KSP support - start
 */
dependencies {
    add("kspMetadata", "dev.fritz2:lenses-annotation-processor:$fritz2Version")
}
kotlin.sourceSets.commonMain { kotlin.srcDir("build/generated/ksp/commonMain/kotlin") }
tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
    if (name != "kspKotlinMetadata") dependsOn("kspKotlinMetadata")
}
// needed to work on Apple Silicon. Should be fixed by 1.6.20 (https://youtrack.jetbrains.com/issue/KT-49109#focus=Comments-27-5259190.0-0)
rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>().nodeVersion = "16.0.0"
}
/**
 * KSP support - end
 */
```

## Pre-release builds

If you want to use the newest snapshot-builds of fritz2 before we release them add the 
following lines to your `build.gradle.kts`:

```kotlin
plugins {
    kotlin("multiplatform") version "1.6.10"
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
}

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") // new repository here
}

val fritz2Version = "0.14-SNAPSHOT" // set the newer snapshot version here

//group = "my.fritz2.app"
//version = "0.0.1-SNAPSHOT"

kotlin {
    jvm()
    js(IR) {
        browser()
    }.binaries.executable()

    ...
}
```

## Report Problems

If you find any problems with these snapshot-versions please 
[open an issue](https://github.com/jwstegemann/fritz2/issues/new/choose).