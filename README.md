# fritz2

[![Actions Status](https://github.com/jwstegemann/fritz2/workflows/build/badge.svg)](https://github.com/jwstegemann/fritz2/actions)
[![Extremely Lightweight](https://tokei.rs/b1/github/jwstegemann/fritz2?category=code)](http://todomvc.com/examples/fritz2/)
[![100% Kotlin](https://img.shields.io/badge/pure%20Kotlin-100%25-blue)](https://play.kotlinlang.org/)

A proof of concept for an ***extremely lightweight*** well-performing independent library for client-side ui in ***Kotlin*** heavily depending on coroutines and flows.

## How to try it that early?
Just checkout the project, import in your favourite IDE (or whatever you like), run `gradle run --continuous` and enjoy ;-). Have a look at `demo.kt` (and `oldDemo.kt).
But don't be too disappointed. There is not too much ready yet...

## What is there already?```

- easy reactive one- and two-way-databinding
- first few HTML-elements (`div`, `button`, `input`)
- first attributes
- first event-handlers (onClick, onChange)
- hassle-free redux-like state-handling (work-in-progress)

## What will come next?

- complete set of html-elements, attributtes and events
- set of high-level components to build fully functional, testable, beautiful single page webapps (using some css-lib yet to be chosen) including
  - routing
  - validation
  - server-communication
  - user auth
  - logging

## Overall Goals

- stay extremely lightweight (just a few hundred lines of code for the core)
- try to depend on as less libs as possible (hopefully only for lenses and css...)
- generating elements, attributes, events for html from specification (w3c, mozilla, ...)
