package dev.fritz2.styling

import dev.fritz2.styling.hash.v3
import dev.fritz2.styling.stylis.compile
import dev.fritz2.styling.stylis.middleware
import dev.fritz2.styling.stylis.serialize
import dev.fritz2.styling.stylis.stringify
import kotlinx.browser.document
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.css.CSSStyleSheet

internal object Styling {
    private var counter = 0

    val rules = mutableSetOf<Int>()

    private val sheet by lazy {
        val style = document.createElement("style") as HTMLStyleElement
        // WebKit hack
        style.appendChild(document.createTextNode(""))
        document.head!!.appendChild(style)
        style.sheet!! as CSSStyleSheet
    }

    private val addRuleMiddleware: (dynamic) -> dynamic = { value ->
        try {
            if (value.root == null)
                with(value["return"] as String?) {
                    if (this != null && this.isNotBlank()) sheet.insertRule(this as String, counter++)
                }
        } catch (e: Throwable) {
            console.error("unable to insert rule in stylesheet: ${e.message}", e)
            counter--
        }
        undefined
    }

    val middleware = middleware(arrayOf(::stringify, addRuleMiddleware))
}

/**
 * alias for CSS class names
 */
inline class StyleClass(val name: String) {
    companion object {
        val None = StyleClass("")

        infix operator fun StyleClass?.plus(other: StyleClass) = StyleClass(this?.name.orEmpty() + " " + other.name)

        infix operator fun StyleClass?.plus(other: StyleClass?) =
            StyleClass(this?.name.orEmpty() + " " + other?.name.orEmpty())
    }

    infix operator fun plus(other: StyleClass) = StyleClass(this.name + " " + other.name)

    infix operator fun plus(other: StyleClass?) = StyleClass(this.name + " " + other?.name.orEmpty())
}


/**
 * const for no style class
 */
//const val NoStyle: StyleClass = ""

/**
 * adds a static css-class to your app's dynamic style sheet.
 *
 * @param name of the class to create
 * @param css well formed content of the css-rule to add
 * @return the name of the created class
 */
fun staticStyle(name: String, css: String): StyleClass {
    ".$name { $css }".let {
        serialize(compile(it), Styling.middleware)
    }
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
    val hash = v3(css)
    return StyleClass("$prefix-${generateAlphabeticName(hash)}".also {
        if (!Styling.rules.contains(hash)) staticStyle(it, css)
        Styling.rules.add(hash)
    })
}

//FIXME: change return to Flow<StyleClass>
//FIXME: use StyleClass in fritz2-functions
//FIXME: offer + for concatenating StyleClass Flows
/**
 * function to apply a given class only when a condition is fullfiled.
 *
 * @receiver css class to apply
 * @param upstream [Flow] that holds the value to check
 * @param mapper defining the rule, when to apply the class
 * @return [Flow] containing the class name if check returns true or nothing
 */
inline fun <T> StyleClass.whenever(upstream: Flow<T>, crossinline mapper: suspend (T) -> Boolean): Flow<String> =
    upstream.map { value ->
        if (mapper(value)) this.name else StyleClass.None.name
    }


