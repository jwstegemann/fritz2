[core](../index.md) / [dev.fritz2.lenses](./index.md)

## Package dev.fritz2.lenses

### Types

| (js, jvm) [Lens](-lens/index.md) | `interface Lens<P, T>` |
| (js, jvm) [WithId](-with-id/index.md) | `interface WithId` |

### Annotations

| (js, jvm) [Lenses](-lenses/index.md) | `annotation class Lenses` |

### Functions

| (js, jvm) [buildLens](build-lens.md) | `fun <P, T> buildLens(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, getter: (P) -> T, setter: (P, T) -> P): `[`Lens`](-lens/index.md)`<P, T>` |
| (js, jvm) [elementLens](element-lens.md) | `fun <T : `[`WithId`](-with-id/index.md)`> elementLens(element: T): `[`Lens`](-lens/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>` |

