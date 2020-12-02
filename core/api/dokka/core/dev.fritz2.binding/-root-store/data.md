[core](../../index.md) / [dev.fritz2.binding](../index.md) / [RootStore](index.md) / [data](./data.md)

# data

(js) `open val data: Flow<T>`

the current value of a [RootStore](index.md) is derived be applying the updates on the internal channel one by one to get the next value.
the [Flow](#) only emit's a new value, when the value is differs from the last one to avoid calculations and updates that are not necessary.
This has to be a SharedFlow, because the updated should only be applied once, regardless how many depending values or ui-elements or bound to it.

