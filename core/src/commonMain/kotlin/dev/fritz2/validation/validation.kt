package dev.fritz2.validation

import dev.fritz2.core.Inspector
import dev.fritz2.core.inspectorOf
import kotlin.jvm.JvmInline

/**
 * Encapsulates the logic for validating a given data-model with metadata.
 *
 * The validation logic itself is expressed by a function that must be passed as [validate] parameter.
 * This function takes the actual model-data [D] as well as the metadata [T] in order to create a [List] of
 * validation messages [M]. This value class simply wraps the provided [validate] function in order to make it
 * invocable without any ceremony.
 *
 * It appears to be a good practice to put the implementation of the passed [validate] function right next to your data
 * classes in the `commonMain` section of your Kotlin multiplatform project.
 * This way you can write the validation logic once and use them on the *JS* and *JVM* side.
 *
 * For example:
 * ```kotlin
 * data class Person(val name: String, val birthday: LocalDate) {
 *     companion object {
 *          // define validator inside of its corresponding domain type
 *          val validate: Validator<Person, LocalDate, SomeMessage> = validation { inspector, today ->
 *              inspector.map(Person.name()).let { nameInspector ->
 *                  if(nameInspector.data.isBlank())
 *                      add(SomeMessage(nameInspector.path, "Name must not be blank"))
 *              }
 *              inspector.map(Person.birthday()).let { birthdayInspector ->
 *                  if(birthdayInspector.data > today)
 *                      add(SomeMessage(birthdayInspector, path, "Birthday must not be in the future"))
 *              }
 *          }
 *     }
 * }
 * ```
 *
 * You can also compose validators:
 * ```kotlin
 * data class Person(val name: String, val birthday: LocalDate) {
 *      // take from example above!
 * }
 *
 * data class User(val nickname: String, val person: Person) {
 *      data class UserMetaData(val nicknameRepo: NicknameRepository, val today: LocalDate)
 *
 *      companion object {
 *          val validate: Validator<User, UserMetaData, SomeMessage> = validation { inspector, meta ->
 *              inspector.map(User.nickname()).let { nicknameInspector ->
 *                  if(meta.nicknameRepo.exists(nicknameInspector.data))
 *                      add(SomeMessage(nicknameInspector.path, "Nickname is already in use"))
 *              }
 *              // use validator of `Person` type by just calling the validator and passing the mapped inspector
 *              // and of course the appropriate meta-data!
 *              addAll(Person.validate(inspector.map(User.person()), meta.today))
 *          }
 *      }
 * }
 * ```
 *
 * @param D data-model to validate
 * @param T metadata which perhaps is needed in validation process
 */
@JvmInline
value class Validation<D, T, M>(private inline val validate: (Inspector<D>, T) -> List<M>) {
    operator fun invoke(inspector: Inspector<D>, metadata: T): List<M> = this.validate(inspector, metadata)
    operator fun invoke(data: D, metadata: T): List<M> = this.validate(inspectorOf(data), metadata)
}

/**
 * Convenience function for creating a [Validation] instance accepting model- and metadata by working on a
 * [MutableList] receiver and using an [Inspector] for getting the right [Inspector.path] from sub-models
 * next to the [Inspector.data].
 */
fun <D, T, M> validation(validate: MutableList<M>.(Inspector<D>, T) -> Unit): Validation<D, T, M> =
    Validation { data, metadata ->
        buildList<M> { validate(data, metadata) }
    }

/**
 * Convenience function for creating a [Validation] instance only accepting model-data by working on a
 * [MutableList] receiver and using an [Inspector] for getting the right [Inspector.path] from sub-models
 * next to the [Inspector.data].
 */
fun <D, M> validation(validate: MutableList<M>.(Inspector<D>) -> Unit): Validation<D, Unit, M> =
    Validation { data, _ ->
        buildList<M> { validate(data) }
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