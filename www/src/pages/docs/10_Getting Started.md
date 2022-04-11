---
title: Getting Started
description: "Learn how to get started with fritz2"
layout: layouts/docs.njk
permalink: /docs/start/
eleventyNavigation:
    key: start
    parent: documentation
    title: Getting Started
    order: 10
---
## Create a Project

To use fritz2, you have to set up a Kotlin multiplatform-project. To do so you can either
* [clone our template from GitHub](https://github.com/jwstegemann/fritz2-template)
* If you want to use fritz2 together with [tailwindcss](https://tailwindcss.com/) for the styling, clone
  our [tailwind specific template](https://github.com/jwstegemann/fritz2-tailwind-template) from GitHub instead.
* clone the [fritz2-examples](https://github.com/jamowei/fritz2-examples) and copy from one of the sub-projects
* have a look at
  the [official documentation](https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#setting-up-a-multiplatform-project)
  and use the following `build.gradle.kts` file:

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

val fritz2Version = "1.0-RC1"

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
                // implementation("dev.fritz2:headless:$fritz2Version") // optional
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

## Organize Your Code

When using the Kotlin Multiplatform project structure, we recommend organizing your code like this:

```txt
.
├── src
│   ├── commonMain
│   │   └── kotlin
│   │       └── <packages>
│   │           └── model.kt (common model for client (JS) and server (JVM))
│   └── jsMain
│       ├── kotlin
│       │   └── <packages>
│       │       └── app.kt  (contains main function)
│       └── resources
│           └── index.html  (starting point for your app)
├── build.gradle.kts  (dependencies and tasks)
└── settings.gradle.kts  (project name)
```

The `index.html` is just a normal web-page. Be sure to include the resulting script-file from your KotlinJS-build.
You can mark an element of your choice with an id (or use the body by default) to later render your fritz2 tags to it:

```html
<!doctype html>
<html>
  <head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1" name="viewport">
  </head>
  <body id="target">
    Loading...
    <script src="<project-name>.js"></script>
  </body>
</html>
```

`app.kt` is the starting point of your fritz2 app, so make sure it has a `main`-function.
Inside `main`, create some content by opening a
[render](https://www.fritz2.dev/api/core/dev.fritz2.core/render.html) context and
mount it to the DOM of your `index.html`:

```kotlin
fun main() {
    render("#target") { // using id selector here, leave blank to use document.body by default
        h1 { +"My App" }
        div("some-fix-css-class") {
            p(id = "someId") {
                +"Hello World!"
            }
        }
    }
}
```
![running example in browser](/img/gettingstarted_inital.png)

When calling `render` like that, your content will be mounted to an `HTMLElement` with `id="target"`.
If you want to mount your content to the `body` of your `index.html`, you can omit this parameter.
Instead of using the `selector` string with the [querySelector syntax](https://developer.mozilla.org/en-US/docs/Web/API/Document/querySelector),
you can also specify an `HTMLElement` directly on the `targetElement` parameter.
Setting the `override` parameter to `false` means that your content will be appended. By default, all child
elements will be removed before your content is appended to the `targetElement`.

Run the project by calling `./gradlew jsRun` in your project's main directory. Add `-t` to enable automatic
building and reloading in the browser after changing your code.

## Pre-release builds

If you want to use the newest snapshot-builds of fritz2 before we release them add the 
following lines to your `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") // new repository here
}

val fritz2Version = "1.0-SNAPSHOT" // set the newer snapshot version here
```

If you find any problems with these snapshot-versions please
[open an issue](https://github.com/jwstegemann/fritz2/issues/new/choose).