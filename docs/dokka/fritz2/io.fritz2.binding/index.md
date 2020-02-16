[fritz2](../index.md) / [io.fritz2.binding](./index.md)

## Package io.fritz2.binding

### Types

| [Applicator](-applicator/index.md) | `class Applicator<A, X>` |
| [Const](-const/index.md) | `class Const<T> : Flow<T>` |
| [Failable](-failable/index.md) | `interface Failable : withId` |
| [Handler](-handler/index.md) | `class Handler<A>` |
| [MapRoute](-map-route/index.md) | [MapRoute](-map-route/index.md) marshals and unmarshals a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) to and from *window.location.hash*. It is like using url parameters with pairs of key and value. In the begin there is only a **#** instead of **?**.`class MapRoute : `[`Route`](-route/index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>` |
| [MultiMountPoint](-multi-mount-point/index.md) | `abstract class MultiMountPoint<T>` |
| [Patch](-patch/index.md) | `data class Patch<out T>` |
| [RootStore](-root-store/index.md) | `open class RootStore<T> : `[`Store`](-store/index.md)`<T>` |
| [Route](-route/index.md) | A Route is a abstraction for routes which needed for routing`interface Route<T>` |
| [Router](-router/index.md) | Router register the event-listener for hashchange-event and handles route-changes. Therefore it uses a [Route](-route/index.md) object which can [Route.marshal](-route/marshal.md) and [Route.unmarshal](-route/unmarshal.md) the given type.`open class Router<T>` |
| [Seq](-seq/index.md) | `class Seq<T>` |
| [SingleMountPoint](-single-mount-point/index.md) | `abstract class SingleMountPoint<T>` |
| [Store](-store/index.md) | `abstract class Store<T>` |
| [StringRoute](-string-route/index.md) | [StringRoute](-string-route/index.md) is a simple [Route](-route/index.md) which marshals and unmarshals nothing.`class StringRoute : `[`Route`](-route/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [SubStore](-sub-store/index.md) | `class SubStore<R, P, T> : `[`Store`](-store/index.md)`<T>` |
| [Update](-update.md) | `typealias Update<T> = (T) -> T` |
| [Validation](-validation/index.md) | `interface Validation<D, M : `[`Failable`](-failable/index.md)`, T>` |
| [Validator](-validator/index.md) | `abstract class Validator<D, M : `[`Failable`](-failable/index.md)`, T>` |

### Extensions for External Classes

| [kotlinx.coroutines.flow.Flow](kotlinx.coroutines.flow.-flow/index.md) |  |

### Functions

| [decodeURIComponent](decode-u-r-i-component.md) | `fun decodeURIComponent(encodedURI: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [eachStore](each-store.md) | `fun <T : withId> `[`RootStore`](-root-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.eachStore(): `[`Seq`](-seq/index.md)`<`[`SubStore`](-sub-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>>`<br>`fun <R, P, T : withId> `[`SubStore`](-sub-store/index.md)`<R, P, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.eachStore(): `[`Seq`](-seq/index.md)`<`[`SubStore`](-sub-store/index.md)`<R, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>>` |
| [encodeURIComponent](encode-u-r-i-component.md) | `fun encodeURIComponent(decodedURI: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [routing](routing.md) | Creates a new simple [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) based [Router](-router/index.md)`fun routing(default: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Router`](-router/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>Creates a new [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) based [Router](-router/index.md)`fun routing(default: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`Router`](-router/index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>`<br>Creates a new type based [Router](-router/index.md). Therefore the given type must implement the [Route](-route/index.md) interface.`fun <T> routing(default: `[`Route`](-route/index.md)`<T>): `[`Router`](-router/index.md)`<T>` |
| [select](select.md) | Select return a [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html) of the value and the complete routing [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) for the given key in the [mapper](select.md#io.fritz2.binding$select(io.fritz2.binding.Router((kotlin.collections.Map((kotlin.String, )))), kotlin.String, kotlin.Function1((kotlin.Pair((kotlin.String, kotlin.collections.Map((, )))), io.fritz2.binding.select.X)))/mapper) function.`fun <X> `[`Router`](-router/index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>.select(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, mapper: (`[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>) -> X): Flow<X>` |
| [sub](sub.md) | `fun <T : withId> `[`RootStore`](-root-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.sub(element: T): `[`SubStore`](-sub-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>`<br>`fun <R, P, T : withId> `[`SubStore`](-sub-store/index.md)`<R, P, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.sub(element: T): `[`SubStore`](-sub-store/index.md)`<R, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>` |

