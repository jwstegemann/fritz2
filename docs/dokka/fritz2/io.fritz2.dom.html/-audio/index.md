[fritz2](../../index.md) / [io.fritz2.dom.html](../index.md) / [Audio](./index.md)

# Audio

`@ExperimentalCoroutinesApi @FlowPreview class Audio : `[`Tag`](../../io.fritz2.dom/-tag/index.md)`<`[`HTMLAudioElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-audio-element/index.html)`>, `[`WithText`](../../io.fritz2.dom/-with-text/index.md)`<`[`HTMLAudioElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-audio-element/index.html)`>`

Exposes the JavaScript [HTMLAudioElement](https://developer.mozilla.org/en/docs/Web/API/HTMLAudioElement) to Kotlin

### Constructors

| [&lt;init&gt;](-init-.md) | Exposes the JavaScript [HTMLAudioElement](https://developer.mozilla.org/en/docs/Web/API/HTMLAudioElement) to Kotlin`Audio(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)` |

### Properties

| [autoplay](autoplay.md) | `var autoplay: Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [controls](controls.md) | `var controls: Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [currentTime](current-time.md) | `var currentTime: Flow<`[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>` |
| [defaultMuted](default-muted.md) | `var defaultMuted: Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [defaultPlaybackRate](default-playback-rate.md) | `var defaultPlaybackRate: Flow<`[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>` |
| [loop](loop.md) | `var loop: Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [muted](muted.md) | `var muted: Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [playbackRate](playback-rate.md) | `var playbackRate: Flow<`[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>` |
| [preload](preload.md) | `var preload: Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [src](src.md) | `var src: Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [volume](volume.md) | `var volume: Flow<`[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>` |

### Extension Functions

| [mount](../../io.fritz2.dom/mount.md) | `fun <X : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> `[`Tag`](../../io.fritz2.dom/-tag/index.md)`<X>.mount(targetId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

