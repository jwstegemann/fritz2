package io.fritz2.binding

import io.fritz2.dom.html.Events
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.w3c.dom.events.Event
import kotlin.browser.window

external fun decodeURIComponent(encodedURI: String): String
external fun encodeURIComponent(decodedURI: String): String

@FlowPreview
@ExperimentalCoroutinesApi
object Router {

    private const val divider = "&"
    private const val assignment = "="
    private var current: Map<String, String>
    init {
        val hash = window.document.location?.hash
        current = if(hash != null) unmarshal(hash) else emptyMap()
    }

    private val flow: Flow<Map<String, String>> = callbackFlow {
        val listener: (Event) -> Unit = {
            val hash = window.document.location?.hash
            if(hash != null && hash.isNotBlank()) {
                current = unmarshal(hash)
                channel.offer(current)
            }
        }
        window.addEventListener(Events.hashchange.name, listener)

        awaitClose { window.removeEventListener(Events.hashchange.name, listener) }
    }

    val params = flow.distinctUntilChanged()

    fun set(key: String, value: String) {
        current += key to value
        window.location.hash = marshal(current)
    }

    fun setAll(all: Map<String, String>) {
        current = all
        window.location.hash = marshal(current)
    }

    fun listenFor(key: String): Flow<String> = params.filter{m -> m.containsKey(key)}.map{m -> m[key] ?: ""}

    private fun marshal(params: Map<String, String>): String =
        params.map { (key, value) -> "$key$assignment${encodeURIComponent(value)}" }.joinToString(separator = divider)

    private fun unmarshal(params: String): Map<String, String> =
        params.drop(1).split(divider).filter{s -> s.isNotBlank()}.asReversed().map(::extractPair).toMap()

    private fun extractPair(param: String): Pair<String, String> {
        val equalsPos = param.indexOf(assignment)
        return if (equalsPos > 0) {
            val key = param.substring(0, equalsPos)
            val value = decodeURIComponent(param.substring(equalsPos + 1))
            key to value
        }
        else param to "true"
    }
}