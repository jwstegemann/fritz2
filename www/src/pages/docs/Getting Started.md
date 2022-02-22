---
layout: layouts/docsWithContentNav.njk
title: Getting Started
permalink: /docs/gettingStarted/
eleventyNavigation:
    key: gettingStarted
    parent: documentation
    title: Getting Started
    order: 30
---

We recommend organizing your source code like this:

```txt
<project-root>/
├── src/
│   ├── commonMain/
│   │   └── kotlin/
│   │       └── <packages>/
│   │           └── model.kt (common model for client (JS) and server (JVM))
│   └── jsMain/
│       ├── kotlin/
│       │   └── <packages>/
│       │       └── app.kt  (contains main function)
│       └── resources/
│           └── index.html  (starting point for your app)
├── build.gradle.kts  (dependencies and tasks)
└── settings.gradle.kts  (project name)
```

The `index.html` is just a normal web-page. Be sure to include the resulting script-file from your KotlinJS-build.
You can mark an element of your choice with an id (or use the body) to later mount your fritz2 elements to it:

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
[render](https://api.fritz2.dev/core/core/dev.fritz2.dom.html/render.html) context and 
mount it to the DOM of your `index.html`:

```kotlin
fun main() {
    render("#target") { // using id selector here, leave blank to use document.body
        h1 { +"My App" }
        div("some-fix-css-class") {
            p(id = "someId") {
                +"Hello World!"
            }
        }
    }
}
```
![components example basics](/img/gettingstarted_inital.png)

When calling `render` like that, your content will be mounted to an `HTMLElement` with `id="target"`. 
If you want to mount your content to the `body` of your `index.html`, you can omit this parameter. 
Instead of using the `selector` string with the [querySelector syntax](https://developer.mozilla.org/en-US/docs/Web/API/Document/querySelector), 
you can also specify an `HTMLElement` directly on the `targetElement` parameter. 
Setting the `override` parameter to `false` means that your content will be appended. By default, all child
elements will be removed before your content is appended to the `targetElement`.

Run the project by calling `./gradlew jsRun` in your project's main directory. Add `--continuous` to enable automatic
building and reloading in the browser after changing your code.

And voilà, you are done! Maybe you would like to create some more versatile [HTML](Attributes%20and%20CSS.html) now?