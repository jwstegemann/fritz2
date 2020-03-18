[fritz2](../../index.md) / [io.fritz2.binding](../index.md) / [Applicator](index.md) / [andThen](./and-then.md)

# andThen

`infix fun andThen(nextHandler: `[`Handler`](../-handler/index.md)`<X>): `[`Handler`](../-handler/index.md)`<A>`
`infix fun <Y> andThen(nextApplicator: `[`Applicator`](index.md)`<X, Y>): `[`Applicator`](index.md)`<A, Y>`