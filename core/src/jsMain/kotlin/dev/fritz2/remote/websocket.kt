package dev.fritz2.remote

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.ArrayBufferView
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.files.Blob

fun websocket(url: String, vararg protocols: String): Socket = Socket(url, protocols)

class SendException(message: String) : Exception(message)
class CloseException(message: String) : Exception(message)

sealed class SocketState {

    abstract fun asShort(): Short

    object Connecting : SocketState() {
        override fun asShort(): Short = WebSocket.CONNECTING
        override fun toString(): String = "Connecting"
    }

    class Open(val event: Event) : SocketState() {
        override fun asShort(): Short = WebSocket.OPEN
        override fun toString(): String = "Open"
    }

    class Closed(val event: CloseEvent) : SocketState() {
        override fun asShort(): Short = WebSocket.CLOSED
        override fun toString(): String = "Closed"
    }
}

open class Socket(
    private val baseUrl: String = "",
    private val protocols: Array<out String> = emptyArray()
) {

    fun append(subUrl: String) = Socket(
        "${baseUrl.trimEnd('/')}/${subUrl.trimStart('/')}", protocols
    )

    fun protocols(vararg protocols: String) = Socket(baseUrl, protocols)

    fun connect(subUrl: String = ""): Session {
        val url = buildString {
            append(baseUrl.trimEnd('/'))
            if (subUrl.isNotEmpty()) {
                append("/${subUrl.trimStart('/')}")
            }
        }
        return Session(WebSocket(url, protocols))
    }
}

open class Session(private val webSocket: WebSocket) {

    val state: MutableStateFlow<SocketState> = MutableStateFlow(SocketState.Connecting)

    init {
        val openListener: (Event) -> Unit = {
            state.value = SocketState.Open(it)
        }
        webSocket.addEventListener("open", openListener)

        val closeListener: (Event) -> Unit = {
            state.value = SocketState.Closed(it.unsafeCast<CloseEvent>())
        }
        webSocket.addEventListener("close", closeListener)
        state.onCompletion {
            webSocket.removeEventListener("open", openListener)
            webSocket.removeEventListener("close", closeListener)
        }
    }

    val errors: Flow<Event> by lazy {
        callbackFlow {
            val listener: (Event) -> Unit = {
                offer(it)
            }
            webSocket.addEventListener("error", listener)
            awaitClose { webSocket.removeEventListener("error", listener) }
        }
    }

    val messages: Flow<MessageEvent> = callbackFlow {
        val listener: (Event) -> Unit = {
            offer(it.unsafeCast<MessageEvent>())
        }
        webSocket.addEventListener("message", listener)
        awaitClose { webSocket.removeEventListener("message", listener) }
    }

    suspend fun send(message: String) {
        doWhenOpen {
            webSocket.send(message)
        }
    }

    suspend fun send(message: ArrayBuffer) {
        doWhenOpen {
            webSocket.binaryType = BinaryType.ARRAYBUFFER
            webSocket.send(message)
        }
    }

    suspend fun send(message: ArrayBufferView) {
        doWhenOpen {
            webSocket.binaryType = BinaryType.ARRAYBUFFER
            webSocket.send(message)
        }
    }

    suspend fun send(message: Blob) {
        doWhenOpen {
            webSocket.binaryType = BinaryType.BLOB
            webSocket.send(message)
        }
    }

    private suspend fun doWhenOpen(run: (WebSocket) -> Unit) {
        when (webSocket.readyState) {
            WebSocket.CONNECTING -> {
                delay(50); doWhenOpen(run)
            }
            WebSocket.OPEN -> run(webSocket)
            WebSocket.CLOSING -> throw SendException("session is closing")
            WebSocket.CLOSED -> throw SendException("session is closed")
        }
    }

    suspend fun close(code: Short = 1000, reason: String = "") {
        try {
            webSocket.close(code, reason)
        } catch (t: Throwable) {
            throw CloseException(t.message ?: "error while closing session")
        }
    }
}

val Session.isConnecting: Flow<Boolean>
    get() = state.map { it is SocketState.Connecting }
val Session.isOpen: Flow<Boolean>
    get() = state.map { it is SocketState.Open }
val Session.isClosed: Flow<Boolean>
    get() = state.map { it is SocketState.Closed }

val Session.opens: Flow<Event>
    get() = state.mapNotNull { (it as? SocketState.Open)?.event }
val Session.closes: Flow<CloseEvent>
    get() = state.mapNotNull { (it as? SocketState.Closed)?.event }

val Flow<MessageEvent>.data: Flow<Any?>
    get() = this.map { it.data }
val Flow<MessageEvent>.body: Flow<String>
    get() = this.map { it.data.unsafeCast<String>() }
val Flow<MessageEvent>.blob: Flow<Blob>
    get() = this.map { it.data.unsafeCast<Blob>() }
val Flow<MessageEvent>.arrayBuffer: Flow<ArrayBuffer>
    get() = this.map { it.data.unsafeCast<ArrayBuffer>() }