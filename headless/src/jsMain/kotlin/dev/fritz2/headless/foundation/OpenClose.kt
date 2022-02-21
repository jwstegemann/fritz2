package dev.fritz2.headless.foundation

import dev.fritz2.core.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.merge

/**
 * Base class that provides all functionality needed for components, that have some "open" and "close" state of
 * representation.
 *
 * Just extend from this class to gain and provide access to the basic data-binding [openClose] that holds the
 * central state, the [opened] data-flow and expressive handler like [close] or [open] to set the state.
 *
 * Typical examples of [OpenClose] based components are modal dialogs or all popup-components, that appear and
 * disappear based upon user interaction.
 */
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

    /**
     * Use this function on [Tag]s, that should trigger the component to open or to close in order to enable
     * keyboard support. Applying this function will toggle the state by the keys `Space` and `Enter` like a
     * `button` element behave natively.
     *
     * So it is usually a good idea to apply this feature, if the trigger is *not* a button!
     */
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
        }.onEach {
            console.log("toggling")
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
