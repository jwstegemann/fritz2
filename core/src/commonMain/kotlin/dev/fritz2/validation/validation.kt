package dev.fritz2.validation

import dev.fritz2.identification.Inspector
import dev.fritz2.identification.inspectorOf
import kotlin.jvm.JvmInline

/**
 * Encapsulates the logic for validating a given data-model and some optional meta-data.
 *
 * The validation logic itself is expressed by some function that must be passed as validate parameter.
 * This function must accept the actual model-data and some optional meta-data in order to create a [List] of messages.
 * This value class simply wraps the provided validation function in order to make it invocable without any ceremony.
 *
 * It appears to be a good practise, to put the implementation of the passed validate function right next to your data
 * classes in the `commonMain` section of your Kotlin multiplatform project.
 * So you can write the validation logic once and use them on the JS and JVM side.
 *
 * @param D data to validate
 * @param T metadata which perhaps is needed in validation process
 */
@JvmInline
value class Validation<D, T, M>(private inline val validate: (D, T?) -> List<M>) {
    operator fun invoke(data: D, metadata: T? = null): List<M> = this.validate(data, metadata)
}

/**
 * Convenience function for creating a [Validation] instance accepting model- and meta-data by working on a
 * [MutableList] receiver and using an [Inspector] for getting the right [Inspector.path] from sub-models
 * next to the [Inspector.data].
 */
fun <D, T, M> validation(validate: MutableList<M>.(Inspector<D>, T?) -> Unit): Validation<D, T, M> =
    Validation { data, metadata ->
        buildList<M> { validate(inspectorOf(data), metadata) }
    }

/**
 * Convenience function for creating a [Validation] instance only accepting model-data by working on a
 * [MutableList] receiver and using an [Inspector] for getting the right [Inspector.path] from sub-models
 * next to the [Inspector.data].
 */
fun <D, M> validation(validate: MutableList<M>.(Inspector<D>) -> Unit): Validation<D, Unit, M> =
    Validation { data, _ ->
        buildList<M> { validate(inspectorOf(data)) }
    }

/**
 * Minimal interface for a validation message that exposes the model path for matching relevant sub-model-data and
 * probably relevant UI element representation for showing the message and getting information about the valid state
 * after validation process.
 */
interface ValidationMessage {

    /**
     * Path inside your model derived from [Inspector.path]
     */
    val path: String

    /**
     * Decides if the [ValidationMessage] is an error which is needed to determine if validation state is
     * successful or not.
     *
     * It is intentional to explicitly define a message as an error to realize scenarios, where also pure information
     * or warning messages could arise, that should *not* stop the process.
     *
     * If an application considers every message as error, just set this to `true`.
     */
    val isError: Boolean
}

/**
 * Returns *true* when the list contains no [ValidationMessage] which is marked with [ValidationMessage.isError].
 */
val <M : ValidationMessage> List<M>.valid: Boolean get() = none { it.isError }