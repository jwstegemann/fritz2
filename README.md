# fritz2

[![Actions Status](https://github.com/jwstegemann/fritz2/workflows/build/badge.svg)](https://github.com/jwstegemann/fritz2/actions)
[![Extremely Lightweight](https://tokei.rs/b1/github/jwstegemann/fritz2?category=code)](http://todomvc.com/examples/fritz2/)
[![100% Kotlin](https://img.shields.io/badge/pure%20Kotlin-100%25-blue)](https://play.kotlinlang.org/)

A proof of concept for an ***extremely lightweight*** well-performing independent library for client-side ui in ***Kotlin*** heavily depending on coroutines and flows.

Using fritz2 you can easily create lightweight **reactive** html-components (using an intuitive dsl) that **automatically** change, whenever the underlying model data they are bound to changes.

fritz2 implements **precise data binding**. That means that exactly those dom-nodes (and **only** those) change, that depend on the parts of your data-model, that have changed. 
There is no intermediate layer needed like a virtual DOM and you do not have to implement methodes to decide, which parts of your component have to be rerendered, when your data changes.
This makes it more efficient than the react-approach - at runtime and for development.

The learning curve should be quite flat. We chose Kotlin as a language, that is easy to learn and has a focus on writing clean and intuitive code.
fritz2 itself only depends on only a handfull concepts you have to master. The core API consists of just about a dozen key objects and types offering only the methods und functions, that are really needed. You can have a quick look at our [API documentation (work in progress)](https://jwstegemann.github.io/fritz2/dokka/fritz2/) and convince yourself.  


## How to try it that early?
Your can either
* checkout the project, import in your favourite IDE (or whatever you like) and run `./gradlew :examples:gettingstarted:run` (or another example)
* set up a new project on your own, using one of our examples as a template

## What is there already?

- easy reactive one- and two-way-databinding
- even for lists and deep nested structures
- all html 5 elements
- complete set of attributes 
- all html 5 event-handlers (clicks, changes, ...)
- hassle-free redux-like state-handling
- model-validation and message handling 

## What will come next?

- documentation
- complete example (ToDoMVC)
- routing
- server-communication (Rest APIs, etc.)
- user auth (examle with OAuth)

## Overall Goals

- stay extremely lightweight (just a few hundred lines of code for the core)
- try to depend on as less libs as possible (zero up to now!)
- generating elements, attributes, events for html from specification (w3c, mozilla, ...)

## Inspiration

fritz2 is heavily inspired by the great [Binding.scala](https://github.com/ThoughtWorksInc/Binding.scala). Later I discovered that a lot of those concepts are described independently in [Meiosis](https://meiosis.js.org/).