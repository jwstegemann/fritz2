package dev.fritz2.components

import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.StyleClass.Companion.plus
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.StyleParamsImpl


typealias BasicComponent<E> = (String?, String?, E.() -> Unit) -> E

typealias StyledComponent<E> = (styling: BasicParams.() -> Unit, StyleClass?, String?, prefix: String, E.() -> Unit) -> E

fun <E> BasicComponent<E>.styled(
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "css",
    styling: BoxParams.() -> Unit
): HtmlElements.(E.() -> Unit) -> E {
    val additionalClass = StyleParamsImpl().apply(styling).cssClasses(prefix)
    return { init ->
        this@styled("${baseClass?.name.orEmpty()} ${additionalClass?.name.orEmpty()}", id, init)
    }
}


fun <E> BasicComponent<E>.styled(
    parentStyling: BoxParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "css",
    styling: BoxParams.() -> Unit
): HtmlElements.(E.() -> Unit) -> E {
    val additionalClass = StyleParamsImpl().apply {
        styling()
        parentStyling()
    }.cssClasses(prefix)
    return { init ->
        this@styled((baseClass+ additionalClass).name, id, init)
    }
}


fun <E> StyledComponent<E>.styled(
    parentStyling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "css",
    styling: BasicParams.() -> Unit
): HtmlElements.(E.() -> Unit) -> E {
    val additionalClass = StyleParamsImpl().apply(parentStyling).cssClasses(prefix)
    return { init ->
        this@styled(styling, baseClass + additionalClass, id, prefix, init)
    }
}




