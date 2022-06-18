![fritz2](https://www.fritz2.dev/img/fritz2_header.png)

[![Actions Status](https://github.com/jwstegemann/fritz2/workflows/build/badge.svg)](https://github.com/jwstegemann/fritz2/actions)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)
[![Download](https://img.shields.io/maven-central/v/dev.fritz2/core)](https://search.maven.org/search?q=g:dev.fritz2)
[![IR](https://img.shields.io/badge/Kotlin%2FJS-IR%20supported-yellow)](https://kotl.in/jsirsupported)
[![Kotlin](https://img.shields.io/badge/kotlin-1.6-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Examples](https://img.shields.io/badge/examples-showcase-yellow)](https://examples.fritz2.dev/)
[![API](https://img.shields.io/badge/API-dokka-green)](https://fritz2.dev/api)
[![Docs](https://img.shields.io/badge/docs-online-violet)](https://fritz2.dev/docs)
[![Slack chat](https://img.shields.io/badge/kotlinlang-%23fritz2-B37700?logo=slack)](https://kotlinlang.slack.com/messages/fritz2)

fritz2 is an ***extremely lightweight***, well-performing, independent library to build 
reactive web apps in ***Kotlin*** heavily depending on coroutines and flows.

fritz2 includes an intuitive way to build and render html-elements using a type-safe dsl. 
You can easily create lightweight **reactive** html-components that are bound to an underlying model 
and **automatically** change whenever the model data changes:

```kotlin
val model = storeOf("init value")

render {
    div("some-css-class") {
        input {
            value(model.data)
            changes.values() handledBy model.update 
        }
        p {
            +"model value = "
            model.data.renderText()
        }
    }
}
```

fritz2 implements **precise data binding**. This means that when parts of your data model change, 
**exactly those** and only those DOM-nodes depending on the changed parts will automatically change as well. 
No intermediate layer (like a virtual DOM) is needed. fritz2 requires no additional methods to decide 
which parts of your component have to be re-rendered. 
fritz2 also supports **two-way data binding** out-of-the-box to update your model by listening on events:

![State management in fritz2](https://fritz2.dev/img/fritz2_cycle_of_life.png)

Utilizing Kotlin's multiplatform-abilities, you'll write the code of your data classes only once and use 
it on your client and server (i.e. in a [SpringBoot](https://github.com/jamowei/fritz2-spring-todomvc)- or 
[Ktor](https://github.com/jamowei/fritz2-ktor-todomvc)-Backend). 
This is also true for your model-validation-code, which can quickly become far more complex than your data model.

## Key Features

- easy reactive one- and two-way data binding (even for lists and deep nested structures)
- hassle-free state-handling
- model-validation and message handling
- http and websockets
- hash-based routing
- history / undo
- processing state ("spinning wheel")
- backend-repositories (Rest APIs, WebSockets, LocalStorage, etc.)
- webcomponents
- easy to learn
- [documentation](https://fritz2.dev/docs)
- [examples](https://examples.fritz2.dev/) i.e. implementing the specification of [TodoMVC](http://todomvc.com/)

## How to try it?

* Take a look at our hosted [examples](https://examples.fritz2.dev/)
* Or set up a new project on your own, following our [documentation](https://www.fritz2.dev/docs/start/)

## Overall Goals

- staying lightweight
- keeping dependencies as low as possible
- providing tags, attributes, events for html from specification (w3c, mozilla, ...)
- make it as easy as possible to write reactive web-apps in pure kotlin

## Inspiration

fritz2 is hugely inspired by the great [Binding.scala](https://github.com/ThoughtWorksInc/Binding.scala) framework. 
Later we discovered that a lot of those concepts are described independently in [Meiosis](https://meiosis.js.org/).
Also, fritz2 relies heavily on the great Kotlin [coroutines](https://github.com/Kotlin/kotlinx.coroutines) library.

## Leave us a star...

If you like the idea of a lightweight pure Kotlin implementation for building reactive web-apps, 
please give us a star. Thank you!
