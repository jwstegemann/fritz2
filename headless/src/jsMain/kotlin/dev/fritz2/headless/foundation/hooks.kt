package dev.fritz2.headless.foundation

import dev.fritz2.dom.RenderContext
import dev.fritz2.dom.Tag
import kotlinx.coroutines.flow.Flow

/**
 * This alias should express the main concept of a [Hook]: The encapsulated effect; that is applying some function
 * with a payload [P] onto a receiver [C] (often some [Tag]) and return some result [R] (often also a [Tag]).
 */
typealias Effect<C, R, P> = C.(P) -> R

/**
 * This specialized payload alias should express the common needed parameters when the [Effect] render some [Tag],
 * so it ueses a [Triple] to group a [String] for the classes, a [String] for the [Tag]'s id and the unspecific
 * payload [P] itself.
 *
 * Always use this special payload type, if the [Hook] creates some [Tag]!
 *
 * @see [TagHook]
 */
typealias TagPayload<P> = Triple<String?, String?, P>

/**
 * A [Hook] is a special kind of [Property] that encapsulates an [Effect] as the [Property]'s [value].
 *
 * A hook enables a client to define some custom behaviour that the applicator of the hook (a component for most
 * cases) should apply. The behaviour is often closely tied to some data provided by the client. In fact, in most
 * cases those data is needed to configure the [Effect], which is usually done in appropriate `operator fun invoke`
 * methods.
 *
 * So, the client has to configure the values defined by the property portion and the applicator applies the effect
 * within its own UI context [C].
 *
 * The applicator can pass additional data into the effect as payload [P] (think of some ``close`` handler passed
 * into the apply-expression to enable the client to create some custom close-button for example).
 *
 * The hook can return some type which is expressed by the [R] type parameter of the [Effect] expression.
 *
 * The applicator can easily execute the [Hook] by calling one of the [hook] methods, which basically simply pass
 * the payload into the [Effect] and executes it. Finally an optional [alsoExpr] is applied to the return value [R].
 *
 * If an implementation needs some complex payload, use some dataclass or other containers to comply to its signature.
 *
 * DO NOT CREATE SOME OTHER HOOK ABSTRACTION WITH MULTIPLE TYPES FOR THE PAYLOAD!
 * (So please NO `Hook<C, R, P1, P2, ...>`!!!)
 *
 * For the special cases where an applicator knows about the specific internal rendering structure of a hook
 * implementation, prefer to use [TagHook] or apply the special payload type [TagPayload] by yourself.
 *
 * There exist special [hook] methods for this type alias, which offers a nice convenience API for the hook applicator
 * to directly pass the styling and id parameters additionally to the generic payload [P].
 *
 * Components should strive to use hooks for all their parts that need to be configurable and varying in their
 * behaviours. Think of some input-field that should render a label next to it. This label clearly needs to be
 * configured by the calling client, so it must be configurable. The label could be reused for different kind of
 * form control elements too, so it clearly makes sense to encapsulate the configuration and its representation
 * (the rendering function) into one object that should be some kind of hook.
 *
 * @see Property
 * @see TagHook
 * @see Enhanceable
 */
abstract class Hook<C, R, P> : Property<Effect<C, R, P>>() {

    /**
     * This field encapsulates some behaviour, which should be applied to the result [R] of the [Hook]'s [Effect].
     *
     * @see also
     */
    var alsoExpr: (R.() -> Unit)? = null
        protected set

    /**
     * This method offers the created result as receiver within a context expression in order to expose some additional
     * configuration entry point for a client.
     *
     * This is also the most *unspecific* way to configure the behaviour of a hook.
     *
     * That's why it is intentionally a *terminal* operation, thus no other configuration function of a hook can
     * be chained after. The more specific ones are enforced to appear closer to the `invoke` call though.
     *
     * Example:
     * ```
     * btn {
     *     icon(someIcon) // main configuration
     *         .right() // some additional, component specific and precise configuration
     *         .also { // final block of various additional configuration
     *             className("text-red-500")
     *         }
     *         // no more chaining possible
     * }
     * ```
     *
     * @param expr [Tag] specific configuration expression with optional applied classes injected for possible extension
     */
    fun also(expr: R.() -> Unit) {
        alsoExpr = expr
    }
}

/**
 * This hook method applies a [Hook]'s encapsulated behaviour to the calling context and passes a given payload.
 * It also applies a given also-expression.
 *
 * @see Hook
 *
 * @param h The hook implementation
 * @param payload some additional data
 */
fun <C, R, P> C.hook(h: Hook<C, R, P>, payload: P) =
    h.value?.invoke(this, payload)?.also { h.alsoExpr?.invoke(it) }

/**
 * This hook method applies a [Hook]'s encapsulated behaviour to the calling context.
 * It also applies a given also-expression.
 *
 * @see Hook
 *
 * @param h The hook implementation
 */
fun <C, R> C.hook(h: Hook<C, R, Unit>) =
    h.value?.invoke(this, Unit)?.also { h.alsoExpr?.invoke(it) }

/**
 * This hook method applies multiple [Hook]'s encapsulated behaviour to the calling context and passes the _same_
 * given payload to each hook. It also applies a given also-expression of each hook.
 *
 * This is a shortcut for situation where lots of hooks needs to be applied at the same location:
 * ```
 * // instead of this...
 * hook(a, String)
 * hook(b, String)
 * hook(c, String)
 *
 * // ... the application of this variant shortens the code:
 * hook(a, b, c, payload = "some Value")
 * ```
 *
 * @see Hook
 *
 * @param h some hook implementations
 * @param payload some additional data
 */
fun <C, R, P> C.hook(vararg h: Hook<C, R, P>, payload: P) = h.forEach { hook ->
    hook.value?.invoke(this, payload)?.also { hook.alsoExpr?.invoke(it) }
}

/**
 * This hook method applies multiple [Hook]'s encapsulated behaviour to the calling context with the payload of type
 * `Unit`. It also applies a given also-expression of each hook.
 *
 * This is a shortcut for situation where lots of hooks needs to be applied at the same location:
 * ```
 * // instead of this...
 * hook(a)
 * hook(b)
 * hook(c)
 *
 * // ... the application of this variant shortens the code:
 * hook(a, b, c)
 * ```
 *
 * @see Hook
 *
 * @param h some hook implementations
 */
fun <C, R> C.hook(vararg h: Hook<C, R, Unit>) = h.forEach { hook ->
    hook.value?.invoke(this, Unit)?.also { hook.alsoExpr?.invoke(it) }
}

/**
 * This hook method applies a [Hook]'s encapsulated behaviour to the calling context with a specific payload
 * [TagPayload], which is tailored to [Tag] creation, as it offers parameters for the [id] and a styling parameter
 * [classes] on top to the generic payload [P].
 *
 * As implementor of the [Hook] the creation of the value property simply requires to unpack the [TagPayload] [Triple].
 *
 * @see TagPayload
 *
 * @param h The hook implementation
 * @param classes some optional styling information
 * @param id an optional id for applying to the hook's content
 * @param payload some additional data
 */
fun <C, R, P> C.hook(h: Hook<C, R, TagPayload<P>>, classes: String?, id: String?, payload: P) =
    h.value?.invoke(this, Triple(classes, id, payload))?.also { h.alsoExpr?.invoke(it) }

/**
 * This hook variant is a _safe_ application of a hook, which offers a [fallback] parameter that is executed, if
 * hook's apply field is not set.
 *
 * This is especially tied to the [TagPayload] application, thus all hooks, that creates [Tag]s. Exactly within this
 * context there is the need to define the fallback at the calling side.
 *
 * TODO: Show example (Somewhere in the tailwind-project there was an application... buttons, spinner?)
 */
fun <C, R, P> C.hook(h: Hook<C, R, TagPayload<P>>, classes: String?, id: String?, payload: P, fallback: C.() -> R) =
    if (h.isSet) hook(h, classes, id, payload)
    else fallback.invoke(this)

/**
 * This hook abstraction simplifies a [Tag] creating [Hook] by offering static [invoke] methods, which already
 * deal with constructing the [value] field, so that an abstract [renderTag] method is called. This covers the typical
 * use case where a client only wants to configure exactly one value (flow or static) and the hook should use this to
 * render some artifact.
 *
 * A good example would be a hook for rendering some icon; it would accept a special ``IconDefinition`` and render
 * some ``<svg>``-tag including sub-tags:
 * ```kotlin
 * class IconHook : RenderOneValueTagHook<Svg, Null, IconDefinition>() {
 *      override fun RenderContext.renderTag(classes: String?, id: String?, data: IconDefinition, payload: Unit): Svg {
 *          svg(classes, id) {
 *              // set some attributes based upon the fields of ``IconDefinition``
 *          }
 *      }
 * }
 * ```
 * So the client can easily call this:
 * ```kotlin
 * // a component exposes this hook:
 * class SomeComponent {
 *      val closeIcon = IconHook()
 * }
 *
 * // Client configures this easily:
 * closeIcon(Icons.close)
 * ```
 *
 * Its main motivation is to reduce the repeating boilerplate code of exactly the same [invoke] method implementations.
 *
 * @see TagPayload
 */
abstract class TagHook<R : Tag<*>, P, I> : Hook<RenderContext, R, TagPayload<P>>() {
    protected abstract fun RenderContext.renderTag(classes: String?, id: String?, data: I, payload: P): R

    operator fun invoke(value: I) = this.apply {
        this.value = { (classes, id, payload) ->
            renderTag(classes, id, value, payload)
        }
    }

    operator fun invoke(value: Flow<I>) = this.apply {
        this.value = { (classes, id, payload) ->
            export {
                value.render {
                    export(renderTag(classes, id, it, payload))
                }
            }
        }
    }
}
