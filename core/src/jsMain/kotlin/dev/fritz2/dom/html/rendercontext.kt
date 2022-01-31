package dev.fritz2.dom.html

import dev.fritz2.binding.Patch
import dev.fritz2.binding.Store
import dev.fritz2.binding.mountSimple
import dev.fritz2.binding.sub
import dev.fritz2.dom.*
import dev.fritz2.lenses.IdProvider
import dev.fritz2.utils.Myer
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.dom.clear
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node

/**
 * Context for rendering static and dynamical content
 */
interface RenderContext : WithJob, WithScope {

    /**
     * Renders the data of a [Flow] as [HtmlTag]s to the DOM.
     *
     * @receiver [Flow] containing the data
     * @param into target to mount content to. If not set a child div is added to the [HtmlTag] this method is called on
     * @param content [RenderContext] for rendering the data to the DOM
     */
    fun <V> Flow<V>.render(into: HtmlTag<HTMLElement>? = null, content: RenderContext.(V) -> Unit) {
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
     * @param into target to mount content to. If not set a child div is added to the [HtmlTag] this method is called on
     * @param content [RenderContext] for rendering the data to the DOM
     */
    fun <V> Flow<List<V>>.renderEach(
        idProvider: IdProvider<V, *>? = null,
        into: HtmlTag<HTMLElement>? = null,
        content: RenderContext.(V) -> HtmlTag<HTMLElement>
    ) {
        mountPatches(into, this) { upstreamValues, mountPoints ->
            upstreamValues.scan(Pair(emptyList(), emptyList())) { acc: Pair<List<V>, List<V>>, new ->
                Pair(acc.second, new)
            }.map { (old, new) ->
                val diff = if (idProvider != null) Myer.diff(old, new, idProvider) else Myer.diff(old, new)
                diff.map { patch ->
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
     * @param into target to mount content to. If not set a child div is added to the [HtmlTag] this method is called on
     * @param content [RenderContext] for rendering the data to the DOM
     */
    fun <V> Store<List<V>>.renderEach(
        idProvider: IdProvider<V, *>,
        into: HtmlTag<HTMLElement>? = null,
        content: RenderContext.(Store<V>) -> HtmlTag<HTMLElement>
    ) {
        data.renderEach(idProvider, into) { value ->
            content(this@renderEach.sub(value, idProvider))
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
        content: HtmlTag<*>.() -> Unit
    ): HtmlTag<*> =
        register(HtmlTag(tagName, id, baseClass, job, evalScope(scope)), content)

    fun a(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: A.() -> Unit
    ): A =
        register(A(id, baseClass, job, evalScope(scope)), content)

    fun area(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Area.() -> Unit
    ): Area =
        register(Area(id, baseClass, job, evalScope(scope)), content)

    fun br(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Br.() -> Unit
    ): Br =
        register(Br(id, baseClass, job, evalScope(scope)), content)

    fun button(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Button.() -> Unit
    ): Button =
        register(Button(id, baseClass, job, evalScope(scope)), content)

    fun canvas(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Canvas.() -> Unit
    ): Canvas =
        register(Canvas(id, baseClass, job, evalScope(scope)), content)

    fun dl(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Dl.() -> Unit
    ): Dl =
        register(Dl(id, baseClass, job, evalScope(scope)), content)

    fun dt(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("dt", id, baseClass, job, evalScope(scope)), content)

    fun dd(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("dd", id, baseClass, job, evalScope(scope)), content)

    fun data(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Data.() -> Unit
    ): Data =
        register(Data(id, baseClass, job, evalScope(scope)), content)

    fun datalist(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: DataList.() -> Unit
    ): DataList =
        register(DataList(id, baseClass, job, evalScope(scope)), content)

    fun details(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Details.() -> Unit
    ): Details =
        register(Details(id, baseClass, job, evalScope(scope)), content)

    fun dialog(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Dialog.() -> Unit
    ): Dialog =
        register(Dialog(id, baseClass, job, evalScope(scope)), content)

    fun div(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Div.() -> Unit
    ): Div =
        register(Div(id, baseClass, job, evalScope(scope)), content)

    fun embed(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Embed.() -> Unit
    ): Embed =
        register(Embed(id, baseClass, job, evalScope(scope)), content)

    fun fieldset(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: FieldSet.() -> Unit
    ): FieldSet =
        register(FieldSet(id, baseClass, job, evalScope(scope)), content)

    fun form(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Form.() -> Unit
    ): Form =
        register(Form(id, baseClass, job, evalScope(scope)), content)

    fun hr(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Hr.() -> Unit
    ): Hr =
        register(Hr(id, baseClass, job, evalScope(scope)), content)

    fun h1(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: H.() -> Unit
    ): H =
        register(H(1, id, baseClass, job, evalScope(scope)), content)

    fun h2(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: H.() -> Unit
    ): H =
        register(H(2, id, baseClass, job, evalScope(scope)), content)

    fun h3(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: H.() -> Unit
    ): H =
        register(H(3, id, baseClass, job, evalScope(scope)), content)

    fun h4(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: H.() -> Unit
    ): H =
        register(H(4, id, baseClass, job, evalScope(scope)), content)

    fun h5(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: H.() -> Unit
    ): H =
        register(H(5, id, baseClass, job, evalScope(scope)), content)

    fun h6(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: H.() -> Unit
    ): H =
        register(H(6, id, baseClass, job, evalScope(scope)), content)

    fun iframe(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: IFrame.() -> Unit
    ): IFrame =
        register(IFrame(id, baseClass, job, evalScope(scope)), content)

    fun img(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Img.() -> Unit
    ): Img =
        register(Img(id, baseClass, job, evalScope(scope)), content)

    fun input(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Input.() -> Unit
    ): Input =
        register(Input(id, baseClass, job, evalScope(scope)), content)

    fun li(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Li.() -> Unit
    ): Li =
        register(Li(id, baseClass, job, evalScope(scope)), content)

    fun label(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Label.() -> Unit
    ): Label =
        register(Label(id, baseClass, job, evalScope(scope)), content)

    fun legend(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Legend.() -> Unit
    ): Legend =
        register(Legend(id, baseClass, job, evalScope(scope)), content)

    fun map(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Map.() -> Unit
    ): Map =
        register(Map(id, baseClass, job, evalScope(scope)), content)

    fun audio(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Audio.() -> Unit
    ): Audio =
        register(Audio(id, baseClass, job, evalScope(scope)), content)

    fun video(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Video.() -> Unit
    ): Video =
        register(Video(id, baseClass, job, evalScope(scope)), content)

    fun meter(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Meter.() -> Unit
    ): Meter =
        register(Meter(id, baseClass, job, evalScope(scope)), content)

    fun ins(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Ins.() -> Unit
    ): Ins =
        register(Ins(id, baseClass, job, evalScope(scope)), content)

    fun del(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Del.() -> Unit
    ): Del =
        register(Del(id, baseClass, job, evalScope(scope)), content)

    fun ol(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Ol.() -> Unit
    ): Ol =
        register(Ol(id, baseClass, job, evalScope(scope)), content)

    fun `object`(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Object.() -> Unit
    ): Object =
        register(Object(id, baseClass, job, evalScope(scope)), content)

    fun optgroup(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Optgroup.() -> Unit
    ): Optgroup =
        register(Optgroup(id, baseClass, job, evalScope(scope)), content)

    fun option(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Option.() -> Unit
    ): Option =
        register(Option(id, baseClass, job, evalScope(scope)), content)

    fun output(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Output.() -> Unit
    ): Output =
        register(Output(id, baseClass, job, evalScope(scope)), content)

    fun p(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: P.() -> Unit
    ): P =
        register(P(id, baseClass, job, evalScope(scope)), content)

    fun param(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Param.() -> Unit
    ): Param =
        register(Param(id, baseClass, job, evalScope(scope)), content)

    fun picture(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Picture.() -> Unit
    ): Picture =
        register(Picture(id, baseClass, job, evalScope(scope)), content)

    fun pre(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Pre.() -> Unit
    ): Pre =
        register(Pre(id, baseClass, job, evalScope(scope)), content)

    fun progress(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Progress.() -> Unit
    ): Progress =
        register(Progress(id, baseClass, job, evalScope(scope)), content)

    fun quote(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Quote.() -> Unit
    ): Quote =
        register(Quote(id, baseClass, job, evalScope(scope)), content)

    fun script(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Script.() -> Unit
    ): Script =
        register(Script(id, baseClass, job, evalScope(scope)), content)

    fun select(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Select.() -> Unit
    ): Select =
        register(Select(id, baseClass, job, evalScope(scope)), content)

    fun span(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Span.() -> Unit
    ): Span =
        register(Span(id, baseClass, job, evalScope(scope)), content)

    fun caption(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Caption.() -> Unit
    ): Caption =
        register(Caption(id, baseClass, job, evalScope(scope)), content)

    fun th(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Th.() -> Unit
    ): Th =
        register(Th(id, baseClass, job, evalScope(scope)), content)

    fun td(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Td.() -> Unit
    ): Td =
        register(Td(id, baseClass, job, evalScope(scope)), content)

    fun col(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Col.() -> Unit
    ): Col =
        register(Col(id, baseClass, job, evalScope(scope)), content)

    fun colgroup(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Colgroup.() -> Unit
    ): Colgroup =
        register(Colgroup(id, baseClass, job, evalScope(scope)), content)

    fun table(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Table.() -> Unit
    ): Table =
        register(Table(id, baseClass, job, evalScope(scope)), content)

    fun tr(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tr.() -> Unit
    ): Tr =
        register(Tr(id, baseClass, job, evalScope(scope)), content)

    fun tfoot(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TFoot.() -> Unit
    ): TFoot =
        register(TFoot(id, baseClass, job, evalScope(scope)), content)

    fun thead(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: THead.() -> Unit
    ): THead =
        register(THead(id, baseClass, job, evalScope(scope)), content)

    fun tbody(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TBody.() -> Unit
    ): TBody =
        register(TBody(id, baseClass, job, evalScope(scope)), content)

    fun textarea(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextArea.() -> Unit
    ): TextArea =
        register(TextArea(id, baseClass, job, evalScope(scope)), content)

    fun time(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Time.() -> Unit
    ): Time =
        register(Time(id, baseClass, job, evalScope(scope)), content)

    fun track(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Track.() -> Unit
    ): Track =
        register(Track(id, baseClass, job, evalScope(scope)), content)

    fun ul(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Ul.() -> Unit
    ): Ul =
        register(Ul(id, baseClass, job, evalScope(scope)), content)

    fun address(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("address", id, baseClass, job, evalScope(scope)), content)

    fun article(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("article", id, baseClass, job, evalScope(scope)), content)

    fun aside(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("aside", id, baseClass, job, evalScope(scope)), content)

    fun bdi(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("bdi", id, baseClass, job, evalScope(scope)), content)

    fun figcaption(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("figcaption", id, baseClass, job, evalScope(scope)), content)

    fun figure(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("figure", id, baseClass, job, evalScope(scope)), content)

    fun footer(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("footer", id, baseClass, job, evalScope(scope)), content)

    fun header(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("header", id, baseClass, job, evalScope(scope)), content)

    fun main(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("main", id, baseClass, job, evalScope(scope)), content)

    fun mark(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("mark", id, baseClass, job, evalScope(scope)), content)

    fun nav(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("nav", id, baseClass, job, evalScope(scope)), content)

    fun noscript(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("noscript", id, baseClass, job, evalScope(scope)), content)

    fun rp(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("rp", id, baseClass, job, evalScope(scope)), content)

    fun rt(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("rt", id, baseClass, job, evalScope(scope)), content)

    fun ruby(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("ruby", id, baseClass, job, evalScope(scope)), content)

    fun section(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("section", id, baseClass, job, evalScope(scope)), content)

    fun summary(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("summary", id, baseClass, job, evalScope(scope)), content)

    fun wbr(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("wbr", id, baseClass, job, evalScope(scope)), content)

    fun blockquote(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("blockquote", id, baseClass, job, evalScope(scope)), content)

    fun em(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("em", id, baseClass, job, evalScope(scope)), content)

    fun strong(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("strong", id, baseClass, job, evalScope(scope)), content)

    fun small(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("small", id, baseClass, job, evalScope(scope)), content)

    fun s(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("s", id, baseClass, job, evalScope(scope)), content)

    fun cite(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("cite", id, baseClass, job, evalScope(scope)), content)

    fun q(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("q", id, baseClass, job, evalScope(scope)), content)

    fun dfn(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("dfn", id, baseClass, job, evalScope(scope)), content)

    fun abbr(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("abbr", id, baseClass, job, evalScope(scope)), content)

    fun code(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("code", id, baseClass, job, evalScope(scope)), content)

    fun `var`(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("var", id, baseClass, job, evalScope(scope)), content)

    fun samp(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("samp", id, baseClass, job, evalScope(scope)), content)

    fun kbd(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("kbd", id, baseClass, job, evalScope(scope)), content)

    fun sub(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("sub", id, baseClass, job, evalScope(scope)), content)

    fun sup(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("sup", id, baseClass, job, evalScope(scope)), content)

    fun i(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("i", id, baseClass, job, evalScope(scope)), content)

    fun b(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("b", id, baseClass, job, evalScope(scope)), content)

    fun u(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("u", id, baseClass, job, evalScope(scope)), content)

    fun bdo(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("bdo", id, baseClass, job, evalScope(scope)), content)

    fun command(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("command", id, baseClass, job, evalScope(scope)), content)

    fun svg(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Svg.() -> Unit
    ): Svg =
        register(Svg(id, baseClass, job = job, evalScope(scope)), content)

    fun path(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Path.() -> Unit
    ): Path =
        register(Path(id, baseClass, job = job, evalScope(scope)), content)
}