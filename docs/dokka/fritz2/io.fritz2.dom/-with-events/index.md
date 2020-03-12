[fritz2](../../index.md) / [io.fritz2.dom](../index.md) / [WithEvents](./index.md)

# WithEvents

`@FlowPreview @ExperimentalCoroutinesApi abstract class WithEvents<out T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> : `[`WithDomNode`](../-with-dom-node/index.md)`<T>`

### Constructors

| [&lt;init&gt;](-init-.md) | `WithEvents()` |

### Properties

| [aborts](aborts.md) | `val aborts: Flow<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`>` |
| [afterprints](afterprints.md) | `val afterprints: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [beforeprints](beforeprints.md) | `val beforeprints: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [beforeunloads](beforeunloads.md) | `val beforeunloads: Flow<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`>` |
| [blurs](blurs.md) | `val blurs: Flow<`[`FocusEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-focus-event/index.html)`>` |
| [canplays](canplays.md) | `val canplays: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [canplaythroughs](canplaythroughs.md) | `val canplaythroughs: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [changes](changes.md) | `val changes: Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [clicks](clicks.md) | `val clicks: Flow<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`>` |
| [contextmenus](contextmenus.md) | `val contextmenus: Flow<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`>` |
| [copys](copys.md) | `val copys: Flow<`[`ClipboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.clipboard/-clipboard-event/index.html)`>` |
| [cuts](cuts.md) | `val cuts: Flow<`[`ClipboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.clipboard/-clipboard-event/index.html)`>` |
| [dblclicks](dblclicks.md) | `val dblclicks: Flow<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`>` |
| [dragends](dragends.md) | `val dragends: Flow<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`>` |
| [dragenters](dragenters.md) | `val dragenters: Flow<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`>` |
| [dragleaves](dragleaves.md) | `val dragleaves: Flow<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`>` |
| [dragovers](dragovers.md) | `val dragovers: Flow<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`>` |
| [drags](drags.md) | `val drags: Flow<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`>` |
| [dragstarts](dragstarts.md) | `val dragstarts: Flow<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`>` |
| [drops](drops.md) | `val drops: Flow<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`>` |
| [durationchanges](durationchanges.md) | `val durationchanges: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [endeds](endeds.md) | `val endeds: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [focusins](focusins.md) | `val focusins: Flow<`[`FocusEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-focus-event/index.html)`>` |
| [focusouts](focusouts.md) | `val focusouts: Flow<`[`FocusEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-focus-event/index.html)`>` |
| [focuss](focuss.md) | `val focuss: Flow<`[`FocusEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-focus-event/index.html)`>` |
| [fullscreenchanges](fullscreenchanges.md) | `val fullscreenchanges: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [fullscreenerrors](fullscreenerrors.md) | `val fullscreenerrors: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [hashchanges](hashchanges.md) | `val hashchanges: Flow<`[`HashChangeEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-hash-change-event/index.html)`>` |
| [inputs](inputs.md) | `val inputs: Flow<`[`InputEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-input-event/index.html)`>` |
| [invalids](invalids.md) | `val invalids: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [keydowns](keydowns.md) | `val keydowns: Flow<`[`KeyboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-keyboard-event/index.html)`>` |
| [keypresss](keypresss.md) | `val keypresss: Flow<`[`KeyboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-keyboard-event/index.html)`>` |
| [keyups](keyups.md) | `val keyups: Flow<`[`KeyboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-keyboard-event/index.html)`>` |
| [loadeddatas](loadeddatas.md) | `val loadeddatas: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [loadedmetadatas](loadedmetadatas.md) | `val loadedmetadatas: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [loads](loads.md) | `val loads: Flow<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`>` |
| [loadstarts](loadstarts.md) | `val loadstarts: Flow<`[`ProgressEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.xhr/-progress-event/index.html)`>` |
| [messages](messages.md) | `val messages: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [mousedowns](mousedowns.md) | `val mousedowns: Flow<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`>` |
| [mouseenters](mouseenters.md) | `val mouseenters: Flow<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`>` |
| [mouseleaves](mouseleaves.md) | `val mouseleaves: Flow<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`>` |
| [mousemoves](mousemoves.md) | `val mousemoves: Flow<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`>` |
| [mouseouts](mouseouts.md) | `val mouseouts: Flow<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`>` |
| [mouseovers](mouseovers.md) | `val mouseovers: Flow<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`>` |
| [mouseups](mouseups.md) | `val mouseups: Flow<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`>` |
| [offlines](offlines.md) | `val offlines: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [onlines](onlines.md) | `val onlines: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [opens](opens.md) | `val opens: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [pagehides](pagehides.md) | `val pagehides: Flow<`[`PageTransitionEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-page-transition-event/index.html)`>` |
| [pageshows](pageshows.md) | `val pageshows: Flow<`[`PageTransitionEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-page-transition-event/index.html)`>` |
| [pastes](pastes.md) | `val pastes: Flow<`[`ClipboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.clipboard/-clipboard-event/index.html)`>` |
| [pauses](pauses.md) | `val pauses: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [playings](playings.md) | `val playings: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [plays](plays.md) | `val plays: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [popstates](popstates.md) | `val popstates: Flow<`[`PopStateEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-pop-state-event/index.html)`>` |
| [progresss](progresss.md) | `val progresss: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [ratechanges](ratechanges.md) | `val ratechanges: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [resets](resets.md) | `val resets: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [resizes](resizes.md) | `val resizes: Flow<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`>` |
| [scrolls](scrolls.md) | `val scrolls: Flow<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`>` |
| [searchs](searchs.md) | `val searchs: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [seekeds](seekeds.md) | `val seekeds: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [seekings](seekings.md) | `val seekings: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [selects](selects.md) | `val selects: Flow<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`>` |
| [shows](shows.md) | `val shows: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [stalleds](stalleds.md) | `val stalleds: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [storages](storages.md) | `val storages: Flow<`[`StorageEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-storage-event/index.html)`>` |
| [submits](submits.md) | `val submits: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [suspends](suspends.md) | `val suspends: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [timeupdates](timeupdates.md) | `val timeupdates: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [toggles](toggles.md) | `val toggles: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [touchcancels](touchcancels.md) | `val touchcancels: Flow<`[`TouchEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-touch-event/index.html)`>` |
| [touchends](touchends.md) | `val touchends: Flow<`[`TouchEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-touch-event/index.html)`>` |
| [touchmoves](touchmoves.md) | `val touchmoves: Flow<`[`TouchEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-touch-event/index.html)`>` |
| [touchstarts](touchstarts.md) | `val touchstarts: Flow<`[`TouchEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-touch-event/index.html)`>` |
| [unloads](unloads.md) | `val unloads: Flow<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`>` |
| [volumechanges](volumechanges.md) | `val volumechanges: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [waitings](waitings.md) | `val waitings: Flow<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`>` |
| [wheels](wheels.md) | `val wheels: Flow<`[`WheelEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-wheel-event/index.html)`>` |

### Functions

| [subscribe](subscribe.md) | `fun <T> subscribe(type: `[`EventType`](../../io.fritz2.dom.html/-event-type/index.md)`<T>): Flow<T>` |

### Inheritors

| [Tag](../-tag/index.md) | `abstract class Tag<out T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> : `[`WithDomNode`](../-with-dom-node/index.md)`<T>, `[`WithAttributes`](../-with-attributes/index.md)`<T>, `[`WithEvents`](./index.md)`<T>, `[`HtmlElements`](../../io.fritz2.dom.html/-html-elements/index.md) |

