package dev.fritz2.components

import dev.fritz2.dom.html.A
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import kotlinx.coroutines.flow.Flow

/**
 * This class extends the [PushButtonComponent] and adds link specific properties to it.
 *
 *
 * Example:
 * ```
 * linkButton {
 *     icon { fromTheme { check } } // set up an icon
 *     iconPlacement { right } // place the icon on the right side (``left`` is the default)
 *     loading(someStore.loading) // pass in some [Flow<Boolean>] that shows a spinner if ``true`` is passed
 *     loadingText("saving") // show an _alternate_ label, if store sends ``true``
 *     text("save") // define the default label
 *     disabled(true) // disable the button; could also be a ``Flow<Boolean>`` for dynamic disabling
 *     events { // open inner context with all DOM-element events
 *         clicks handledBy someStore.update // react to click event
 *     }
 *     element {
 *         // exposes the underlying HTML button element for direct access. Use with caution!
 *     }
 *     elementLink {
 *         // exposes the underlying HTML anchor element for direct access. Use with caution!
 *     }
 * }
 * ```
 *
 * @see PushButtonComponent
 */
open class LinkButtonComponent : PushButtonComponent() {

    private var href: A.() -> Unit = {}
    fun href(value: String) {
        href = {
            href(value)
        }
    }

    fun href(value: Flow<String>) {
        href = {
            href(value)
        }
    }

    private var target: A.() -> Unit = {}
    fun target(value: String) {
        target = {
            target(value)
        }
    }

    fun target(value: Flow<String>) {
        target = {
            target(value)
        }
    }

    val elementLink: ComponentProperty<A.() -> Unit> = ComponentProperty {}

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            a {
                this@LinkButtonComponent.elementLink.value(this)
                this@LinkButtonComponent.href(this)
                this@LinkButtonComponent.target(this)
                super.render(this, styling, baseClass, id, prefix)
            }
        }
    }
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