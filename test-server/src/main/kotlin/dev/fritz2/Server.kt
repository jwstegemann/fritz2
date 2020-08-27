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
import java.util.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

typealias Json = Map<String, Any>

object CRUD {
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
}

@KtorExperimentalAPI
fun Application.main() {

    install(ContentNegotiation) {
        jackson()
    }

    install(Authentication) {
        basic("auth") {
            realm = "Authenticated"
            validate { if (it.name == "test" && it.password == "password") UserIdPrincipal(it.name) else null }
        }
    }

    routing {
        route("/rest") {
            get {
                call.respond(CRUD.read())
            }
            post {
                call.respond(CRUD.create(call.receive()))
            }
            put("{id}") {
                val id = call.parameters["id"] ?: throw MissingRequestParameterException("id")
                call.respond(CRUD.update(id, call.receive()))
            }
            delete("{id}") {
                val id = call.parameters["id"] ?: throw MissingRequestParameterException("id")
                CRUD.delete(id)
                call.response.status(HttpStatusCode.OK)
            }
        }

        get("/") {
            call.respondText("Test Server running...", contentType = ContentType.Text.Plain)
        }

        authenticate("auth") {
            get("/basicAuth") {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respondText("Hello ${principal.name}")
            }
        }
    }
}