# fritz2

A proof of concept for an ***extemely lightweight*** well-performing independent library for client-side ui in ***Kotlin*** heavily depending on coroutines and flows.

## How to try it that early?
Just checkout the project, import in your favourite IDE (or whatever you like), run gradle run and enjoy. Have a look at `demo.kt`.
But don't be too diappointed. There is not too much ready yet...

## What is there already?

- reactive one-way-databinding
- first few HTML-elements (`div`, `button`)
- first attributes
- first event-handlers (onClick) (with ugly syntax, though)


## What will come next?

- efficient one-way-databinding for lists (`MultiMountPoint`)
- effortless two-way-databinding (`ViewModel`)
- redux-like state-handling
- set of high-level components to build fully functional, testable, beautiful single page webapps (using some css-lib yet to be chosen) including
  - routing
  - validation
  - server-communicaiton
  - user auth
  - logging

## Overall Goals

- stay extremely lightweight (just a few hundred lines of code for the core)
- try to depend on as less libs as possible (hopefully only for lenses and css...)
- generating elements, attributes, events for html from specification (w3c, mozilla, ...)
