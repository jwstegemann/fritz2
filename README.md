# fritz2 - the potato among the ui-libs

[![Actions Status](https://github.com/jwstegemann/fritz2/workflows/build/badge.svg)](https://github.com/jwstegemann/fritz2/actions)
[![LOC](https://img.shields.io/badge/lines%20of%20code-1%2C5k-green)](https://clean-code-developer.com/grades/grade-1-red/#Keep_it_simple_stupid_KISS)
[![100% Kotlin](https://img.shields.io/badge/kotlin-100%25-blue)](https://play.kotlinlang.org/)
[![Examples](https://img.shields.io/badge/examples-showcase-yellow)](https://www.fritz2.dev/examples.html)
[![API](https://img.shields.io/badge/API-dokka-green)](https://api.fritz2.dev/fritz2/)
[![Docs](https://img.shields.io/badge/docs-wiki-blue)](https://docs.fritz2.dev)
[![Download](https://api.bintray.com/packages/jwstegemann/fritz2/fritz2-core/images/download.svg?version=0.3)](https://bintray.com/jwstegemann/fritz2/fritz2-core/0.3/link)

fritz2 is a proof of concept for an ***extremely lightweight*** well-performing independent library for client-side ui in ***Kotlin*** heavily depending on coroutines and flows.

fritz2 includes an intuitive way to build and render html-elements using a type-safe dsl. You can easily create lightweight **reactive** html-components that are bound to an underlying model and **automatically** change, whenever the model data changes:

```kotlin
val model = RootStore<String>("init value")

html {
    div("some-css-class") {
        input {
            value = model.data
            model.update <= changes.values()
        }
        p {
            text("model value = ")
            store.data.bind()
        }
    }
}.mount("target")
```

fritz2 implements **precise data binding**. That means that exactly those (and **only** those) dom-nodes (elements, attributes, etc.) change, that depend on the parts of your data-model, that have changed. 
There is no intermediate layer needed like a virtual DOM and you do not have to implement any additional methods to decide, which parts of your component have to be rerendered, when your data changes:

![State management in fritz2](https://www.fritz2.dev/static/fritz2_state.001.png)

Utilizing Koltin's multiplatform-abilities, you have to write the code of your data classes just once and use it on your client and server (i.e. in a [SpringBoot](https://spring.io/guides/gs/rest-service/)-Backend). This of course also true for your model-validation-code, that can become far more complex than your data model really fast.

The learning curve should be quite flat. We chose Kotlin as a language, that is easy to learn and has a focus on writing clean and intuitive code.
fritz2 itself depends on only a handful of [concepts](https://docs.fritz2.dev)) you have to master. The [core API](https://api.fritz2.dev/fritz2/) consists of just about a dozen key objects and types offering only the methods und functions, that are really needed. 

## How to try it?
You can either
* checkout the project, import it in your favourite IDE (or whatever you like) and run `./gradlew :examples:todomvc:run` (or another example)
* take a look at our hosted [examples](https://www.fritz2.dev/examples.html)
* set up a new project on your own following our [documentation](https://docs.fritz2.dev/Project.html)


## What is there already?

- easy reactive one- and two-way-databinding
- even for lists and deep nested structures
- complete set of html5 elements, attributes and events
- hassle-free redux-like state-handling
- model-validation and message handling 
- routing (for SPAs, hash-based)
- [examples](https://www.fritz2.dev/examples.html) i.e. implementing the specification of [TodoMVC](http://todomvc.com/)
- server-communication (Rest APIs, etc.) (work in progress)
- [documentation](https://docs.fritz2.dev) (work in progress)

## What will come next?

- performance and memory optimizations
- user auth (example with OAuth)

## Overall Goals

- stay extremely lightweight (just a few hundred lines of code for the core)
- try to depend on as less libs as possible (zero up to now!)
- generating elements, attributes, events for html from specification (w3c, mozilla, ...)

## Inspiration

fritz2 is heavily inspired by the great [Binding.scala](https://github.com/ThoughtWorksInc/Binding.scala).
Later I discovered that a lot of those concepts are described independently in [Meiosis](https://meiosis.js.org/).
