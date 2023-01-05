---
title: Web Components
description: "Learn how to create and use Web Components in fritz2"
layout: layouts/docs.njk
permalink: /docs/web-components/
eleventyNavigation:
    key: web-components
    parent: documentation
    title: Web Components
    order: 100
---

With fritz2, you can easily use [WebComponents](https://webcomponents.org) in any HTML-context. 
Some of the following code-snippets are not runnable on their own. Please find the complete example 
[here](/examples/webcomponent).

### Import as script

Before you can use a custom HTML element, you have to add the component to your site's scripts.
One way is adding a `<script>` tag which points to the component in your HTML file:
```html
<script type="module" src="https://unpkg.com/@mat3e-ux/stars"></script>
```
then you can use the `custom()` tag to render the imported webcomponent:
```kotlin
render {
    custom("m3-stars") {
        attr("max", "5")
        attr("current", "3.5")
    }   
}
```

### Import using Gradle

If the component you want to use is published on [npm](https://www.npmjs.com/), you can add it as a dependency 
in your `gradle.build.kts`:
```kotlin
dependencies {
    implementation(npm("@mat3e-ux/stars", "0.2.5"))
}
```
and import it in your Kotlin-Code:
```kotlin
@JsNonModule
@JsModule("@mat3e-ux/stars")
external object Stars
```

Please see the [official documentation](https://kotlinlang.org/docs/js-modules.html#apply-jsmodule-to-packages) 
for more details on this.

For obvious reasons we cannot provide typesafe attributes for custom elements, 
but you can implement a `HtmlTag` and provide an extension function for `RenderContext`:

```kotlin
class M3Stars(job: Job, scope: Scope) : HtmlTag<HTMLElement>("m3-stars", job = job, scope = scope) {
    fun max(value: Flow<Int>) = attr("max", value.asString())
    fun max(value: Int) = attr("max", value)
    fun current(value: Flow<Float>) = attr("current", value.asString())
    fun current(value: Float) = attr("current", value)
}

fun RenderContext.m3Stars(content: M3Stars.() -> Unit): M3Stars = register(M3Stars(job, scope), content)
```

## Build a WebComponent

To build a WebComponent with fritz2, two steps are necessary. First, implement your WebComponent class:
```kotlin
object WeatherCard : WebComponent<HTMLDivElement>() {

    private val city: Flow<String> = attributeChanges("city")

    override fun RenderContext.init(element: HTMLElement, shadowRoot: ShadowRoot): HtmlTag<HTMLDivElement> =
        div("weather-card") {
            h2 { city.renderText() }
            // ...
        }
}
```
We also add an observed attribute named `city` which then can be set by the user of our component.
Next, register your component:
```kotlin
fun main() {
    registerWebComponent("weather-card", WeatherCard, "city")
}
```
Here you can see that we observe the `city` which must be added to the registration.

To react to the lifecycle of your component, you can override the according methods from the specification.

Packaging (i.e. as a npm-package) and publishing is out of scope of this documentation.

Again, to see it in action, please have a look at our [webcomponents example](/examples/webcomponent).

