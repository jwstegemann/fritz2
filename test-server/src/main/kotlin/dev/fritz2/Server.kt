package dev.fritz2

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.withContext
import org.slf4j.event.Level
import java.util.*

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 3000
    embeddedServer(Netty, port = port, module = Application::module).start(wait = true)
}

typealias Json = Map<String, Any>

object CRUDRepo {
    private const val idKey = "_id"
    private val store: MutableMap<String, Json> = mutableMapOf()

    fun create(json: Json): Json {
        val id = UUID.randomUUID().toString()
        val new = json.toMutableMap()
        new[idKey] = id
        store[id] = new
        return new
    }

    fun read() =
        store.values.toList()

    fun read(id: String) = store[id]

    fun update(id: String, json: Json): Json {
        val updated = json.toMutableMap()
        updated[idKey] = id
        store[id] = updated
        return json
    }

    fun delete(id: String) {
        store.remove(id)
    }

    fun clear() {
        store.clear()
    }

    override fun toString(): String {
        return store.toString()
    }
}

fun Application.module() {

    install(CallLogging) {
        level = Level.INFO
    }

    install(ContentNegotiation) {
        jackson()
    }

    authentication {
        basic("auth") {
            realm = "Authenticated"
            validate { if (it.name == "test" && it.password == "password") UserIdPrincipal(it.name) else null }
        }
    }


    install(WebSockets)

    routing {

        get("/") {
            call.respondText("Test Server is running...", contentType = ContentType.Text.Plain)
        }

        // RESTFul API
        route("/rest") {
            get {
                val body = CRUDRepo.read()
                call.application.environment.log.info("GET: $body")
                call.respond(body)
            }
            get("{id}") {
                val id = call.parameters["id"] ?: throw MissingRequestParameterException("id")
                val json = CRUDRepo.read(id) ?: throw NotFoundException("item with id=$id not found")
                call.application.environment.log.info("GET: id=$id; json=$json")
                call.respond(json)
            }
            post {
                val body = call.receive<Json>()
                call.application.environment.log.info("POST: $body")
                call.respond(CRUDRepo.create(body))
            }
            put("{id}") {
                val id = call.parameters["id"] ?: throw MissingRequestParameterException("id")
                val body = call.receive<Json>()
                call.application.environment.log.info("PUT: $id; $body")
                call.respond(CRUDRepo.update(id, body))
            }
            delete("{id}") {
                val id = call.parameters["id"] ?: throw MissingRequestParameterException("id")
                CRUDRepo.delete(id)
                call.response.status(HttpStatusCode.OK)
            }
            get("/clear") {
                CRUDRepo.clear()
                call.response.status(HttpStatusCode.OK)
            }
        }

        // Remote Basics
        route("test") {
            get("/get") {
                call.respondText("GET")
            }
            delete("/delete") {
                call.respondText("DELETE")
            }
            patch("/patch") {
                call.respondText("PATCH")
            }
            post("/post") {
                call.respondText("POST")
            }
            put("/put") {
                call.respondText("PUT")
            }
            head("/head") {
                call.respondText("HEAD")
            }
            get("/status/{code}") {
                val code = call.parameters["code"] ?: throw MissingRequestParameterException("code")
                call.respond(HttpStatusCode.fromValue(code.toInt()))
            }
            authenticate("auth") {
                get("/basicAuth") {
                    val principal = call.principal<UserIdPrincipal>()!!
                    call.respondText("Hello ${principal.name}")
                }
            }
            get("/headers") {
                call.respond(call.request.headers.toMap())
            }
        }

        // Authentication-Interceptor test
        route("authenticated") {
            get("/get") {
                val isValid = call.request.headers["authtoken"] != "123456789"
                if (isValid) call.respond(HttpStatusCode.Unauthorized)
                else call.respondText("GET")
            }
        }

        route("/extra") {
            post("/arraybuffer") {
                val received = call.receiveChannel().toByteArray()
                call.application.environment.log.info("[arraybuffer] received: $received")
                call.respondBytes(received)
            }
            post("/formData") {
                call.receiveMultipart().forEachPart {
                    when (it) {
                        is PartData.FormItem -> call.application.environment.log.info("[formData] received: ${it.value}")
                        is PartData.FileItem -> call.application.environment.log.info("[formData] received: ${it.originalFileName}")
                        else -> call.application.environment.log.info("[formData] received: name=${it.name}, contentType=${it.contentType}")
                    }
                }
            }
        }

        route("/failure") {
            get("/{code}") {
                val code = call.parameters["code"] ?: throw MissingRequestParameterException("code")
                val status = HttpStatusCode.fromValue(code.toInt())
                call.respond(status, status.description)
            }
        }

        webSocket("/text") {
            for (frame in incoming) {
                try {
                    when (frame) {
                        is Frame.Text -> {
                            val text = frame.readText()
                            call.application.environment.log.info("[ws-text] receiving: $text")
                            outgoing.send(Frame.Text("Client said: $text"))
                            if (text.equals("bye", ignoreCase = true)) {
                                close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                            }
                        }

                        is Frame.Close -> {
                            call.application.environment.log.info("[ws-text] closing: ${closeReason.await()}")
                        }

                        else -> call.application.environment.log.info(frame.frameType.name)
                    }
                } catch (e: ClosedReceiveChannelException) {
                    call.application.environment.log.error("[ws-text] close: ${closeReason.await()}")
                } catch (e: Throwable) {
                    call.application.environment.log.error("[ws-text] error: ${closeReason.await()}")
                    e.printStackTrace()
                }
            }
        }
        webSocket("/binary") {
            for (frame in incoming) {
                try {
                    when (frame) {
                        is Frame.Binary -> {
                            val data = frame.data
                            call.application.environment.log.info("[ws-binary] receiving: $data")
                            outgoing.send(Frame.Binary(true, data))
                        }

                        is Frame.Close -> {
                            call.application.environment.log.info("[ws-binary] closing: ${closeReason.await()}")
                        }

                        else -> call.application.environment.log.info(frame.frameType.name)
                    }
                } catch (e: ClosedReceiveChannelException) {
                    call.application.environment.log.error("[ws-binary] close: ${closeReason.await()}")
                } catch (e: Throwable) {
                    call.application.environment.log.error("[ws-binary] error: ${closeReason.await()}")
                    e.printStackTrace()
                }
            }
        }
        webSocket("/json") {
            val objectMapper = ObjectMapper()
            for (frame in incoming) {
                try {
                    when (frame) {
                        is Frame.Text -> {
                            val bodyIn = withContext(Dispatchers.IO) {
                                objectMapper.readValue(frame.readText(), jacksonTypeRef<MutableMap<String, Any>>())
                            }
                            call.application.environment.log.info("[ws-json] receiving: $bodyIn")
                            if (bodyIn["_id"] == "test") {
                                bodyIn["name"] = "Hans"
                                call.application.environment.log.info("[ws-json] sending: $bodyIn")
                                val bodyOut = withContext(Dispatchers.IO) {
                                    objectMapper.writeValueAsString(bodyIn)
                                }
                                outgoing.send(Frame.Text(bodyOut))
                            }
                        }

                        is Frame.Close -> {
                            call.application.environment.log.info("[ws-json] closing: ${closeReason.await()}")
                        }

                        else -> call.application.environment.log.info(frame.frameType.name)
                    }
                } catch (e: ClosedReceiveChannelException) {
                    call.application.environment.log.error("[ws-json] close: ${closeReason.await()}")
                } catch (e: Throwable) {
                    call.application.environment.log.error("[ws-json] error: ${closeReason.await()}")
                    e.printStackTrace()
                }
            }
        }
    }
}