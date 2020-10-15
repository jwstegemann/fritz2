package dev.fritz2.components

import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.NoStyle
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.StyleParams
import dev.fritz2.styling.params.use
import dev.fritz2.styling.style
import dev.fritz2.styling.theme.theme

typealias Context<T> = T.() -> Unit

inline class Component<C>(private val factory: (C.() -> Unit) -> Unit) {
    fun apply(content: C.() -> Unit) = factory(content)
}

open class BasicComponentContext(val prefix: String) : BasicParams {
    override val smProperties = StringBuilder()
    override val mdProperties = StringBuilder()
    override val lgProperties = StringBuilder()
    override val xlProperties = StringBuilder()

    /**
     * creates a valid responsive css-rule-body from the content of the screen-size-[StringBuilder]s
     */
    fun toCss(): String {
        if (mdProperties.isNotEmpty()) smProperties.append(theme().mediaQueryMd, "{", mdProperties, "}")
        if (lgProperties.isNotEmpty()) smProperties.append(theme().mediaQueryLg, "{", lgProperties, "}")
        if (xlProperties.isNotEmpty()) smProperties.append(theme().mediaQueryXl, "{", xlProperties, "}")

        return smProperties.toString()
    }

    val cssClass: StyleClass
        get() = toCss().let { if (it.isNotEmpty()) style(it, prefix) else NoStyle }

    var baseClasses: String? = null

    fun classes(vararg styles: StyleClass) {
        baseClasses = styles.joinToString(" ")
    }

}
