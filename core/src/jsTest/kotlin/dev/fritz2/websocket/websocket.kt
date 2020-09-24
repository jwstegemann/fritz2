package dev.fritz2.websocket

import dev.fritz2.binding.watch
import dev.fritz2.test.runTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.test.Test
import kotlin.test.assertFailsWith

class WebSocketTests {

    @Test
    fun testWebSocketSimple() = runTest {
        val socket = websocket("ws://localhost:3000/simple")

        val session = socket.connect()

        session.state.map { println("Connection state is: $it\n") }.watch()

        session.messages.onEach { println("Server said: ${it.data as String}\n") }.watch()

        session.send("A")
        delay(100)
        session.send("B")
        delay(100)
        session.send("C")

        delay(200)

        session.close(reason = "test done")

        delay(200)

        assertFailsWith(SendException::class) {
            println("send after socket is closed\n")
            session.send("must fail")
        }

        delay(200)
    }
}