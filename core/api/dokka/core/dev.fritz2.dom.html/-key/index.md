[core](../../index.md) / [dev.fritz2.dom.html](../index.md) / [Key](./index.md)

# Key

(js) `data class Key`

[Key](./index.md) represents a key press e.g. for keypress events

### Constructors

| (js) [&lt;init&gt;](-init-.md) | [Key](./index.md) represents a key press e.g. for keypress events`Key(code: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, ctrl: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, alt: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, shift: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, meta: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`)` |

### Properties

| (js) [alt](alt.md) | `val alt: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| (js) [code](code.md) | `val code: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| (js) [ctrl](ctrl.md) | `val ctrl: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| (js) [meta](meta.md) | `val meta: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| (js) [name](name.md) | `val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| (js) [shift](shift.md) | `val shift: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Functions

| (js) [isKey](is-key.md) | `fun isKey(keys: `[`Keys`](../-keys/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Companion Object Functions

| (js) [from](from.md) | `fun from(e: `[`KeyboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-keyboard-event/index.html)`): `[`Key`](./index.md) |

