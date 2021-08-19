package dev.fritz2.components.popup

import kotlinx.browser.window
import org.w3c.dom.HTMLDivElement

/**
 * This class helps to find the right positioning of the [PopupComponent.content]
 *
 * @property triggerInformation
 * @property element
 * @property offset
 * @property placement
 * @property flipping
 *
 * @see PopupComponent
 * @see TriggerInformation
 * @see Placement
 */
class Positioning(
    private val triggerInformation: TriggerInformation,
    private val element: HTMLDivElement,
    private val offset: Double,
    private val placement: Placement,
    private val flipping: Boolean = true
) {

    private val defaultPosition = placement.position(triggerInformation, element, offset)

    /**
     * Calculations of individual availability of placements
     */
    private val topAvailable =
        triggerInformation.domRect.top - offset - element.offsetHeight > 0
    private val bottomAvailable =
        triggerInformation.domRect.top + offset + element.offsetHeight - window.innerHeight < 0
    private val leftAvailable =
        triggerInformation.domRect.left - offset - element.offsetWidth > 0
    private val rightAvailable =
        triggerInformation.domRect.left + triggerInformation.domRect.width + offset + element.offsetWidth - window.innerWidth < 0
    private val horizontalStartAvailable =
        triggerInformation.domRect.left + element.offsetWidth - window.innerWidth < 0
    private val horizontalEndAvailable =
        triggerInformation.domRect.left + triggerInformation.domRect.width -  element.offsetWidth > 0
    private val verticalStartAvailable =
        triggerInformation.domRect.top + element.offsetHeight > 0
    private val verticalEndAvailable =
        triggerInformation.domRect.top + triggerInformation.domRect.height - element.offsetHeight > 0

    /**
     * Checks if space available for given [Placement]
     *
     * @param placement
     */
    private fun spaceAvailable(placement: Placement?): Boolean {
        return when (placement) {
            Placement.Top -> topAvailable
            Placement.TopStart -> topAvailable && horizontalStartAvailable
            Placement.TopEnd -> topAvailable && horizontalEndAvailable
            Placement.Bottom -> bottomAvailable
            Placement.BottomStart -> bottomAvailable && horizontalStartAvailable
            Placement.BottomEnd -> bottomAvailable && horizontalEndAvailable
            Placement.Left -> leftAvailable
            Placement.LeftStart -> leftAvailable && verticalStartAvailable
            Placement.LeftEnd -> leftAvailable && verticalEndAvailable
            Placement.Right -> rightAvailable
            Placement.RightStart -> rightAvailable && verticalStartAvailable
            Placement.RightEnd -> rightAvailable && verticalEndAvailable
            else -> true
        }
    }

    /**
     * Position(top/left) of [Placement]
     */
    val position: Pair<Double, Double>
        get() {
            return if (flipping) {
                if (spaceAvailable(this.placement)) {
                    defaultPosition
                } else {
                    this.placement.flips.firstOrNull { spaceAvailable(it) }
                        ?.let { it.position(triggerInformation, element, offset) } ?: defaultPosition
                }
            } else {
                defaultPosition
            }
        }


    /**
     * Inline style for the wrapper of [PopupComponent.content]
     */
    val inlineStyle = buildString {
        if (triggerInformation.active) {
            append(
                "transform: translate(${(position.first + window.scrollX).toInt()}px, " +
                        "${(position.second + window.scrollY).toInt()}px);"
            )
        }
    }
}