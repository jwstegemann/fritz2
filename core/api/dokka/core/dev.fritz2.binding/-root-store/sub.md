[core](../../index.md) / [dev.fritz2.binding](../index.md) / [RootStore](index.md) / [sub](./sub.md)

# sub

(js) `fun <X> sub(lens: `[`Lens`](../../dev.fritz2.lenses/-lens/index.md)`<T, X>): `[`SubStore`](../-sub-store/index.md)`<T, T, X>`

create a [SubStore](../-sub-store/index.md) that represents a certain part of your data model.

### Parameters

`lens` - : a [Lens](../../dev.fritz2.lenses/-lens/index.md#dev.fritz2.lenses.Lens) describing, which part of your data model you will create [SubStore](../-sub-store/index.md) for. Use @Lenses to let your compiler
create the lenses for you or use the buildLens-factory-method.