package dev.fritz2

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import org.slf4j.event.Level
import java.util.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

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

@KtorExperimentalAPI
fun Application.main() {

    install(CallLogging) {
        level = Level.INFO
    }

    install(ContentNegotiation) {
        jackson()
    }

    install(Authentication) {
        basic("auth") {
            realm = "Authenticated"
            validate { if (it.name == "test" && it.password == "password") UserIdPrincipal(it.name) else null }
        }
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        method(HttpMethod.Head)
        header(HttpHeaders.Authorization)
        header(HttpHeaders.ContentType)
        header(HttpHeaders.Accept)
        header(HttpHeaders.CacheControl)
        header("test")
        anyHost()
        host("localhost")
        allowXHttpMethodOverride()
        allowCredentials = true
        allowNonSimpleContentTypes = true
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
                log.info("GET: $body")
                call.respond(body)
            }
            get("{id}") {
                val id = call.parameters["id"] ?: throw MissingRequestParameterException("id")
                val json = CRUDRepo.read(id) ?: throw NotFoundException("item with id=$id not found")
                log.info("GET: id=$id; json=$json")
                call.respond(json)
            }
            post {
                val body = call.receive<Json>()
                log.info("POST: $body")
                call.respond(CRUDRepo.create(body))
            }
            put("{id}") {
                val id = call.parameters["id"] ?: throw MissingRequestParameterException("id")
                val body = call.receive<Json>()
                log.info("PUT: $id; $body")
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

        route("/extra") {
            post("/arraybuffer") {
                val received = call.receiveChannel().toByteArray()
                log.info("[arraybuffer] received: $received")
                call.respondBytes(received)
            }
            post("/formData") {
                call.receiveMultipart().forEachPart {
                    when (it) {
                        is PartData.FormItem -> log.info("[formData] received: ${it.value}")
                        is PartData.FileItem -> log.info("[formData] received: ${it.originalFileName}")
                        else -> log.info("[formData] received: name=${it.name}, contentType=${it.contentType}")
                    }
                }
            }
        }

        webSocket("/text") {
            for (frame in incoming) {
                try {
                    when (frame) {
                        is Frame.Text -> {
                            val text = frame.readText()
                            log.info("[ws-text] receiving: $text")
                            outgoing.send(Frame.Text("Client said: $text"))
                            if (text.equals("bye", ignoreCase = true)) {
                                close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                            }
                        }
                        is Frame.Close -> {
                            log.info("[ws-text] closing: ${closeReason.await()}")
                        }
                        else -> log.info(frame.frameType.name)
                    }
                } catch (e: ClosedReceiveChannelException) {
                    log.error("[ws-text] close: ${closeReason.await()}")
                } catch (e: Throwable) {
                    log.error("[ws-text] error: ${closeReason.await()}")
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
                            log.info("[ws-binary] receiving: $data")
                            outgoing.send(Frame.Binary(true, data))
                        }
                        is Frame.Close -> {
                            log.info("[ws-binary] closing: ${closeReason.await()}")
                        }
                        else -> log.info(frame.frameType.name)
                    }
                } catch (e: ClosedReceiveChannelException) {
                    log.error("[ws-binary] close: ${closeReason.await()}")
                } catch (e: Throwable) {
                    log.error("[ws-binary] error: ${closeReason.await()}")
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
                            val body = objectMapper.readValue(frame.readText(), jacksonTypeRef<MutableMap<String, Any>>())
                            log.info("[ws-json] receiving: $body")
                            if(body["_id"] == "test") {
                                body["name"] = "Hans"
                                log.info("[ws-json] sending: $body")
                                outgoing.send(Frame.Text(objectMapper.writeValueAsString(body)))
                            }
                        }
                        is Frame.Close -> {
                            log.info("[ws-json] closing: ${closeReason.await()}")
                        }
                        else -> log.info(frame.frameType.name)
                    }
                } catch (e: ClosedReceiveChannelException) {
                    log.error("[ws-json] close: ${closeReason.await()}")
                } catch (e: Throwable) {
                    log.error("[ws-json] error: ${closeReason.await()}")
                    e.printStackTrace()
                }
            }
        }
    }
}