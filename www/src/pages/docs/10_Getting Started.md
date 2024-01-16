---
title: Getting Started
description: "How to get started with fritz2"
layout: layouts/docs.njk
permalink: /docs/start/
eleventyNavigation:
    key: start
    parent: documentation
    title: Getting Started
    order: 10
---
## Create a Project

To use fritz2, set up a Kotlin multiplatform-project using one of these options:
* [Clone our template from GitHub](https://github.com/jwstegemann/fritz2-template)
* If you want to use fritz2 together with [tailwindcss](https://tailwindcss.com/) for the styling, clone
  our [tailwind specific template](https://github.com/jwstegemann/fritz2-tailwind-template) from GitHub instead.
* Check out the [examples](https://fritz2.dev/examples) and see how to use the fritz2 features
* Have a look at the [official multiplatform documentation](https://kotlinlang.org/docs/multiplatform-get-started.html)
  and use the following `build.gradle.kts` file:

## Setup Gradle Build

```kotlin
plugins {
    kotlin("multiplatform") version "1.9.22"
    // KSP support needed for Lens generation
    id("com.google.devtools.ksp") version "1.9.22-1.0.16"
}

repositories {
    mavenCentral()
}

val fritz2Version = "{{ fritz2.version }}"

//group = "my.fritz2.app"
//version = "0.0.1-SNAPSHOT"

kotlin {
    jvm()
    js(IR) {
        browser()
    }.binaries.executable()

    sourceSets {
        commonMain {
            dependencies {
                implementation("dev.fritz2:core:$fritz2Version")
                // implementation("dev.fritz2:headless:$fritz2Version") // optional headless comp
            }
        }
        jvmMain {
            dependencies {
            }
        }
        jsMain {
            dependencies {
            }
        }
    }
}

/**
 * KSP support for Lens generation - start
 */
dependencies.kspCommonMainMetadata("dev.fritz2:lenses-annotation-processor:$fritz2Version")
kotlin.sourceSets.commonMain { tasks.withType<KspTaskMetadata> { kotlin.srcDir(destinationDirectory) } }
tasks.withType<KotlinCompilationTask<*>> { if (this !is KspTask) dependsOn(tasks.withType<KspTask>()) }
tasks.withType<AbstractArchiveTask> { dependsOn(tasks.withType<KspTask>()) }
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
    <script src="project-name.js"></script>
  </body>
</html>
```

`app.kt` is the starting point of your fritz2 app, so make sure it has a `main`-function.
Inside `main`, create some content by opening a
[render](https://www.fritz2.dev/api/core/dev.fritz2.core/render.html) context and
mounting it to the DOM of your `index.html`:

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

Run the project by calling `./gradlew jsRun` in your project's main directory. Add `-t` to enable automatic
building and reloading in the browser after changing your code.

## Pre-Release Builds

If you want to use the latest fritz2 snapshot-builds before we release them, add the 
following lines to your `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") // new repository here
}

val fritz2Version = "1.0-SNAPSHOT" // set the newer snapshot version here
```

If you encounter any problems with these snapshot-versions, please
[open an issue](https://github.com/jwstegemann/fritz2/issues/new/choose).