package dev.fritz2.components

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import kotlinx.browser.document
import kotlinx.coroutines.Job
import org.w3c.dom.HTMLElement
import kotlin.js.Date
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Gives a global [RenderContext] which is appended to the html body element.
 * @param id for [RenderContext] DOM element
 * @param job [Job] used in this [RenderContext]
 */
internal fun globalRenderContext(id: String, job: Job): RenderContext {
    val element = document.getElementById(id)
    return if (element != null) {
        Tag("div", element.id, job = job, domNode = (element as HTMLElement))
    } else {
        Div(id, job = job).apply { document.body?.appendChild(this.domNode) }
    }.apply {
        domNode.innerHTML = ""
    }
}

/**
 * Creates a random id based on random number and timestamp.
 */
fun randomId(): String {
    val random = Random.nextUInt(10000U, 99999U).toString()
    val time = Date().getMilliseconds().toString()
    return "$random${time.substring(time.length - 3)}"
}