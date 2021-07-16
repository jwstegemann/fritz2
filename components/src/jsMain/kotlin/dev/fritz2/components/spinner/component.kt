package dev.fritz2.components.spinner

import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.components.foundations.EventMixin
import dev.fritz2.components.foundations.EventProperties
import dev.fritz2.components.icon
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.div
import dev.fritz2.styling.params.BoxParams
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
 */
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
            if (this@SpinnerComponent.icon.value == null) {
                div({
                    css("animation: loading ${this@SpinnerComponent.speed.value} linear infinite;")
                    border { width { this@SpinnerComponent.thickness.value(Theme().borderWidths) } }
                    width { "1rem" }
                    height { "1rem" }
                }, styling, baseClass + staticCss, id, prefix) {
                    this@SpinnerComponent.events.value.invoke(this)
                }
            } else {
                div {
                    icon({
                        css(
                            """
                @keyframes spinner {
                  to {transform: rotate(360deg);}
                }    
                animation: spinner ${this@SpinnerComponent.speed.value} linear infinite;
            """.trimIndent()
                        )
                        styling(this as BoxParams)
                    }, baseClass, id, prefix) {
                        def(this@SpinnerComponent.icon.value!!.invoke(Theme().icons))
                    }
                    this@SpinnerComponent.events.value.invoke(this)
                }
            }
        }
    }
}