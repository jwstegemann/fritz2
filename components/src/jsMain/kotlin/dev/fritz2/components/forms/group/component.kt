package dev.fritz2.components.forms.group

import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.components.formGroup
import dev.fritz2.components.forms.formGroupElementContainerMarker
import dev.fritz2.components.forms.formGroupElementLabelMarker
import dev.fritz2.components.forms.formGroupElementLegendMarker
import dev.fritz2.components.gridBox
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.DisplayProperty
import dev.fritz2.styling.params.DisplayValues
import dev.fritz2.styling.params.GridTemplateContext
import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.Theme

/**
 * This component class manages the configuration of a [formGroup] and does the rendering.
 *
 * For details of the usage see its factory function [formGroup].
 *
 * @see formGroup
 */
open class FormGroupComponent : Component<Unit> {

    companion object {
        const val sm = "sm"
        const val md = "md"
        const val lg = "lg"
        const val xl = "xl"
    }

    val items = ComponentProperty<RenderContext.() -> Unit> {}

    enum class LabelPlacement {
        TOP, LEFT
    }

    object LabelPlacementContext {
        val top = LabelPlacement.TOP
        val left = LabelPlacement.LEFT
    }

    class LabelContext {
        val placement = ComponentProperty<LabelPlacementContext.() -> LabelPlacement> { top }
        val width = ComponentProperty("auto")
    }

    fun labels(expression: LabelContext.() -> Unit) {
        labelValues[sm] = LabelContext().apply(expression)
    }

    fun labels(
        sm: (LabelContext.() -> Unit)? = null,
        md: (LabelContext.() -> Unit)? = null,
        lg: (LabelContext.() -> Unit)? = null,
        xl: (LabelContext.() -> Unit)? = null,
    ) {
        if (sm != null) labelValues[FormGroupComponent.sm] = LabelContext().apply(sm)
        if (md != null) labelValues[FormGroupComponent.md] = LabelContext().apply(md)
        if (lg != null) labelValues[FormGroupComponent.lg] = LabelContext().apply(lg)
        if (xl != null) labelValues[FormGroupComponent.xl] = LabelContext().apply(xl)
    }

    protected val labelValues = mutableMapOf<String, LabelContext?>(
        sm to LabelContext().apply {
            placement {
                when (Theme().formGroup.label.placement.lowercase()) {
                    "left" -> left
                    else -> top
                }
            }
        }
    )

    protected fun cssLabelColumnWidth(breakpoint: String): (GridTemplateContext.() -> Property)? {
        return if (labelValues[breakpoint] != null) {
            when (labelValues[breakpoint]!!.placement.value(LabelPlacementContext)) {
                LabelPlacement.LEFT -> {
                    { "${this@FormGroupComponent.labelValues[breakpoint]!!.width.value} 1fr" }
                }
                LabelPlacement.TOP -> null
            }
        } else null
    }

    protected fun cssDisplayValue(breakpoint: String): (DisplayValues.() -> DisplayProperty)? {
        val context = this@FormGroupComponent.labelValues[breakpoint]
        return if (context != null && context.placement.value(LabelPlacementContext) == LabelPlacement.LEFT) {
            { contents }
        } else null
    }

    protected fun placementMappingForLabelAlignment() = labelValues.map { (key, value) ->
        key to if (value != null
            && value.placement.value(LabelPlacementContext) == LabelPlacement.LEFT
        ) "left" else "top"
    }.toMap()


    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            gridBox({
                columns(
                    sm = this@FormGroupComponent.cssLabelColumnWidth(sm),
                    md = this@FormGroupComponent.cssLabelColumnWidth(md),
                    lg = this@FormGroupComponent.cssLabelColumnWidth(lg),
                    xl = this@FormGroupComponent.cssLabelColumnWidth(xl),
                )
                children(".$formGroupElementContainerMarker") {
                    display(
                        sm = this@FormGroupComponent.cssDisplayValue(sm),
                        md = this@FormGroupComponent.cssDisplayValue(md),
                        lg = this@FormGroupComponent.cssDisplayValue(lg),
                        xl = this@FormGroupComponent.cssDisplayValue(xl)
                    )
                }
                Theme().formGroup.base()
                children(".$formGroupElementLabelMarker") {
                    Theme().formGroup.label.alignmentLabel.invoke(
                        this,
                        this@FormGroupComponent.placementMappingForLabelAlignment()
                    )
                }
                children(".$formGroupElementLegendMarker") {
                    Theme().formGroup.label.alignmentLegend.invoke(
                        this,
                        this@FormGroupComponent.placementMappingForLabelAlignment()
                    )
                }
                styling(this as BoxParams)
            }, baseClass, id, prefix) {
                this@FormGroupComponent.items.value(this)
            }
        }
    }
}
