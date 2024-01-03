package dev.fritz2.headless.foundation

import dev.fritz2.core.*
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.Node

/**
 * Base class that provides all functionality needed for components, that have some "open" and "close" state of
 * representation.
 *
 * Just extend from this class to gain and provide access to the basic data binding [openState] that holds the
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
abstract class OpenClose: WithJob {

    val openState = DatabindingProperty<Boolean>()

    val opened: Flow<Boolean> by lazy { openState.data }

    val close by lazy {
        SimpleHandler<Unit> { data, _ ->
            openState.handler?.invoke(this, data.map { false })
        }
    }

    val open by lazy {
        SimpleHandler<Unit> { data, _ ->
            openState.handler?.invoke(this, data.map { true })
        }
    }

    val toggle by lazy {
        SimpleHandler<Unit> { data, _ ->
            openState.handler?.invoke(this, data.map { !opened.first() })
        }
    }

    /**
     * Use this function on [Tag]s, that should trigger the component to open or to close in order to enable
     * keyboard support. Applying this function will toggle the state by the keys `Space` and `Enter` like a
     * `button` element behave natively.
     */
    protected fun Tag<*>.toggleOnClicksEnterAndSpace() {
        // If the wrapped element is a button, click events are already triggered by the Enter and Space keys.
        val events = if (domNode is HTMLButtonElement) {
            clicks
        } else {
            merge(clicks, keydowns.filter { setOf(Keys.Space, Keys.Enter).contains(shortcutOf(it)) })
        }
        events handledBy toggle
    }

    /**
     * Apply this function on the panel representing [Tag] of the [OpenClose] implementing component, if the panel
     * should be closed by pressing the *Escape* key.
     */
    protected fun Tag<*>.closeOnEscape() {
        Window.keydowns.filter { opened.first() && shortcutOf(it) == Keys.Escape } handledBy close
    }

    /**
     * Apply this function on the panel representing [Tag] of the [OpenClose] implementing component, if the panel
     * should be closed on clicking to somewhere outside the panel.
     */
    protected fun Tag<*>.closeOnBlur() {
        Window.clicks.filter { event -> opened.first() && !domNode.contains(event.target as? Node) && event.composedPath().none { it == this } } handledBy close
    }

}
