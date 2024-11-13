package dev.fritz2.examples.remote

import dev.fritz2.core.RootStore
import dev.fritz2.core.placeholder
import dev.fritz2.core.render
import dev.fritz2.core.values
import dev.fritz2.remote.http
import kotlinx.coroutines.Job

fun main() {

    val userStore = object : RootStore<String>("", job = Job()) {

        val users = http("https://reqres.in/api/users")

        val loadAllUsers = handle {
            users.get().body()
        }

        val loadUserById = handle { _, s: String ->
            users.acceptJson().get(s).body()
        }

        val saveUserWithName = handle { _, s: String ->
            users.body(
                """
                    {
                        "name": "$s",
                        "job": "programmer"
                    }
                """.trimIndent()
            )
                .contentType("application/json; charset=utf-8")
                .acceptJson().post().body()
        }
    }

    render("#target") {
        div {
            div("form-group") {
                label("load-user") {
                    +"Load user by id"
                }
                input("form-control", id = "load-user") {
                    placeholder("Enter user id")
                    changes.values() handledBy userStore.loadUserById
                }
            }

            hr("my-4") { }

            div("form-group") {
                label("save-user") {
                    +"Save user"
                }
                input("form-control", id = "save-user") {
                    placeholder("Enter new user name")
                    changes.values() handledBy userStore.saveUserWithName
                }
            }

            hr("my-4") { }

            div("form-group") {
                button("btn btn-primary") {
                    +"Load all users"
                    clicks handledBy userStore.loadAllUsers
                }
            }
            div("card card-body") {
                h6("card-title") {
                    +"User store data"
                }
                pre("text-wrap") {
                    code {
                        userStore.data.renderText()
                    }
                }
            }
        }
    }
}
