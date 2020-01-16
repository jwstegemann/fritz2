package io.fritz2.dom.html

import io.fritz2.binding.Slot
import io.fritz2.dom.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import kotlin.reflect.KProperty

@ExperimentalCoroutinesApi
@FlowPreview
interface EventDelegate<T> {
    operator fun <X : Element> getValue(thisRef: Tag<X>, property: KProperty<*>): Slot<T> = throw NotImplementedError()
    operator fun <X : Element> setValue(thisRef: Tag<X>, property: KProperty<*>, slot: Slot<T>)
}

@ExperimentalCoroutinesApi
@FlowPreview
open class EventType<T>(val name: String) {
    open fun extract(event: Event): T = event.unsafeCast<T>()

    val delegate: EventDelegate<T> = object : EventDelegate<T> {
        override operator fun <X : Element> setValue(thisRef: Tag<X>, property: KProperty<*>, slot: Slot<T>) {
            slot.connect(thisRef.event(this@EventType))
        }
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
val Change = object: EventType<String>("change") {
    override fun extract(event: Event): String = (event.target as HTMLInputElement).value
}

@ExperimentalCoroutinesApi
@FlowPreview
val Click = EventType<MouseEvent>("click")


enum class Keys(val code: Int) {
        ArrowLeft(37),
        ArrowUp(38),
        ArrowRight(39),
        ArrowDown(40),
        Enter(13),
        Esc(27),
        Backspace(8),
        Tab(9)
}