[core](../../index.md) / [dev.fritz2.binding](../index.md) / [Applicator](./index.md)

# Applicator

(js) `class Applicator<A, X>`

### Constructors

| (js) [&lt;init&gt;](-init-.md) | `Applicator(execute: suspend (A) -> Flow<X>)` |

### Properties

| (js) [execute](execute.md) | `val execute: suspend (A) -> Flow<X>` |

### Functions

| (js) [andThen](and-then.md) | `infix fun andThen(nextHandler: `[`Handler`](../-handler/index.md)`<X>): `[`SimpleHandler`](../-simple-handler/index.md)`<A>`<br>`infix fun <Y> andThen(nextApplicator: `[`Applicator`](./index.md)`<X, Y>): `[`Applicator`](./index.md)`<A, Y>` |

