---
layout: layouts/docsWithContentNav.njk
title: Flows
permalink: /docs/flows/
eleventyNavigation:
    key: flows
    parent: documentation
    title: Flows
    order: 62
---

fritz2 heavily depends on flows, introduced by [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines).

A `Flow` is a time discrete stream of values. 

Like a collection, you can use `Flow`s to represent multiple values, but unlike other collections like `List`s, for example, 
the values are retrieved one by one. fritz2 relies on `Flow`s to represent values that change over time and lets you react to them (your data-model for example) .

A `Flow` is built from a source which creates the values. This source could be your model or the events raised by an element, 
for example. On the other end of the `Flow`, a simple function called for each element collects the values one by one. 
Between those two ends, various actions can be taken on the data (formatting strings, filtering the values, combining values, etc).

The great thing about `Flow`s is that they are _cold_, which means that nothing is calculated before the result is needed. 
This makes them perfect for fritz2's use case.

In Kotlin, there is another communication model called `Channel` which is the _hot_ counterpart of the `Flow`. 
fritz2 only uses `Channel`s internally to feed the flows, so you should not encounter them while using fritz2. 

To get more information about `Flow`s, `Channel`s, and their API, 
have a look at the [official documentation](https://kotlinlang.org/docs/reference/coroutines/flow.html).

Now you have seen how fritz2 handles events and the state of your app. 
Next, have a closer look at the [Store](Store.html)...
