package dev.fritz2.headless.foundation

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