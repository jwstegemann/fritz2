/**
 * # Module fritz2-serialization
 *
 * This module contains a few helper functions for easy interoperability
 * with [`kotlinx.serialization`](https://kotlinlang.org/api/kotlinx.serialization/).
 *
 * The example in the [documentation](https://www.fritz2.dev/docs/http/)
 * could be simplified as follows.
 *
 * ```kotlin
 * @Serializable
 * data class Planet(val name: String)
 *
 * val swapiStore = object : RootStore<String>("") {
 *
 *     private val api = http("https://swapi.dev/api")
 *         .acceptJson()
 *         .contentType("application/json")
 *
 *     val planetName = handle<Int> { _, num ->
 *         val resp = api.get("planets/$num")
 *         require(resp.ok)
 *         resp.decoded<Planet>().name
 *     }
 * }
 * ```
 *
 * **Note**:
 * for performance reasons this module does **not** convert values
 * to string using
 * [`encodeToString`](https://kotlinlang.org/api/kotlinx.serialization/kotlinx-serialization-core/kotlinx.serialization/encode-to-string.html)
 * and [`decodeFromString`](https://kotlinlang.org/api/kotlinx.serialization/kotlinx-serialization-core/kotlinx.serialization/decode-from-string.html).
 * Values are instead turned into "native" JavaScript objects using
 * [`encodeToDynamic`](https://kotlinlang.org/api/kotlinx.serialization/kotlinx-serialization-json/kotlinx.serialization.json/encode-to-dynamic.html)
 * and [`decodeFromDynamic`](https://kotlinlang.org/api/kotlinx.serialization/kotlinx-serialization-json/kotlinx.serialization.json/decode-from-dynamic.html),
 * which work natively with the Fetch API used by `dev.fritz2.remote`.
 */

package dev.fritz2.remote

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic
import kotlinx.serialization.json.encodeToDynamic

@PublishedApi internal val lenientJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

/**
 * Encodes the given value as the body of the request.
 *
 * @param value information to be encoded
 * @param json (optional) JSON serialization configuration
 */
@ExperimentalSerializationApi
inline fun <reified T> Request.bodyJson(
    value: T,
    json: Json = lenientJson,
): Request = body(JSON.stringify(json.encodeToDynamic(value)))

/**
 * Encodes the given value as the body of the request.
 *
 * @param value information to be encoded
 * @param serializer serializer from `kotlinx-serialization`
 * @param json (optional) JSON serialization configuration
 */
@ExperimentalSerializationApi
fun <T> Request.bodyJson(
    value: T,
    serializer: KSerializer<T>,
    json: Json = lenientJson,
): Request = body(JSON.stringify(json.encodeToDynamic(serializer, value)))

/**
 * Decodes the information in the response from the JSON representation.
 *
 * @param json (optional) JSON serialization configuration
 */
@ExperimentalSerializationApi
suspend inline fun <reified T> Response.decoded(
    json: Json = lenientJson,
): T = json.decodeFromDynamic<T>(this.json())

/**
 * Decodes the information in the response from the JSON representation.
 *
 * @param serializer serializer from `kotlinx-serialization`
 * @param json (optional) JSON serialization configuration
 */
@ExperimentalSerializationApi
suspend fun <T> Response.decoded(
    serializer: KSerializer<T>,
    json: Json = lenientJson,
): T = json.decodeFromDynamic(serializer, this.json())