package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.components.validation.Severity
import dev.fritz2.dom.DomListener
import dev.fritz2.dom.HtmlTagMarker
import dev.fritz2.dom.WithEvents
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.style
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.Icons
import dev.fritz2.styling.theme.SeverityStyles
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.w3c.dom.*
import org.w3c.dom.events.MouseEvent

/**
 * A marker to separate the layers of calls in the type-safe-builder pattern.
 */
@DslMarker
annotation class ComponentMarker


/**
 * Generic container for modeling a property for a component class.
 * Use it like this:
 * ```
 * open class SomeComponent {
 *      val myProp = ComponentProperty("some text")
 *      val myBooleanProp = ComponentProperty(false)
 *
 *      // should rather be some implementation in the default theme of course!
 *      class SizesContext {
 *          small: Style<BasicParams> = { fontSize { small } }
 *          normal: Style<BasicParams> = { fontSize { small } }
 *          large: Style<BasicParams> = { fontSize { small } }
 *      }
 *
 *      val sizes = ComponentProperty<SizesContext.() -> Style<BasicParams>> { normal }
 * }
 * // within your UI declaration:
 * someComponent {
 *      myProp("Some specific content") // pass simple parameter
 *      myBooleanProp(true)
 *      sizes { large } // use expression syntax
 * }
 * ```
 */
class ComponentProperty<T>(var value: T) {
    operator fun invoke(newValue: T) {
        value = newValue
    }
}

/**
 * Generic container for modeling a property for a component class that could be either consist on a value or on a
 * [Flow] of a value.
 *
 * Use it like this:
 * ```
 * open class SomeComponent {
 *      val myProp = DynamicComponentProperty("some text")
 *      val myBooleanProp = DynamicComponentProperty(false)
 * }
 * // within your UI declaration and static values:
 * someComponent {
 *      myProp("Some specific content")
 *      myBooleanProp(true)
 * }
 * // within your UI declaration and dynamic values:
 * val contentStore = storeOf("Empty")
 * val toggle = storeOf(false)
 * someComponent {
 *      myProp(contentStore.data)
 *      myBooleanProp(toggle.data)
 * }
 * ```
 */
class DynamicComponentProperty<T>(var values: Flow<T>) {
    operator fun invoke(newValue: T) {
        values = flowOf(newValue)
    }

    operator fun invoke(newValues: Flow<T>) {
        values = newValues
    }
}

/**
 * Generic container for modeling a property for a component class that could be either consist on a nullable value
 * or on a [Flow] of a nullable value. This specific implementation could be useful for properties where the distinction
 * between some states and the "not yet set" state is important.
 *
 * Use it like this:
 * ```
 * open class SomeComponent<T> {
 *      val selectedItem = NullableDynamicComponentProperty<T>(emptyFlow())
 * }
 * // within your UI declaration and static values:
 * val selectedStore = storeOf<String>(null)
 * someComponent<String> {
 *      selectedItem(selectedStore.data) // no selection at start up!
 * }
 * ```
 */
class NullableDynamicComponentProperty<T>(var values: Flow<T?>) {
    operator fun invoke(newValue: T?) {
        values = flowOf(newValue)
    }

    operator fun invoke(newValues: Flow<T?>) {
        values = newValues
    }
}

/**
 * Marker interface that *every* component should implement, so that the central render method appears in a unified way
 * throughout this framework.
 *
 * The render method has to be implemented in order to do the actual rendering of one component.
 * This reduces the boilerplate code within the corresponding factory function(s):
 * ```
 * open class MyComponent: Component {
 *      override fun render(...) {
 *          // some content rendering
 *      }
 * }
 *
 * RenderContext.myComponent(
 *      // most params omitted
 *      build: MyComponent.() -> Unit = {}
 * ) {
 *      MyComponent().apply(build).render(this, /* params */)
 *      //                         ^^^^^^
 *      //                         just start the rendering by one additional call!
 * }
 * ```
 *
 * Often a component needs *additional* parameters that are passed into the factory functions (remember that those
 * should be located starting after the ``styling`` parameter in first position and before the ``baseClass`` parameter)
 * Typical use cases are [Store]s or list of items, as for [RenderContext.checkboxGroup] for example.
 * Those additional parameters should be passed via contructor injection into the component class:
 * ```
 * open class MyComponent(protected val items: List<String>, protected val store: Store<String>?): Component {
 *      override fun render(...) {
 *          // some content rendering with access to the ``items`` and the ``store``
 *      }
 * }
 *
 * RenderContext.myComponent(
 *      styling: BasicParams.() -> Unit,
 *      items: List<String>,          // two additional parameters
 *      store: Store<String>? = null, // after ``styling`` and before ``baseClass``!
 *      baseClass: StyleClass,
 *      id: String?,
 *      prefix: String
 *      build: MyComponent.() -> Unit = {}
 * ) {
 *      MyComponent(items, store) // inject additional parameters
 *          .apply(build)
 *          .render(this, styling, baseClass, id, prefix) // pass context + regular parameters
 * }
 * ```
 */
@HtmlTagMarker
interface Component<T> {

    /**
     * Central method that should do the actual rendering of a component.
     *
     * Consider to declare your implementation as ``open`` in order to allow the customization of a component.
     *
     * @param context the receiver to render the component into
     * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
     * @param baseClass optional CSS class that should be applied to the element
     * @param id the ID of the element
     * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
     */
    fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ): T
}

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
    val events: ComponentProperty<WithEvents<T>.() -> Unit>
}

/**
 * Default implementation of the [EventProperties] interface in order to apply this as mixin for a component
 */
class EventMixin<T : Element> : EventProperties<T> {
    override val events: ComponentProperty<WithEvents<T>.() -> Unit> = ComponentProperty {}
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
 * This store can be used for components with an *internal* store that has to deal with a [List] of some type T.
 *
 * Use the [toggle] method to add or remove an selected item from the current selection:
 * ```
 * val selection = MultiSelectionStore<String>()
 * lineUp {
 *     items {
 *         listOf("One", "Two", "Three", "Four", "Five").forEach { value ->
 *             box({
 *                 background { color { info } }
 *             }) {
 *                 +value
 *                 clicks.events.map { value } handledBy selection.toggle
 *                 //                  ^^^^^                       ^^^^^^
 *                 //                  pass current value to the ``toggle`` handler!
 *             }
 *         }
 *     }
 * }
 * div {
 *     +"Selection:"
 *     ul {
 *         selection.data.renderEach { value ->
 *             li { +value }
 *         }
 *     }
 * }
 * ```
 *
 * RFC: Never ever expose the internal store directly to the client side! Only accept values or [Flow]s and return
 * those in order to exchange data with the client!
 */
open class MultiSelectionStore<T> : RootStore<List<T>>(emptyList()) {
    val toggle = handleAndEmit<T, List<T>> { selectedRows, new ->
        val newSelection = if (selectedRows.contains(new))
            selectedRows - new
        else
            selectedRows + new
        emit(newSelection)
        newSelection
    }
}

/**
 * This store can be used for components with an *internal* store that has to deal with a single element *selection*
 * from a collection of predefined values (like for a [selectField] or [radioGroup] component)
 *
 * It is based upon the *index* of an item from the list represented by the [Int] type.
 *
 * RFC: Never ever expose the internal store directly to the client side! Only accept values or [Flow]s and return
 * those in order to exchange data with the client!
 */
open class SingleSelectionStore : RootStore<Int?>(null) {
    val toggle = handleAndEmit<Int, Int> { _, new ->
        emit(new)
        new
    }
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
    override val closeButtonStyle = ComponentProperty<Style<BasicParams>> {}
    override val closeButtonIcon = ComponentProperty<Icons.() -> IconDefinition> { Theme().icons.close }
    override val hasCloseButton = ComponentProperty(true)
    override val closeButtonRendering = ComponentProperty<RenderContext.() -> DomListener<MouseEvent, HTMLElement>> {
        clickButton({
            defaultStyle()
            closeButtonStyle.value()
        }, id = "close-button-${uniqueId()}", prefix = closeButtonPrefix) {
            variant { ghost }
            icon { def(closeButtonIcon.value(Theme().icons)) }
        }
    }
}
