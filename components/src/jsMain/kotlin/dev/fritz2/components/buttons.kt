package dev.fritz2.components

import dev.fritz2.components.buttons.LinkButtonComponent
import dev.fritz2.components.buttons.PushButtonComponent
import dev.fritz2.dom.DomListener
import dev.fritz2.dom.Listener
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.events.MouseEvent

/**
 * This component generates a simple button.
 *
 * You can set the label, an icon, the position of the icon and access its events.
 * For a detailed overview about the possible properties of the component object itself, have a look at
 * [PushButtonComponent].
 *
 * Example:
 * ```
 * pushButton {
 *     text("Click!")
 *     events {
 *         // need to couple the `clicks` event within the `events` context
 *         clicks handledBy someHandler
 *     }
 * }
 * ```
 *
 * In contrast to the [clickButton] component, this one does not return a [Listener] (basically a [Flow]) and so
 * the event handling has to be done manually!
 *
 * @see PushButtonComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [PushButtonComponent]
 */
fun RenderContext.pushButton(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "push-button",
    build: PushButtonComponent.() -> Unit = {}
) {
    PushButtonComponent().apply(build).render(this, styling, baseClass, id, prefix)
}

/**
 * This component generates a simple button.
 *
 * You can set the label, an icon, the position of the icon and access its events.
 * For a detailed overview about the possible properties of the component object itself, have a look at
 * [PushButtonComponent]
 *
 * In contrast to the [pushButton] component, this variant returns a [Listener] (basically a [Flow]) in order
 * to combine the button declaration directly to a fitting _handler_. Some other components
 * offer such a handler btw, so for example you can combine such a [clickButton] with a [modal] like this:
 *
 * Example:
 * ```
 * clickButton { text("save") } handledBy modal {
 *      items { p {+"foo"} }
 * }
 * ```
 *
 * @see PushButtonComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [PushButtonComponent]
 * @return a listener (think of a flow!) that offers the clicks of the button
 */
fun RenderContext.clickButton(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "click-button",
    build: PushButtonComponent.() -> Unit = {}
): DomListener<MouseEvent, HTMLButtonElement> {
    var clickEvents: DomListener<MouseEvent, HTMLButtonElement>? = null
    pushButton(styling, baseClass, id, prefix) {
        build()
        events {
            clickEvents = clicks
        }
    }
    return clickEvents!!
}

/**
 * This component generates a simple link button.
 *
 * You can set the label, an icon, the position of the icon and access its events.
 * For a detailed overview about the possible properties of the component object itself, have a look at
 * [LinkButtonComponent].
 *
 * In contrast to the [pushButton] component, this one wraps the button into an html anchor element.
 *
 * Example:
 * ```
 * linkButton {
 *    text("fritz2")
 *    href("//www.fritz2.dev")
 * }
 * ```
 *
 * @see PushButtonComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [LinkButtonComponent]
 */
fun RenderContext.linkButton(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "link-button",
    build: LinkButtonComponent.() -> Unit = {}
) {
    LinkButtonComponent().apply(build).render(this, styling, baseClass, id, prefix)
}
