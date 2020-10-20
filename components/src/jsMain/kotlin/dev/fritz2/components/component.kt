package dev.fritz2.components

import dev.fritz2.dom.Tag
import dev.fritz2.styling.StyleClass.Companion.None
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.style
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Element

open class BaseComponent(val prefix: String) : BoxParams {
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

    val cssClasses: StyleClass?
        get() = (toCss().let { if (it.isNotEmpty()) style(it, prefix) else None })
}

interface Application<T> {
    var application: (T.() -> Unit)?

    fun hugo(application: T.() -> Unit) {
        this.application = application
    }

    fun use(receiver: T, vararg params: (T.() -> Unit)?) {
        params.forEach {
            it?.let { receiver.it() }
        }
    }

}

class ApplicationDelegate<T>(override var application: (T.() -> Unit)? = null) : Application<T>

class SimpleDelegate<T>(private val attribute: String) {
    var value: T? = null

    fun invoke(value: T) {
        this.value = value
    }
}


class StringAttributeDelegate<E : Element, T : Tag<E>>(private val attribute: String) {
    var value: (T.() -> Unit)? = null

    operator fun invoke(value: String) {
        this.value = { attr(attribute, value) }
    }

    operator fun invoke(value: Flow<String>) {
        this.value = { value.bindAttr(attribute) }
    }
}


