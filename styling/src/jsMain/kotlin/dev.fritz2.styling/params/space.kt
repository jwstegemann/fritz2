package dev.fritz2.styling.params

import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

internal const val marginKey = "margin: "
internal const val marginTopKey = "margin-top: "
internal const val marginRightKey = "margin-right: "
internal const val marginBottomKey = "margin-bottom: "
internal const val marginLeftKey = "margin-left: "

internal const val paddingKey = "padding: "
internal const val paddingTopKey = "padding-top: "
internal const val paddingRightKey = "padding-right: "
internal const val paddingBottomKey = "padding-bottom: "
internal const val paddingLeftKey = "padding-left: "


/**
 * This _context class_ enables the definition of the common space oriented (margin and padding) styling properties.
 *
 * This _context_ is passed as receiver either by [Space.margin] or [Space.padding] functions for setting the properties for all
 * sides at once or by [Space.margins] or [Space.paddings] functions for defining each side independently.
 *
 * This usage enable styling like this:
 * ```
 * margin { small }
 * ```
 *
 * or alternatively
 *
 * ```
 * margins {
 *     top { small }
 *     left { normal }
 * }
 * ```
 *
 * @param topKey the CSS-property for the _top_
 * @param leftKey the CSS-property for the left
 * @param bottomKey the CSS-property for the bottom
 * @param rightKey the CSS-property for the right
 * @param styleParams basic context scope interface
 * @param target the defined output [StringBuilder] to write the generated CSS into
 */
@ExperimentalCoroutinesApi
class SpacesContext(
    private val topKey: String,
    private val leftKey: String,
    private val bottomKey: String,
    private val rightKey: String,
    val styleParams: StyleParams,
    private val target: StringBuilder
) : StyleParams by styleParams {
    /**
     * This function is used to set the _top_ padding or margin
     * property according to the passed [SpacesContext.topKey] value like ``margin-top``.
     *
     * example call:
     * ```
     * margins {
     *     top { normal }
     * }
     * ```
     *
     * @param value extension function parameter to bring the predefined space values of the theme
     *              into the scope of the functional expression
     */
    fun top(value: ScaledValueProperty) = property(topKey, theme().space, value, target)

    /**
     * This function is used to set the _left_ padding or margin
     * property according to the passed [SpacesContext.leftKey] value like ``margin-left``.
     *
     * example call:
     * ```
     * margins {
     *     left { normal }
     * }
     * ```
     *
     * @param value extension function parameter to bring the predefined space values of the theme
     *              into the scope of the functional expression
     */
    fun left(value: ScaledValueProperty) = property(leftKey, theme().space, value, target)

    /**
     * This function is used to set the bottom padding or margin
     * property according to the passed [SpacesContext.bottomKey] value like ``padding-bottom``.
     *
     * example call:
     * ```
     * paddings {
     *     bottom { small }
     * }
     * ```
     *
     * @param value extension function parameter to bring the predefined space values of the theme
     *              into the scope of the functional expression
     */
    fun bottom(value: ScaledValueProperty) = property(bottomKey, theme().space, value, target)

    /**
     * This function is used to set the _right_ padding or margin
     * property according to the passed [SpacesContext.rightKey] value like ``padding-right``.
     *
     * example call:
     * ```
     * paddings {
     *     right { large }
     * }
     * ```
     *
     * @param value extension function parameter to bring the predefined space values of the theme
     *              into the scope of the functional expression
     */
    fun right(value: ScaledValueProperty) = property(rightKey, theme().space, value, target)

    /**
     * This function is used to set the _vertical_ padding or margin
     * according to the passed [SpacesContext.topKey] and [SpacesContext.bottomKey] value like ``margin-top`` and ``margin-bottom`.
     *
     * example call:
     * ```
     * margins {
     *     vertical { normal }
     * }
     * ```
     *
     * @param value extension function parameter to bring the predefined space values of the theme
     *              into the scope of the functional expression
     */
    fun vertical(value: ScaledValueProperty) {
        property(topKey, theme().space, value, target)
        property(bottomKey, theme().space, value, target)
    }

    /**
     * This function is used to set the _horizontal_ padding or margin
     * according to the passed [SpacesContext.leftKey] and [SpacesContext.rightKey] value like ``padding-left`` and ``padding-right`.
     *
     * example call:
     * ```
     * paddings {
     *     horizontal { small }
     * }
     * ```
     *
     * @param value extension function parameter to bring the predefined space values of the theme
     *              into the scope of the functional expression
     */
    fun horizontal(value: ScaledValueProperty) {
        property(leftKey, theme().space, value, target)
        property(rightKey, theme().space, value, target)
    }
}

/**
 * This _context_ interface offers functions to style the space related CSS properties of a component.
 *
 * It offers functions to define padding and margin properties.

 * There are overrides for all functions that enable one to define the styling for
 * the different media devices independently.
 */
@ExperimentalCoroutinesApi
interface Space : StyleParams {
    /**
     * This function sets the [margin](https://developer.mozilla.org/de/docs/Web/CSS/margin) property.
     *
     * Example call:
     * ```
     * margin { small }
     * ```
     *
     * @param value extension function parameter for small media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.spaces] that offer the properties of [ScaledValue]
     */
    fun margin(value: ScaledValueProperty) = property(marginKey, theme().space, value)

    /**
     * This function sets the [margin](https://developer.mozilla.org/de/docs/Web/CSS/margin) property
     * for each media device independently.
     *
     * Example call:
     * ```
     * margin(
     *     sm = { small }
     *     lg = { normal }
     * )
     * ```
     *
     * @param sm extension function parameter for small media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.spaces] that offer the properties of [ScaledValue]
     * @param md extension function parameter for medium sized media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.spaces] that offer the properties of [ScaledValue]
     * @param lg extension function parameter for large media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.spaces] that offer the properties of [ScaledValue]
     * @param xl extension function parameter for extra large media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.spaces] that offer the properties of [ScaledValue]
     */
    fun margin(
        sm: ScaledValueProperty? = null,
        md: ScaledValueProperty? = null,
        lg: ScaledValueProperty? = null,
        xl: ScaledValueProperty? = null
    ) =
        property(marginKey, theme().space, sm, md, lg, xl)

    /**
     * This function opens the context for defining the margin related properties.
     *
     * Example call:
     * ```
     * margins {
     *     top { small }
     *     left { small }
     * }
     * ```
     *
     * @param value extension function parameter to bring the specialized topical functions of the [SpacesContext]
     *           into the scope of the functional expression
     */
    fun margins(value: SpacesContext.() -> Unit) {
        SpacesContext(marginTopKey, marginLeftKey, marginBottomKey, marginRightKey, this, smProperties).value()
    }

    /**
     * This function opens the context for defining the margin related properties for each media device independently.
     *
     * Example call:
     * ```
     * margins(
     *     sm = {
     *         top { small }
     *         left { small }
     *     },
     *     lg = {
     *         top { large }
     *         left { large }
     *     }
     * )
     * ```
     *
     * @param sm extension function parameter to bring the specialized topical functions of the [SpacesContext]
     *           into the scope of the functional expression
     * @param md extension function parameter to bring the specialized topical functions of the [SpacesContext]
     *           into the scope of the functional expression
     * @param lg extension function parameter to bring the specialized topical functions of the [SpacesContext]
     *           into the scope of the functional expression
     * @param xl extension function parameter to bring the specialized topical functions of the [SpacesContext]
     *           into the scope of the functional expression
     */
    fun margins(
        sm: (SpacesContext.() -> Unit)? = null,
        md: (SpacesContext.() -> Unit)? = null,
        lg: (SpacesContext.() -> Unit)? = null,
        xl: (SpacesContext.() -> Unit)? = null
    ) {
        if (sm != null) SpacesContext(
            marginTopKey,
            marginLeftKey,
            marginBottomKey,
            marginRightKey,
            this,
            smProperties
        ).sm()
        if (md != null) SpacesContext(
            marginTopKey,
            marginLeftKey,
            marginBottomKey,
            marginRightKey,
            this,
            mdProperties
        ).md()
        if (lg != null) SpacesContext(
            marginTopKey,
            marginLeftKey,
            marginBottomKey,
            marginRightKey,
            this,
            lgProperties
        ).lg()
        if (xl != null) SpacesContext(
            marginTopKey,
            marginLeftKey,
            marginBottomKey,
            marginRightKey,
            this,
            xlProperties
        ).xl()
    }

    /**
     * This function sets the [padding](https://developer.mozilla.org/de/docs/Web/CSS/padding) property
     *
     * Example call:
     * ```
     * padding { small }
     * ```
     *
     * @param value extension function parameter for small media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.spaces] that offer the properties of [ScaledValue]
     */
    fun padding(value: ScaledValueProperty) = property(paddingKey, theme().space, value)

    /**
     * This function sets the [padding](https://developer.mozilla.org/de/docs/Web/CSS/padding) property
     * for each media device independently.
     *
     * Example call:
     * ```
     * padding(
     *     sm = { small }
     *     lg = { normal }
     * )
     * ```
     *
     * @param sm extension function parameter for small media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.spaces] that offer the properties of [ScaledValue]
     * @param md extension function parameter for medium sized media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.spaces] that offer the properties of [ScaledValue]
     * @param lg extension function parameter for large media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.spaces] that offer the properties of [ScaledValue]
     * @param xl extension function parameter for extra large media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.spaces] that offer the properties of [ScaledValue]
     */
    fun padding(
        sm: ScaledValueProperty? = null,
        md: ScaledValueProperty? = null,
        lg: ScaledValueProperty? = null,
        xl: ScaledValueProperty? = null
    ) =
        property(paddingKey, theme().space, sm, md, lg, xl)

    /**
     * This function opens the context for defining the padding related properties.
     *
     * Example call:
     * ```
     * paddings {
     *     top { small }
     *     left { small }
     * }
     * ```
     *
     * @param value extension function parameter to bring the specialized topical functions of the [SpacesContext]
     *           into the scope of the functional expression
     */
    fun paddings(value: SpacesContext.() -> Unit) {
        SpacesContext(paddingTopKey, paddingLeftKey, paddingBottomKey, paddingRightKey, this, smProperties).value()
    }

    /**
     * This function opens the context for defining the padding related properties for each media device independently.
     *
     * Example call:
     * ```
     * paddings(
     *     sm = {
     *         top { small }
     *         left { small }
     *     },
     *     lg = {
     *         top { large }
     *         left { large }
     *     }
     * )
     * ```
     *
     * @param sm extension function parameter to bring the specialized topical functions of the [SpacesContext]
     *           into the scope of the functional expression
     * @param md extension function parameter to bring the specialized topical functions of the [SpacesContext]
     *           into the scope of the functional expression
     * @param lg extension function parameter to bring the specialized topical functions of the [SpacesContext]
     *           into the scope of the functional expression
     * @param xl extension function parameter to bring the specialized topical functions of the [SpacesContext]
     *           into the scope of the functional expression
     */
    fun paddings(
        sm: (SpacesContext.() -> Unit)? = null,
        md: (SpacesContext.() -> Unit)? = null,
        lg: (SpacesContext.() -> Unit)? = null,
        xl: (SpacesContext.() -> Unit)? = null
    ) {
        if (sm != null) SpacesContext(
            paddingTopKey,
            paddingLeftKey,
            paddingBottomKey,
            paddingRightKey,
            this,
            smProperties
        ).sm()
        if (md != null) SpacesContext(
            paddingTopKey,
            paddingLeftKey,
            paddingBottomKey,
            paddingRightKey,
            this,
            mdProperties
        ).md()
        if (lg != null) SpacesContext(
            paddingTopKey,
            paddingLeftKey,
            paddingBottomKey,
            paddingRightKey,
            this,
            lgProperties
        ).lg()
        if (xl != null) SpacesContext(
            paddingTopKey,
            paddingLeftKey,
            paddingBottomKey,
            paddingRightKey,
            this,
            xlProperties
        ).xl()
    }
}
