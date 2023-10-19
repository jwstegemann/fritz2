@file:Suppress("unused")

package dev.fritz2.validation

import dev.fritz2.core.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*


/**
 * A [ValidatingStore] is a [Store] which also contains a [Validation] for its model and by default applies it
 * to every update.
 *
 * This store is intentionally configured to validate the data on each update, that is why the [validateAfterUpdate]
 * parameter is set to `true` by default.
 *
 * There might be special situations where it is reasonable to disable this behaviour by setting [validateAfterUpdate]
 * to `false` and to prefer applying the validation individually within custom handlers, for example if a model should
 * only be validated after the user has completed his input or if metadata is needed for the validation process.
 * Then be aware of the fact, that the call of the [validate] function actually updates the [messages] [Flow] already.
 *
 * In order for the automatic validation to work, a [metadataDefault] value must be specified. This is needed due to no
 * specific metadata being present during automatic validation. When calling [validate] manually, the appropriate
 * metadata can be supplied directly.
 *
 * If the new data is not passed to the store's state after validating it, the messages are probably out of sync with
 * the actual store's state!
 * This could lead to false assumptions and might produce hard to detect bugs in your application.
 *
 * @param initialData first current value of this [Store]
 * @param validation [Validation] function to use at the data on this [Store].
 * @param metadataDefault default metadata to be used by the automatic validation (where no explicit values are given)
 * @param job Job to be used by the [Store]
 * @param validateAfterUpdate flag to decide if a new value gets automatically validated after setting it to the [Store].
 * @param id id of this [Store]. Ids of parent [Store]s will be concatenated.
 */
open class ValidatingStore<D, T, M>(
    initialData: D,
    private val validation: Validation<D, T, M>,
    private val metadataDefault: T,
    job: Job,
    private val validateAfterUpdate: Boolean = true,
    override val id: String = Id.next(),
) : RootStore<D>(initialData, job, id) {

    private val validationMessages: MutableStateFlow<List<M>> = MutableStateFlow(emptyList())

    /**
     * [Flow] of the [List] of validation-messages.
     * Use this [Flow] to render out the validation-messages and to detect the valid state of the current [data] [Flow].
     */
    val messages: Flow<List<M>> = validationMessages.asStateFlow()

    /**
     * Resets the validation result.
     *
     * Beware that cleaning the messages should not be done, if the [data] [Flow] remains in an invalid state.
     * Please refer to the class's description for details about the need for a sound data and messages state.
     *
     * @param messages list of messages to reset to. Default is an empty list.
     */
    protected fun resetMessages(messages: List<M> = emptyList()) {
        validationMessages.value = messages
    }

    /**
     * Validates the given [data] using the given [metadata], updates the [messages] list and returns them.
     * If no metadata is specified, [metadataDefault] is used.
     *
     * Use this method from inside your [Handler]s to publish
     * the new state of the validation result via the [messages] flow.
     *
     * @param data data to validate
     * @param metadata metadata for validation
     * @return [List] of messages
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun validate(data: D, metadata: T = metadataDefault): List<M> =
        validation(data, metadata).also { validationMessages.value = it }

    init {
        if (validateAfterUpdate) data.drop(1) handledBy { newValue ->
            validate(newValue, metadataDefault)
        }
    }
}

/**
 * Checks if a [Flow] of a [List] of [ValidationMessage]s is valid.
 */
val <M : ValidationMessage> Flow<List<M>>.valid: Flow<Boolean>
    get() = this.map { it.valid }

/**
 * Convenience function to create a simple [ValidatingStore] without any handlers, etc.
 *
 * The created [Store] validates its model after every update automatically.
 *
 * @param initialData first current value of this [Store]
 * @param validation [Validation] instance to use at the data on this [Store].
 * @param metadataDefault default metadata to be used by the automatic validation (where no explicit values are given)
 * @param job Job to be used by the [Store]
 * @param id id of this [Store]. Ids of [SubStore]s will be concatenated.
 */
fun <D, T, M> storeOf(
    initialData: D,
    validation: Validation<D, T, M>,
    metadataDefault: T,
    job: Job = Job(),
    id: String = Id.next(),
): ValidatingStore<D, T, M> =
    ValidatingStore(initialData, validation, metadataDefault, job, validateAfterUpdate = true, id)

/**
 * Convenience function to create a simple [ValidatingStore] without any metadata and handlers.
 *
 * The created [Store] validates its model after every update automatically.
 *
 * @param initialData first current value of this [Store]
 * @param validation [Validation] instance to use at the data on this [Store].
 * @param job Job to be used by the [Store]
 * @param id id of this [Store]. Ids of [SubStore]s will be concatenated.
 */
fun <D, M> storeOf(
    initialData: D,
    validation: Validation<D, Unit, M>,
    job: Job,
    id: String = Id.next(),
): ValidatingStore<D, Unit, M> =
    ValidatingStore(initialData, validation, Unit, job, validateAfterUpdate = true, id)

/**
 * Convenience function to create a simple [ValidatingStore] without any handlers, etc.
 *
 * The created [Store] validates its model after every update automatically.
 *
 * @param initialData first current value of this [Store]
 * @param validation [Validation] instance to use at the data on this [Store].
 * @param metadataDefault default metadata to be used by the automatic validation (where no explicit values are given)
 * @param job Job to be used by the [Store]
 * @param id id of this [Store]. Ids of [SubStore]s will be concatenated.
 */
fun <D, T, M> WithJob.storeOf(
    initialData: D,
    validation: Validation<D, T, M>,
    metadataDefault: T,
    job: Job = this.job,
    id: String = Id.next(),
): ValidatingStore<D, T, M> =
    ValidatingStore(initialData, validation, metadataDefault,  job, validateAfterUpdate = true, id)

/**
 * Convenience function to create a simple [ValidatingStore] without any metadata and handlers.
 *
 * The created [Store] validates its model after every update automatically.
 *
 * @param initialData first current value of this [Store]
 * @param validation [Validation] instance to use at the data on this [Store].
 * @param job Job to be used by the [Store]
 * @param id id of this [Store]. Ids of [SubStore]s will be concatenated.
 */
fun <D, M> WithJob.storeOf(
    initialData: D,
    validation: Validation<D, Unit, M>,
    job: Job = this.job,
    id: String = Id.next(),
): ValidatingStore<D, Unit, M> =
    ValidatingStore(initialData, validation, Unit, job, validateAfterUpdate = true, id)

/**
 * Finds all corresponding [ValidationMessage]s to this [Store].
 *
 * Be aware that the  filtering is based upon the correct usage of [Store.path]'s field. This can be reliably achieved
 * by using [dev.fritz2.core.Inspector]s and their mappings for creating the correct path values.
 */
fun <M : ValidationMessage> Store<*>.messages(): Flow<List<M>>? =
    when (this) {
        is ValidatingStore<*, *, *> -> {
            try {
                this.messages.map { it.unsafeCast<List<M>>() }
            } catch (e: Exception) {
                null
            }
        }

        is SubStore<*, *> -> {
            var store: Store<*> = this
            while (store is SubStore<*, *>) {
                store = store.parent
            }
            if (store is ValidatingStore<*, *, *>) {
                try {
                    store.messages.map {
                        it.unsafeCast<List<M>>()
                            .filter { m -> m.path == this.path || m.path.startsWith("${this.path}.") }
                    }
                } catch (e: Exception) {
                    null
                }
            } else null
        }

        else -> null
    }