package dev.fritz2.headless.foundation

/**
 * This class implements the concept of some data holder, that can be configured *after* construction by some client
 * code. A property is usually used within some kind of component, which needs to hold some specific data portion
 * in order to work, but to delegate the setup of the data to the client code.
 *
 * A property should be applied for composed data and not so likely for simple types like [Boolean], [Int] or alike.
 * (Prefer to offer a simple `public var` field for the latter!)
 *
 * The data should be set by dedicated ``operator fun invoke()`` methods, that defines the public API for
 * the configuration aspect:
 * ```kotlin
 * val user = object : Property<User>() {
 *      // Only visible API for the client, "hide" the complex type by offering the parameters directly!
 *     operator fun invoke(name: String, alias: String, mail: String) {
 *          value = User(name, alias, mail)
 *     }
 *
 *     // perhaps some other `invoke`s with convenience parameters!
 * }
 * ```
 *
 *  A property offers two important functions for the applicator:
 *  - checking whether a value was set ...
 *  - ... and using some other [Property]'s value.
 *
 *  As a client cannot be forced to `invoke` some [Property] it is often important to check for the applicator,
 *  if some data is present or not in order to fall back to some default behaviour. The [Property] class offers the
 *  [isSet] property for this use case.
 *
 *  If a component is based upon the headless approach (very likely if you use this part of fritz2's lib), it is common
 *  for a component to forward some property to the underlying headless-component. To easily support this feature,
 *  the [use] method exists. Just pass the outer component's [Property] [value] to the headless component's [Property]'s
 *  [use] method.
 */
abstract class Property<T> {
    var value: T? = null
        protected set

    val isSet: Boolean
        get() = value != null

    fun use(item: T) {
        this.value = item
    }
}
