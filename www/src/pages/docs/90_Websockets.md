---
title: Websockets
description: "Learn how to communicate via Websockets in fritz2"
layout: layouts/docs.njk
permalink: /docs/websockets/
eleventyNavigation:
    key: websockets
    parent: documentation
    title: Websockets
    order: 90
---

fritz2 offers support for websockets you can use with different protocols. 

First create a socket:
 
```kotlin
val websocket: Socket = websocket("ws://myserver:3333")
```
You can specify one or more protocols on creation. See these 
[docs](https://developer.mozilla.org/en-US/docs/Web/API/WebSocket/WebSocket) for more information.

Your socket is now ready to establish a `Session` with the server, using the method `connect()`. Messages can now be
exchanged between socket and server, which looks like this:

```kotlin
val session: Session = websocket("ws://...").connect()

// receiving messages from server
session.messages.body handledBy {
    window.alert("Server said: $it")
}

// sending messages to server
session.send("Hello")
```

As you can see, `Session` offers a `Flow` of `MessageEvent`s in messages. When a new message from the server arrives, 
a new message pops up on the `Flow`. Get the content of the message with one of the following methods (depending on
content type):
* `data(): Flow<Any?>`
* `body(): Flow<String>`
* `blob(): Flow<Blob>`
* `arrayBuffer(): Flow<ArrayBuffer>`

More information regarding the connection status is provided by the `Session` as `Flow`s:
* `isConnecting: Flow<Boolean>`
* `isOpen: Flow<Boolean>`
* `isClosed: Flow<Boolean>`
* `opens: Flow<Event>`
* `closes: Flow<CloseEvent>`

When you're done, close the session client-side. Supplying a code and a reason is optional.
```kotlin
session.close(reason = "finished")
```
After the `Session` was closed by either client or server, no further messages can be sent, and trying to do so 
throws a `SendException`.


You can synchronize the content of a `Store` with a server via websockets. You can use a function like 
`syncWith(socket: Socket, resource: Resource<T, I>)` in the following example:

```kotlin
@Lenses
@Serializable
data class Person(val name: String = "", val age: Int = -1, val _id: String = Id.next())

fun Store<Person>.syncWith(socket: Socket) {
    val session = socket.connect()
    var last: Person? = null
    apply {
        session.messages.body.map {
            val received = Json.decodeFromString(Person.serializer(), it)
            last = received
            received
        } handledBy this@syncWith.update

        this@syncWith.data.drop(1) handledBy {
            if (last != it) session.send(Json.encodeToString(Person.serializer(), it))
        }
    }
}

val socket = websocket("ws://...")

val entityStore = object : RootStore<Person>(Person()) {
    init {
        syncWith(socket, PersonResource)
    }
    // your handlers...
}
```

When the model in the `Store` changes, it will be sent to the server via Websockets, and vice versa of course.
For a full working example have a look at our [Ktor Chat](https://github.com/jamowei/fritz2-ktor-chat) 
project.