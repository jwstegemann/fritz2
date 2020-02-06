# fritz2

[![Actions Status](https://github.com/jwstegemann/fritz2/workflows/build/badge.svg)](https://github.com/jwstegemann/fritz2/actions)
[![Extremely Lightweight](https://tokei.rs/b1/github/jwstegemann/fritz2?category=code)](http://todomvc.com/examples/fritz2/)
[![100% Kotlin](https://img.shields.io/badge/pure%20Kotlin-100%25-blue)](https://play.kotlinlang.org/)

A proof of concept for an ***extremely lightweight*** well-performing independent library for client-side ui in ***Kotlin*** heavily depending on coroutines and flows.

## How to try it that early?
Just checkout the project, import in your favourite IDE (or whatever you like), run `./gradlew :examples:gettingstarted:run --continuous` and enjoy ;-). Have a look at the examples sub-project.
But don't be too disappointed. There is not too much ready yet...

## What is there already?

- easy reactive one- and two-way-databinding
- even for lists and deep nested structures
- all html 5 elements
- complete set of attributes 
- all event-handlers (clicks, changes, ...)
- hassle-free redux-like state-handling
- model-validation and message handling 

## What will come next?

- complete example (ToDoMVC)
- routing
- server-communication
- user auth
- documentation


## Overall Goals

- stay extremely lightweight (just a few hundred lines of code for the core)
- try to depend on as less libs as possible (zero up to now!)
- generating elements, attributes, events for html from specification (w3c, mozilla, ...)
