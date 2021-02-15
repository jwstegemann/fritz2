package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.TextArea
import dev.fritz2.dom.values
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.StyleClass.Companion.plus
import dev.fritz2.styling.className
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.TextAreaResize
import dev.fritz2.styling.theme.FormSizes
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLTextAreaElement

/**
 * This class handles the configuration of an textarea element
 *
 * The TextArea can be configured for the following aspects:
 *  - the size of the element
 *  - the direction of resizing
 *  - some predefined styles
 *  - a default value
 *
 *  - the base options of the HTML input element can be set.
 *  [Attributes](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/textarea#Attributes)
 *
 *  * For a detailed explanation and examples of usage have a look at the [textArea] function !
 *
 */
@ComponentMarker
class TextAreaComponent :
    EventProperties<HTMLTextAreaElement> by EventMixin(),
    ElementProperties<TextArea> by ElementMixin(),
    InputFormProperties by InputFormMixin(),
    SeverityProperties by SeverityMixin() {

    companion object {
        val staticCss = staticStyle(
            "textAreaContainer",
            """
            outline: 0px;
            position: relative;
            appearance: none;
            transition: all 0.2s ease 0s;
            min-height: 80px;
            
            """
        )
    }

    val basicInputStyles: Style<BasicParams> = {

        radius { normal }
        fontWeight { normal }

        border {
            width { thin }
            style { solid }
            color { lightGray }

        }

        background { color { "white" } }

        disabled {
            background {
                color { base }
            }
            color { disabled }

        }

        focus {
            border {
                color { "#3182ce" }
            }
            boxShadow { outline }
        }
    }

    val value = DynamicComponentProperty(flowOf(""))
    val placeholder = DynamicComponentProperty(flowOf(""))
    val resizeBehavior = ComponentProperty<TextAreaResize.() -> Style<BasicParams>> { Theme().textArea.resize.vertical }
    val size = ComponentProperty<FormSizes.() -> Style<BasicParams>> { Theme().textArea.sizes.normal }
}

/**
 * This component generates a TextArea.
 *
 * You can optionally pass a store in order to set the value and react to updates automatically.
 *
 * To enable or disable it or to make it readOnly, just use the well known attributes of HTML.
 *
 * Possible values to set are( default *) :
 *  - size : small | normal * | large
 *  - resizeBehavior : none | vertical *| horizontal
 *  - placeholder : String | Flow<String>
 *  - disable : Boolean | Flow<Boolean>
 *  - value -> maybe you want to set an initial value instead of a placeholder
 *  - events -> access the DOM events of the underlying HTML element
 *  - element -> basic properties of the textarea html element; use with caution!
 *
 * textArea(store = dataStore) {
 *     placeholder { "My placeholder" } // render a placeholder text for empty textarea
 *     resizeBehavior { horizontal } // resize textarea horizontal
 *     size { small } // render a smaller textarea
 * }
 *
 *
 * textArea({ // use the styling parameter
 *     background {
 *         color { dark }
 *     }
 *     color { light }
 *     radius { "1rem" }}, store = dataStore) {
 *     disable(true) // textarea is disabled
 *     resizeBehavior { none } // resizing is not possible
 *     size { large } // render a large textarea
 * }
 *
 * textArea {
 *     value { dataStore.data } // value depends on value in store
 *     disable(true) // editing is disabled, but resizing still works
 * }
 *
 * // all state management can also be done manually if needed:
 * val someStore = storeOf("some initial text")
 * textArea {
 *     value(someStore.data)
 *     events {
 *         changes.values() handledBy someStore.update
 *     }
 * }
 *
 * @see TextAreaComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param store optional [Store] that holds the data of the textarea
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [TextAreaComponent]
 */


fun RenderContext.textArea(
    styling: BasicParams.() -> Unit = {},
    store: Store<String>? = null,
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "textArea",
    build: TextAreaComponent.() -> Unit
) {

    val component = TextAreaComponent().apply(build)

    (::textarea.styled(styling, baseClass + TextAreaComponent.staticCss, id, prefix) {
        component.resizeBehavior.value.invoke(Theme().textArea.resize)()
        component.size.value.invoke(Theme().textArea.sizes)()
        component.basicInputStyles()

    }){
        component.element.value.invoke(this)
        component.events.value.invoke(this)
        disabled(component.disabled.values)
        readOnly(component.readonly.values)
        placeholder(component.placeholder.values)
        value(component.value.values)
        className(component.severityClassOf(Theme().textArea.severity, prefix))
        store?.let {
            value(it.data)
            changes.values() handledBy it.update
        }
    }
}