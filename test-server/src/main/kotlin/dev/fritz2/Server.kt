package dev.fritz2

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import io.ktor.util.*
import org.slf4j.event.Level
import java.util.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

typealias Json = Map<String, Any>

object CRUDRepo {
    private val store: MutableMap<String, Json> = mutableMapOf()

    fun create(json: Json): Json {
        val id = UUID.randomUUID().toString()
        val new = json.toMutableMap()
        new["id"] = id
        store[id] = new
        return new
    }

    fun read() =
        store.values.toList()

    fun update(id: String, json: Json): Json {
        store[id] = json
        return json
    }

    fun delete(id: String) {
        store.remove(id)
    }

    fun clear() {
        store.clear()
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

    routing {

        get("/") {
            call.respondText("Test Server is running...", contentType = ContentType.Text.Plain)
        }

        // RESTFul API
        route("/rest") {
            get {
                call.respond(CRUDRepo.read())
            }
            post {
                call.respond(CRUDRepo.create(call.receive()))
            }
            put("{id}") {
                val id = call.parameters["id"] ?: throw MissingRequestParameterException("id")
                call.respond(CRUDRepo.update(id, call.receive()))
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
            options("/options") {
                call.respondText("OPTIONS")
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
    }
}