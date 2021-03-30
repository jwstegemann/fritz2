package dev.fritz2.components

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.*
import org.w3c.dom.HTMLDivElement

/**
 * This component class offers different configuration values of a spinner.
 *
 * If you set an ``icon``, the spinner will be icon based. The default behaviour is pure CSS based.
 * You can pass the ``size`` of the border by passing a [Thickness] value. Have a look at the definitions of your
 * [Theme.borderWidths] property.
 * Also you can control the ``speed`` of the animation by passing a value with time unit suffix, either ``s`` or ``ms``
 * according to the [standard](https://developer.mozilla.org/en-US/docs/Web/CSS/animation-duration)
 *
 * Have a look at the following example calls:
 * ```
 * // minimal setup -> pure CSS
 * spinner {}
 *
 * // pure CSS spinner
 * spinner {
 *      size { fat } // really, really fat spinner!
 * }
 *
 * // icon based
 * spinner {
 *      icon { star }
 * }
 *
 * // with styling and speed customized
 * spinner({
 *      color { "purple" }
 *      size { large }
 * }) {
 *      icon { star }
 *      speed { "300ms" }
 * }
 * ```
 *
 */
@ComponentMarker
open class SpinnerComponent : Component<Unit>,
    EventProperties<HTMLDivElement> by EventMixin() {
    companion object {
        val staticCss = staticStyle(
            "spinner",
            """ 
            display: inline-block;
            border-color: currentColor;
            border-style: solid;
            border-radius: 99999px;
            border-bottom-color: transparent;
            border-left-color: transparent;
            color: currentColor;
            
            @keyframes loading {
              to {transform: rotate(360deg);}
            }            
        """
        )
    }

    val icon = ComponentProperty<(Icons.() -> IconDefinition)?>(null)
    val speed = ComponentProperty("0.5s")
    val thickness = ComponentProperty<Thickness.() -> Property> { Theme().borderWidths.normal }

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {

        context.apply {
            if (icon.value == null) {
                (::div.styled(styling, baseClass + staticCss, id, prefix) {
                    css("animation: loading ${speed.value} linear infinite;")
                    border { width { thickness.value(Theme().borderWidths) } }
                    width { "1rem" }
                    height { "1rem" }
                }) {
                    events.value.invoke(this)
                }
            } else {
                div {
                    icon({
                        css(
                            """
                                @keyframes spinner {
                                  to {transform: rotate(360deg);}
                                }
                                animation: spinner ${speed.value} linear infinite;
                            """.trimIndent()
                        )
                        styling(this as BoxParams)
                    }, baseClass, id, prefix) {
                        def(this@SpinnerComponent.icon.value!!.invoke(Theme().icons))
                    }
                    events.value.invoke(this)
                }
            }
        }
    }
}


/**
 * This component generates an animated spinner. The spinner is either pure CSS (just a rotating curved border segment)
 * or [icon] based.
 *
 * You can customize the animation speed in CSS notation (``s`` or ``ms`` suffix is needed!).
 *
 * If the spinner is made of pure CSS, you can configure the size of the border on top.
 *
 * ```
 * // pure CSS spinner
 * spinner {
 *      size { fat } // really, really fat spinner!
 * }
 *
 * // icon based
 * spinner {
 *      icon { star }
 * }
 * ```
 *
 * For a detailed overview of the configuration options have a look at [SpinnerComponent]
 *
 * @see SpinnerComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [SpinnerComponent]
 */
fun RenderContext.spinner(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "spinner",
    build: SpinnerComponent.() -> Unit
) {
    SpinnerComponent().apply(build).render(this, styling, baseClass, id, prefix)
}