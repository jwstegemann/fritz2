[fritz2](../../index.md) / [io.fritz2.binding](../index.md) / [Applicator](./index.md)

# Applicator

`@FlowPreview @ExperimentalCoroutinesApi class Applicator<A, X>`

### Constructors

| [&lt;init&gt;](-init-.md) | `Applicator(execute: suspend (A) -> Flow<X>)` |

### Properties

| [execute](execute.md) | `val execute: suspend (A) -> Flow<X>` |

### Functions

| [andThen](and-then.md) | `infix fun andThen(nextHandler: `[`Handler`](../-handler/index.md)`<X>): `[`Handler`](../-handler/index.md)`<A>`<br>`infix fun <Y> andThen(nextApplicator: `[`Applicator`](./index.md)`<X, Y>): `[`Applicator`](./index.md)`<A, Y>` |

