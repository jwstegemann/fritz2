package dev.fritz2.headless.foundation

import dev.fritz2.core.Keys
import dev.fritz2.core.SimpleHandler
import dev.fritz2.core.Tag
import dev.fritz2.core.shortcutOf
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLElement


abstract class OpenClose {

    val openClose = DatabindingProperty<Boolean>()

    val opened: Flow<Boolean> by lazy { openClose.data }

    val close by lazy {
        SimpleHandler<Unit> { data, _ ->
            openClose.handler?.invoke(data.map { false })
        }
    }

    val open by lazy {
        SimpleHandler<Unit> { data, _ ->
            openClose.handler?.invoke(data.map { true })
        }
    }

    val toggle by lazy {
        SimpleHandler<Unit> { data, _ ->
            openClose.handler?.invoke(openClose.data.flatMapLatest { state -> data.map { !state } })
        }
    }

    protected fun Tag<HTMLElement>.handleOpenCloseEvents() {
        openClose.handler?.invoke(openClose.data.flatMapLatest { state ->
            merge(
                clicks,
                keydowns.filter { setOf(Keys.Space, Keys.Enter).contains(shortcutOf(it)) }.onEach {
                    it.stopImmediatePropagation()
                    it.preventDefault()
                }).map { !state }
        })
    }
}
