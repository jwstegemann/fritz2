package dev.fritz2.components

import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams


typealias BasicComponent<E> = (String?, String?, E.() -> Unit) -> E

typealias StyledComponent<E> = (styling: BasicParams.() -> Unit, String?, String?, prefix: String, E.() -> Unit) -> E

fun <E> BasicComponent<E>.styled(
    baseClass: String? = null,
    id: String? = null,
    prefix: String = "css",
    styling: BoxParams.() -> Unit
): HtmlElements.(E.() -> Unit) -> E {
    val additionalClass = BaseComponent(prefix).apply(styling).cssClasses
    return { init ->
        this@styled("${baseClass.orEmpty()} ${additionalClass.orEmpty()}", id, init)
    }
}


fun <E> BasicComponent<E>.styled(
    parentStyling: BoxParams.() -> Unit = {},
    baseClass: String? = null,
    id: String? = null,
    prefix: String = "css",
    styling: BoxParams.() -> Unit
): HtmlElements.(E.() -> Unit) -> E {
    val additionalClass = BaseComponent(prefix).apply {
        styling()
        parentStyling()
    }.cssClasses
    return { init ->
        this@styled("${baseClass.orEmpty()} ${additionalClass.orEmpty()}", id, init)
    }
}


fun <E> StyledComponent<E>.styled(
    parentStyling: BasicParams.() -> Unit = {},
    baseClass: String? = null,
    id: String? = null,
    prefix: String = "css",
    styling: BasicParams.() -> Unit
): HtmlElements.(E.() -> Unit) -> E {
    val additionalClass = BaseComponent(prefix).apply(parentStyling).cssClasses
    return { init ->
        this@styled(styling, "$baseClass $additionalClass", id, prefix, init)
    }
}




