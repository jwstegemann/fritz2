package dev.fritz2.headless.foundation

/**
 * An Exporter is a simple container for capturing some return value. Its main purpose is to reduce boilerplate code
 * if the value to be returned is created within some nested structure and would therefor need some preliminary
 * defined `var` outside the structure and some explicit return of the value afterwards.
 *
 * It is best used implicitly by the corresponding [export] function.
 *
 * ```kotlin
 * // function wants to return an inner Tag `input`
 * fun RenderContext.renderSomeStructure() : Input {
 *      return export {
 * //   ^^^^^^^^^^^^^
 * //   return captured value
 *          div {
 *              export(input {
 * //           ^^^^^^^^^^^^
 * //           capture the needed value; the input tag in this case!
 *                  type("text")
 *                  value(someStore.data)
 *              })
 *          }
*       }
 * }
 * ```
 */
class Exporter<T : Any>(initialize: Exporter<T>.() -> Unit) {
    lateinit var payload: T

    fun export(payload: T): T {
        this.payload = payload
        return payload
    }

    init {
        initialize()
    }
}

/**
 * Scoping function to capture some value by an [Exporter] and return the value.
 *
 * @see [Exporter]
 *
 * @param scope some value generating expression with [Exporter] as receiver
 * @return some arbitrary type [T]
 */
fun <T : Any> export(scope: Exporter<T>.() -> Unit): T =
    Exporter(scope).payload
