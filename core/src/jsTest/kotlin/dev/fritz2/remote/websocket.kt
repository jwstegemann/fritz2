package dev.fritz2.remote

import dev.fritz2.binding.RootStore
import dev.fritz2.dom.html.handledBy
import dev.fritz2.dom.html.render
import dev.fritz2.identification.Id
import dev.fritz2.lenses.IdProvider
import dev.fritz2.lenses.lens
import dev.fritz2.resource.Resource
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
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
            println("Connection state is: $it\n")
            when (counter) {
                0 -> assertTrue(it is SessionState.Connecting, "state not matching")
                1 -> assertTrue(it is SessionState.Open, "state not matching")
                2 -> assertTrue(it is SessionState.Closed, "state not matching")
            }
        }

        session.messages.body handledBy {
            println("Server said: ${it}\n")
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

    private val nameLens = lens("name", SocketPerson::name) { p, v -> p.copy(name = v) }
    private val ageLens = lens("age", SocketPerson::age) { p, v -> p.copy(age = v) }
    private val idLens = lens("id", SocketPerson::_id) { p, v -> p.copy(_id = v) }

    object PersonResource : Resource<SocketPerson, String> {
        override val idProvider: IdProvider<SocketPerson, String> = SocketPerson::_id
        override fun serialize(item: SocketPerson): String = Json.encodeToString(SocketPerson.serializer(), item)
        override fun deserialize(source: String): SocketPerson =
            Json.decodeFromString(SocketPerson.serializer(), source)
    }

    @Test
    fun testSyncWith() = runTest {
        initDocument()

        val defaultPerson = SocketPerson("", 0)
        val startPerson = SocketPerson("Heinz", 18)
        val changedAge = 99
        val testId = "test"
        val testName = "Hans"

        val socket = websocket.append("json")

        val entityStore = object : RootStore<SocketPerson>(defaultPerson) {
            override fun errorHandler(cause: Throwable) {
                fail(cause.message)
            }

            init {
                syncWith(socket, PersonResource)
            }
        }

        val nameId = "name-${Id.next()}"
        val nameSubStore = entityStore.sub(nameLens)
        val ageId = "age-${Id.next()}"
        val ageSubStore = entityStore.sub(ageLens)
        val idId = "id-${Id.next()}"
        val idSubStore = entityStore.sub(idLens)


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