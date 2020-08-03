package dev.fritz2.routing

import dev.fritz2.binding.SimpleHandler
import dev.fritz2.dom.html.Events
import dev.fritz2.flow.asSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.w3c.dom.events.Event
import kotlin.browser.window

/**
 * Creates a new simple [String] based [Router]
 *
 * @param default default route
 */
fun router(default: String): Router<String> = object : Router<String>(
    StringRoute(default)
) {}

/**
 * Creates a new [Map] based [Router]
 *
 * @param default default route
 */
fun router(default: Map<String, String>): Router<Map<String, String>> = object : Router<Map<String, String>>(
    MapRoute(default)
) {}

/**
 * Select return a [Pair] of the value
 * and the complete routing [Map] for the given key in the [mapper] function.
 */
fun <X> Router<Map<String, String>>.select(key: String, mapper: (Pair<String, Map<String, String>>) -> X): Flow<X> =
    routes.map { m -> mapper((m[key] ?: "") to m) }


/**
 * Creates a new type based [Router].
 * Therefore the given type must implement the [Route] interface.
 *
 * @param default default route
 */
fun <T> router(default: Route<T>): Router<T> = object : Router<T>(default) {}

/**
 * A Route is a abstraction for routes
 * which needed for routing
 *
 * @param T type to marshal and unmarshal from
 */
interface Route<T> {
    /**
     * Gives the default value when initialising the routing
     */
    val default: T

    /**
     * Unmarshals the *window.location.hash* to the
     * given type [T] after getting the hashchange-event.
     */
    fun unmarshal(hash: String): T

    /**
     * Marshals a given object of type [T] to [String]
     * for setting it to the *window.location.hash*
     */
    fun marshal(route: T): String
}

/**
 * [StringRoute] is a simple [Route] which
 * marshals and unmarshals nothing.
 *
 * @param default [String] to use when no explicit *window.location.hash* was set before
 */
class StringRoute(override val default: String) : Route<String> {
    override fun unmarshal(hash: String): String = hash
    override fun marshal(route: String): String = route
}

/**
 * [MapRoute] marshals and unmarshals a [Map] to and from *window.location.hash*.
 * It is like using url parameters with pairs of key and value.
 * In the begin there is only a **#** instead of **?**.
 *
 * @param default [Map] to use when no explicit *window.location.hash* was set before
 */
class MapRoute(override val default: Map<String, String>) :
    Route<Map<String, String>> {
    private val assignment = "="
    private val divider = "&"

    override fun unmarshal(hash: String): Map<String, String> =
        hash.split(divider).filter { s -> s.isNotBlank() }.asReversed().map(::extractPair).toMap()

    override fun marshal(route: Map<String, String>): String =
        route.map { (key, value) -> "$key$assignment${encodeURIComponent(value)}" }
            .joinToString(separator = divider)

    private fun extractPair(param: String): Pair<String, String> {
        val equalsPos = param.indexOf(assignment)
        return if (equalsPos > 0) {
            val key = param.substring(0, equalsPos)
            val value = decodeURIComponent(param.substring(equalsPos + 1))
            key to value
        } else param to "true"
    }
}

/**
 * Router register the event-listener for hashchange-event and
 * handles route-changes. Therefore it uses a [Route] object
 * which can [Route.marshal] and [Route.unmarshal] the given type.
 *
 * @param T type to marshal and unmarshal
 * @property route default route to use when page is called and no hash is set
 */
open class Router<T>(private val route: Route<T>) : CoroutineScope by MainScope() {

    private val prefix = "#"

    private val hashchange: Flow<T> = callbackFlow {

        if (window.location.hash.isBlank()) {
            setRoute(route.default)
            offer(route.default)
        } else {
            offer(route.unmarshal(window.location.hash.removePrefix(prefix)))
        }

        val listener: (Event) -> Unit = {
            println("event occurs: $it")
            it.preventDefault()
            offer(route.unmarshal(window.location.hash.removePrefix(prefix)))
        }
        window.addEventListener(Events.hashchange.name, listener)

        awaitClose { window.removeEventListener(Events.hashchange.name, listener) }
    }

    private fun setRoute(newRoute: T) {
        window.location.hash = prefix + route.marshal(newRoute)
    }

    /**
     * returns a [Flow] with the current route
     */
    val routes: Flow<T> = hashchange.distinctUntilChanged().asSharedFlow()

    /**
     * Handler for setting a new [Route] based on given Flow.
     */
    val navTo: SimpleHandler<T> = SimpleHandler { flow ->
        flow.onEach { setRoute(it) }
            .launchIn(this)
    }

}

external fun decodeURIComponent(encodedURI: String): String
external fun encodeURIComponent(decodedURI: String): String