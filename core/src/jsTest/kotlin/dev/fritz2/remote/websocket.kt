package dev.fritz2.remote

import dev.fritz2.core.*
import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.khronos.webgl.Uint8Array
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import org.w3c.files.FileReader
import kotlin.test.*

class WebSocketTests {

    private val websocket = websocket("ws://localhost:3000")

    @Test
    fun testWebSocketText() = runTest {
        val socket = websocket.append("text")

        val session = socket.connect()
        var counter = 0

        session.state handledBy {
            when (counter) {
                0 -> assertTrue(it is SessionState.Connecting, "state not matching")
                1 -> assertTrue(it is SessionState.Open, "state not matching")
                2 -> assertTrue(it is SessionState.Closed, "state not matching")
            }
        }

        session.messages.body handledBy {
            when (counter) {
                0 -> assertEquals("Server said: Client said: A", it, "message not matching")
                1 -> assertEquals("Server said: Client said: B", it, "message not matching")
                2 -> assertEquals("Server said: Client said: C", it, "message not matching")
            }
            counter++
        }

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

        session.messages.arrayBuffer handledBy {
            val array = Uint8Array(it)
            assertEquals(data, array, "binary data is not matched")
        }


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

        session.messages.blob handledBy {
            val reader = FileReader()
            reader.onload = {
                assertEquals("Hello World", reader.result, "blob data is not matched")
            }
        }


        session.close(reason = "test done")

        delay(200)
    }

    @Serializable
    data class SocketPerson(val name: String, val age: Int, val _id: String = Id.next())

    private val nameLens = lensOf("name", SocketPerson::name) { p, v -> p.copy(name = v) }
    private val ageLens = lensOf("age", SocketPerson::age) { p, v -> p.copy(age = v) }
    private val idLens = lensOf("id", SocketPerson::_id) { p, v -> p.copy(_id = v) }

    fun WithJob.syncWith(store: Store<SocketPerson>, socket: Socket) {
        val session = socket.connect()
        var last: SocketPerson? = null
        apply {
            session.messages.body.map {
                val received = Json.decodeFromString(SocketPerson.serializer(), it)
                last = received
                received
            } handledBy store.update

            store.data.drop(1) handledBy {
                if (last != it) session.send(Json.encodeToString(SocketPerson.serializer(), it))
            }
        }
    }

    @Test
    fun testSyncWith() = runTest {
        
        val defaultPerson = SocketPerson("", 0)
        val startPerson = SocketPerson("Heinz", 18)
        val changedAge = 99
        val testId = "test"
        val testName = "Hans"

        val socket = websocket.append("json")

        val entityStore = object : RootStore<SocketPerson>(defaultPerson, job = Job()) {
            override fun errorHandler(cause: Throwable) {
                fail(cause.message)
            }

            init {
                val store = this
                withJobContext { syncWith(store, socket) }
            }
        }

        val nameId = "name-${Id.next()}"
        val nameSubStore = entityStore.map(nameLens)
        val ageId = "age-${Id.next()}"
        val ageSubStore = entityStore.map(ageLens)
        val idId = "id-${Id.next()}"
        val idSubStore = entityStore.map(idLens)


        render {
            div {
                div(id = idId) { idSubStore.data.renderText() }
                div(id = nameId) { nameSubStore.data.renderText() }
                div(id = ageId) { ageSubStore.data.renderText() }
            }
        }

        entityStore.update(startPerson)
        delay(100)

        val nameAfterStart = document.getElementById(nameId)?.textContent
        assertEquals(startPerson.name, nameAfterStart, "no name after start")

        ageSubStore.update(data = changedAge)
        delay(200)

        val ageAfterUpdate = document.getElementById(ageId)?.textContent
        assertEquals(changedAge.toString(), ageAfterUpdate, "wrong age after update")

        idSubStore.update(data = testId)
        delay(200)

        val nameAfterUpdate = document.getElementById(nameId)?.textContent
        assertEquals(testName, nameAfterUpdate, "wrong name after server update")
    }

}