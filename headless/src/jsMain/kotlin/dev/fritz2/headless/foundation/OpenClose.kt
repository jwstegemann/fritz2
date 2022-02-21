package dev.fritz2.headless.foundation

import dev.fritz2.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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
 *
 * There are some protected functions in order to configure the appropriate actions for opening and closing based
 * upon user interaction like pressing some keys or clicking with the mouse:
 * - [toggleOnClicksEnterAndSpace]
 * - [closeOnEscape]
 * - [closeOnBlur]
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
        })
    }

    /**
     * Apply this function on the panel representing [Tag] of the [OpenClose] implementing component, if the panel
     * should be closed by pressing the *Escape* key.
     */
    protected fun Tag<*>.closeOnEscape() {
        opened.flatMapLatest { isOpen ->
            Window.keydowns.filter { isOpen && shortcutOf(it) == Keys.Escape }
        } handledBy close
    }

    /**
     * Apply this function on the panel representing [Tag] of the [OpenClose] implementing component, if the panel
     * should be closed on clicking to somewhere outside the panel.
     */
    protected fun Tag<*>.closeOnBlur() {
        opened.flatMapLatest { isOpen ->
            Window.clicks.filter {
                isOpen && it.composedPath().none { it == this }
            }
        } handledBy close
    }

}
