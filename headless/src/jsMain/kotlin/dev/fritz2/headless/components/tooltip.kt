import dev.fritz2.core.RenderContext
import dev.fritz2.core.ScopeContext
import dev.fritz2.core.Tag
import dev.fritz2.headless.foundation.Hook
import dev.fritz2.headless.foundation.PopUpPanel
import dev.fritz2.headless.foundation.Property
import dev.fritz2.headless.foundation.TagFactory
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

/**
 * This class provides the building blocks to implement a tooltip.
 *
 * Use [tooltip] functions to create an instance, set up the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://docs.fritz2.dev/headless/tooltip/)
 */

class Tooltip<C : HTMLElement>(
    val renderContext: Tag<HTMLElement>,
    tagFactory: TagFactory<Tag<C>>,
    classes: String?,
    id: String?,
    scope: ScopeContext.() -> Unit
) : PopUpPanel<C>(
    renderContext.annex, tagFactory, classes, id, scope,
    opened = renderContext.run {
        merge(mouseenters.map { true }, mouseleaves.map { false })
    },
    fullWidth = false, renderContext
)

/**
 * Factory function to create a [tooltip].
 *
 * For more information refer to the
 * [official documentation](https://docs.fritz2.dev/headless/tooltip/)
 */
fun <C : HTMLElement> Tag<HTMLElement>.tooltip(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: Tooltip<C>.() -> Unit
) {
    Tooltip(this, tag, classes, id, scope).run {
        initialize()
        render()
    }
}

/**
 * Factory function to create a [tooltip] with a [HTMLDivElement] as default [Tag].
 *
 * For more information refer to the
 * [official documentation](https://docs.fritz2.dev/headless/tooltip)
 */
fun Tag<HTMLElement>.tooltip(
    classes: String? = null,
    id: String? = null,
    internalScope: (ScopeContext.() -> Unit) = {},
    initialize: Tooltip<HTMLDivElement>.() -> Unit
) = tooltip(classes, id, internalScope, RenderContext::div, initialize)
