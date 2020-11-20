package dev.fritz2.styling.params

import dev.fritz2.styling.theme.Property
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
interface PseudoElements : StyleParams {
    private inline fun pseudoElement(key: String, content: BasicParams.() -> Unit) {
        StyleParamsImpl().let { base ->
            base.content()
            smProperties.append(
                " &::",
                key,
                "{",
                base.toCss(),
                "} "
            )
        }
    }

    private inline fun pseudoElement(key: String, content: BasicParams.() -> Unit, parameter: Property = "") {
        StyleParamsImpl().let { base ->
            base.content()
            smProperties.append(
                " &::",
                key,
                "($parameter)",
                "{",
                base.toCss(),
                "} "
            )
        }
    }

    fun after(content: BasicParams.() -> Unit) = pseudoElement("after", content)
    fun before(content: BasicParams.() -> Unit) = pseudoElement("before", content)
    fun firstLetter(content: BasicParams.() -> Unit) = pseudoElement("first-letter", content)
    fun firstLine(content: BasicParams.() -> Unit) = pseudoElement("first-line", content)
    fun grammarError(content: BasicParams.() -> Unit) = pseudoElement("grammar-error", content)
    fun marker(content: BasicParams.() -> Unit) = pseudoElement("marker", content)
    fun selection(content: BasicParams.() -> Unit) = pseudoElement("selection", content)
    fun spellingError(content: BasicParams.() -> Unit) = pseudoElement("spelling-error", content)
}

@ExperimentalCoroutinesApi
interface PseudoClasses : StyleParams {
    private inline fun pseudoClass(key: String, content: BasicParams.() -> Unit) {
        StyleParamsImpl().let { base ->
            base.content()
            smProperties.append(
                " &:",
                key,
                "{",
                base.toCss(),
                "} "
            )
        }
    }

    private inline fun pseudoClass(key: String, content: BasicParams.() -> Unit, parameter: Property = "") {
        StyleParamsImpl().let { base ->
            base.content()
            smProperties.append(
                " &:",
                key,
                "($parameter)",
                "{",
                base.toCss(),
                "} "
            )
        }
    }

    fun active(content: BasicParams.() -> Unit) = pseudoClass("active", content)
    fun anyLink(content: BasicParams.() -> Unit) = pseudoClass("any-link", content)
    fun blank(content: BasicParams.() -> Unit) = pseudoClass("blank", content)
    fun checked(content: BasicParams.() -> Unit) = pseudoClass("checked", content)
    fun current(content: BasicParams.() -> Unit) = pseudoClass("current", content)
    fun default(content: BasicParams.() -> Unit) = pseudoClass("default", content)
    fun disabled(content: BasicParams.() -> Unit) = pseudoClass("disabled", content)
    fun empty(content: BasicParams.() -> Unit) = pseudoClass("empty", content)
    fun enabled(content: BasicParams.() -> Unit) = pseudoClass("enabled", content)
    fun first(content: BasicParams.() -> Unit) = pseudoClass("first", content)
    fun firstChild(content: BasicParams.() -> Unit) = pseudoClass("first-child", content)
    fun firstOfType(content: BasicParams.() -> Unit) = pseudoClass("first-of-type", content)
    fun fullscreen(content: BasicParams.() -> Unit) = pseudoClass("fullscreen", content)
    fun focus(content: BasicParams.() -> Unit) = pseudoClass("focus", content)
    fun focusVisible(content: BasicParams.() -> Unit) = pseudoClass("focus-visible", content)
    fun focusWithin(content: BasicParams.() -> Unit) = pseudoClass("focus-within", content)
    fun future(content: BasicParams.() -> Unit) = pseudoClass("future", content)
    fun hover(content: BasicParams.() -> Unit) = pseudoClass("hover", content)
    fun indeterminate(content: BasicParams.() -> Unit) = pseudoClass("indeterminate", content)
    fun inRange(content: BasicParams.() -> Unit) = pseudoClass("in-range", content)
    fun invalid(content: BasicParams.() -> Unit) = pseudoClass("invalid", content)
    fun lastChild(content: BasicParams.() -> Unit) = pseudoClass("last-child", content)
    fun lastOfType(content: BasicParams.() -> Unit) = pseudoClass("last-of-type", content)
    fun left(content: BasicParams.() -> Unit) = pseudoClass("left", content)
    fun link(content: BasicParams.() -> Unit) = pseudoClass("link", content)
    fun localLink(content: BasicParams.() -> Unit) = pseudoClass("local-link", content)
    fun onlyChild(content: BasicParams.() -> Unit) = pseudoClass("only-child", content)
    fun onlyOfType(content: BasicParams.() -> Unit) = pseudoClass("only-of-type", content)
    fun optional(content: BasicParams.() -> Unit) = pseudoClass("optional", content)
    fun outOfRange(content: BasicParams.() -> Unit) = pseudoClass("out-of-range", content)
    fun readOnly(content: BasicParams.() -> Unit) = pseudoClass("read-only", content)
    fun readWrite(content: BasicParams.() -> Unit) = pseudoClass("read-write", content)
    fun required(content: BasicParams.() -> Unit) = pseudoClass("required", content)
    fun right(content: BasicParams.() -> Unit) = pseudoClass("right", content)
    fun root(content: BasicParams.() -> Unit) = pseudoClass("root", content)
    fun scope(content: BasicParams.() -> Unit) = pseudoClass("scope", content)
    fun target(content: BasicParams.() -> Unit) = pseudoClass("target", content)
    fun valid(content: BasicParams.() -> Unit) = pseudoClass("valid", content)
    fun visited(content: BasicParams.() -> Unit) = pseudoClass("visited", content)

    fun dir(param: Property, content: BasicParams.() -> Unit) = pseudoClass("dir", content, param)
    fun lang(param: Property, content: BasicParams.() -> Unit) = pseudoClass("lang", content, param)
    fun not(param: Property, content: BasicParams.() -> Unit) = pseudoClass("not", content, param)
    fun nthChild(param: Property, content: BasicParams.() -> Unit) = pseudoClass("nth-child", content, param)
    fun nthLastChild(param: Property, content: BasicParams.() -> Unit) = pseudoClass("nth-last-child", content, param)
    fun nthLastOfType(param: Property, content: BasicParams.() -> Unit) =
        pseudoClass("nth-last-of-type", content, param)

    fun nthOfType(param: Property, content: BasicParams.() -> Unit) = pseudoClass("nth-of-type", content, param)

    fun children(selector: String, content: BasicParams.() -> Unit) {
        StyleParamsImpl().let { base ->
            base.content()
            smProperties.append(
                selector,
                "{",
                base.toCss(),
                "} "
            )
        }
    }

}