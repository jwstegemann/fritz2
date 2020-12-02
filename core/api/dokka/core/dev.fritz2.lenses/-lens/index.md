[core](../../index.md) / [dev.fritz2.lenses](../index.md) / [Lens](./index.md)

# Lens

(js, jvm) `interface Lens<P, T>`

### Properties

| (js, jvm) [_id](_id.md) | `abstract val _id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| (js, jvm) [apply](apply.md) | `open fun apply(parent: P, mapper: (T) -> T): P` |
| (js, jvm) [get](get.md) | `abstract fun get(parent: P): T` |
| (js, jvm) [plus](plus.md) | `open operator fun <X> plus(other: `[`Lens`](./index.md)`<T, X>): `[`Lens`](./index.md)`<P, X>` |
| (js, jvm) [set](set.md) | `abstract fun set(parent: P, value: T): P` |

