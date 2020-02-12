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

external fun decodeURIComponent(encodedURI: String): String
external fun encodeURIComponent(decodedURI: String): String

@FlowPreview
@ExperimentalCoroutinesApi
fun routing(default: String = ""): Router<String> = object : Router<String>(default) {
    override fun unmarshal(hash: String): String = hash
    override fun marshal(route: String): String = route
}

@FlowPreview
@ExperimentalCoroutinesApi
fun routing(default: Map<String, String> = emptyMap()) = RouterWithMap(default)

//TODO add router for data classes

/**
 * Router register the event-listener for hashchange-event and
 * handles route-changes. Therefore it uses [marshal] and [unmarshal]
 *
 * @param T type to marshal to
 * @property initialData default value
 */
@FlowPreview
@ExperimentalCoroutinesApi
abstract class Router<T>(initialData: T) {
    private val prefix = "#"

    private val updates: Flow<Update<T>> = callbackFlow {
        val listener: (Event) -> Unit = {
            it.preventDefault()
            val hash = window.document.location?.hash?.removePrefix(prefix)
            if (hash != null && hash.isNotBlank()) {
                channel.offer { unmarshal(decodeURIComponent(hash)) }
            }
        }
        window.addEventListener(Events.load.name, listener)
        window.addEventListener(Events.hashchange.name, listener)

        awaitClose { window.removeEventListener(Events.hashchange.name, listener) }
    }
    private val applyUpdate: suspend (T, Update<T>) -> T = { lastValue, update -> update(lastValue) }

    val data: Flow<T> = updates.scan(initialData, applyUpdate).distinctUntilChanged()

    abstract fun unmarshal(hash: String): T
    abstract fun marshal(route: T): String

    //FIXME not working yet... always getting a full page reload!?
    val to: Handler<T> = Handler { route ->
        val newRoute = prefix + encodeURIComponent(marshal(route))
        console.log("change hash to: $newRoute")
        window.location.hash = newRoute
        route
    }

    inner class Handler<T>(inline val handler: (T) -> T) {
        private fun handle(action: Flow<T>) {
            GlobalScope.launch {
                action.collect {
                    handler(it)
                }
            }
        }

        // syntactical sugar to write handler <= event-stream
        operator fun compareTo(action: Flow<T>): Int {
            handle(action)
            return 0
        }
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
class RouterWithMap(default: Map<String, String>) : Router<Map<String, String>>(default) {
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