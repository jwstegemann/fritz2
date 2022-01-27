package dev.fritz2.headless.foundation

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
interface Property {
    val isSet: Boolean
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