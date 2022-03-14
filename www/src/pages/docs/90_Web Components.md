---
title: Web Components
description: "Learn how to create and use Web Components in fritz2"
layout: layouts/docs.njk
permalink: /docs/web-components/
eleventyNavigation:
    key: web-components
    parent: documentation
    title: Web Components
    order: 90
---

With fritz2, you can easily use [WebComponents](https://webcomponents.org) in any html-context. 
Some of the following code-snippets are not runnable on their own. Please find the complete example 
[here](https://examples.fritz2.dev/webcomponent/build/distributions/index.html).

```kotlin
render {
    div("weather-card") {
        h2 { "Goslar" }
        custom("m3-stars") {
            attr("max", "5")
            attr("current", "3.5")
        }
        // ...
    }
}
```

Before you can use a custom element, you have to add the component to your site's scripts.
One way is adding a script link pointing to the component which is hosted somewhere you can access it:
```html
<script type="module" src="https://unpkg.com/@mat3e-ux/stars"></script>
```

If the component you want to use is published on [npm](https://www.npmjs.com/), you can add it as a dependency in your Gradle-build:

```kotlin
dependencies {
    implementation(kotlin("stdlib-js"))
    // ...
    implementation(npm("@mat3e-ux/stars"))
}
```

... and import it in your Kotlin-Code:

```kotlin
@JsModule("@mat3e-ux/stars")
@JsNonModule
abstract external class Stars : HTMLElement
```

Please see the [official documentation](https://kotlinlang.org/docs/js-modules.html#apply-jsmodule-to-packages) for more details on this.

Depending on how the component is internally built, you might have to register it with the browser:

```kotlin
fun main() {
    window.customElements.define("m3-stars", Stars::class.js.unsafeCast<() -> dynamic>())
    // ...
}
```

For obvious reasons we cannot provide typesafe attributes for custom elements, but you can implement a `Tag` and provide an extension function for `RenderContext`:

```kotlin
class M3Stars(job: Job) : Tag<HTMLElement>("m3-stars", job = job), WithText<HTMLElement> {
    fun max(value: Flow<Int>) = attr("max", value.asString())
    fun current(value: Flow<Float>) = attr("current", value.asString())
}

fun RenderContext.m3Stars(content: M3Stars.() -> Unit): M3Stars = register(M3Stars(job), content)
```

## Build a WebComponent

To build a WebComponent with fritz2, two steps are neccessary. First, implement your WebComponent-class: 

```kotlin
object MyComponent : WebComponent<HTMLParagraphElement>() {
    override fun RenderContext.init(element: HTMLElement, shadowRoot: ShadowRoot): HtmlTag<HTMLParagraphElement> {
        return p {
            +"I am a WebComponent"
        }
    }
}
```

Next, register your component:

```kotlin
fun main() {
    registerWebComponent("my-component", MyComponent)
}
```

To observe one or more arguments, just add them to the registration:

```kotlin
registerWebComponent("my-component", MyComponent, "first-attr", "second-attr")
```

You can then use the values of these observed attributes in your init-method as a `Flow`:

```kotlin
val first = MyComponent.attributeChanges("first-attr")

render {
    first.render { firstAttr ->
        p { +firstAttr }
    }
}
```

To react to the lifecycle of your component, you can override the according methods from the specification.

Packaging (i.e. as a npm-package) and publishing is out of scope of this documentation.

Again, to see it in action, please have a look at our [webcomponents example](https://examples.fritz2.dev/webcomponent/build/distributions/index.html).

