package io.fritz2.dom.html

import io.fritz2.binding.Slot
import io.fritz2.dom.Element
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import kotlin.reflect.KProperty

class EventType<E,T>(val name: String, val extract :(Event) -> T)

val Change = EventType<Event, String>("change") {
    (it.target as HTMLInputElement).value
}

object ChangeEventDelegate {
    operator fun getValue(thisRef: Element, property: KProperty<*>): Slot<String> = throw NotImplementedError()
    operator fun setValue(thisRef: Element, property: KProperty<*>, slot: Slot<String>) {
        slot(thisRef.event(Change))
    }
}

enum class Keys(code: Int) {
        ArrowLeft(37),
        ArrowUp(38),
        ArrowRight(39),
        ArrowDown(40),
        Enter(13),
        Esc(27),
        Backspace(8),
        Tab(9)
}