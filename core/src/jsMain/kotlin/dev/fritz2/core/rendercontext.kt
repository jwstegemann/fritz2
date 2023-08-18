@file:Suppress("unused")

package dev.fritz2.core

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.dom.clear
import org.w3c.dom.*

/**
 * Context for rendering static and dynamical content
 */
interface RenderContext : WithJob, WithScope {

    /**
     * Renders the data of a [Flow] as [Tag]s to the DOM.
     *
     * @receiver [Flow] containing the data
     * @param into target to mount content to. If not set a child div is added to the [Tag] this method is called on
     * @param content [RenderContext] for rendering the data to the DOM
     */
    fun <V> Flow<V>.render(into: Tag<HTMLElement>? = null, content: Tag<*>.(V) -> Unit) {
        val target = into?.apply(SET_MOUNT_POINT_DATA_ATTRIBUTE)
            ?: div(MOUNT_POINT_STYLE_CLASS, content = SET_MOUNT_POINT_DATA_ATTRIBUTE)

        val mountContext = MountContext(Job(job), target)

        mountSimple(job, this) {
            mountContext.job.cancelChildren()
            mountContext.runBeforeUnmounts()
            target.domNode.clear()
            content(mountContext, it)
            mountContext.runAfterMounts()
        }
    }

    /**
     * Renders each element of a [Flow]s content.
     * Internally the [Patch]es are determined using Myer's diff-algorithm.
     * This allows the detection of moves. Keep in mind, that no [Patch] is derived,
     * when an element stays the same, but changes its internal values.
     *
     * @param idProvider function to identify a unique entity in the list
     * @param into target to mount content to. If not set a child div is added to the [Tag] this method is called on
     * @param batch hide [into] while rendering patches. Useful to avoid flickering when you make many changes (like sorting)
     * @param content [RenderContext] for rendering the data to the DOM
     */
    fun <V> Flow<List<V>>.renderEach(
        idProvider: IdProvider<V, *>? = null,
        into: Tag<HTMLElement>? = null,
        batch: Boolean = false,
        content: RenderContext.(V) -> Tag<HTMLElement>,
    ) {
        mountPatches(into, this, batch) { upstreamValues, mountPoints ->
            upstreamValues.scan(Pair(emptyList(), emptyList())) { acc: Pair<List<V>, List<V>>, new ->
                Pair(acc.second, new)
            }.map { (old, new) ->
                Myer.diff(old, new, idProvider).map { patch ->
                    patch.map(job) { value, newJob ->
                        val mountPoint = BuildContext(newJob, scope)
                        content(mountPoint, value).also {
                            mountPoints[it.domNode] = mountPoint
                        }
                    }
                }
            }
        }
    }

    /**
     * Renders each element of a [Store]s [List] content.
     * Internally the [Patch]es are determined using Myer's diff-algorithm.
     * This allows the detection of moves. Keep in mind, that no [Patch] is derived,
     * when an element stays the same, but changes its internal values.
     *
     * @param idProvider function to identify a unique entity in the list
     * @param into target to mount content to. If not set a child div is added to the [Tag] this method is called on
     * @param content [RenderContext] for rendering the data to the DOM
     */
    fun <V> Store<List<V>>.renderEach(
        idProvider: IdProvider<V, *>,
        into: Tag<HTMLElement>? = null,
        content: RenderContext.(Store<V>) -> HtmlTag<HTMLElement>,
    ) {
        val store = this
        data.renderEach(idProvider, into) { value ->
            content(store.mapByElement(value, idProvider))
        }
    }

    /**
     * Converts the content of a [Flow] to [String] by using [toString] method.
     *
     * @receiver [Flow] with content
     * @return [Flow] with content as [String]
     */
    fun <T> Flow<T>.asString(): Flow<String> = this.map { it.toString() }

    fun <N : Node, W : WithDomNode<N>> register(element: W, content: (W) -> Unit): W

    /**
     * Evaluates the scope context and initializes a [ScopeContext]
     * for setting new entries to the scope.
     *
     * @param context to evaluate
     */
    private inline fun evalScope(context: (ScopeContext.() -> Unit)): Scope {
        return ScopeContext(this@RenderContext.scope).apply(context).scope
    }

    /**
     * Creates a custom [HtmlTag] with the provided [content].
     *
     * @param tagName Name of the [HtmlTag] in DOM
     * @param content content scope for inner [HtmlTag]s
     * @return custom [HtmlTag]
     */
    fun custom(
        tagName: String,
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<Element>.() -> Unit,
    ): HtmlTag<Element> =
        register(HtmlTag(tagName, id, baseClass, job, evalScope(scope)), content)

    fun a(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLAnchorElement>.() -> Unit,
    ): HtmlTag<HTMLAnchorElement> =
        register(HtmlTag("a", id, baseClass, job, evalScope(scope)), content)

    fun area(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLAreaElement>.() -> Unit,
    ): HtmlTag<HTMLAreaElement> =
        register(HtmlTag("area", id, baseClass, job, evalScope(scope)), content)

    fun br(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLBRElement>.() -> Unit,
    ): HtmlTag<HTMLBRElement> =
        register(HtmlTag("br", id, baseClass, job, evalScope(scope)), content)

    fun button(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLButtonElement>.() -> Unit,
    ): HtmlTag<HTMLButtonElement> =
        register(HtmlTag("button", id, baseClass, job, evalScope(scope)), content)

    fun canvas(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLCanvasElement>.() -> Unit,
    ): HtmlTag<HTMLCanvasElement> =
        register(HtmlTag("canvas", id, baseClass, job, evalScope(scope)), content)

    fun dl(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLDListElement>.() -> Unit,
    ): HtmlTag<HTMLDListElement> =
        register(HtmlTag("dl", id, baseClass, job, evalScope(scope)), content)

    fun dt(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLSpanElement>.() -> Unit,
    ): HtmlTag<HTMLSpanElement> =
        register(HtmlTag("dt", id, baseClass, job, evalScope(scope)), content)

    fun dd(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLSpanElement>.() -> Unit,
    ): HtmlTag<HTMLSpanElement> =
        register(HtmlTag("dd", id, baseClass, job, evalScope(scope)), content)

    fun data(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLDataElement>.() -> Unit,
    ): HtmlTag<HTMLDataElement> =
        register(HtmlTag("data", id, baseClass, job, evalScope(scope)), content)

    fun datalist(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLDataListElement>.() -> Unit,
    ): HtmlTag<HTMLDataListElement> =
        register(HtmlTag("datalist", id, baseClass, job, evalScope(scope)), content)

    fun details(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLDetailsElement>.() -> Unit,
    ): HtmlTag<HTMLDetailsElement> =
        register(HtmlTag("details", id, baseClass, job, evalScope(scope)), content)

    fun dialog(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLDialogElement>.() -> Unit,
    ): HtmlTag<HTMLDialogElement> =
        register(HtmlTag("dialog", id, baseClass, job, evalScope(scope)), content)

    fun div(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLDivElement>.() -> Unit,
    ): HtmlTag<HTMLDivElement> =
        register(HtmlTag("div", id, baseClass, job, evalScope(scope)), content)

    fun embed(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLEmbedElement>.() -> Unit,
    ): HtmlTag<HTMLEmbedElement> =
        register(HtmlTag("embed", id, baseClass, job, evalScope(scope)), content)

    fun fieldset(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLFieldSetElement>.() -> Unit,
    ): HtmlTag<HTMLFieldSetElement> =
        register(HtmlTag("fieldset", id, baseClass, job, evalScope(scope)), content)

    fun form(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLFormElement>.() -> Unit,
    ): HtmlTag<HTMLFormElement> =
        register(HtmlTag("form", id, baseClass, job, evalScope(scope)), content)

    fun hr(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLHRElement>.() -> Unit,
    ): HtmlTag<HTMLHRElement> =
        register(HtmlTag("hr", id, baseClass, job, evalScope(scope)), content)

    fun h1(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLHeadingElement>.() -> Unit,
    ): HtmlTag<HTMLHeadingElement> =
        register(HtmlTag("h1", id, baseClass, job, evalScope(scope)), content)

    fun h2(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLHeadingElement>.() -> Unit,
    ): HtmlTag<HTMLHeadingElement> =
        register(HtmlTag("h2", id, baseClass, job, evalScope(scope)), content)

    fun h3(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLHeadingElement>.() -> Unit,
    ): HtmlTag<HTMLHeadingElement> =
        register(HtmlTag("h3", id, baseClass, job, evalScope(scope)), content)

    fun h4(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLHeadingElement>.() -> Unit,
    ): HtmlTag<HTMLHeadingElement> =
        register(HtmlTag("h4", id, baseClass, job, evalScope(scope)), content)

    fun h5(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLHeadingElement>.() -> Unit,
    ): HtmlTag<HTMLHeadingElement> =
        register(HtmlTag("h5", id, baseClass, job, evalScope(scope)), content)

    fun h6(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLHeadingElement>.() -> Unit,
    ): HtmlTag<HTMLHeadingElement> =
        register(HtmlTag("h6", id, baseClass, job, evalScope(scope)), content)

    fun iframe(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLIFrameElement>.() -> Unit,
    ): HtmlTag<HTMLIFrameElement> =
        register(HtmlTag("iframe", id, baseClass, job, evalScope(scope)), content)

    fun img(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLImageElement>.() -> Unit,
    ): HtmlTag<HTMLImageElement> =
        register(HtmlTag("img", id, baseClass, job, evalScope(scope)), content)

    fun input(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLInputElement>.() -> Unit,
    ): HtmlTag<HTMLInputElement> =
        register(HtmlTag("input", id, baseClass, job, evalScope(scope)), content)

    fun li(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLLIElement>.() -> Unit,
    ): HtmlTag<HTMLLIElement> =
        register(HtmlTag("li", id, baseClass, job, evalScope(scope)), content)

    fun label(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLLabelElement>.() -> Unit,
    ): HtmlTag<HTMLLabelElement> =
        register(HtmlTag("label", id, baseClass, job, evalScope(scope)), content)

    fun legend(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLLegendElement>.() -> Unit,
    ): HtmlTag<HTMLLegendElement> =
        register(HtmlTag("legend", id, baseClass, job, evalScope(scope)), content)

    fun map(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLMapElement>.() -> Unit,
    ): HtmlTag<HTMLMapElement> =
        register(HtmlTag("map", id, baseClass, job, evalScope(scope)), content)

    fun audio(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLAudioElement>.() -> Unit,
    ): HtmlTag<HTMLAudioElement> =
        register(HtmlTag("audio", id, baseClass, job, evalScope(scope)), content)

    fun video(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLVideoElement>.() -> Unit,
    ): HtmlTag<HTMLVideoElement> =
        register(HtmlTag("video", id, baseClass, job, evalScope(scope)), content)

    fun meter(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLMeterElement>.() -> Unit,
    ): HtmlTag<HTMLMeterElement> =
        register(HtmlTag("meter", id, baseClass, job, evalScope(scope)), content)

    fun ins(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLModElement>.() -> Unit,
    ): HtmlTag<HTMLModElement> =
        register(HtmlTag("ins", id, baseClass, job, evalScope(scope)), content)

    fun del(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLModElement>.() -> Unit,
    ): HtmlTag<HTMLModElement> =
        register(HtmlTag("del", id, baseClass, job, evalScope(scope)), content)

    fun ol(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLOListElement>.() -> Unit,
    ): HtmlTag<HTMLOListElement> =
        register(HtmlTag("ol", id, baseClass, job, evalScope(scope)), content)

    fun `object`(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLObjectElement>.() -> Unit,
    ): HtmlTag<HTMLObjectElement> =
        register(HtmlTag("object", id, baseClass, job, evalScope(scope)), content)

    fun optgroup(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLOptGroupElement>.() -> Unit,
    ): HtmlTag<HTMLOptGroupElement> =
        register(HtmlTag("optgroup", id, baseClass, job, evalScope(scope)), content)

    fun option(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLOptionElement>.() -> Unit,
    ): HtmlTag<HTMLOptionElement> =
        register(HtmlTag("option", id, baseClass, job, evalScope(scope)), content)

    fun output(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLOutputElement>.() -> Unit,
    ): HtmlTag<HTMLOutputElement> =
        register(HtmlTag("output", id, baseClass, job, evalScope(scope)), content)

    fun p(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLParagraphElement>.() -> Unit,
    ): HtmlTag<HTMLParagraphElement> =
        register(HtmlTag("p", id, baseClass, job, evalScope(scope)), content)

    fun param(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLParamElement>.() -> Unit,
    ): HtmlTag<HTMLParamElement> =
        register(HtmlTag("param", id, baseClass, job, evalScope(scope)), content)

    fun picture(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLPictureElement>.() -> Unit,
    ): HtmlTag<HTMLPictureElement> =
        register(HtmlTag("picture", id, baseClass, job, evalScope(scope)), content)

    fun pre(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLPreElement>.() -> Unit,
    ): HtmlTag<HTMLPreElement> =
        register(HtmlTag("pre", id, baseClass, job, evalScope(scope)), content)

    fun progress(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLProgressElement>.() -> Unit,
    ): HtmlTag<HTMLProgressElement> =
        register(HtmlTag("progress", id, baseClass, job, evalScope(scope)), content)

    fun quote(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLQuoteElement>.() -> Unit,
    ): HtmlTag<HTMLQuoteElement> =
        register(HtmlTag("quote", id, baseClass, job, evalScope(scope)), content)

    fun script(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLScriptElement>.() -> Unit,
    ): HtmlTag<HTMLScriptElement> =
        register(HtmlTag("script", id, baseClass, job, evalScope(scope)), content)

    fun select(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLSelectElement>.() -> Unit,
    ): HtmlTag<HTMLSelectElement> =
        register(HtmlTag("select", id, baseClass, job, evalScope(scope)), content)

    fun span(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLSpanElement>.() -> Unit,
    ): HtmlTag<HTMLSpanElement> =
        register(HtmlTag("span", id, baseClass, job, evalScope(scope)), content)

    fun caption(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLTableCaptionElement>.() -> Unit,
    ): HtmlTag<HTMLTableCaptionElement> =
        register(HtmlTag("caption", id, baseClass, job, evalScope(scope)), content)

    fun th(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLTableCellElement>.() -> Unit,
    ): HtmlTag<HTMLTableCellElement> =
        register(HtmlTag("th", id, baseClass, job, evalScope(scope)), content)

    fun td(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLTableCellElement>.() -> Unit,
    ): HtmlTag<HTMLTableCellElement> =
        register(HtmlTag("td", id, baseClass, job, evalScope(scope)), content)

    fun col(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLTableColElement>.() -> Unit,
    ): HtmlTag<HTMLTableColElement> =
        register(HtmlTag("col", id, baseClass, job, evalScope(scope)), content)

    fun colgroup(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLTableColElement>.() -> Unit,
    ): HtmlTag<HTMLTableColElement> =
        register(HtmlTag("colgroup", id, baseClass, job, evalScope(scope)), content)

    fun table(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLTableElement>.() -> Unit,
    ): HtmlTag<HTMLTableElement> =
        register(HtmlTag("table", id, baseClass, job, evalScope(scope)), content)

    fun tr(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLTableRowElement>.() -> Unit,
    ): HtmlTag<HTMLTableRowElement> =
        register(HtmlTag("tr", id, baseClass, job, evalScope(scope)), content)

    fun tfoot(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLTableSectionElement>.() -> Unit,
    ): HtmlTag<HTMLTableSectionElement> =
        register(HtmlTag("tfoot", id, baseClass, job, evalScope(scope)), content)

    fun thead(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLTableSectionElement>.() -> Unit,
    ): HtmlTag<HTMLTableSectionElement> =
        register(HtmlTag("thead", id, baseClass, job, evalScope(scope)), content)

    fun tbody(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLTableSectionElement>.() -> Unit,
    ): HtmlTag<HTMLTableSectionElement> =
        register(HtmlTag("tbody", id, baseClass, job, evalScope(scope)), content)

    fun textarea(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLTextAreaElement>.() -> Unit,
    ): HtmlTag<HTMLTextAreaElement> =
        register(HtmlTag("textarea", id, baseClass, job, evalScope(scope)), content)

    fun time(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLTimeElement>.() -> Unit,
    ): HtmlTag<HTMLTimeElement> =
        register(HtmlTag("time", id, baseClass, job, evalScope(scope)), content)

    fun track(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLTrackElement>.() -> Unit,
    ): HtmlTag<HTMLTrackElement> =
        register(HtmlTag("track", id, baseClass, job, evalScope(scope)), content)

    fun ul(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLUListElement>.() -> Unit,
    ): HtmlTag<HTMLUListElement> =
        register(HtmlTag("ul", id, baseClass, job, evalScope(scope)), content)

    fun address(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("address", id, baseClass, job, evalScope(scope)), content)

    fun article(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("article", id, baseClass, job, evalScope(scope)), content)

    fun aside(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("aside", id, baseClass, job, evalScope(scope)), content)

    fun bdi(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("bdi", id, baseClass, job, evalScope(scope)), content)

    fun figcaption(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("figcaption", id, baseClass, job, evalScope(scope)), content)

    fun figure(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("figure", id, baseClass, job, evalScope(scope)), content)

    fun footer(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("footer", id, baseClass, job, evalScope(scope)), content)

    fun header(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("header", id, baseClass, job, evalScope(scope)), content)

    fun main(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("main", id, baseClass, job, evalScope(scope)), content)

    fun mark(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("mark", id, baseClass, job, evalScope(scope)), content)

    fun nav(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("nav", id, baseClass, job, evalScope(scope)), content)

    fun noscript(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("noscript", id, baseClass, job, evalScope(scope)), content)

    fun rp(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("rp", id, baseClass, job, evalScope(scope)), content)

    fun rt(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("rt", id, baseClass, job, evalScope(scope)), content)

    fun ruby(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("ruby", id, baseClass, job, evalScope(scope)), content)

    fun section(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("section", id, baseClass, job, evalScope(scope)), content)

    fun summary(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("summary", id, baseClass, job, evalScope(scope)), content)

    fun wbr(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("wbr", id, baseClass, job, evalScope(scope)), content)

    fun blockquote(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLQuoteElement>.() -> Unit,
    ): HtmlTag<HTMLQuoteElement> =
        register(HtmlTag("blockquote", id, baseClass, job, evalScope(scope)), content)

    fun em(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("em", id, baseClass, job, evalScope(scope)), content)

    fun strong(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("strong", id, baseClass, job, evalScope(scope)), content)

    fun s(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("s", id, baseClass, job, evalScope(scope)), content)

    fun cite(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("cite", id, baseClass, job, evalScope(scope)), content)

    fun q(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLQuoteElement>.() -> Unit,
    ): HtmlTag<HTMLQuoteElement> =
        register(HtmlTag("q", id, baseClass, job, evalScope(scope)), content)

    fun dfn(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("dfn", id, baseClass, job, evalScope(scope)), content)

    fun abbr(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("abbr", id, baseClass, job, evalScope(scope)), content)

    fun code(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("code", id, baseClass, job, evalScope(scope)), content)

    fun `var`(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("var", id, baseClass, job, evalScope(scope)), content)

    fun samp(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("samp", id, baseClass, job, evalScope(scope)), content)

    fun kbd(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("kbd", id, baseClass, job, evalScope(scope)), content)

    fun sub(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("sub", id, baseClass, job, evalScope(scope)), content)

    fun sup(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("sup", id, baseClass, job, evalScope(scope)), content)

    fun i(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("i", id, baseClass, job, evalScope(scope)), content)

    fun b(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("b", id, baseClass, job, evalScope(scope)), content)

    fun u(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("u", id, baseClass, job, evalScope(scope)), content)

    fun bdo(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("bdo", id, baseClass, job, evalScope(scope)), content)

    fun command(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLElement>.() -> Unit,
    ): HtmlTag<HTMLElement> =
        register(HtmlTag("command", id, baseClass, job, evalScope(scope)), content)

    fun svg(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: SvgTag.() -> Unit,
    ): SvgTag =
        register(SvgTag("svg", id, baseClass, job = job, evalScope(scope)), content)

    fun path(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: SvgTag.() -> Unit,
    ): SvgTag =
        register(SvgTag("path", id, baseClass, job = job, evalScope(scope)), content)
}
