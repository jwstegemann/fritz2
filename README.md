![fritz2](https://www.fritz2.dev/images/fritz2_logo_grey.png)

[![Actions Status](https://github.com/jwstegemann/fritz2/workflows/build/badge.svg)](https://github.com/jwstegemann/fritz2/actions)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)
[![LOC](https://img.shields.io/badge/lines%20of%20code-1%2C5k-green)](https://clean-code-developer.com/grades/grade-1-red/#Keep_it_simple_stupid_KISS)
[![Download](https://api.bintray.com/packages/jwstegemann/fritz2/core/images/download.svg)](https://bintray.com/jwstegemann/fritz2/core/_latestVersion)

[![Examples](https://img.shields.io/badge/examples-showcase-yellow)](https://www.fritz2.dev/examples.html)
[![API](https://img.shields.io/badge/API-dokka-green)](https://api.fritz2.dev/core/)
[![Docs](https://img.shields.io/badge/doc-umentation-blue)](https://docs.fritz2.dev)
[![Slack](https://img.shields.io/badge/slack-channel-green?logo=slack)](https://fritz2workspace.slack.com/)
[![Discord](https://img.shields.io/discord/723492739819241513?color=7389D8&label=Chat&logo=discord&logoColor=ffffff)](https://discord.gg/UwQRH93)

fritz2 is an ***extremely lightweight***, well-performing, independent library to build reactive web apps in ***Kotlin*** heavily depending on coroutines and flows.

fritz2 includes an intuitive way to build and render html-elements using a type-safe dsl. You can easily create lightweight **reactive** html-components that are bound to an underlying model and **automatically** change whenever the model data changes:

```kotlin
val model = storeOf("init value")

render {
    div("some-css-class") {
        input {
            value = model.data
            changes.values() handledBy model.update 
        }
        p {
            +"model value = "
            store.data.bind()
        }
    }
}.mount("target")
```

fritz2 implements **precise data binding**. This means that when parts of your data model change, **exactly those** and ONLY those DOM-nodes depending on the changed parts will automatically change as well.

No intermediate layer (like a virtual DOM) is needed. fritz2 requires no additional methods to decide which parts of your component have to be re-rendered:

![State management in fritz2](https://www.fritz2.dev/static/fritz2_state.001.png)

Utilizing Kotlin's multiplatform-abilities, you'll write the code of your data classes only once and use it on your client and server (i.e. in a [SpringBoot](https://github.com/jamowei/fritz2-spring-todomvc)- or [Ktor](https://github.com/jamowei/fritz2-ktor-todomvc)-Backend). This is also true for your model-validation-code, which can quickly become far more complex than your data model.

Expect a flat learning curve - we chose Kotlin for its focus on writing clean and intuitive code, which makes working with fritz2 easy to learn.
fritz2 itself depends on only a handful of [concepts](https://docs.fritz2.dev) you have to master. The [core API](https://api.fritz2.dev/fritz2/) consists of about a dozen key objects and types offering only essential methods und functions. 

## How to try it?

* Take a look at our hosted [examples](https://www.fritz2.dev/examples.html)
* Or set up a new project on your own, following our [documentation](https://docs.fritz2.dev/Project.html)


## What we've got so far

- easy reactive one- and two-way-databinding
- even for lists and deep nested structures
- complete set of html5 elements, attributes and events
- hassle-free redux-like state-handling
- model-validation and message handling 
- routing (for SPAs, hash-based)
- [examples](https://www.fritz2.dev/examples.html) i.e. implementing the specification of [TodoMVC](http://todomvc.com/)
- backend-repositories (Rest APIs, LocalStorage, etc.)
- history / undo
- [documentation](https://docs.fritz2.dev)

## Overall Goals

- staying extremely lightweight (a few hundred lines of code for the core)
- keeping dependencies as low as possible (zero up to now!)
- generating elements, attributes, events for html from specification (w3c, mozilla, ...)

## Inspiration

fritz2 is hugely inspired by the great [Binding.scala](https://github.com/ThoughtWorksInc/Binding.scala). Later we discovered that a lot of those concepts are described independently in [Meiosis](https://meiosis.js.org/).

## Leave us a star...

If you like the idea of a lightweight pure Kotlin implementation for building reactive web apps, please give us a star on github. Thank you.
