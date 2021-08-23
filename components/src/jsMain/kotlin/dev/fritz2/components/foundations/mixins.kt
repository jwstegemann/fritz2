package dev.fritz2.components.foundations

import dev.fritz2.components.buttons.PushButtonComponent
import dev.fritz2.components.clickButton
import dev.fritz2.components.tooltip
import dev.fritz2.components.tooltip.TooltipComponent
import dev.fritz2.components.validation.Severity
import dev.fritz2.dom.DomListener
import dev.fritz2.dom.EventContext
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.style
import dev.fritz2.styling.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.MouseEvent

/**
 * An interface for exposing an HTML element. Use this vor components that more or less wrap a single basic HTML
 * tag like ``inputField`` or ``pushButton``.
 *
 * The offered [element] property enables the client to access the deeper features of an element even though the
 * component itself does not offer an appropriate functionality. A client should use this with caution,
 * as it might massively change the default behaviour of the component!
 *
 * Example usage:
 * ```
 * // apply interface to a component class
 * open class SomeComponent : ElementProperties<Div> by ElementMixin() {
 *                            ^^^^^^^^^^^^^     ^^^     ^^^^^^^^^^^^^^
 *                            implement the      |      mix in the default implementation
 *                            interface          |
 *                                              Expose the underlying element,
 *                                              in this case a ``Div``!
 * }
 *
 * // use the property offered by the interface
 * someComponent {
 *      element {
 *          // all properties of the ``Div`` element are accessible here
 *      }
 * }
 * ```
 *
 * Advice: Try to offer *all* useful properties for a component in a redundant way if the component usage will benefit
 * from a feature the underlying HTML element offers! This will make it much easier for the client user to use
 * a component and much easier to read and maintain at client side code.
 *
 * RFC: If the component itself offers some redundant property, the values from within the [element] property
 * context should win and used for the rendering! This is the *mandatory* behaviour!
 */
interface ElementProperties<T> {

    /**
     * This property enables the client to access the deeper features of an element even though the component itself
     * does not offer an appropriate functionality. A client should use this with caution, as it might massively change
     * the default behaviour of the component!
     */
    val element: ComponentProperty<T.() -> Unit>
}

/**
 * Default implementation of the [ElementProperties] interface in order to apply this as mixin for a component
 */
// TODO: Constraint für Typ: T : Tag<E> ?
class ElementMixin<T> : ElementProperties<T> {
    override val element: ComponentProperty<T.() -> Unit> = ComponentProperty {}
}

/**
 * An interface for exposing the events of a component. The type of the underlying HTML element must be specified.
 * This way *all* events of the wrapped element get exposed to the client via the [events] property.
 *
 * Example usage:
 * ```
 * // apply interface to a component class
 * open class SomeComponent : EventProperties<HTMLDivElement> by EventMixin() {
 *                            ^^^^^^^^^^^^^   ^^^^^^^^^^^^^^     ^^^^^^^^^^^^
 *                            implement the    |                 mix in the default implementation
 *                            interface        |
 *                                            Enables to access the underlying element,
 *                                            in this case a ``Div`` element in order
 *                                            to expose or wrap its events!
 * }
 *
 * // use the property offered by the interface
 * someComponent {
 *      events {
 *          // all events of the ``Div`` element are accessible here
 *          clicks.value handledBy someStore.someHandler
 *      }
 * }
 * ```
 *
 * RFC: If your component does not simply wrap some element and expose its events but instead has to offer its custom
 * and specific events, please offer an ``events`` property by yourself, so that the access remains unified
 * throughout this framework!
 */
interface EventProperties<T : Element> {

    /**
     * This property enables the client to access *all* events offered by the underlying HTML element.
     */
    val events: ComponentProperty<EventContext<T>.() -> Unit>
}

/**
 * Default implementation of the [EventProperties] interface in order to apply this as mixin for a component
 */
class EventMixin<T : Element> : EventProperties<T> {
    override val events: ComponentProperty<EventContext<T>.() -> Unit> = ComponentProperty {}
}

/**
 * This interface add typical state properties for en- or disabling form components.
 *
 * As it often depends on the specific use case, the two complementary properties [disabled] and [enabled] are both
 * offered. This is primarily for better readability at client side code.
 *
 * example usage:
 * ```
 * // apply interface to component class
 * open class MyControl : FormProperties by FormMixin() {
 * }
 *
 * // use the property offered by the interface
 * val disable = storeOf(true)
 * myControl {
 *      disabled(disable.value)
 * }
 *
 * // and instead of double negative expression ``val disable = storeOf(false)`` better use:
 * val enable = storeOf(false)
 * myControl {
 *      enabled(enable.value)
 * }
 * ```
 */
interface FormProperties {
    val disabled: DynamicComponentProperty<Boolean>

    fun enabled(value: Flow<Boolean>) {
        disabled(value.map { !it })
    }

    fun enabled(value: Boolean) {
        enabled(flowOf(value))
    }
}

/**
 * Default implementation of the [FormProperties] interface in order to apply this as mixin for a component
 */
open class FormMixin : FormProperties {
    override val disabled = DynamicComponentProperty(flowOf(false))
}

/**
 * This interface offers a convenience property for input form based components.
 *
 * By setting the [readonly] property to ``true`` the component should become readonly.
 *
 * Example usage:
 * ```
 * open class MyComponent : InputFormProperties by InputFormMixin() {
 * }
 *
 * // use the property offered by the interface
 * val readonly = storeOf(true)
 * myControl {
 *      readonly(readonly.value)
 * }
 * ```
 */
interface InputFormProperties : FormProperties {
    val readonly: DynamicComponentProperty<Boolean>
}

/**
 * Default implementation of the [InputFormProperties] interface in order to apply this as mixin for a component
 */
class InputFormMixin : InputFormProperties, FormMixin() {
    override val readonly = DynamicComponentProperty(flowOf(false))
}

/**
 * This interface offers a convenience property for inputField based components.
 *
 * Example usage:
 * ```
 * open class MyComponent : InputFieldProperties by InputFieldMixin() {
 * }
 *
 * // use the property offered by the interface
 * myControl {
 *      variant { outline }
 *      size { small }
 *      placeholder("Password")
 * }
 * ```
 */
interface InputFieldProperties {
    val variant: ComponentProperty<InputFieldVariants.() -> Style<BasicParams>>
    val size: ComponentProperty<FormSizesStyles.() -> Style<BasicParams>>
    val placeholder: DynamicComponentProperty<String>
}

/**
 * Default implementation of the [InputFieldProperties] interface in order to apply this as mixin for a component
 */
class InputFieldMixin : InputFieldProperties {
    override val variant = ComponentProperty<InputFieldVariants.() -> Style<BasicParams>> {
        Theme().input.variants.outline
    }
    override val size = ComponentProperty<FormSizesStyles.() -> Style<BasicParams>> {
        Theme().input.sizes.normal
    }
    override val placeholder = DynamicComponentProperty(flowOf(""))
}

/**
 * This interface defines convenience properties and helper functions to easily apply [Severity] based behaviour to
 * a component.
 *
 * The property [severity] is offered in order to hold the current severity value.
 *
 * In order to reactively apply an appropriate styling based upon the current severity value of the component -
 * changing the background color for example - the helper function [severityClassOf] helps to choose the corresponding
 * style bades upon the [SeverityStyles] interface from the [dev.fritz2.styling.theme] package.
 */
interface SeverityProperties {
    val severity: NullableDynamicComponentProperty<Severity?>

    class SeverityContext {
        val info: Severity = Severity.Info
        val success: Severity = Severity.Success
        val warning: Severity = Severity.Warning
        val error: Severity = Severity.Error
    }

    /**
     * Property to manage the severity value of the component.
     * ```
     * myComponent {
     *      severity { warning } // often this is passed from a [Flow] of course
     * }
     * ```
     *
     * @param value mapping expression from a [SeverityContext] to the enumeration value.
     */
    fun severity(value: SeverityContext.() -> Severity) {
        severity(value(SeverityContext()))
    }

    /**
     * This function manages the task to map a value of the [Severity] enumeration to a corresponding style defined
     * within the [SeverityStyles] interface. The severity itself is taken from the [severity] property, so only the
     * styling interface's implementation has to be injected:
     * ```
     * open class MyComponent {
     *      override fun render(/*...*/, prefix: String) {
     *          someElement {
     *              // set fitting severity for example by using a store with validation
     *              severity(/* get severity from somewhere */)
     *              // apply the correct style based upon the severity
     *              className(severityClassOf(Theme().myComponent.severity, prefix))
     *                                        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^
     *                                        It's a good idea to offer a
     *                                        specific property in the
     *                                        component's theme section
     *                                        based upon ``SeverityStyles``
     *          }
     *      }
     * }
     * ```
     *
     * @param severityStyle definition for the different styling for each state
     *
     * @return a flow of the current mapped style class. Can be applied to the component via [className] extension
     *         method
     */
    fun severityClassOf(severityStyle: SeverityStyles): Flow<StyleClass> =
        severity.values.map {
            when (it) {
                Severity.Info -> style("severity-info", severityStyle.info)
                Severity.Success -> style("severity-success", severityStyle.success)
                Severity.Warning -> style("severity-warning", severityStyle.warning)
                Severity.Error -> style("severity-error", severityStyle.error)
                else -> StyleClass.None
            }
        }
}

/**
 * Default implementation of the [SeverityProperties] interface in order to apply this as mixin for a component
 */
class SeverityMixin : SeverityProperties {
    override val severity = NullableDynamicComponentProperty<Severity?>(emptyFlow())
}

/**
 * This interface offers some convenience properties for adding a close button to a component.
 *
 * If offers the possibilities to:
 * - change the styling of the default appearance
 * - decide whether there is a close button or not
 * - change the whole rendering by a custom implementation
 *
 * Example integration:
 * ```
 * open class MyComponent : Component<Unit>, CloseButtonProperty by CloseButtonMixin(ComponentProperty{})  {
 * //                                        ^^^^^^^^^^^^^^^^^^^    ^^^^^^^^^^^^^^^^ ^^^^^^^^^^^^^^^^^^^
 * //                                        implement interface    delegate to      inject empty custom
 * //                                                               mixin            styling
 *      override fun render(/* params omitted */) {
 *          div {
 *              // check if closeButton is needed and render it then
 *              if (hasCloseButton.value) {
 *                  closeButtonRendering.value(this) handledBy closeHandler
 *                  //                               ^^^^^^^^^^^^^^^^^^^^^^
 *                  //                               use return value (event)
 *                  //                               to handle it by your closing mechanism
 *              }
 *          }
 *      }
 * }
 * ```
 *
 * For some example usages, have a look at the following components:
 * @see [ModalComponent]
 * @see [PopoverComponent]
 * @see [ToastComponent]
 */
interface CloseButtonProperty {
    val closeButtonPrefix: String
    val closeButtonStyle: ComponentProperty<Style<BasicParams>>
    val closeButtonIcon: ComponentProperty<Icons.() -> IconDefinition>
    val hasCloseButton: ComponentProperty<Boolean>
    val closeButtonRendering: ComponentProperty<RenderContext.() -> DomListener<MouseEvent, HTMLElement>>
}

/**
 * Default implementation of the [CloseButtonProperty] interface in order to apply this as mixin for a component
 *
 * @param closeButtonPrefix the prefix for the generated CSS class
 * @param defaultStyle define the default styling of the button fitting for the implementing component needs
 *                     (the placement within the component's space for example)
 */
class CloseButtonMixin(
    override val closeButtonPrefix: String = "close-button",
    private val defaultStyle: Style<BasicParams>
) : CloseButtonProperty {

    private val resetButtonStyles: Style<BasicParams> = {
        lineHeight { "unset" }
        radius { "unset" }
        fontWeight { "unset" }
        padding { "unset" }
        height { "unset" }
        minWidth { "unset" }
    }

    override val closeButtonStyle = ComponentProperty<Style<BasicParams>> {}
    override val closeButtonIcon = ComponentProperty<Icons.() -> IconDefinition> { Theme().icons.close }
    override val hasCloseButton = ComponentProperty(true)
    override val closeButtonRendering = ComponentProperty<RenderContext.() -> DomListener<MouseEvent, HTMLElement>> {
        clickButton({
            resetButtonStyles()
            defaultStyle()
            closeButtonStyle.value()
        }, prefix = closeButtonPrefix) {
            variant { PushButtonComponent.VariantContext.ghost }
            icon { closeButtonIcon.value(Theme().icons) }
        }
    }
}

/**
 * Definition of the layout orientation of a form.
 */
enum class Orientation {
    HORIZONTAL, VERTICAL
}

/**
 * A context class for allowing an expressive DSL for component's configuration:
 *
 * ```
 * // 'orientation' is provided by ``OrientationProperty.orientation``
 * orientation { horizontal }
 * orientation { vertical }
 * ```
 */
object OrientationContext {
    val horizontal: Orientation = Orientation.HORIZONTAL
    val vertical: Orientation = Orientation.VERTICAL
}

/**
 * This interface add an orientation property for position the component's element(s) into an horizontal or
 * vertical orientation.
 */
interface OrientationProperty {
    val orientation: ComponentProperty<OrientationContext.() -> Orientation>
}

/**
 * Default implementation of the [OrientationProperty] interface in order to apply this as mixin for a component
 *
 * @param default set the default orientation for the implementing component (checkBoxGroup needs vertical, but
 *                slider horizontal orientation for example)
 */
class OrientationMixin(default: Orientation) : OrientationProperty {
    override val orientation: ComponentProperty<OrientationContext.() -> Orientation> by lazy {
        ComponentProperty { default }
    }
}

interface TooltipProperties {
    val tooltipStyling: ComponentProperty<BasicParams.() -> Unit>
    val tooltipText: DynamicComponentProperty<List<String>>
    val tooltipBuild: ComponentProperty<TooltipComponent.() -> Unit>

    fun tooltip(
        styling: BasicParams.() -> Unit,
        text: String?,
       //baseClass: StyleClass,
       //id: String,
       //prefix: String,
        build: TooltipComponent.() -> Unit
    ){
        tooltipStyling(styling)
        tooltipText(listOf(text ?: ""))
        tooltipBuild(build)
    }

    fun renderTooltip(context: RenderContext) {
        context.apply {
            tooltipText.values.render {
                if( it.isNotEmpty() ) {
                    tooltip(
                        tooltipStyling.value
                    ) {
                        tooltipBuild.value(this@tooltip)
                        text(it)
                    }
                }
            }
        }
    }

}


open class TooltipMixin: TooltipProperties {
    override val tooltipStyling = ComponentProperty<BasicParams.() -> Unit> {}
    override val tooltipText =  DynamicComponentProperty<List<String>>(flowOf(listOf("")))
    override val tooltipBuild =  ComponentProperty<TooltipComponent.() -> Unit> {}
}
