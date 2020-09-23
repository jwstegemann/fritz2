package dev.fritz2.remote

import dev.fritz2.binding.watch
import dev.fritz2.test.runTest
import dev.fritz2.test.testWebsocketServer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.test.Test
import kotlin.test.assertFailsWith

class WebSocketTests {

    @Test
    fun testWebSocketSimple() = runTest {
        val connection = testWebsocketServer("simple")

        connection.onOpen { println("Connection is opening: $it\n") }
        connection.onClose { println("Connection is closing: $it\n") }
        connection.onError { println("Connection has error: $it\n") }

        val socket = connection.connect()

        socket.state.map { println("Connection state is: ${it.name}\n") }.watch()

        socket.messages.onEach { println("Server said: $it\n") }.watch()

        socket.send("Test")
        delay(100)
        socket.send("Test1")
        delay(100)
        socket.send("Test2")

        delay(200)

        socket.close(reason = "test done")

        delay(200)

        assertFailsWith(SendException::class) {
            println("send after socket is closed\n")
            socket.send("must fail")
        }

        delay(200)
    }
}