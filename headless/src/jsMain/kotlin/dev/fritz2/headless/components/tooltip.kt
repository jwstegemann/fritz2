import dev.fritz2.core.RenderContext
import dev.fritz2.core.ScopeContext
import dev.fritz2.core.Tag
import dev.fritz2.headless.foundation.PopUpPanel
import dev.fritz2.headless.foundation.TagFactory
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

class Tooltip<C : HTMLElement>(
    val renderContext: Tag<HTMLElement>,
    tagFactory: TagFactory<Tag<C>>,
    classes: String?,
    id: String?,
    scope: ScopeContext.() -> Unit
) : PopUpPanel<C>(renderContext, tagFactory, classes, id, scope, renderContext.run {
    merge(mouseenters.map {true}, mouseleaves.map {false})
}, fullWidth = false, renderContext)

/**
 * Factory function to create a [popOverPanel].
 *
 * For more information refer to the
 * [official documentation](https://docs.fritz2.dev/headless/popover/#popoverpanel)
 */
fun <C: HTMLElement> Tag<HTMLElement>.tooltip(
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
 * Factory function to create a [popOverPanel] with a [HTMLDivElement] as default [Tag].
 *
 * For more information refer to the
 * [official documentation](https://docs.fritz2.dev/headless/popover/#popoverpanel)
 */
fun Tag<HTMLElement>.tooltip(
    classes: String? = null,
    id: String? = null,
    internalScope: (ScopeContext.() -> Unit) = {},
    initialize: Tooltip<HTMLDivElement>.() -> Unit
) = tooltip(classes, id, internalScope, RenderContext::div, initialize)
