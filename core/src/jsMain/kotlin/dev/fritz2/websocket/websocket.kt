package dev.fritz2.websocket

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

//enum class State(event: Event) { CONNECTING, OPEN, CLOSED, ERROR }

sealed class State {

    abstract fun asShort(): Short

    object Connecting : State() {
        override fun asShort(): Short = WebSocket.CONNECTING
        override fun toString(): String = "Connecting"
    }
    class Open(val event: Event) : State() {
        override fun asShort(): Short = WebSocket.OPEN
        override fun toString(): String = "Open"
    }
    class Closed(val event: CloseEvent): State() {
        override fun asShort(): Short = WebSocket.CLOSED
        override fun toString(): String = "Closed"
    }
}

//enum class MessageType { TEXT, BINARY, BINARY_VIEW, BLOB }

//sealed class Message<T> (
//    val messageType: MessageType,
//    val data: T
//) {
//    class Binary(data: ArrayBuffer) : Message<ArrayBuffer>(MessageType.BINARY, data)
//    class Text(data: String) : Message<String>(MessageType.TEXT, data)
//    class File(data: Blob) : Message<Blob>(MessageType.BLOB, data)
//}

open class Socket(
    private val baseUrl: String = "",
    private val protocols: Array<out String> = emptyArray()
) {

    suspend fun append(subUrl: String) = Socket(
        "${baseUrl.trimEnd('/')}/${subUrl.trimStart('/')}", protocols
    )

    suspend fun protocols(vararg protocols: String) = Socket(baseUrl, protocols)

    suspend fun connect(subUrl: String = ""): Session {
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

    val state: MutableStateFlow<State> = MutableStateFlow(State.Connecting)

    init {
        webSocket.addEventListener("open", {
            state.value = State.Open(it)
        })
        webSocket.addEventListener("close", {
            state.value = State.Closed(it.unsafeCast<CloseEvent>())
        })
    }

    val isConnecting: Flow<Boolean> = state.map { it is State.Connecting }
    val isOpen: Flow<Boolean> = state.map { it is State.Open }
    val isClosed: Flow<Boolean> = state.map { it is State.Closed }

    val opens: Flow<Event> = state.mapNotNull { (it as? State.Open)?.event }
    val closes: Flow<CloseEvent> = state.mapNotNull { (it as? State.Closed)?.event }

    val errors: Flow<Event> = callbackFlow {
        val listener: (Event) -> Unit = {
            offer(it)
        }
        webSocket.addEventListener("error", listener)
        awaitClose { webSocket.removeEventListener("error", listener) }
    }

    val messages: Flow<MessageEvent> = callbackFlow {
        val listener: (Event) -> Unit = {
            offer(it.unsafeCast<MessageEvent>())
        }
        webSocket.addEventListener("message", listener)
        awaitClose { webSocket.removeEventListener("message", listener) }
    }

    suspend fun send(message: String) {
        doWhenOpen { webSocket.send(message) }
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

//    suspend fun <T> send(message: Message<T>) {
//        when (webSocket.readyState) {
//            WebSocket.CONNECTING -> {
//                delay(50); send(message)
//            }
//            WebSocket.OPEN -> {
//                when (message.messageType) {
//                    MessageType.BINARY -> {
//                        webSocket.send(message.data.unsafeCast<ArrayBuffer>())
//                    }
//                    MessageType.TEXT -> webSocket.send(message.data.unsafeCast<String>())
//                    MessageType.BLOB -> webSocket.send(message.data.unsafeCast<Blob>())
//                    else -> throw SendException("frame type not supported")
//                }
//            }
//            WebSocket.CLOSING -> throw SendException("socket is closing")
//            WebSocket.CLOSED -> throw SendException("socket is closed")
//        }
//    }

    suspend fun close(code: Short = 1000, reason: String = "") {
        try {
            webSocket.close(code, reason)
        } catch (t: Throwable) {
            throw CloseException(t.message ?: "error while closing session")
        }
    }

}