[fritz2](../../index.md) / [io.fritz2.dom](../index.md) / [WithEvents](./index.md)

# WithEvents

`@FlowPreview @ExperimentalCoroutinesApi abstract class WithEvents<T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> : `[`WithDomNode`](../-with-dom-node/index.md)`<T>`

### Constructors

| [&lt;init&gt;](-init-.md) | `WithEvents()` |

### Properties

| [aborts](aborts.md) | `val aborts: `[`Listener`](../-listener/index.md)`<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`, T>` |
| [afterprints](afterprints.md) | `val afterprints: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [beforeprints](beforeprints.md) | `val beforeprints: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [beforeunloads](beforeunloads.md) | `val beforeunloads: `[`Listener`](../-listener/index.md)`<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`, T>` |
| [blurs](blurs.md) | `val blurs: `[`Listener`](../-listener/index.md)`<`[`FocusEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-focus-event/index.html)`, T>` |
| [canplays](canplays.md) | `val canplays: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [canplaythroughs](canplaythroughs.md) | `val canplaythroughs: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [changes](changes.md) | `val changes: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [clicks](clicks.md) | `val clicks: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| [contextmenus](contextmenus.md) | `val contextmenus: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| [copys](copys.md) | `val copys: `[`Listener`](../-listener/index.md)`<`[`ClipboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.clipboard/-clipboard-event/index.html)`, T>` |
| [cuts](cuts.md) | `val cuts: `[`Listener`](../-listener/index.md)`<`[`ClipboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.clipboard/-clipboard-event/index.html)`, T>` |
| [dblclicks](dblclicks.md) | `val dblclicks: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| [dragends](dragends.md) | `val dragends: `[`Listener`](../-listener/index.md)`<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`, T>` |
| [dragenters](dragenters.md) | `val dragenters: `[`Listener`](../-listener/index.md)`<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`, T>` |
| [dragleaves](dragleaves.md) | `val dragleaves: `[`Listener`](../-listener/index.md)`<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`, T>` |
| [dragovers](dragovers.md) | `val dragovers: `[`Listener`](../-listener/index.md)`<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`, T>` |
| [drags](drags.md) | `val drags: `[`Listener`](../-listener/index.md)`<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`, T>` |
| [dragstarts](dragstarts.md) | `val dragstarts: `[`Listener`](../-listener/index.md)`<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`, T>` |
| [drops](drops.md) | `val drops: `[`Listener`](../-listener/index.md)`<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`, T>` |
| [durationchanges](durationchanges.md) | `val durationchanges: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [endeds](endeds.md) | `val endeds: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [focusins](focusins.md) | `val focusins: `[`Listener`](../-listener/index.md)`<`[`FocusEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-focus-event/index.html)`, T>` |
| [focusouts](focusouts.md) | `val focusouts: `[`Listener`](../-listener/index.md)`<`[`FocusEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-focus-event/index.html)`, T>` |
| [focuss](focuss.md) | `val focuss: `[`Listener`](../-listener/index.md)`<`[`FocusEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-focus-event/index.html)`, T>` |
| [fullscreenchanges](fullscreenchanges.md) | `val fullscreenchanges: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [fullscreenerrors](fullscreenerrors.md) | `val fullscreenerrors: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [hashchanges](hashchanges.md) | `val hashchanges: `[`Listener`](../-listener/index.md)`<`[`HashChangeEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-hash-change-event/index.html)`, T>` |
| [inputs](inputs.md) | `val inputs: `[`Listener`](../-listener/index.md)`<`[`InputEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-input-event/index.html)`, T>` |
| [invalids](invalids.md) | `val invalids: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [keydowns](keydowns.md) | `val keydowns: `[`Listener`](../-listener/index.md)`<`[`KeyboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-keyboard-event/index.html)`, T>` |
| [keypresss](keypresss.md) | `val keypresss: `[`Listener`](../-listener/index.md)`<`[`KeyboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-keyboard-event/index.html)`, T>` |
| [keyups](keyups.md) | `val keyups: `[`Listener`](../-listener/index.md)`<`[`KeyboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-keyboard-event/index.html)`, T>` |
| [loadeddatas](loadeddatas.md) | `val loadeddatas: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [loadedmetadatas](loadedmetadatas.md) | `val loadedmetadatas: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [loads](loads.md) | `val loads: `[`Listener`](../-listener/index.md)`<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`, T>` |
| [loadstarts](loadstarts.md) | `val loadstarts: `[`Listener`](../-listener/index.md)`<`[`ProgressEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.xhr/-progress-event/index.html)`, T>` |
| [messages](messages.md) | `val messages: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [mousedowns](mousedowns.md) | `val mousedowns: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| [mouseenters](mouseenters.md) | `val mouseenters: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| [mouseleaves](mouseleaves.md) | `val mouseleaves: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| [mousemoves](mousemoves.md) | `val mousemoves: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| [mouseouts](mouseouts.md) | `val mouseouts: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| [mouseovers](mouseovers.md) | `val mouseovers: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| [mouseups](mouseups.md) | `val mouseups: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| [offlines](offlines.md) | `val offlines: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [onlines](onlines.md) | `val onlines: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [opens](opens.md) | `val opens: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [pagehides](pagehides.md) | `val pagehides: `[`Listener`](../-listener/index.md)`<`[`PageTransitionEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-page-transition-event/index.html)`, T>` |
| [pageshows](pageshows.md) | `val pageshows: `[`Listener`](../-listener/index.md)`<`[`PageTransitionEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-page-transition-event/index.html)`, T>` |
| [pastes](pastes.md) | `val pastes: `[`Listener`](../-listener/index.md)`<`[`ClipboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.clipboard/-clipboard-event/index.html)`, T>` |
| [pauses](pauses.md) | `val pauses: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [playings](playings.md) | `val playings: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [plays](plays.md) | `val plays: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [popstates](popstates.md) | `val popstates: `[`Listener`](../-listener/index.md)`<`[`PopStateEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-pop-state-event/index.html)`, T>` |
| [progresss](progresss.md) | `val progresss: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [ratechanges](ratechanges.md) | `val ratechanges: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [resets](resets.md) | `val resets: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [resizes](resizes.md) | `val resizes: `[`Listener`](../-listener/index.md)`<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`, T>` |
| [scrolls](scrolls.md) | `val scrolls: `[`Listener`](../-listener/index.md)`<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`, T>` |
| [searchs](searchs.md) | `val searchs: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [seekeds](seekeds.md) | `val seekeds: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [seekings](seekings.md) | `val seekings: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [selects](selects.md) | `val selects: `[`Listener`](../-listener/index.md)`<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`, T>` |
| [shows](shows.md) | `val shows: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [stalleds](stalleds.md) | `val stalleds: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [storages](storages.md) | `val storages: `[`Listener`](../-listener/index.md)`<`[`StorageEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-storage-event/index.html)`, T>` |
| [submits](submits.md) | `val submits: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [suspends](suspends.md) | `val suspends: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [timeupdates](timeupdates.md) | `val timeupdates: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [toggles](toggles.md) | `val toggles: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [touchcancels](touchcancels.md) | `val touchcancels: `[`Listener`](../-listener/index.md)`<`[`TouchEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-touch-event/index.html)`, T>` |
| [touchends](touchends.md) | `val touchends: `[`Listener`](../-listener/index.md)`<`[`TouchEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-touch-event/index.html)`, T>` |
| [touchmoves](touchmoves.md) | `val touchmoves: `[`Listener`](../-listener/index.md)`<`[`TouchEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-touch-event/index.html)`, T>` |
| [touchstarts](touchstarts.md) | `val touchstarts: `[`Listener`](../-listener/index.md)`<`[`TouchEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-touch-event/index.html)`, T>` |
| [unloads](unloads.md) | `val unloads: `[`Listener`](../-listener/index.md)`<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`, T>` |
| [volumechanges](volumechanges.md) | `val volumechanges: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [waitings](waitings.md) | `val waitings: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| [wheels](wheels.md) | `val wheels: `[`Listener`](../-listener/index.md)`<`[`WheelEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-wheel-event/index.html)`, T>` |

### Functions

| [subscribe](subscribe.md) | `fun <E : `[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`> subscribe(type: `[`EventType`](../../io.fritz2.dom.html/-event-type/index.md)`<E>): `[`Listener`](../-listener/index.md)`<E, T>` |

### Inheritors

| [Tag](../-tag/index.md) | `abstract class Tag<T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> : `[`WithDomNode`](../-with-dom-node/index.md)`<T>, `[`WithAttributes`](../-with-attributes/index.md)`<T>, `[`WithEvents`](./index.md)`<T>, `[`HtmlElements`](../../io.fritz2.dom.html/-html-elements/index.md) |

