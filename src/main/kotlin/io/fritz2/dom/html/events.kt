package io.fritz2.dom.html

import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event

class EventType<E,T>(val name: String, val extract :(Event) -> T)

val Change = EventType<Event, String>("change") {
    (it.target as HTMLInputElement).value
}