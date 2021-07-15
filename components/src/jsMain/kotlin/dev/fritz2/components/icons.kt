package dev.fritz2.components

import dev.fritz2.components.icon.IconComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.theme.IconDefinition


/**
 * This component enables to render an icon. It basically wraps raw SVG images into a nicer API.
 *
 * fritz2's default theme offers some basic predefined icons, have a look at [dev.fritz2.styling.theme.Theme.icons].
 *
 * Every icon must be wrapped inside an [IconDefinition], that acts as a value class for the raw SVG markup.
 * Such a definition is implicitly set by using the ``fromTheme`` configuration function:
 *
 * Basic Usage
 * ```
 * icon { fromTheme { fritz2 } }
 * //     ^^^^^^^^^
 *        convenient function for easily set the predefined icons from the theme
 * ```
 *
 * @see IconComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [IconComponent]
 */
fun RenderContext.icon(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = IconComponent.prefix,
    build: IconComponent.() -> Unit = {}
) {
    IconComponent().apply {
        build()
        if (displayName.value != null && svg.value != null) {
            def(IconDefinition(displayName.value!!, viewBox.value, svg.value!!))
        }
    }.render(this, styling, baseClass, id, prefix)
}


