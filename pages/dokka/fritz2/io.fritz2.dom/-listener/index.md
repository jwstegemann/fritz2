[fritz2](../../index.md) / [io.fritz2.dom](../index.md) / [Listener](./index.md)

# Listener

`@FlowPreview @ExperimentalCoroutinesApi inline class Listener<E : `[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, X : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`>`

[Listener](./index.md) handles a Flow of [Event](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)s.

### Constructors

| [&lt;init&gt;](-init-.md) | [Listener](./index.md) handles a Flow of [Event](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)s.`Listener(events: Flow<E>)` |

### Properties

| [events](events.md) | `val events: Flow<E>` |

### Functions

| [map](map.md) | Maps the given [Event](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html) to a new value.`fun <R> map(mapper: suspend (E) -> R): Flow<R>` |

### Extension Functions

| [files](../files.md) | Gives you the [FileList](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.files/-file-list/index.html) from the targeting [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`fun `[`Listener`](./index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, `[`HTMLInputElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-input-element/index.html)`>.files(): Flow<`[`FileList`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.files/-file-list/index.html)`?>` |
| [key](../key.md) | Gives you the pressed key as [Key](../../io.fritz2.dom.html/-key/index.md) from a [KeyboardEvent](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-keyboard-event/index.html)`fun <X : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> `[`Listener`](./index.md)`<`[`KeyboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-keyboard-event/index.html)`, X>.key(): Flow<`[`Key`](../../io.fritz2.dom.html/-key/index.md)`>` |
| [selectedIndex](../selected-index.md) | Gives you the selected index as [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) from the targeting [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`fun `[`Listener`](./index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, `[`HTMLSelectElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-select-element/index.html)`>.selectedIndex(): Flow<`[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>` |
| [states](../states.md) | Gives you the checked value as [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) from the targeting [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`fun `[`Listener`](./index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, `[`HTMLInputElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-input-element/index.html)`>.states(): Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [values](../values.md) | Gives you the new value as [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) from the targeting [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`fun `[`Listener`](./index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, `[`HTMLInputElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-input-element/index.html)`>.values(): Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>`fun `[`Listener`](./index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, `[`HTMLSelectElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-select-element/index.html)`>.values(): Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>`fun `[`Listener`](./index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, `[`HTMLTextAreaElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-text-area-element/index.html)`>.values(): Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

