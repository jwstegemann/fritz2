---
layout: layouts/docsWithContentNav.njk
title: Websockets
permalink: /docs/websockets/
eleventyNavigation:
    key: websockets
    parent: documentation
    title: Websockets
    order: 120
---

# Websockets

fritz2 offers support for websockets you can use with different protocols. To use it, you first have to create a socket:
 
```kotlin
val websocket: Socket = websocket("ws://myserver:3333")
```
You can specify one or more protocols on creation. See these 
[docs](https://developer.mozilla.org/en-US/docs/Web/API/WebSocket/WebSocket) for more information.

Your socket is now ready to establish a `Session` with the server, using the method `connect()`. Messages can now be
exchanged between socket and server, which looks like this:

```kotlin
val session: Session = websocket.connect()

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


You can synchronize the content of a `Store` with a server via websockets. Use the function 
`syncWith(socket: Socket, resource: Resource<T, I>)` like in the following example:

```kotlin
data class Person(val name: String, val age: Int, val _id: String = Id.next())

val personResource = Resource(Person::_id, PersonSerializer, Person("", 0))
val socket = websocket("ws://myserver:3333")

val entityStore = object : RootStore<Person>(personResource.emptyEntity) {
    init {
        syncWith(socket, personResource)
    }
    // your handlers...
}
```

When the model in the `Store` changes, it will be sent to the server via Websockets, and vice versa of course.
To get a full working example you can have a look at our [Ktor Chat](https://github.com/jamowei/fritz2-ktor-chat) 
project.


That way fritz2 provides an easy way to synchronize your stores with a server. Want more? Keep on reading about 
[Routing](Routing.html).