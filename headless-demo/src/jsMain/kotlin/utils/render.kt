package utils

import dev.fritz2.dom.html.MountTargetNotFoundException
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.addGlobalStyles
import dev.fritz2.dom.html.render
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

val tailwindGlobalStyles = listOf(
    """
    .popper[data-popper-reference-hidden] {
        visibility: hidden;
        pointer-events: none;
    }""".trimIndent(),

    """.popper-arrow,
    .popper-arrow::before {
        position: absolute;
        width: 8px;
        height: 8px;
        background: inherit;
    }""".trimIndent(),

    """.popper-arrow {
        visibility: hidden;
    }""".trimIndent(),

    """.popper-arrow::before {
        visibility: visible;
        content: '';
        transform: rotate(45deg);
    }""".trimIndent(),

    """.popper[data-popper-placement^='top'] > .popper-arrow {
        bottom: -4px;
    }""".trimIndent(),

    """.popper[data-popper-placement^='bottom'] > .popper-arrow {
        top: -4px;
    }""".trimIndent(),

    """.popper[data-popper-placement^='left'] > .popper-arrow {
        right: -4px;
    }""".trimIndent(),

    """.popper[data-popper-placement^='right'] > .popper-arrow {
        left: -4px;
    }""".trimIndent(),

    """.popper[data-popper-placement='bottom'] > .transform {
        transform-origin: top;
    }""".trimIndent(),

    """.popper[data-popper-placement='bottom-start'] > .transform {
        transform-origin: top left;
    }""".trimIndent(),

    """.popper[data-popper-placement='bottom-right'] > .transform {
        transform-origin: top right;
    }""".trimIndent(),

    """.popper[data-popper-placement='top'] > .transform {
        transform-origin: bottom;
    }""".trimIndent(),

    """.popper[data-popper-placement='top-start'] > .transform {
        transform-origin: bottom left;
    }""".trimIndent(),

    """.popper[data-popper-placement='top-right'] > .transform {
        transform-origin: bottom right;
    }""".trimIndent(),

    """.popper[data-popper-placement='left'] > .transform {
        transform-origin: right;
    }""".trimIndent(),

    """.popper[data-popper-placement='left-start'] > .transform {
        transform-origin: top right;
    }""".trimIndent(),

    """.popper[data-popper-placement='left-end'] > .transform {
        transform-origin: bottom right;
    }""".trimIndent(),

    """.popper[data-popper-placement='right'] > .transform {
        transform-origin: left;
    }""".trimIndent(),

    """.popper[data-popper-placement='right-start'] > .transform {
        transform-origin: top left;
    }""".trimIndent(),

    """.popper[data-popper-placement='right-end'] > .transform {
        transform-origin: bottom left;
    }""".trimIndent()
)

/**
 * Creates a [RenderContext] suitable for tailwind styling and components for [Tag]s and
 * mounts it to a constant element in the static html file
 * which id matches the [selector].
 *
 * @param selector [query selector](https://developer.mozilla.org/en-US/docs/Web/API/Document/querySelector)
 * of the element to mount to
 * @param override if true all child elements are removed before rendering
 * @param css path to file containing the tailwind-css-directives
 * @param content [RenderContext] for rendering the data to the DOM
 * @throws MountTargetNotFoundException if target element with [selector] not found
 */
fun renderTailwind(
    selector: String,
    override: Boolean = true,
    content: RenderContext.() -> Unit
) {
    document.querySelector(selector)?.let { parentElement ->
        if (parentElement is HTMLElement) {
            renderTailwind(parentElement, override, content)
        } else MountTargetNotFoundException("element with id=$selector is not an HTMLElement")
    } ?: throw MountTargetNotFoundException("html document contains no element with id=$selector")
}

external fun require(module: String): dynamic

/**
 * Creates a [RenderContext] suitable for tailwind styling and components for [Tag]s and mounts it to a [targetElement].
 *
 * @param targetElement [HTMLElement] to mount to, default is *document.body*
 * @param override if true all child elements are removed before rendering
 * @param css path to file containing the tailwind-css-directives
 * @param content [RenderContext] for rendering the data to the DOM
 * @throws MountTargetNotFoundException if [targetElement] not found
 */
fun renderTailwind(
    targetElement: HTMLElement? = document.body,
    override: Boolean = true,
    content: RenderContext.() -> Unit
) {
    addGlobalStyles(tailwindGlobalStyles)
    render(targetElement, override, content)
}
