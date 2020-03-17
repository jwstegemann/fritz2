[fritz2](../../index.md) / [io.fritz2.dom](../index.md) / [Listener](./index.md)

# Listener

`@FlowPreview @ExperimentalCoroutinesApi inline class Listener<E : `[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, X : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`>`

[Listener](./index.md) handles a Flow of [Action](../-action/index.md)s and gives
the [Event](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html) with [events](events.md) as Flow or
the targeting [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html) with [targets](targets.md) also as Flow back.
If you don't need either the [Event](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html) or [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html) you can call the [Listener](./index.md)
directly (e.g. `clicks()`) to get an Flow of [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) instead.

### Constructors

| [&lt;init&gt;](-init-.md) | [Listener](./index.md) handles a Flow of [Action](../-action/index.md)s and gives the [Event](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html) with [events](events.md) as Flow or the targeting [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html) with [targets](targets.md) also as Flow back. If you don't need either the [Event](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html) or [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html) you can call the [Listener](./index.md) directly (e.g. `clicks()`) to get an Flow of [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) instead.`Listener(actions: Flow<`[`Action`](../-action/index.md)`<E, X>>)` |

### Properties

| [actions](actions.md) | `val actions: Flow<`[`Action`](../-action/index.md)`<E, X>>` |

### Functions

| [events](events.md) | `fun events(): Flow<E>` |
| [invoke](invoke.md) | `operator fun invoke(): Flow<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>` |
| [targets](targets.md) | `fun targets(): Flow<X>` |

### Extension Functions

| [checked](../checked.md) | Gives you the checked value as [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) from the targeting [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`fun `[`Listener`](./index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, `[`HTMLInputElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-input-element/index.html)`>.checked(): Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [fileList](../file-list.md) | Gives you the [FileList](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.files/-file-list/index.html) from the targeting [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`fun `[`Listener`](./index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, `[`HTMLInputElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-input-element/index.html)`>.fileList(): Flow<`[`FileList`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.files/-file-list/index.html)`?>` |
| [key](../key.md) | Gives you the pressed key as [Key](../../io.fritz2.dom.html/-key/index.md) from a [KeyboardEvent](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-keyboard-event/index.html)`fun <X : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> `[`Listener`](./index.md)`<`[`KeyboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-keyboard-event/index.html)`, X>.key(): Flow<`[`Key`](../../io.fritz2.dom.html/-key/index.md)`>` |
| [selectedIndex](../selected-index.md) | Gives you the selected index as [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) from the targeting [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`fun `[`Listener`](./index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, `[`HTMLSelectElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-select-element/index.html)`>.selectedIndex(): Flow<`[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>` |
| [value](../value.md) | Gives you the new value as [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) from the targeting [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`fun `[`Listener`](./index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, `[`HTMLInputElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-input-element/index.html)`>.value(): Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>`fun `[`Listener`](./index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, `[`HTMLSelectElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-select-element/index.html)`>.value(): Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>`fun `[`Listener`](./index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, `[`HTMLTextAreaElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-text-area-element/index.html)`>.value(): Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

