package dev.fritz2.styling

import dev.fritz2.dom.Tag
import dev.fritz2.styling.hash.v3
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.StyleParamsImpl
import dev.fritz2.styling.stylis.compile
import dev.fritz2.styling.stylis.middleware
import dev.fritz2.styling.stylis.serialize
import dev.fritz2.styling.stylis.stringify
import kotlinx.browser.document
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.Element
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.css.CSSStyleSheet

internal object Styling {
    class Sheet(val id: String) {
        var counter: Int = 0
        val styleSheet: CSSStyleSheet = create()
        val middleware = middleware(arrayOf(::stringify, addRuleMiddleware()))

        private fun addRuleMiddleware(): (dynamic) -> dynamic = { value ->
            try {
                if (value.root == null)
                    with(value["return"] as String?) {
                        if (this != null && this.isNotBlank()) styleSheet.insertRule(this, counter++)
                    }
            } catch (e: Throwable) {
                console.error("unable to insert rule in stylesheet: ${e.message}", e)
                counter--
            }
            undefined
        }


        private fun create(): CSSStyleSheet {
            val style = document.createElement("style") as HTMLStyleElement
            style.setAttribute("id", id)
            // WebKit hack
            style.appendChild(document.createTextNode(""))
            document.head!!.appendChild(style)
            return style.sheet!! as CSSStyleSheet
        }

        fun remove() {
            styleSheet.disabled = true
            document.getElementById(id)?.let {
                it.parentNode?.removeChild(it)
            }

        }
    }

    private const val dynamicStyleSheetId = "fritz2Dynamic"
    private const val staticStyleSheetId = "fritz2Static"

    private val rules = mutableSetOf<String>()

    private val staticSheet = Sheet(staticStyleSheetId)
    private var dynamicSheet = Sheet(dynamicStyleSheetId)

    fun resetCss(css: String) {
        dynamicSheet.remove()
        rules.clear()
        dynamicSheet = Sheet(dynamicStyleSheetId)
        addDynamicCss("reset", css)
    }

    fun addStaticCss(css: String) {
        serialize(compile(css), staticSheet.middleware)
    }

    fun addDynamicCss(key: String, css: String) {
        if (!rules.contains(key)) {
            serialize(compile(css), dynamicSheet.middleware)
            rules.add(key)
        }
    }
}

fun resetCss(css: String) {
    Styling.resetCss(css)
}

/**
 * Alias class for css classes
 */
inline class StyleClass(val name: String) {
    companion object {
        val None = StyleClass("")
    }

    infix operator fun plus(other: StyleClass): StyleClass {
        return when {
            this == None -> other
            other == None -> this
            else -> StyleClass("${this.name} ${other.name}")
        }
    }
}

fun <E : Element> Tag<E>.className(values: Flow<StyleClass>) {
    className(values.map { it.name })
}

fun <E : Element> Tag<E>.classList(values: Flow<List<StyleClass>>) {
    classList(values.map { it.map { styleClass -> styleClass.name } })
}

fun <E : Element> Tag<E>.classMap(values: Flow<Map<StyleClass, Boolean>>) {
    classMap(values.map { it.mapKeys { (k, _) -> k.name } })
}

/**
 * adds some static css to your app's dynamic style sheet.
 *
 * @param css well formed content of the css-rule to add
 */
fun staticStyle(css: String) {
    Styling.addStaticCss(css)
}

/**
 * adds a static css-class to your app's dynamic style sheet.
 *
 * @param name of the class to create
 * @param css well formed content of the css-rule to add
 * @return the name of the created class
 */
fun staticStyle(name: String, css: String): StyleClass {
    Styling.addStaticCss(".$name { $css }")
    return StyleClass(name)
}

/**
 * adds a static css-class to your app's dynamic style sheet.
 *
 * @param name of the class to create
 * @param styling styling DSL expression
 * @return the name of the created class
 */
fun staticStyle(name: String, styling: BoxParams.() -> Unit): StyleClass {
    val css = StyleParamsImpl().apply(styling).toCss()
    Styling.addStaticCss(".$name { $css }")
    return StyleClass(name)
}

/**
 * creates a dynamic css-class and add it to your app's dynamic style sheet.
 * To make the name unique a hash is calculated from your content. This hash is also used to make sure
 * that no two rules with identical content are created but the already existing class is used in this case.
 *
 * @param css well formed content of the css-rule to add
 * @param prefix that is added in front of the created class name
 * @return the name of the created class
 */
fun style(css: String, prefix: String = "s"): StyleClass {
    return if(css.isBlank()) StyleClass.None
    else {
        val hash = v3(css)
        StyleClass("$prefix-${generateAlphabeticName(hash)}").also {
            Styling.addDynamicCss(it.name, ".${it.name} { $css }")
        }
    }
}

/**
 * creates a dynamic css class and add it to your app's dynamic style sheet.
 * To make the name unique a hash is calculated from your content. This hash is also used to make sure
 * that no two rules with identical content are created but the already existing class is used in this case.
 *
 * @param prefix that is added in front of the created class name
 * @param styling styling DSL expression
 * @return the name of the created class
 */
fun style(prefix: String = "s", styling: BoxParams.() -> Unit): StyleClass {
    val css = StyleParamsImpl().apply(styling).toCss()
    return if(css.isNotBlank()) style(css, prefix) else StyleClass.None
}

/**
 * applies a given css class only when a condition is fulfilled.
 *
 * @receiver css class to apply
 * @param value [Flow] that holds the value to check
 * @param predicate defining the rule, when to apply the class
 * @return [Flow] containing the class name if check returns true or nothing
 */
inline fun <T> StyleClass.whenever(value: Flow<T>, crossinline predicate: suspend (T) -> Boolean): Flow<StyleClass> =
    value.map { if (predicate(it)) this else StyleClass.None }

/**
 * applies a given css class only when a condition is fulfilled.
 *
 * @receiver css class to apply
 * @param value [Flow] that holds the value to check
 * @param predicate defining the rule, when to apply the class
 * @return [Flow] containing the class name if check returns true or null
 */
inline fun <T> StyleClass.whenever(value: T, predicate: (T) -> Boolean): StyleClass =
    if (predicate(value)) this else StyleClass.None

/**
 * applies a given css class only when a condition is fulfilled.
 *
 * @receiver css class to apply
 * @param condition [Flow] that holds the value to check
 * @return [Flow] containing the class name if check returns true or null
 */
fun StyleClass.whenever(condition: Flow<Boolean>): Flow<StyleClass> =
    condition.map { value -> if (value) this else StyleClass.None }

/**
 * applies a given css class only when a condition is fulfilled.
 *
 * @receiver css class to apply
 * @param condition that holds the value to check
 * @return [StyleClass] containing the class name if check returns true or null
 */
fun StyleClass.whenever(condition: Boolean): StyleClass = if (condition) this else StyleClass.None

/**
 * use [name] on a [Flow] of [StyleClass] to get the css classname out of it.
 * If the [StyleClass] is null it returns a empty [String].
 */
val Flow<StyleClass>.name: Flow<String>
    get() = this.map { it.name }