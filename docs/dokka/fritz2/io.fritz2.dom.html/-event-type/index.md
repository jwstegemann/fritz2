[fritz2](../../index.md) / [io.fritz2.dom.html](../index.md) / [EventType](./index.md)

# EventType

`class EventType<T : `[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>`

[EventType](./index.md) contains the javascript related [name](name.md) of an [Event](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)
and has a method [extract](extract.md) to cast an [Event](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html) to its actual event-type.

### Constructors

| [&lt;init&gt;](-init-.md) | [EventType](./index.md) contains the javascript related [name](name.md) of an [Event](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html) and has a method [extract](extract.md) to cast an [Event](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html) to its actual event-type.`EventType(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Properties

| [name](name.md) | `val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| [extract](extract.md) | `fun extract(event: `[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`): T` |

