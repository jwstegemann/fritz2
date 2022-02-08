---
layout: layouts/docsWithContentNav.njk
title: Lenses
permalink: /docs/lenses/
eleventyNavigation:
    key: lenses
    parent: documentation
    title: Lenses
    order: 82
---

A `Lens` is basically a way to describe the relation between an outer and inner entity in a structure.
It focuses on the inner entity from the viewpoint of the outer entity, which is how it got its name.
Lenses are especially useful when using immutable data-types like fritz2 does.
A `Lens` needs to handle the following: 

  * Getting the value of the inner entity from a given instance of the outer entity
  * Creating a new instance of the outer entity (immutable!) as a copy of a given one with a different value only for the inner entity

In fritz2, a `Lens` is defined by the following interface:
```kotlin
interface Lens<P,T> {
    val id: String
    fun get(parent: P): T
    fun set(parent: P, value: T): P
}
```

You can easily use this interface by just implementing `get()` and `set()`. 
fritz2 also offers the method `lens()` for a short-and-sweet-experience:

```kotlin
val nameLens = lens("name", { it.name }, { person, value -> person.copy(name = value) })
```

No magic there. The first parameter sets an id for the `Lens`. When using `Lens`es with `SubStore`s, the `id` will be used to generate a valid html-id representing the path through your model.
This can be used to identify your elements semantically (for automated ui-tests for example).

If you have deep nested structures or a lot of them, you may want to automate this behavior. 
fritz2 offers an annotation `@Lenses` you can add to your data-classes in the `commonMain` source-set of 
your multiplatform project:
```kotlin
@Lenses
data class Person(val name: String, val value: String) {
    companion object // needs to be declared! The generated lens-factories are created within.
}
```
Using an annotation-processor, fritz2 builds factory methods for each public constructor property within the
companion object of the data class from these annotations which contains all the `Lens`es you need.
They are named exactly like the entities and properties, so it's easy to use:

```kotlin
val nameLens = Person.name()
```

You can see it in action at our [nestedmodel-example](https://examples.fritz2.dev/nestedmodel/build/distributions/index.html).

Keep in mind that your annotated classes have to be in your `commonMain` source-set
otherwise the automatic generation of the lenses will not work!

Have a look at the [validation-example](https://examples.fritz2.dev/validation/build/distributions/index.html) to see how to set it up.

This will also help you define a multiplatform project for sharing your model and validation code between 
the browser and backend.  