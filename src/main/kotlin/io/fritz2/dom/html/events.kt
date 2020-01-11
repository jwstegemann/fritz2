package io.fritz2.dom.html

import io.fritz2.binding.Slot
import io.fritz2.dom.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import kotlin.reflect.KProperty

class EventType<E,T>(val name: String, val extract :(Event) -> T)

abstract class AbstractEventDelegate<E,T>(val type: EventType<E,T>) {
    operator fun getValue(thisRef: Tag, property: KProperty<*>): Slot<T> = throw NotImplementedError()
    @ExperimentalCoroutinesApi
    @FlowPreview
    operator fun setValue(thisRef: Tag, property: KProperty<*>, slot: Slot<T>) {
        slot.connect(thisRef.event(type))
    }

}

val Change = EventType<Event, String>("change") {
    (it.target as HTMLInputElement).value
}
object ChangeEventDelegate : AbstractEventDelegate<Event, String>(Change)

val Click = EventType<MouseEvent, MouseEvent>("click") {
    (it as MouseEvent)
}
object ClickEventDelegate : AbstractEventDelegate<MouseEvent, MouseEvent>(Click)


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