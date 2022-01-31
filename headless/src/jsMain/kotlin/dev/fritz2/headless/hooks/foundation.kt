package dev.fritz2.headless.hooks

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.headless.foundation.export
import kotlinx.coroutines.flow.Flow

/**
 * This interface defines the minimal base for all hooks.
 *
 * A hook is a type, that combines configuration mechanisms (preferable by `invoke` functions) with encapsulated
 * behaviour like rendering specific, small ui snippets or handling typical data-binding like accepting a store and
 * also some fitting [Flow]. The hook itself will use the configuration aspect as well as it offers those values
 * to the holder (a component in most cases), which might also need to use them.
 *
 * Hooks can therefore be used to build rather generic, reusable building blocks of an application, but also to
 * implement very specific solutions.
 *
 * Components should strive to use hooks for all their parts that need to be configurable and varying in their
 * behaviours. Think of some input-field that should render a label next to it. This label clearly needs to be
 * configured by the calling client, so it must be configurable. The label could be reused for different kind of
 * form control elements too, so it clearly makes sense to encapsulate the configuration and its representation
 * (the rendering function) into one object that should be some kind of hook.
 *
 * As one main aspect of a hook is the configuration, it is possible, that the hook never gets configured by the client.
 * That is why it is extremely important for the application of a hook to check, whether it is set or not. So this
 * interface offers the [isSet] method as minimal contract.
 */
interface Hook {
    val isSet: Boolean
}

/**
 * Apply this interface to every hook in order to make it enhanceable for client calls (after `invoke`).
 * As the hook is executed later, the result is not available during configuration. So the action must be stored
 * internally until the hook is applied.
 */
interface Enhanceable<R> {
    val alsoExpr: (R.() -> Unit)?

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
    fun also(expr: R.() -> Unit)
}

/**
 * This implementation of the [Enhanceable] interface could be used as "mixin" by leveraging Kotlin's delegation
 * functionality:
 *
 * ```
 * class SomeHook<R> : Hook<SomeHook>, Enhanceable<R> by EnhanceableMixin() {
 *     // and so on
 * }
 *
 * // use this later by supplying an `also` call onto the hook:
 * myComponent {
 *     someHook(/* ... */).also { // `this` is the result type of the hooks execution
 *         // manipulate the result
 *     }
 * }
 * ```
 */
class EnhanceableMixin<R> : Enhanceable<R> {
    private var alsoExprField: (R.() -> Unit)? = null

    /**
     * This property should be used by the application of a hook (see the different `hook` functions) in order to
     * apply the expression to the returned result of the hook.
     */
    override val alsoExpr: (R.() -> Unit)?
        get() = alsoExprField

    /**
     * This method should be used by a client, to modify the result of the application of the hook.
     * Think of applying some specific styling or get access to specific events of some [Tag] or alike.
     *
     * @see [Enhanceable.also]
     */
    override fun also(expr: R.() -> Unit) {
        alsoExprField = expr
    }
}

/**
 * Apply this interface to some hook in order to enable it to be configured by some other, already configured hook.
 * This is important for dev.fritz2.headless.components that *reuse* others internally, so those have to offer the same (or some similar)
 * hook from within its own initializer block. Those initialized hooks must then be passed in some way to the sub-
 * component, which [Usable.use] is all about.
 */
interface Usable<T> {

    /**
     * This method should act as central place to copy the appropriate fields from the [other] hook.
     * At minimum this affects the `apply` and [Enhanceable.alsoExpr] fields, but often some more fields might
     * be needed.
     *
     * @param other the specific hook type this hook should be able to use.
     */
    fun use(other: T)
}

/**
 * This abstraction defines the base hook for all use cases, where the applicator of the hook (a component for most
 * cases) does not need or does not have any knowledge about how the hook solves its task. Sometimes the applicator
 * might inject some payload into the hook, in order to enable the hook to adapt to ist execution context or to
 * enable the hook to offer something to its client.
 *
 * The [apply] field stores the executable behaviour of the hook, which needs to be implemented. Its shape is a
 * functional expression with some receiver type `C`, the payload parameter `P` and some result type `R`.
 *
 * If an implementation needs some complex payload, use some dataclass or other containers to comply to its signature.
 *
 * DO NOT CREATE SOME OTHER HOOK ABSTRACTION WITH MORE TYPES FOR THE PAYLOAD!
 *
 * For the special cases where an applicator knows about the specific internal rendering structure of a hook
 * implementation, prefer to use [PreciseRenderingHook]
 *
 * @see Hook
 * @see PreciseRenderingHook
 * @see Enhanceable
 */
abstract class BasicHook<C, R, P> : Hook, Enhanceable<R> by EnhanceableMixin() {

    /**
     * Holds the encapsulated effect. The caller (component) has no clue about the content, at maximum about a
     * specific result type, that can be further refined.
     *
     * Offers the possibility to provide any payload parameter by the caller (component). Use data classes or other
     * containers to provide complex payloads.
     *
     * Do not provide any style or id within payload parameter `<P>`! Use [PreciseRenderingHook] instead!
     */
    var apply: (C.(P) -> R)? = null

    override val isSet: Boolean
        get() = (apply != null)
}

/**
 * This hook method applies a [BasicHook]'s encapsulated behaviour to the calling context and passes a given payload.
 * It also applies a given also-expression.
 *
 * @see BasicHook
 *
 * @param h The hook implementation
 * @param payload some additional data
 */
fun <C, R, P> C.hook(h: BasicHook<C, R, P>, payload: P) =
    h.apply?.invoke(this, payload)?.also { h.alsoExpr?.invoke(it) }

/**
 * This hook method applies a [BasicHook]'s encapsulated behaviour to the calling context.
 * It also applies a given also-expression.
 *
 * @see BasicHook
 *
 * @param h The hook implementation
 */
fun <C, R> C.hook(h: BasicHook<C, R, Unit>) =
    h.apply?.invoke(this, Unit)?.also { h.alsoExpr?.invoke(it) }

// TODO: Might be useful to offer some safe variation of this hook with a fallback parameter?
//  (see hook variant for ``PreciseRenderingHook`` below)

/**
 * This hook method applies multiple [BasicHook]'s encapsulated behaviour to the calling context and passes the _same_
 * given payload to each hook. It also applies a given also-expression of each hook.
 *
 * This is a shortcut for situation where lots of hooks needs to be applied at the same location:
 * ```
 * // instead of this...
 * hook(a, Unit)
 * hook(b, Unit)
 * hook(c, Unit)
 *
 * // ... the application of this variant shortens the code:
 * hook(a, b, c, payload = Unit)
 * ```
 *
 * @see BasicHook
 *
 * @param h some hook implementations
 * @param payload some additional data
 */
fun <C, R, P> C.hook(vararg h: BasicHook<C, R, P>, payload: P) = h.forEach { hook ->
    hook.apply?.invoke(this, payload)?.also { hook.alsoExpr?.invoke(it) }
}

/**
 * This hook offers a base for implementations that should render a specific structure, which should be styleable
 * by the caller. It offers the possibility to inject style classes and an id for the caller.
 *
 * Use this hook as base in case that the hook itself should have full control over the rendered structure, which
 * itself enables the caller (component) to modify the appearance by some classes and to provide an id that is bound
 * to a precise place within the DOM. The client should be by intention very limited in providing content.
 *
 * If an applicator does not need any knowledge about the internal result structure of a hook, prefer to use
 * [BasicHook].
 *
 * @see Hook
 * @see BasicHook
 * @see Enhanceable
 */
abstract class PreciseRenderingHook<C, R, P> : Hook, Enhanceable<R> by EnhanceableMixin() {

    /**
     * Holds the encapsulated precise rendering, that is some rendering the hook has full control so the resulting
     * HTML structure is well-defined and can therefore be tuned by the caller (component) with classes and id!
     */
    var apply: (C.(String?, String?, P) -> R)? = null

    override val isSet: Boolean
        get() = (apply != null)
}

/**
 * This hook method applies a [PreciseRenderingHook]'s encapsulated behaviour to the calling context and passes
 * an optional given styling parameter, an optional given id for the hooks content and  a given payload.
 * It also applies a given also-expression.
 *
 * @see PreciseRenderingHook
 *
 * @param h The hook implementation
 * @param classes some optional styling information
 * @param id an optional id for applying to the hook's content
 * @param payload some additional data
 */
fun <C, R, P> C.hook(h: PreciseRenderingHook<C, R, P>, classes: String?, id: String?, payload: P) =
    h.apply?.invoke(this, classes, id, payload)?.also { h.alsoExpr?.invoke(it) }

// no vararg variant provided -> id must be unique, so multiple application does not make any sense!

/**
 * This hook variant is a _safe_ application of a hook, which offers a [fallback] parameter that is executed, if
 * hook's apply field is not set.
 */
fun <C, R, P> C.hook(h: PreciseRenderingHook<C, R, P>, classes: String?, id: String?, payload: P, fallback: C.() -> R) =
    if (h.isSet) hook(h, classes, id, payload)
    else fallback.invoke(this)

/**
 * This hook abstraction refines the `PreciseRenderingHook` further by offering static [invoke] methods, which already
 * deal with constructing the [apply] field, so that an abstract [renderTag] method is called. This covers the typical
 * use case where a client only wants to configure exactly one value (flow or static) and the hook should use this to
 * render some artifact.
 *
 * Its main motivation is to reduce the repeating boilerplate code of exactly the same [invoke] method implementations.
 *
 * @see PreciseRenderingHook
 */
abstract class RenderOneValueTagHook<R : Tag<*>, P, I> : PreciseRenderingHook<RenderContext, R, P>() {
    protected abstract fun RenderContext.renderTag(classes: String?, id: String?, data: I, payload: P): R

    operator fun invoke(value: I) = this.apply {
        apply = { classes, id, payload ->
            renderTag(classes, id, value, payload)
        }
    }

    operator fun invoke(value: Flow<I>) = this.apply {
        apply = { classes, id, payload ->
            export {
                value.render {
                    export(renderTag(classes, id, it, payload))
                }
            }
        }
    }
}
