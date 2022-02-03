package dev.fritz2.headless.foundation

/**
 * This interface defines concept of some data holder, that can be configured after construction by some client
 * code.
 *
 * The data should be set by dedicated ``operator fun invoke()`` methods, that defines the public API for
 * configuration.
 *
 */
interface Property {
    val isSet: Boolean
}

/**
 * This is a companion interface to [Property] and should always be implemented by all properties!
 *
 * It defines the [use] method, which acts as some kind of copy function in order to take all fields from some
 * other [Property] and transfer them to the fields of the calling [Property] instance.
 *
 * Beware that all [Hook]s should also implement this interface, as they are only some enhanced properties in the end.
 */
interface Usable<T> {

    /**
     * This method should act as central place to copy the appropriate fields from the [other] [Property].
     *
     * @param other the specific [Property] instance this hook should copy to its own fields.
     */
    fun use(other: T)
}