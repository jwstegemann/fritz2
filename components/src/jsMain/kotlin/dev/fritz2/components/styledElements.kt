package dev.fritz2.components

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.StyleClass.Companion.plus
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.StyleParamsImpl


/**
 * Typealias for the common function signature of [RenderContext. methods.
 * It is used for defining a generic extension method for styling basic HTML elements.
 *
 * @see BasicComponent.styled
 */
typealias BasicComponent<E> = (String?, String?, E.() -> Unit) -> E

/**
 * Extension method that enables the usage of fritz2's powerful styling DSL for basic HTML elements.
 * This method is used for *unstyled* HTML elements.
 *
 * The syntax required to apply this function might look a bit awkward at first, but easy to grasp once understood:
 * This is caused by the fact, that this function *extends* another function and we must provide a _reference_ to the
 * latter. Therefore we must create a [function reference](https://kotlinlang.org/docs/reference/lambdas.html#instantiating-a-function-type)
 * first. In order to end the scope of the extension function call, the whole expression must be enclosed with brackets.
 *
 * Example
 * ```
 * ( ::p.styled { color { "red" } } ) { +"I will be rendered in red" }
 * ^ ^^         ^^^^^^^^^^^^^^^^^^^ ^ ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * | |          use the ``styling`` | provide the parameters to ``p``,
 * | |          param to describe   | often the ``content`` like in
 * | |          the styling         | this example
 * | |                              |
 * | +-create a function reference  |
 * |   to ``p`` and call this       |
 * |   ``styled`` extension function|
 * |                                |
 * |+- limit the scope -------------+
 * ```
 *
 * Of course you can provide all the other parameters too:
 * ```
 * (::div.styled(
 *     baseClass = StyleClass("my-style"),
 *     id = "main-content",
 *     prefix = "another-style",
 * ) {/* styling parameter */
 *     border {
 *         color { dark }
 *         style { solid }
 *         width { "5px" }
 *     }
 *     margin { huge }
 * }) { /* content parameter of ``RenderContext.div``-method */
 *     h1 { +"Some heading" }
 *     p { +"Some content" }
 * }
 * ```
 *
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 */
fun <E> BasicComponent<E>.styled(
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "css",
    styling: BoxParams.() -> Unit
): RenderContext.(E.() -> Unit) -> E {
    val additionalClass = StyleParamsImpl().apply(styling).cssClasses(prefix)
    return { init ->
        this@styled("${baseClass?.name.orEmpty()} ${additionalClass?.name.orEmpty()}", id, init)
    }
}

/**
 * Extension method that enables the usage of fritz2's powerful styling DSL for basic HTML elements.
 * This method is used for HTML elements that should apply an additional dynamic styling provided as
 * external parameter. This is needed and extremely useful for implementing _components_!
 *
 * A component should behave in a similar way as a basic HTML element. So like the basic [BasicComponent.styled]
 * version you should be able to call a component like this:
 * ```
 * myComponent({ /* styling */}) { /* content and events */}
 * ```
 * In order to provide this syntax, the CSS DSl expression must be passed into the component.
 * If the component consists of an HTML element (at least at the top level), the here provided variant of
 * a ``styled`` extension method is needed. With this function you can pass dynamic CSS into a basic HTML element:
 * ```
 * fun RenderContext.myComponent(
 *    // provide a styling DSL expression as parameter
 *    styling: BasicParams.() -> Unit = {},
 *     // just offer the "basic params" in order to simply pass them by
 *     baseClass: StyleClass? = null,
 *     id: String? = null,
 *     prefix: String = "my-red-link",
 *     // offer some extension method to setup the content or events of the inner HTML element
 *     init: Div.() -> Unit
 * ): Div =
 *     (::div.styled(styling, baseClass, id, prefix) {
 *     //     ^^^^^^ ^^^^^^^
 *     //     |      pass in a dynamic styling expression! (key difference to the basic ``styled`` variant!)
 *     //     |
 *     //     +- use *this* ``styled`` extension method
 *         border {
 *             color { dark }
 *             style { solid }
 *             width { thin }
 *         }
 *    })(init)
 * ```
 * This way we can use the component like this:
 * ```
 * myComponent({/* we can provide a styling - analog to the styling of a basic HTML element! */
 *     border {
 *         color { "purple" } // override the component's default
 *     }
 *     // and add some "ad hoc" styling
 *     background {
 *         color { "snow" }
 *     }
 * }) {
 *     h1 { +"My important content" }
 *     p { +"Some really important stuff..." }
 * }
 * ```
 * Pay attention to the *consistent* syntactic structure of a component compared to a styled basic HTML element
 * in fritz2! This is the core intention of our design choice, how to style elements!
 *
 * @param parentStyling a lambda expression for declaring the styling as fritz2's styling DSL; best passed into from
 *                      an outer source
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 */
fun <E> BasicComponent<E>.styled(
    parentStyling: BoxParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "css",
    styling: BoxParams.() -> Unit
): RenderContext.(E.() -> Unit) -> E {
    val additionalClass = StyleParamsImpl().apply {
        styling()
        parentStyling()
    }.cssClasses(prefix)
    return { init ->
        this@styled((baseClass + additionalClass).name, id, init)
    }
}

/**
 * WARNING: This will be removed for the 0.8 release
 *
 * Typealias for the extended function signatures of *some* [RenderContext. methods, that require an additional
 * parameter compared to [BasicComponent]
 * It is used for defining a generic extension method for styling corresponding basic HTML elements.
 *
 * @see BasicComponent.styled
 */
// TODO: will be removed with 0.8 because extra parameters like label's "for" will not be in signature any more
typealias ExtendedComponent<E> = (String?, String?, String?, E.() -> Unit) -> E

/**
 * WARNING: This will be removed for the 0.8 release
 *
 * Temporary variant of a styling extension function for basic HTML elements with *extended* parameter signature.
 * @see ExtendedComponent
 * @see BasicComponent.styled
 */
// TODO: will be removed with 0.8 because extra parameters like label's "for" will not be in signature any more
fun <E> ExtendedComponent<E>.styled(
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "css",
    extension: String? = null,
    styling: BoxParams.() -> Unit
): RenderContext.(E.() -> Unit) -> E {
    val additionalClass = StyleParamsImpl().apply(styling).cssClasses(prefix)
    return { init ->
        this@styled("${baseClass?.name.orEmpty()} ${additionalClass?.name.orEmpty()}", id, extension, init)
    }
}

/**
 * Typealias for the common function signature of s styled component method.
 * It is used for defining a generic extension method for styling _components_.
 *
 * @see BasicComponent.styled
 */
// TODO: Are we really gonna need this? You can apply the styling easily within the allready provided component parameter
// so this variant isn't really needed - and nobody has been used this besides the earliest exmaples of ``myRedLink``
// in the app.kt exmaples! Since then everyone used to "assemble" the added styling with the passed in styling by
// writing both into the styling parameter of the called component!
typealias StyledComponent<E> = (styling: BasicParams.() -> Unit, StyleClass?, String?, prefix: String, E.() -> Unit) -> E

fun <E> StyledComponent<E>.styled(
    parentStyling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "css",
    styling: BasicParams.() -> Unit
): RenderContext.(E.() -> Unit) -> E {
    val additionalClass = StyleParamsImpl().apply(parentStyling).cssClasses(prefix)
    return { init ->
        this@styled(styling, baseClass + additionalClass, id, prefix, init)
    }
}




