package dev.fritz2.remote

import dev.fritz2.binding.watch
import dev.fritz2.test.runTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.khronos.webgl.Uint8Array
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import org.w3c.files.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class WebSocketTests {

    private val websocket = websocket("ws://localhost:3000")

    @Test
    fun testWebSocketText() = runTest {
        val socket = websocket.append("text")

        val session = socket.connect()
        var counter = 0

        session.state.map {
            println("Connection state is: $it\n")
            when (counter) {
                0 -> assertTrue(it is SessionState.Connecting, "state not matching")
                1 -> assertTrue(it is SessionState.Open, "state not matching")
                2 -> assertTrue(it is SessionState.Closed, "state not matching")
            }
        }.watch()

        session.messages.body.onEach {
            println("Server said: ${it}\n")
            when (counter) {
                0 -> assertEquals("Server said: Client said: A", it, "message not matching")
                1 -> assertEquals("Server said: Client said: B", it, "message not matching")
                2 -> assertEquals("Server said: Client said: C", it, "message not matching")
            }
            counter++
        }.watch()

        session.send("A")
        delay(100)
        session.send("B")
        delay(100)
        session.send("C")

        delay(200)

        session.close(reason = "test done")
    }

    @Test
    fun testWebSocketExceptionAfterClientClose() = runTest {
        val socket = websocket.append("text")

        val session = socket.connect()
        session.send("Test error after close")
        delay(100)

        session.close(reason = "test done")

        delay(200)

        assertFailsWith(SendException::class) {
            session.send("must fail")
        }

        delay(200)
    }

    @Test
    fun testWebSocketExceptionAfterServerClose() = runTest {
        val socket = websocket.append("text")

        val session = socket.connect()

        session.closes.onEach {
            assertEquals("Client said BYE", it.reason, "close reason does not match")
        }

        session.send("bye")
        delay(200)


        assertFailsWith(SendException::class) {
            session.send("must fail")
        }

        delay(200)
    }

    @Test
    fun testWebSocketBinary() = runTest {
        val socket = websocket.append("binary")

        val session = socket.connect()

        val data = Uint8Array(arrayOf(1, 2, 3))
        session.send(data)
        delay(100)
        session.send(data.buffer)
        delay(100)

        session.messages.arrayBuffer.onEach {
            val array = Uint8Array(it)
            assertEquals(data, array, "binary data is not matched")
        }.watch()


        session.close(reason = "test done")

        delay(200)
    }

    @Test
    fun testWebSocketBlob() = runTest {
        val socket = websocket.append("binary")

        val session = socket.connect()

        val data = Blob(arrayOf("Hello World"), BlobPropertyBag("text/plain"))
        session.send(data)
        delay(100)

        session.messages.blob.onEach {
            val reader = FileReader()
            reader.onload = {
                assertEquals("Hello World", reader.result, "blob data is not matched")
            }
        }.watch()


        session.close(reason = "test done")

        delay(200)
    }


}