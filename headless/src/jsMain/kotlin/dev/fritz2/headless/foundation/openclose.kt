package dev.fritz2.headless.foundation

import dev.fritz2.binding.SimpleHandler
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.Keys
import dev.fritz2.dom.html.shortcutOf

import dev.fritz2.headless.hooks.DatabindingHook
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLElement

interface OpenClose {
    val openClose: OpenCloseDatabindingHook
    val opened: Flow<Boolean>
    val close: SimpleHandler<Unit>
    val open: SimpleHandler<Unit>
    val toggle: SimpleHandler<Unit>
}

class OpenCloseDatabindingHook : DatabindingHook<Tag<HTMLElement>, Unit, Boolean>() {
    override fun Tag<HTMLElement>.render(payload: Unit) {
        handler?.invoke(data.flatMapLatest { state ->
            merge(
                clicks.events,
                keydowns.events.filter { setOf(Keys.Space, Keys.Enter).contains(shortcutOf(it)) }.onEach {
                    it.stopImmediatePropagation()
                    it.preventDefault()
                }).map { !state }
        })
    }
}

class OpenCloseDelegate : OpenClose {

    override val openClose = OpenCloseDatabindingHook()

    override val opened: Flow<Boolean> by lazy { openClose.data }

    override val close by lazy {
        SimpleHandler<Unit> { data, _ ->
            openClose.handler?.invoke(data.map { false })
        }
    }

    override val open by lazy {
        SimpleHandler<Unit> { data, _ ->
            openClose.handler?.invoke(data.map { true })
        }
    }

    override val toggle by lazy {
        SimpleHandler<Unit> { data, _ ->
            openClose.handler?.invoke(openClose.data.flatMapLatest { state -> data.map { !state } })
        }
    }
}
