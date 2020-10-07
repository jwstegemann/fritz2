package dev.fritz2.styling.params

import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
interface Pseudos : StyleParams {
    private inline fun pseudo(key: String, content: BasicStyleParams.() -> Unit) {
        StyleParamsImpl(theme()).let { base ->
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

    private inline fun pseudo(key: String, content: BasicStyleParams.() -> Unit, parameter: Property = "") {
        StyleParamsImpl(theme()).let { base ->
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

    fun active(content: BasicStyleParams.() -> Unit) = pseudo("active", content)
    fun checked(content: BasicStyleParams.() -> Unit) = pseudo("checked", content)
    fun default(content: BasicStyleParams.() -> Unit) = pseudo("default", content)
    fun disabled(content: BasicStyleParams.() -> Unit) = pseudo("disabled", content)
    fun empty(content: BasicStyleParams.() -> Unit) = pseudo("empty", content)
    fun enabled(content: BasicStyleParams.() -> Unit) = pseudo("enabled", content)
    fun first(content: BasicStyleParams.() -> Unit) = pseudo("first", content)
    fun firstChild(content: BasicStyleParams.() -> Unit) = pseudo("first-child", content)
    fun firstOfType(content: BasicStyleParams.() -> Unit) = pseudo("first-of-type", content)
    fun fullscreen(content: BasicStyleParams.() -> Unit) = pseudo("fullscreen", content)
    fun focus(content: BasicStyleParams.() -> Unit) = pseudo("focus", content)
    fun hover(content: BasicStyleParams.() -> Unit) = pseudo("hover", content)
    fun indeterminate(content: BasicStyleParams.() -> Unit) = pseudo("indeterminate", content)
    fun inRange(content: BasicStyleParams.() -> Unit) = pseudo("in-range", content)
    fun invalid(content: BasicStyleParams.() -> Unit) = pseudo("invalid", content)
    fun lastChild(content: BasicStyleParams.() -> Unit) = pseudo("last-child", content)
    fun lastOfType(content: BasicStyleParams.() -> Unit) = pseudo("last-of-type", content)
    fun left(content: BasicStyleParams.() -> Unit) = pseudo("left", content)
    fun link(content: BasicStyleParams.() -> Unit) = pseudo("link", content)
    fun onlyChild(content: BasicStyleParams.() -> Unit) = pseudo("only-child", content)
    fun onlyOfType(content: BasicStyleParams.() -> Unit) = pseudo("only-of-type", content)
    fun optional(content: BasicStyleParams.() -> Unit) = pseudo("optional", content)
    fun outOfRange(content: BasicStyleParams.() -> Unit) = pseudo("out-of-range", content)
    fun readOnly(content: BasicStyleParams.() -> Unit) = pseudo("read-only", content)
    fun readWrite(content: BasicStyleParams.() -> Unit) = pseudo("read-write", content)
    fun required(content: BasicStyleParams.() -> Unit) = pseudo("required", content)
    fun right(content: BasicStyleParams.() -> Unit) = pseudo("right", content)
    fun root(content: BasicStyleParams.() -> Unit) = pseudo("root", content)
    fun scope(content: BasicStyleParams.() -> Unit) = pseudo("scope", content)
    fun target(content: BasicStyleParams.() -> Unit) = pseudo("target", content)
    fun valid(content: BasicStyleParams.() -> Unit) = pseudo("valid", content)
    fun visited(content: BasicStyleParams.() -> Unit) = pseudo("visited", content)

    fun dir(param: Property, content: BasicStyleParams.() -> Unit) = pseudo("dir", content, param)
    fun lang(param: Property, content: BasicStyleParams.() -> Unit) = pseudo("lang", content, param)
    fun not(param: Property, content: BasicStyleParams.() -> Unit) = pseudo("not", content, param)
    fun nthChild(param: Property, content: BasicStyleParams.() -> Unit) = pseudo("nth-child", content, param)
    fun nthLastChild(param: Property, content: BasicStyleParams.() -> Unit) = pseudo("nth-last-child", content, param)
    fun nthLastOfType(param: Property, content: BasicStyleParams.() -> Unit) =
        pseudo("nth-last-of-type", content, param)

    fun nthOfType(param: Property, content: BasicStyleParams.() -> Unit) = pseudo("nth-of-type", content, param)
}