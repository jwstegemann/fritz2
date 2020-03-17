[fritz2](../../index.md) / [io.fritz2.dom.html](../index.md) / [Key](./index.md)

# Key

`data class Key`

[Key](./index.md) represents a key press e.g. for keypress events

### Constructors

| [&lt;init&gt;](-init-.md) | [Key](./index.md) represents a key press e.g. for keypress events`Key(code: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, ctrl: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, alt: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, shift: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, meta: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`)` |

### Properties

| [alt](alt.md) | `val alt: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [code](code.md) | `val code: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [ctrl](ctrl.md) | `val ctrl: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [meta](meta.md) | `val meta: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [name](name.md) | `val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [shift](shift.md) | `val shift: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Functions

| [isKey](is-key.md) | `fun isKey(keys: `[`Keys`](../-keys/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Companion Object Functions

| [from](from.md) | `fun from(e: `[`KeyboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-keyboard-event/index.html)`): `[`Key`](./index.md) |

