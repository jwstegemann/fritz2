package dev.fritz2.styling.params

import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
interface Pseudos : StyleParams {
    private inline fun pseudo(key: String, content: BasicParams.() -> Unit) {
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

    private inline fun pseudo(key: String, content: BasicParams.() -> Unit, parameter: Property = "") {
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

    fun active(content: BasicParams.() -> Unit) = pseudo("active", content)
    fun before(content: BasicParams.() -> Unit) = pseudo("before", content)
    fun checked(content: BasicParams.() -> Unit) = pseudo("checked", content)
    fun default(content: BasicParams.() -> Unit) = pseudo("default", content)
    fun disabled(content: BasicParams.() -> Unit) = pseudo("disabled", content)
    fun empty(content: BasicParams.() -> Unit) = pseudo("empty", content)
    fun enabled(content: BasicParams.() -> Unit) = pseudo("enabled", content)
    fun first(content: BasicParams.() -> Unit) = pseudo("first", content)
    fun firstChild(content: BasicParams.() -> Unit) = pseudo("first-child", content)
    fun firstOfType(content: BasicParams.() -> Unit) = pseudo("first-of-type", content)
    fun fullscreen(content: BasicParams.() -> Unit) = pseudo("fullscreen", content)
    fun focus(content: BasicParams.() -> Unit) = pseudo("focus", content)
    fun hover(content: BasicParams.() -> Unit) = pseudo("hover", content)
    fun indeterminate(content: BasicParams.() -> Unit) = pseudo("indeterminate", content)
    fun inRange(content: BasicParams.() -> Unit) = pseudo("in-range", content)
    fun invalid(content: BasicParams.() -> Unit) = pseudo("invalid", content)
    fun lastChild(content: BasicParams.() -> Unit) = pseudo("last-child", content)
    fun lastOfType(content: BasicParams.() -> Unit) = pseudo("last-of-type", content)
    fun left(content: BasicParams.() -> Unit) = pseudo("left", content)
    fun link(content: BasicParams.() -> Unit) = pseudo("link", content)
    fun onlyChild(content: BasicParams.() -> Unit) = pseudo("only-child", content)
    fun onlyOfType(content: BasicParams.() -> Unit) = pseudo("only-of-type", content)
    fun optional(content: BasicParams.() -> Unit) = pseudo("optional", content)
    fun outOfRange(content: BasicParams.() -> Unit) = pseudo("out-of-range", content)
    fun readOnly(content: BasicParams.() -> Unit) = pseudo("read-only", content)
    fun readWrite(content: BasicParams.() -> Unit) = pseudo("read-write", content)
    fun required(content: BasicParams.() -> Unit) = pseudo("required", content)
    fun right(content: BasicParams.() -> Unit) = pseudo("right", content)
    fun root(content: BasicParams.() -> Unit) = pseudo("root", content)
    fun scope(content: BasicParams.() -> Unit) = pseudo("scope", content)
    fun target(content: BasicParams.() -> Unit) = pseudo("target", content)
    fun valid(content: BasicParams.() -> Unit) = pseudo("valid", content)
    fun visited(content: BasicParams.() -> Unit) = pseudo("visited", content)

    fun dir(param: Property, content: BasicParams.() -> Unit) = pseudo("dir", content, param)
    fun lang(param: Property, content: BasicParams.() -> Unit) = pseudo("lang", content, param)
    fun not(param: Property, content: BasicParams.() -> Unit) = pseudo("not", content, param)
    fun nthChild(param: Property, content: BasicParams.() -> Unit) = pseudo("nth-child", content, param)
    fun nthLastChild(param: Property, content: BasicParams.() -> Unit) = pseudo("nth-last-child", content, param)
    fun nthLastOfType(param: Property, content: BasicParams.() -> Unit) =
        pseudo("nth-last-of-type", content, param)

    fun nthOfType(param: Property, content: BasicParams.() -> Unit) = pseudo("nth-of-type", content, param)

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