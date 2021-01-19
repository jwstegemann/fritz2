package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.TextArea
import dev.fritz2.dom.values
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.StyleClass.Companion.plus
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.TextAreaResize
import dev.fritz2.styling.theme.TextAreaSize
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

/**
 * This class handles the configuration of an textarea element
 *
 * The textArea can be configured for the following aspects:
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
class TextAreaComponent : ElementProperties<TextArea> by Element(), InputFormProperties by InputForm() {

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
            color { light }

        }

        background { color { "white" } }

        disabled {
            background {
                color { disabled }
            }

        }

        focus {
            border {
                color { "#3182ce" }
            }
            boxShadow { outline }
        }
    }

    var value: Flow<String>? = null

    fun value(value: Flow<String>) {
        this.value = value
    }

    fun value(value: String) {
        value(flowOf(value))
    }

    var placeholder: Flow<String>? = null

    fun placeholder(value: String) {
        placeholder = flowOf(value)
    }

    fun placeholder(value: Flow<String>) {
        placeholder = value
    }

    var size: TextAreaSize.() -> Style<BasicParams> = { Theme().textarea.size.normal }
    fun size(value: TextAreaSize.() -> Style<BasicParams>) {
        size = value
    }

    var resizeBehavior: TextAreaResize.() -> Style<BasicParams> = { Theme().textarea.resize.vertical }
    fun resizeBehavior(value: TextAreaResize.() -> Style<BasicParams>) {
        resizeBehavior = value
    }
}

/**
 * This component generates a  textarea
 *
 * You can optionally pass in a store in order to set the value and react to updates _automatically_.
 *
 * To enable or disable it or to make it readOnly just use the well known attributes of the HTML
 *
 * Possible values to set are( default *) :
 *  - size : small | normal * | large
 *  - resizeBehavior : none | vertical *| horizontal
 *  - placeholder : String | Flow<String>
 *  - disable : Boolean | Flow<Boolean>
 *  - value -> maybe you want to set an initial value instead of a placeholder
 *  - base -> basic properties of an textarea
 *
 *  textArea(store = dataStore) {
 *        placeholder { "My placeholder" }  // render a placeholder text for empty textarea
 *        resizeBehavior { horizontal }    // resize textarea horizontal
 *        size { small }                   // render a smaller textarea
 *     }
 *
 *
 *   textArea({ // just use the ``styling`` parameter!
 *            background {
 *                color { dark }
 *               }
 *               color { light }
 *               radius { "1rem" }},store = dataStore) {
 *
 *               disable(true)              // textarea is disabled now
 *               resizeBehavior { none }    // resizing is not possible now
 *               size { large }             // render a large textarea
 *
 *               }
 *
 *   textArea {
 *          value { dataStore.data }  // value depends on value in store
 *          disable(true)             // editing is disabled, but resizing still works
 *          }
 *
 *   textArea {
 *         base{                                        // you have access to base properties of a textarea
 *         placeholder("Here is a sample placeholder")
 *         changes.values() handledBy dataStore.update
 *                 }
 *          }
 *
 * @see TextAreaComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param store optional [Store] that holds the data of the textarea
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param init a lambda expression for setting up the component itself. Details in [TextAreaComponent]
 */


fun RenderContext.textArea(
    styling: BasicParams.() -> Unit = {},
    store: Store<String>? = null,
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "textArea",
    init: TextAreaComponent.() -> Unit
) {

    val component = TextAreaComponent().apply(init)

    (::textarea.styled(styling, baseClass + TextAreaComponent.staticCss, id, prefix) {
        component.resizeBehavior.invoke(Theme().textarea.resize)()
        component.size.invoke(Theme().textarea.size)()
        component.basicInputStyles()

    }){
        component.element?.invoke(this)
        disabled(component.disabled)
        readOnly(component.readonly)
        placeholder(component.placeholder ?: emptyFlow())
        value(component.value ?: emptyFlow())
        store?.let {
            value(it.data)
            changes.values() handledBy it.update
        }
    }
}