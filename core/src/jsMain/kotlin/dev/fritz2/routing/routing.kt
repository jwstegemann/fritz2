package dev.fritz2.routing

import dev.fritz2.binding.SimpleHandler
import dev.fritz2.dom.html.Events
import kotlinx.coroutines.MainScope
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
 * Selects with the given key a [Pair] of the value
 * and the complete routing [Map] into the [transform] function.
 *
 * @param key for looking in [Map]
 * @param transform mapping function to run on selected [Pair] of the value and routing [Map]
 * @return new [Flow] of the result by calling the [transform] function
 */
fun <X> Router<Map<String, String>>.select(key: String, transform: suspend (Pair<String?, Map<String, String>>) -> X): Flow<X> =
    route.map { m -> transform((m[key]) to m) }

/**
 * Returns the value for the given key.
 *
 * @param key for looking in [Map]
 * @param orElse if key not in [Map]
 * @return [Flow] of [String] with the value
 */
fun Router<Map<String, String>>.select(key: String, orElse: String): Flow<String> =
    route.map { m -> m[key] ?: orElse }

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
 * @property defaultRoute default route to use when page is called and no hash is set
 */
open class Router<T>(private val defaultRoute: Route<T>) {

    private val prefix = "#"

    /**
     * returns a [Flow] with the current route
     */
    val route = MutableStateFlow<T>(defaultRoute.default)

    /**
     * Handler for setting a new [Route] based on given Flow.
     */
    val navTo: SimpleHandler<T> = SimpleHandler { flow ->
        flow.onEach { setRoute(it) }.launchIn(MainScope())
    }

    init {
        if (window.location.hash.isBlank()) {
            setRoute(defaultRoute.default)
            route.value = defaultRoute.default
        } else {
            route.value = defaultRoute.unmarshal(window.location.hash.removePrefix(prefix))
        }

        val listener: (Event) -> Unit = {
            it.preventDefault()
            route.value = defaultRoute.unmarshal(window.location.hash.removePrefix(prefix))
        }
        window.addEventListener(Events.hashchange.name, listener)
    }

    private fun setRoute(newRoute: T) {
        window.location.hash = prefix + defaultRoute.marshal(newRoute)
    }
}

external fun decodeURIComponent(encodedURI: String): String
external fun encodeURIComponent(decodedURI: String): String