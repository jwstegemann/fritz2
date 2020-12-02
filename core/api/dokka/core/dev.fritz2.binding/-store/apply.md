[core](../../index.md) / [dev.fritz2.binding](../index.md) / [Store](index.md) / [apply](./apply.md)

# apply

(js) `fun <A, X> apply(mapper: suspend (A) -> Flow<X>): `[`Applicator`](../-applicator/index.md)`<A, X>`

factory method, to create an [Applicator](../-applicator/index.md).

### Parameters

`mapper` - defines how to transform the given action into a new asynchronous [Flow](#), for example by calling a remote interface.