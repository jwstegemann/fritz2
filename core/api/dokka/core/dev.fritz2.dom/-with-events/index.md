[core](../../index.md) / [dev.fritz2.dom](../index.md) / [WithEvents](./index.md)

# WithEvents

(js) `abstract class WithEvents<out T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> : `[`WithDomNode`](../-with-dom-node/index.md)`<T>`

this interfaces offers [Listener](../-listener/index.md)s for all DOM-events available

### Constructors

| (js) [&lt;init&gt;](-init-.md) | this interfaces offers [Listener](../-listener/index.md)s for all DOM-events available`WithEvents()` |

### Properties

| (js) [aborts](aborts.md) | `val aborts: `[`Listener`](../-listener/index.md)`<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`, T>` |
| (js) [afterprints](afterprints.md) | `val afterprints: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [beforeprints](beforeprints.md) | `val beforeprints: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [beforeunloads](beforeunloads.md) | `val beforeunloads: `[`Listener`](../-listener/index.md)`<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`, T>` |
| (js) [blurs](blurs.md) | `val blurs: `[`Listener`](../-listener/index.md)`<`[`FocusEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-focus-event/index.html)`, T>` |
| (js) [canplays](canplays.md) | `val canplays: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [canplaythroughs](canplaythroughs.md) | `val canplaythroughs: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [changes](changes.md) | `val changes: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [clicks](clicks.md) | `val clicks: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| (js) [contextmenus](contextmenus.md) | `val contextmenus: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| (js) [copys](copys.md) | `val copys: `[`Listener`](../-listener/index.md)`<`[`ClipboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.clipboard/-clipboard-event/index.html)`, T>` |
| (js) [cuts](cuts.md) | `val cuts: `[`Listener`](../-listener/index.md)`<`[`ClipboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.clipboard/-clipboard-event/index.html)`, T>` |
| (js) [dblclicks](dblclicks.md) | `val dblclicks: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| (js) [dragends](dragends.md) | `val dragends: `[`Listener`](../-listener/index.md)`<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`, T>` |
| (js) [dragenters](dragenters.md) | `val dragenters: `[`Listener`](../-listener/index.md)`<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`, T>` |
| (js) [dragleaves](dragleaves.md) | `val dragleaves: `[`Listener`](../-listener/index.md)`<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`, T>` |
| (js) [dragovers](dragovers.md) | `val dragovers: `[`Listener`](../-listener/index.md)`<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`, T>` |
| (js) [drags](drags.md) | `val drags: `[`Listener`](../-listener/index.md)`<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`, T>` |
| (js) [dragstarts](dragstarts.md) | `val dragstarts: `[`Listener`](../-listener/index.md)`<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`, T>` |
| (js) [drops](drops.md) | `val drops: `[`Listener`](../-listener/index.md)`<`[`DragEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-drag-event/index.html)`, T>` |
| (js) [durationchanges](durationchanges.md) | `val durationchanges: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [endeds](endeds.md) | `val endeds: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [focusins](focusins.md) | `val focusins: `[`Listener`](../-listener/index.md)`<`[`FocusEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-focus-event/index.html)`, T>` |
| (js) [focusouts](focusouts.md) | `val focusouts: `[`Listener`](../-listener/index.md)`<`[`FocusEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-focus-event/index.html)`, T>` |
| (js) [focuss](focuss.md) | `val focuss: `[`Listener`](../-listener/index.md)`<`[`FocusEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-focus-event/index.html)`, T>` |
| (js) [fullscreenchanges](fullscreenchanges.md) | `val fullscreenchanges: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [fullscreenerrors](fullscreenerrors.md) | `val fullscreenerrors: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [hashchanges](hashchanges.md) | `val hashchanges: `[`Listener`](../-listener/index.md)`<`[`HashChangeEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-hash-change-event/index.html)`, T>` |
| (js) [inputs](inputs.md) | `val inputs: `[`Listener`](../-listener/index.md)`<`[`InputEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-input-event/index.html)`, T>` |
| (js) [invalids](invalids.md) | `val invalids: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [keydowns](keydowns.md) | `val keydowns: `[`Listener`](../-listener/index.md)`<`[`KeyboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-keyboard-event/index.html)`, T>` |
| (js) [keypresss](keypresss.md) | `val keypresss: `[`Listener`](../-listener/index.md)`<`[`KeyboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-keyboard-event/index.html)`, T>` |
| (js) [keyups](keyups.md) | `val keyups: `[`Listener`](../-listener/index.md)`<`[`KeyboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-keyboard-event/index.html)`, T>` |
| (js) [loadeddatas](loadeddatas.md) | `val loadeddatas: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [loadedmetadatas](loadedmetadatas.md) | `val loadedmetadatas: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [loads](loads.md) | `val loads: `[`Listener`](../-listener/index.md)`<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`, T>` |
| (js) [loadstarts](loadstarts.md) | `val loadstarts: `[`Listener`](../-listener/index.md)`<`[`ProgressEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.xhr/-progress-event/index.html)`, T>` |
| (js) [messages](messages.md) | `val messages: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [mousedowns](mousedowns.md) | `val mousedowns: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| (js) [mouseenters](mouseenters.md) | `val mouseenters: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| (js) [mouseleaves](mouseleaves.md) | `val mouseleaves: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| (js) [mousemoves](mousemoves.md) | `val mousemoves: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| (js) [mouseouts](mouseouts.md) | `val mouseouts: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| (js) [mouseovers](mouseovers.md) | `val mouseovers: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| (js) [mouseups](mouseups.md) | `val mouseups: `[`Listener`](../-listener/index.md)`<`[`MouseEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-mouse-event/index.html)`, T>` |
| (js) [offlines](offlines.md) | `val offlines: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [onlines](onlines.md) | `val onlines: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [opens](opens.md) | `val opens: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [pagehides](pagehides.md) | `val pagehides: `[`Listener`](../-listener/index.md)`<`[`PageTransitionEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-page-transition-event/index.html)`, T>` |
| (js) [pageshows](pageshows.md) | `val pageshows: `[`Listener`](../-listener/index.md)`<`[`PageTransitionEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-page-transition-event/index.html)`, T>` |
| (js) [pastes](pastes.md) | `val pastes: `[`Listener`](../-listener/index.md)`<`[`ClipboardEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.clipboard/-clipboard-event/index.html)`, T>` |
| (js) [pauses](pauses.md) | `val pauses: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [playings](playings.md) | `val playings: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [plays](plays.md) | `val plays: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [popstates](popstates.md) | `val popstates: `[`Listener`](../-listener/index.md)`<`[`PopStateEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-pop-state-event/index.html)`, T>` |
| (js) [progresss](progresss.md) | `val progresss: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [ratechanges](ratechanges.md) | `val ratechanges: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [resets](resets.md) | `val resets: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [resizes](resizes.md) | `val resizes: `[`Listener`](../-listener/index.md)`<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`, T>` |
| (js) [scrolls](scrolls.md) | `val scrolls: `[`Listener`](../-listener/index.md)`<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`, T>` |
| (js) [searchs](searchs.md) | `val searchs: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [seekeds](seekeds.md) | `val seekeds: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [seekings](seekings.md) | `val seekings: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [selects](selects.md) | `val selects: `[`Listener`](../-listener/index.md)`<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`, T>` |
| (js) [shows](shows.md) | `val shows: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [stalleds](stalleds.md) | `val stalleds: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [storages](storages.md) | `val storages: `[`Listener`](../-listener/index.md)`<`[`StorageEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-storage-event/index.html)`, T>` |
| (js) [submits](submits.md) | `val submits: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [suspends](suspends.md) | `val suspends: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [timeupdates](timeupdates.md) | `val timeupdates: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [toggles](toggles.md) | `val toggles: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [touchcancels](touchcancels.md) | `val touchcancels: `[`Listener`](../-listener/index.md)`<`[`TouchEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-touch-event/index.html)`, T>` |
| (js) [touchends](touchends.md) | `val touchends: `[`Listener`](../-listener/index.md)`<`[`TouchEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-touch-event/index.html)`, T>` |
| (js) [touchmoves](touchmoves.md) | `val touchmoves: `[`Listener`](../-listener/index.md)`<`[`TouchEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-touch-event/index.html)`, T>` |
| (js) [touchstarts](touchstarts.md) | `val touchstarts: `[`Listener`](../-listener/index.md)`<`[`TouchEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-touch-event/index.html)`, T>` |
| (js) [unloads](unloads.md) | `val unloads: `[`Listener`](../-listener/index.md)`<`[`UIEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-u-i-event/index.html)`, T>` |
| (js) [volumechanges](volumechanges.md) | `val volumechanges: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [waitings](waitings.md) | `val waitings: `[`Listener`](../-listener/index.md)`<`[`Event`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html)`, T>` |
| (js) [wheels](wheels.md) | `val wheels: `[`Listener`](../-listener/index.md)`<`[`WheelEvent`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-wheel-event/index.html)`, T>` |

### Inheritors

| (js) [Tag](../-tag/index.md) | Represents a tag in the resulting HTML. Sorry for the name, but we needed to delimit it from the [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html) it is wrapping.`open class Tag<out T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> : `[`WithDomNode`](../-with-dom-node/index.md)`<T>, `[`WithAttributes`](../-with-attributes/index.md)`<T>, `[`WithEvents`](./index.md)`<T>, `[`HtmlElements`](../../dev.fritz2.dom.html/-html-elements/index.md) |

