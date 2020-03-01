# fritz2

[![Actions Status](https://github.com/jwstegemann/fritz2/workflows/build/badge.svg)](https://github.com/jwstegemann/fritz2/actions)
[![100% Kotlin](https://img.shields.io/badge/pure%20Kotlin-100%25-blue)](https://play.kotlinlang.org/)
[ ![Download](https://api.bintray.com/packages/jwstegemann/fritz2/fritz2-core/images/download.svg?version=0.1) ](https://bintray.com/jwstegemann/fritz2/fritz2-core/0.1/link)

fritz2 is a proof of concept for an ***extremely lightweight*** well-performing independent library for client-side ui in ***Kotlin*** heavily depending on coroutines and flows.

fritz2 includes an intuitive way to build and render html-elements using a type-safe dsl:

```kotlin
html {
  p {
    +"Hello World!"
  }
}.mount("target")
```

Using fritz2 you can easily create lightweight **reactive** html-components that are bound to an underlying model and **automatically** change, whenever the model data changes:

```kotlin
val model = RootStore<String>("init value")

val component = html {
    div {
        input {
            value = model.data
            model.update <= changes
        }
        p {
            +"model value = "
            store.data.bind()
        }
    }
}

component.mount("target")
```

fritz2 implements **precise data binding**. That means that exactly those (and **only** those) dom-nodes (elements, attributes, etc.) change, that depend on the parts of your data-model, that have changed. 
There is no intermediate layer needed like a virtual DOM and you do not have to implement any additional methodes to decide, which parts of your component have to be rerendered, when your data changes.
This makes it more efficient than the react-approach - at runtime and for development.

Utilizing Koltin's multiplatform-abilities, you have to write the code of your data classes just once and use it on your client and server (i.e. in a SpringBoot-Backend). This of course also true for your model-validation-code, that can become far more complex than your data model really fast.

The learning curve should be quite flat. We chose Kotlin as a language, that is easy to learn and has a focus on writing clean and intuitive code.
fritz2 itself depends on only a handfull of concepts you have to master. The core API consists of just about a dozen key objects and types offering only the methods und functions, that are really needed. You can have a quick look at our [API documentation (work in progress)](https://jwstegemann.github.io/fritz2/dokka/fritz2/) and convince yourself.  


## How to try it that early?
Your can either
* checkout the project, import in your favourite IDE (or whatever you like) and run `./gradlew :examples:gettingstarted:run` (or another example)
* set up a new project on your own following our [documentation](https://github.com/jwstegemann/fritz2/wiki/Project-Setup)


## What is there already?

- easy reactive one- and two-way-databinding
- even for lists and deep nested structures
- all html 5 elements
- complete set of attributes 
- all html 5 event-handlers (clicks, changes, ...)
- hassle-free redux-like state-handling
- model-validation and message handling 
- routing (for SPAs, hash-based)
- [documentation (work in progress)](https://github.com/jwstegemann/fritz2/wiki)

## What will come next?

- complete example (ToDoMVC)
- tests, tests, tests
- performance and memory optimizations
- streamlined build-process (DCE, etc.)
- server-communication (Rest APIs, etc.) [(work in progress)](https://github.com/jwstegemann/fritz2/pull/14)
- user auth (examle with OAuth)

## Overall Goals

- stay extremely lightweight (just a few hundred lines of code for the core)
- try to depend on as less libs as possible (zero up to now!)
- generating elements, attributes, events for html from specification (w3c, mozilla, ...)

## Inspiration

fritz2 is heavily inspired by the great [Binding.scala](https://github.com/ThoughtWorksInc/Binding.scala). Later I discovered that a lot of those concepts are described independently in [Meiosis](https://meiosis.js.org/).
