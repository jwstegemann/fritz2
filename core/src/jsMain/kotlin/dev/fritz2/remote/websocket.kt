package dev.fritz2.remote

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket
import org.w3c.dom.events.Event

fun websocket(url: String, vararg protocols: String): Connection = Connection(url, protocols)

class SendException(message: String) : Exception(message)
class CloseException(message: String) : Exception(message)

enum class State { CONNECTING, OPEN, CLOSED, ERROR }

open class Connection(
    private val baseUrl: String = "",
    private val protocols: Array<out String> = emptyArray(),
    private inline val onOpen: (Event) -> Unit = {},
    private inline val onClose: (Event) -> Unit = {},
    private inline val onError: (Event) -> Unit = {}
) {

    suspend fun append(subUrl: String) = Connection(
        "${baseUrl.trimEnd('/')}/${subUrl.trimStart('/')}",
        protocols, onOpen, onClose, onError
    )

    suspend fun protocols(vararg protocols: String) = Connection(
        baseUrl, protocols, onOpen, onClose, onError
    )

    suspend fun onOpen(handler: (Event) -> Unit) = Connection(
        baseUrl, protocols, handler, onClose, onError
    )

    suspend fun onClose(handler: (Event) -> Unit) = Connection(
        baseUrl, protocols, onOpen, handler, onError
    )

    suspend fun onError(handler: (Event) -> Unit) = Connection(
        baseUrl, protocols, onOpen, onClose, handler
    )

    suspend fun connect(subUrl: String = ""): Socket {
        val url = buildString {
            append(baseUrl.trimEnd('/'))
            if (subUrl.isNotEmpty()) {
                append("/${subUrl.trimStart('/')}")
            }
        }
        return Socket(WebSocket(url, protocols), onOpen, onClose, onError)
    }
}

open class Socket(
    private val webSocket: WebSocket,
    private inline val onOpen: (Event) -> Unit,
    private inline val onClose: (Event) -> Unit,
    private inline val onError: (Event) -> Unit
) {
    val state: MutableStateFlow<State> = MutableStateFlow(State.CONNECTING)

    init {
        webSocket.addEventListener("open", {
            onOpen(it)
            state.value = State.OPEN
        })
        webSocket.addEventListener("close", {
            onClose(it)
            state.value = State.CLOSED
        })
        webSocket.addEventListener("error", {
            onError(it)
            state.value = State.ERROR
        })
    }

    val isConnecting: Flow<Boolean> = state.map { it == State.CONNECTING }
    val isOpen: Flow<Boolean> = state.map { it == State.OPEN }
    val isClosed: Flow<Boolean> = state.map { it == State.CLOSED }
    val isError: Flow<Boolean> = state.map { it == State.ERROR }

    val messages: Flow<String> = callbackFlow {
        val listener: (Event) -> Unit = {
            offer(it.unsafeCast<MessageEvent>().data.unsafeCast<String>())
        }
        webSocket.addEventListener("message", listener)
        awaitClose { webSocket.removeEventListener("message", listener) }
    }

    suspend fun send(message: String) {
        when (webSocket.readyState) {
            WebSocket.CONNECTING -> {
                delay(50); send(message)
            }
            WebSocket.OPEN -> webSocket.send(message)
            WebSocket.CLOSING -> throw SendException("socket is closing")
            WebSocket.CLOSED -> throw SendException("socket is closed")
        }
    }

    suspend fun close(code: Short = 1000, reason: String = "") {
        try {
            webSocket.close(code, reason)
        } catch (t: Throwable) {
            throw CloseException(t.message ?: "error while closing socket")
        }
    }

}