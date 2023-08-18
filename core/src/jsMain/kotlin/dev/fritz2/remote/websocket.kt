package dev.fritz2.remote

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.ArrayBufferView
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.files.Blob

/**
 * [Exception] for handling errors with sending
 *
 * @property message error message
 */
class SendException(message: String) : Exception(message)

/**
 * [Exception] for handling errors with closing
 *
 * @property message error message
 */
class CloseException(message: String, throwable: Throwable) : Exception(message, throwable)

/**
 * [Exception] for handling errors with connecting
 *
 * @property message error message
 */
class ConnectionException(message: String, throwable: Throwable) : Exception(message, throwable)

/**
 * creates a new [Socket] for bidirectional communication with the server.
 *
 * @param url url to connect
 * @param protocols one or more protocols to use by the socket, default empty
 */
fun websocket(url: String, vararg protocols: String): Socket = Socket(url, protocols)

/**
 * [Socket] defines how to connect via websocket. Therefore, it needs an
 * [baseUrl] and an optional list of [protocols]. The [Session] will be established
 * after calling the [connect] method.
 *
 * @property baseUrl Url for connection via websocket
 * @property protocols optional list of protocols to use in the [Session]
 */
open class Socket(
    private val baseUrl: String = "",
    private val protocols: Array<out String> = emptyArray(),
) {

    /**
     * Appends the given [subUrl] to the [baseUrl] and returns a new instance of [Socket].
     *
     * @param subUrl Url to append
     * @return new [Socket]
     */
    fun append(subUrl: String) = Socket(
        "${baseUrl.trimEnd('/')}/${subUrl.trimStart('/')}",
        protocols,
    )

    /**
     * sets or overrides the given list of [protocols] for the [Socket] and
     * returns a new instance of [Socket].
     *
     * @param protocols list of protocols
     * @return new [Socket]
     */
    fun protocols(vararg protocols: String) = Socket(baseUrl, protocols)

    /**
     * Creates a [Session] by trying to connect to the websocket endpoint.
     * When connection was successfully made, you can send and receive message via websockets.
     *
     * @param subUrl optional Url to append
     * @return [Session] when connection was successful
     * @throws ConnectionException when connection cannot be established
     */
    fun connect(subUrl: String = ""): Session {
        val url = buildString {
            append(baseUrl.trimEnd('/'))
            if (subUrl.isNotEmpty()) {
                append("/${subUrl.trimStart('/')}")
            }
        }
        try {
            return Session(WebSocket(url, protocols))
        } catch (e: Throwable) {
            throw ConnectionException("error while opening connection to: $url", e)
        }
    }
}

/**
 * [SessionState] represents the actual state of the websocket connection.
 * After connection is established it changes from [SessionState.Connecting]
 * to [SessionState.Open] and when connection is closed it is [SessionState.Closed].
 */
sealed class SessionState {

    /**
     * gives the [SessionState] as [Short]
     *
     * @return [SessionState] as [Short]
     */
    abstract fun asShort(): Short

    /**
     * indicates connecting state
     */
    object Connecting : SessionState() {
        override fun asShort(): Short = WebSocket.CONNECTING
        override fun toString(): String = "Connecting"
    }

    /**
     * indicates open state
     *
     * @property event [Event] when connection is open
     */
    class Open(val event: Event) : SessionState() {
        override fun asShort(): Short = WebSocket.OPEN
        override fun toString(): String = "Open"
    }

    /**
     * indicates closed state
     *
     * @property event [CloseEvent] when connection is closed
     */
    class Closed(val event: CloseEvent) : SessionState() {
        override fun asShort(): Short = WebSocket.CLOSED
        override fun toString(): String = "Closed"
    }
}

/**
 * [Session] represents a session via websocket after connection was successful. Within a [Session]
 * you can exchange data with the remote endpoint bi-directionaly.
 *
 * @property webSocket [WebSocket] running websocket instance
 */
open class Session(private val webSocket: WebSocket) {

    /**
     * gives the actual [SessionState] as [Flow].
     */
    val state: MutableStateFlow<SessionState> = MutableStateFlow(SessionState.Connecting)

    init {
        val openListener: (Event) -> Unit = {
            state.value = SessionState.Open(it)
        }
        webSocket.addEventListener("open", openListener)

        val closeListener: (Event) -> Unit = {
            state.value = SessionState.Closed(it.unsafeCast<CloseEvent>())
        }
        webSocket.addEventListener("close", closeListener)
        state.onCompletion {
            webSocket.removeEventListener("open", openListener)
            webSocket.removeEventListener("close", closeListener)
        }
    }

    /**
     * gives a [Flow] of error [Event] when they get fired
     */
    val errors: Flow<Event> by lazy {
        callbackFlow {
            val listener: (Event) -> Unit = {
                trySend(it)
            }
            webSocket.addEventListener("error", listener)
            awaitClose { webSocket.removeEventListener("error", listener) }
        }
    }

    /**
     * gives a [Flow] of [MessageEvent] when the remote endpoint sends them
     */
    val messages: Flow<MessageEvent> = callbackFlow {
        val listener: (Event) -> Unit = {
            trySend(it.unsafeCast<MessageEvent>())
        }
        webSocket.addEventListener("message", listener)
        awaitClose { webSocket.removeEventListener("message", listener) }
    }

    /**
     * sends a new [message] to the remote endpoint
     *
     * @param message message as [String]
     * @throws SendException when something goes wrong
     */
    suspend fun send(message: String) {
        doWhenOpen {
            webSocket.send(message)
        }
    }

    /**
     * sends a new [message] to the remote endpoint
     *
     * @param message message as [ArrayBuffer]
     * @throws SendException when something goes wrong
     */
    suspend fun send(message: ArrayBuffer) {
        doWhenOpen {
            webSocket.binaryType = BinaryType.ARRAYBUFFER
            webSocket.send(message)
        }
    }

    /**
     * sends a new [message] to the remote endpoint
     *
     * @param message message as [ArrayBufferView]
     * @throws SendException when something goes wrong
     */
    suspend fun send(message: ArrayBufferView) {
        doWhenOpen {
            webSocket.binaryType = BinaryType.ARRAYBUFFER
            webSocket.send(message)
        }
    }

    /**
     * sends a new [message] to the remote endpoint
     *
     * @param message message as [Blob]
     * @throws SendException when something goes wrong
     */
    suspend fun send(message: Blob) {
        doWhenOpen {
            webSocket.binaryType = BinaryType.BLOB
            webSocket.send(message)
        }
    }

    private suspend fun doWhenOpen(run: (WebSocket) -> Unit) {
        when (webSocket.readyState) {
            WebSocket.CONNECTING -> {
                delay(50)
                doWhenOpen(run)
            }
            WebSocket.OPEN -> run(webSocket)
            WebSocket.CLOSING -> throw SendException("session is closing")
            WebSocket.CLOSED -> throw SendException("session is closed")
        }
    }

    /**
     * closes the current open [Session].
     * @throws CloseException when something goes wrong
     */
    suspend fun close(code: Short = 1000, reason: String = "") {
        try {
            webSocket.close(code, reason)
        } catch (t: Throwable) {
            throw CloseException(t.message ?: "error while closing session", t)
        }
    }
}

/**
 * gives a [Flow] of [Boolean] when [SessionState] is [SessionState.Connecting]
 */
val Session.isConnecting: Flow<Boolean>
    get() = state.map { it is SessionState.Connecting }

/**
 * gives a [Flow] of [Boolean] when [SessionState] is [SessionState.Open]
 */
val Session.isOpen: Flow<Boolean>
    get() = state.map { it is SessionState.Open }

/**
 * gives a [Flow] of [Boolean] when [SessionState] is [SessionState.Closed]
 */
val Session.isClosed: Flow<Boolean>
    get() = state.map { it is SessionState.Closed }

/**
 * gives a [Flow] of [Event] when [Session] opens
 */
val Session.opens: Flow<Event>
    get() = state.mapNotNull { (it as? SessionState.Open)?.event }

/**
 * gives a [Flow] of [CloseEvent] when [Session] closes
 */
val Session.closes: Flow<CloseEvent>
    get() = state.mapNotNull { (it as? SessionState.Closed)?.event }

/**
 * gives the [MessageEvent.data] as [Flow] of [Any]
 */
val Flow<MessageEvent>.data: Flow<Any?>
    get() = this.map { it.data }

/**
 * gives the [MessageEvent.data] as [Flow] of [String]
 */
val Flow<MessageEvent>.body: Flow<String>
    get() = this.map { it.data.unsafeCast<String>() }

/**
 * gives the [MessageEvent.data] as [Flow] of [Blob]
 */
val Flow<MessageEvent>.blob: Flow<Blob>
    get() = this.map { it.data.unsafeCast<Blob>() }

/**
 * gives the [MessageEvent.data] as [Flow] of [ArrayBuffer]
 */
val Flow<MessageEvent>.arrayBuffer: Flow<ArrayBuffer>
    get() = this.map { it.data.unsafeCast<ArrayBuffer>() }
