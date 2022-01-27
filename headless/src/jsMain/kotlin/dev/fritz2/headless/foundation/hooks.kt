package dev.fritz2.headless.foundation

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.RenderContext
import kotlinx.coroutines.flow.Flow

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
 * @see Property
 * @see PreciseRenderingHook
 * @see Enhanceable
 */
abstract class BasicHook<C, R, P> : Property, Enhanceable<R> by EnhanceableMixin() {

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
 * @see Property
 * @see BasicHook
 * @see Enhanceable
 */
abstract class PreciseRenderingHook<C, R, P> : Property, Enhanceable<R> by EnhanceableMixin() {

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
