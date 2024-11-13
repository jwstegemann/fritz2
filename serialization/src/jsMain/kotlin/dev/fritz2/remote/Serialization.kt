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