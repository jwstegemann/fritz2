[core](../../index.md) / [dev.fritz2.dom.html](../index.md) / [Audio](./index.md)

# Audio

(js) `class Audio : `[`Tag`](../../dev.fritz2.dom/-tag/index.md)`<`[`HTMLAudioElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-audio-element/index.html)`>, `[`WithText`](../../dev.fritz2.dom/-with-text/index.md)`<`[`HTMLAudioElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-audio-element/index.html)`>`

Exposes the JavaScript [HTMLAudioElement](https://developer.mozilla.org/en/docs/Web/API/HTMLAudioElement) to Kotlin

### Constructors

| (js) [&lt;init&gt;](-init-.md) | Exposes the JavaScript [HTMLAudioElement](https://developer.mozilla.org/en/docs/Web/API/HTMLAudioElement) to Kotlin`Audio(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, baseClass: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)` |

### Properties

| (js) [autoplay](autoplay.md) | `var autoplay: Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| (js) [controls](controls.md) | `var controls: Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| (js) [currentTime](current-time.md) | `var currentTime: Flow<`[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>` |
| (js) [defaultMuted](default-muted.md) | `var defaultMuted: Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| (js) [defaultPlaybackRate](default-playback-rate.md) | `var defaultPlaybackRate: Flow<`[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>` |
| (js) [loop](loop.md) | `var loop: Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| (js) [muted](muted.md) | `var muted: Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| (js) [playbackRate](playback-rate.md) | `var playbackRate: Flow<`[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>` |
| (js) [preload](preload.md) | `var preload: Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| (js) [src](src.md) | `var src: Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| (js) [volume](volume.md) | `var volume: Flow<`[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>` |

### Extension Functions

| (js) [mount](../../dev.fritz2.dom/mount.md) | mounts a static [Tag](../../dev.fritz2.dom/-tag/index.md) to an elements content`fun <X : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> `[`Tag`](../../dev.fritz2.dom/-tag/index.md)`<X>.mount(targetId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

