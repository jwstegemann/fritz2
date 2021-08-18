package dev.fritz2.components.popup

import org.w3c.dom.HTMLDivElement

/**
 * Placement consists the [position] information and the `Placement` which to try on `flipping` mode
 * It will be used for the [PopupComponent.PlacementContext] and it's the basis of [Positioning]
 *
 * @property position calculate the default position of Placement
 * @property flips listOf alternative placements for `content`
 *
 * @see PopupComponent
 * @see Positioning
 */
sealed class Placement(
    val position: (trigger: TriggerInformation, element: HTMLDivElement, offset: Double) -> Pair<Double, Double>,
    val flips: List<Placement> = emptyList()
) {
    object Top : Placement(
        position = { triggerInformation, element, offset ->
            val left =
                PopupComponent.leftRenderPosition + triggerInformation.domRect.left + triggerInformation.domRect.width * .5 - element.offsetWidth * .5
            val top = triggerInformation.domRect.top - offset - element.offsetHeight
            left to top
        },
        flips = listOf(Bottom, Left, Right)
    )

    object TopStart : Placement(
        position = { triggerInformation, element, offset ->
            val left = PopupComponent.leftRenderPosition + triggerInformation.domRect.left
            val top = triggerInformation.domRect.top - offset - element.offsetHeight
            left to top
        },
        flips = listOf(BottomStart, LeftStart, RightStart, TopEnd, BottomEnd, LeftEnd, RightEnd)
    )

    object TopEnd : Placement(
        position = { triggerInformation, element, offset ->
            val left =
                PopupComponent.leftRenderPosition + triggerInformation.domRect.left + triggerInformation.domRect.width - element.offsetWidth
            val top = triggerInformation.domRect.top - offset - element.offsetHeight
            left to top
        },
        flips = listOf(BottomEnd, LeftEnd, RightEnd, TopStart, BottomStart, LeftStart, RightStart)
    )

    object Bottom : Placement(
        position = { triggerInformation, element, offset ->
            val left =
                PopupComponent.leftRenderPosition + triggerInformation.domRect.left + triggerInformation.domRect.width * .5 - element.offsetWidth * .5
            val top = triggerInformation.domRect.top + offset + triggerInformation.domRect.height
            left to top
        },
        flips = listOf(Top, Left, Right)
    )

    object BottomStart : Placement(
        position = { triggerInformation, element, offset ->
            val left = PopupComponent.leftRenderPosition + triggerInformation.domRect.left
            val top = triggerInformation.domRect.top + offset + triggerInformation.domRect.height
            left to top
        },
        flips = listOf(TopStart, LeftStart, RightStart, BottomEnd, TopEnd, LeftEnd, RightEnd)
    )

    object BottomEnd : Placement(
        position = { triggerInformation, element, offset ->
            val left =
                PopupComponent.leftRenderPosition + triggerInformation.domRect.left + triggerInformation.domRect.width - element.offsetWidth
            val top = triggerInformation.domRect.top + offset + triggerInformation.domRect.height
            left to top
        },
        flips = listOf(TopEnd, LeftEnd, RightEnd, TopStart, BottomStart, LeftStart, RightStart)
    )

    object Left : Placement(
        position = { triggerInformation, element, offset ->
            val left =
                PopupComponent.leftRenderPosition - offset + triggerInformation.domRect.left - element.offsetWidth
            val top =
                triggerInformation.domRect.top + triggerInformation.domRect.height * .5 - element.offsetHeight * .5
            left to top
        },
        flips = listOf(Right, Top, Bottom)
    )

    object LeftStart : Placement(
        position = { triggerInformation, element, offset ->
            val left =
                PopupComponent.leftRenderPosition - offset + triggerInformation.domRect.left - element.offsetWidth
            val top = triggerInformation.domRect.top
            left to top
        },
        flips = listOf(LeftEnd, Left, RightStart, RightEnd, Right, TopStart, BottomStart, Top, Bottom)
    )

    object LeftEnd : Placement(
        position = { triggerInformation, element, offset ->
            val left =
                PopupComponent.leftRenderPosition - offset + triggerInformation.domRect.left - element.offsetWidth
            val top = triggerInformation.domRect.top + triggerInformation.domRect.height - element.offsetHeight
            left to top
        },
        flips = listOf(LeftStart, Left, RightEnd, RightStart, Right, TopEnd, BottomEnd, Top, Bottom)
    )

    object Right : Placement(
        position = { triggerInformation, element, offset ->
            val left =
                PopupComponent.leftRenderPosition + offset + triggerInformation.domRect.left + triggerInformation.domRect.width
            val top =
                triggerInformation.domRect.top + triggerInformation.domRect.height * .5 - element.offsetHeight * .5
            left to top
        },
        flips = listOf(Left, Top, Bottom)
    )

    object RightStart : Placement(
        position = { triggerInformation, element, offset ->
            val left =
                PopupComponent.leftRenderPosition + offset + triggerInformation.domRect.left + triggerInformation.domRect.width
            val top = triggerInformation.domRect.top
            left to top
        },
        flips = listOf(RightEnd, LeftStart, LeftEnd, TopStart, BottomStart, TopEnd, BottomEnd)
    )

    object RightEnd : Placement(
        position = { triggerInformation, element, offset ->
            val left =
                PopupComponent.leftRenderPosition + offset + triggerInformation.domRect.left + triggerInformation.domRect.width
            val top = triggerInformation.domRect.top + triggerInformation.domRect.height - element.offsetHeight
            left to top
        },
        flips = listOf(RightStart, LeftEnd, LeftStart, TopEnd, BottomEnd, TopStart, BottomStart)
    )
}
