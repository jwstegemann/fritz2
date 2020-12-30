package dev.fritz2.routing

import dev.fritz2.binding.SimpleHandler
import dev.fritz2.dom.html.Events
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import org.w3c.dom.events.Event

/**
 * Creates a new simple [String] based [Router]
 *
 * @param default default route
 */
fun router(default: String): Router<String> = Router(StringRoute(default))

/**
 * Creates a new [Map] based [Router]
 *
 * @param default default route
 */
fun router(default: Map<String, String>): Router<Map<String, String>> = Router(MapRoute(default))

/**
 * Creates a new type based [Router].
 * Therefore the given type must implement the [Route] interface.
 *
 * @param default default route
 */
fun <T> router(default: Route<T>): Router<T> = Router(default)

/**
 * Selects with the given [key] a [Pair] of the value
 * and all routing parameters as [Map].
 *
 * @param key for getting the value from the parameter [Map]
 * @return [Flow] of the resulting [Pair]
 */
fun Router<Map<String, String>>.select(key: String): Flow<Pair<String?, Map<String, String>>> =
    this.data.map { m -> m[key] to m }

/**
 * Returns the value for the given [key] from the routing parameters.
 *
 * @param key for getting the value from the parameter [Map]
 * @param orElse if [key] is not in [Map]
 * @return [Flow] of [String] with the value
 */
fun Router<Map<String, String>>.select(key: String, orElse: String): Flow<String> =
    this.data.map { m -> m[key] ?: orElse }

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
 * marshals and unmarshalls nothing.
 *
 * @param default [String] to use when no explicit *window.location.hash* was set before
 */
open class StringRoute(override val default: String) : Route<String> {
    override fun unmarshal(hash: String): String = decodeURIComponent(hash)
    override fun marshal(route: String): String = encodeURIComponent(route)
}

/**
 * [MapRoute] marshals and unmarshals a [Map] to and from *window.location.hash*.
 * It is like using url parameters with pairs of key and value.
 * In the begin there is only a **#** instead of **?**.
 *
 * @param default [Map] to use when no explicit *window.location.hash* was set before
 */
open class MapRoute(override val default: Map<String, String>) : Route<Map<String, String>> {
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
class Router<T>(
    private val defaultRoute: Route<T>
) {

    private val state: MutableStateFlow<T> = MutableStateFlow(defaultRoute.default)
    private val prefix = "#"

    /**
     * Gives a [Flow] of [T] for rendering the site depending on the current route.
     */
    val data: Flow<T> = state.asStateFlow()

    /**
     * Represents the current route of the [Router].
     */
    val current: T
        get() = state.value

    /**
     * Handler for setting a new [Route] based on given Flow.
     */
    val navTo: SimpleHandler<T> = SimpleHandler { flow, job ->
        flow.onEach { setRoute(it) }.launchIn(MainScope() + job)
    }

    init {
        if (window.location.hash.isBlank()) {
            setRoute(defaultRoute.default)
        } else {
            state.value = defaultRoute.unmarshal(window.location.hash.removePrefix(prefix))
        }

        val listener: (Event) -> Unit = {
            it.preventDefault()
            state.value = defaultRoute.unmarshal(window.location.hash.removePrefix(prefix))
        }
        window.addEventListener(Events.hashchange.name, listener)
    }

    private fun setRoute(newRoute: T) {
        state.value = newRoute
        window.location.hash = prefix + defaultRoute.marshal(newRoute)
    }
}

external fun decodeURIComponent(encodedURI: String): String
external fun encodeURIComponent(decodedURI: String): String