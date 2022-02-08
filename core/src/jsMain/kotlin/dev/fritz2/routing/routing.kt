package dev.fritz2.routing

import dev.fritz2.binding.Store
import dev.fritz2.binding.Update
import kotlinx.browser.window
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.w3c.dom.events.Event

/**
 * Creates a new simple [String] based [Router]
 *
 * @param default default route
 */
fun routerOf(default: String = ""): Router<String> = Router(StringRoute(default))

/**
 * Creates a new [Map] based [Router]
 *
 * @param default default route
 */
fun routerOf(default: Map<String, String> = emptyMap()) = MapRouter(default)

/**
 * Creates a new type based [Router].
 * Therefore, the given type must implement the [Route] interface.
 *
 * @param default default route
 */
fun <T> routerOf(default: Route<T>): Router<T> = Router(default)

/**
 * Router register the event-listener for hashchange-event and
 * handles route-changes. Therefore, it uses a [Route] object
 * which can [Route.serialize] and [Route.deserialize] the given type.
 *
 * @param T type to marshal and unmarshal
 * @property defaultRoute default route to use when page is called and no hash is set
 */
open class Router<T>(
    private val defaultRoute: Route<T>,
) : Store<T> {

    override val id: String = ""

    override val path: String = ""

    override val job: Job = Job()

    private val state: MutableStateFlow<T> = MutableStateFlow(defaultRoute.default)

    private val prefix = "#"

    private val mutex = Mutex()

    override val data: Flow<T> = state.asStateFlow()

    override val current: T
        get() = state.value

    override val update = this.handle<T> { _, newValue -> newValue }

    /**
     * Navigates to the new given route provided as [T].
     */
    open val navTo = this.handle<T> { _, newValue -> newValue }

    override suspend fun enqueue(update: Update<T>) {
        mutex.withLock {
            val newRoute = update(state.value)
            state.value = newRoute
            window.location.hash = prefix + defaultRoute.serialize(newRoute)
        }
    }

    init {
        if (window.location.hash.isBlank()) {
            window.location.hash = prefix + defaultRoute.serialize(defaultRoute.default)
        } else {
            state.value = defaultRoute.deserialize(window.location.hash.removePrefix(prefix))
        }

        val listener: (Event) -> Unit = {
            it.preventDefault()
            state.value = defaultRoute.deserialize(window.location.hash.removePrefix(prefix))
        }
        window.addEventListener("hashchange", listener)
    }
}

/**
 * Represents the current [Route] as [Map] of [String]s.
 *
 * @param defaultRoute default [Route] to start with.
 */
open class MapRouter(defaultRoute: Map<String, String> = emptyMap()) :
    Router<Map<String, String>>(MapRoute(defaultRoute)) {

    /**
     * Selects with the given [key] a [Pair] of the value
     * and all routing parameters as [Map].
     *
     * @param key for getting the value from the parameter [Map]
     * @return [Flow] of the resulting [Pair]
     */
    open fun select(key: String): Flow<Pair<String?, Map<String, String>>> = this.data.map { m -> m[key] to m }

    /**
     * Returns the value for the given [key] from the routing parameters.
     *
     * @param key for getting the value from the parameter [Map]
     * @param orElse if [key] is not in [Map]
     * @return [Flow] of [String] with the value
     */
    open fun select(key: String, orElse: String): Flow<String> = this.data.map { m -> m[key] ?: orElse }
}

/**
 * A Route is an abstraction for routes
 * which needed for routing
 *
 * @param T type to de-/serialize from
 */
interface Route<T> {
    /**
     * Gives the default value when initialising the routing
     */
    val default: T

    /**
     * Deserializes the *window.location.hash* to the
     * given type [T] after the hashchange-event is fired.
     */
    fun deserialize(hash: String): T

    /**
     * Serializes a given object of type [T] to [String]
     * for setting it to the *window.location.hash*
     */
    fun serialize(route: T): String
}

/**
 * [StringRoute] is a simple [Route] which
 * serializes and deserializes nothing.
 *
 * @param default [String] to use when no explicit *window.location.hash* was set before
 */
open class StringRoute(override val default: String = "") : Route<String> {
    override fun deserialize(hash: String): String = decodeURIComponent(hash)
    override fun serialize(route: String): String = encodeURIComponent(route)
}

/**
 * [MapRoute] serializes and deserializes a [Map] to and from *window.location.hash*.
 * It is like using url parameters with pairs of key and value.
 * At the start of the route is only a **#** instead of **?**.
 *
 * @param default [Map] to use when no explicit *window.location.hash* was set before
 */
open class MapRoute(override val default: Map<String, String> = emptyMap()) :
    Route<Map<String, String>> {

    private val assignment = "="
    private val divider = "&"

    override fun deserialize(hash: String): Map<String, String> =
        hash.split(divider).filter { s -> s.isNotBlank() }.asReversed().associate(::extractPair)

    override fun serialize(route: Map<String, String>): String =
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

external fun decodeURIComponent(encodedURI: String): String
external fun encodeURIComponent(decodedURI: String): String