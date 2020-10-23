package dev.fritz2.components

import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.FlexParams
import dev.fritz2.styling.params.GridParams


fun HtmlElements.box(
    styling: FlexParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "box",
    init: Div.() -> Unit
): Div =
    ::div.styled(styling, baseClass, id, prefix) {
    }(init)


fun HtmlElements.flexBox(
    styling: FlexParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "flex-box",
    init: Div.() -> Unit
): Div =
    ::div.styled(styling, baseClass, id, prefix) {
        css("display: flex;")
    }(init)


fun HtmlElements.gridBox(
    styling: GridParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "grid-box",
    init: Div.() -> Unit
): Div =
    ::div.styled(styling, baseClass, id, prefix) {
        css("display: grid;")
    }(init)

