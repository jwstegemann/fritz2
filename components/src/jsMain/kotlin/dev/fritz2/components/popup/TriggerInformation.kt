package dev.fritz2.components.popup

import org.w3c.dom.DOMRect

/**
 * Foundation class of the popper data handling
 *
 * @property id
 * @property active the actual state of `content`
 * @property domRect the size and position of `trigger`
 *
 * @see PopupComponent
 */
data class TriggerInformation(
    val id: String = "",
    val active: Boolean = false,
    val domRect: DOMRect = DOMRect()
)