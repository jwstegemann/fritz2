package io.fritz2.binding

import io.fritz2.dom.html.Events
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.w3c.dom.events.Event
import kotlin.browser.window

@FlowPreview
@ExperimentalCoroutinesApi
fun routing(default: String): Router<String> = object : Router<String>() {
    override fun unmarshal(hash: String): String = hash
    override fun marshal(route: String): String = route
}.apply { if (window.location.hash.removePrefix(prefix).isBlank()) setRoute(default) }

@FlowPreview
@ExperimentalCoroutinesApi
fun routing(default: Map<String, String>): RouterWithMap = RouterWithMap()
    .apply { if (window.location.hash.removePrefix(prefix).isBlank()) setRoute(default) }

//TODO add router for data classes

/**
 * Router register the event-listener for hashchange-event and
 * handles route-changes. Therefore it uses [marshal] and [unmarshal] methods.
 *
 * @param T type to marshal the hash string to
 */
@FlowPreview
@ExperimentalCoroutinesApi
abstract class Router<T> {
    internal val prefix = "#"

    private val updates: Flow<T> = callbackFlow {
        val listener: (Event) -> Unit = {
            it.preventDefault()
            val hash = window.location.hash.removePrefix(prefix)
            if (hash.isNotBlank()) {
                channel.offer(unmarshal(hash))
            }
        }
        window.addEventListener(Events.load.name, listener)
        window.addEventListener(Events.hashchange.name, listener)

        awaitClose { window.removeEventListener(Events.hashchange.name, listener) }
    }

    val data: Flow<T> = updates.distinctUntilChanged()

    abstract fun unmarshal(hash: String): T
    abstract fun marshal(route: T): String

    internal fun setRoute(route: T) {
        val newRoute = prefix + marshal(route)
        window.location.hash = newRoute
    }

    val navTo: Handler<T> = handle { route -> setRoute(route) }

    private inline fun handle(crossinline handler: (T) -> Unit) = Handler<T> {
        GlobalScope.launch {
            it.collect {
                handler(it)
            }
        }
    }
}

external fun decodeURIComponent(encodedURI: String): String
external fun encodeURIComponent(decodedURI: String): String

@FlowPreview
@ExperimentalCoroutinesApi
class RouterWithMap : Router<Map<String, String>>() {
    private val assignment = "="
    private val divider = "&"

    override fun unmarshal(hash: String): Map<String, String> =
        hash.split(divider).filter { s -> s.isNotBlank() }.asReversed().map(::extractPair).toMap()

    override fun marshal(route: Map<String, String>): String =
        route.map { (key, value) -> "$key$assignment${encodeURIComponent(value)}" }.joinToString(separator = divider)

    private fun extractPair(param: String): Pair<String, String> {
        val equalsPos = param.indexOf(assignment)
        return if (equalsPos > 0) {
            val key = param.substring(0, equalsPos)
            val value = decodeURIComponent(param.substring(equalsPos + 1))
            key to value
        } else param to "true"
    }

    fun <X> select(key: String, mapper: (Pair<String, Map<String, String>>) -> X): Flow<X> =
        data.map { m -> mapper((m[key] ?: "") to m) }
}