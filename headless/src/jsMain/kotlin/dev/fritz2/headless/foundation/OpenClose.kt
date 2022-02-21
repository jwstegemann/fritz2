package dev.fritz2.headless.foundation

import dev.fritz2.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge


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

    protected fun Tag<*>.toggleOnClicksEnterAndSpace() {
        openClose.handler?.invoke(openClose.data.flatMapLatest { state ->
            merge(
                clicks,
                keydowns.filter { setOf(Keys.Space, Keys.Enter).contains(shortcutOf(it)) }
            ).map {
                it.stopImmediatePropagation()
                it.preventDefault()
                !state
            }
        })
    }

    protected fun Tag<*>.closeOnEscape() {
        opened.flatMapLatest { isOpen ->
            Window.keydowns.filter { isOpen && shortcutOf(it) == Keys.Escape }
        } handledBy close
    }

    protected fun Tag<*>.closeOnBlur() {
        opened.flatMapLatest { isOpen ->
            Window.clicks.filter {
                isOpen && it.composedPath().none { it == this}
            }
        } handledBy close
    }

}
