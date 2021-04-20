package dev.fritz2.styling

import dev.fritz2.dom.html.*
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.StyleParamsImpl

fun TagContext.a(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: A.() -> Unit
): A = register(A(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.a(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: A.() -> Unit
): A = register(
    A(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.area(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Area.() -> Unit
): Area = register(Area(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.area(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Area.() -> Unit
): Area = register(
    Area(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.br(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Br.() -> Unit
): Br = register(Br(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.br(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Br.() -> Unit
): Br = register(
    Br(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.button(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Button.() -> Unit
): Button = register(Button(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.button(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Button.() -> Unit
): Button = register(
    Button(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.canvas(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Canvas.() -> Unit
): Canvas = register(Canvas(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.canvas(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Canvas.() -> Unit
): Canvas = register(
    Canvas(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.dl(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Dl.() -> Unit
): Dl = register(Dl(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.dl(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Dl.() -> Unit
): Dl = register(
    Dl(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.dt(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("dt", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.dt(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "dt",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.dd(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("dd", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.dd(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "dd",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.data(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Data.() -> Unit
): Data = register(Data(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.data(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Data.() -> Unit
): Data = register(
    Data(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.datalist(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: DataList.() -> Unit
): DataList = register(DataList(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.datalist(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: DataList.() -> Unit
): DataList = register(
    DataList(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.details(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Details.() -> Unit
): Details = register(Details(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.details(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Details.() -> Unit
): Details = register(
    Details(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.dialog(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Dialog.() -> Unit
): Dialog = register(Dialog(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.dialog(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Dialog.() -> Unit
): Dialog = register(
    Dialog(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.div(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Div.() -> Unit
): Div = register(Div(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.div(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Div.() -> Unit
): Div = register(
    Div(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.embed(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Embed.() -> Unit
): Embed = register(Embed(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.embed(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Embed.() -> Unit
): Embed = register(
    Embed(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.fieldset(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: FieldSet.() -> Unit
): FieldSet = register(FieldSet(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.fieldset(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: FieldSet.() -> Unit
): FieldSet = register(
    FieldSet(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.form(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Form.() -> Unit
): Form = register(Form(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.form(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Form.() -> Unit
): Form = register(
    Form(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.hr(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Hr.() -> Unit
): Hr = register(Hr(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.hr(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Hr.() -> Unit
): Hr = register(
    Hr(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.h1(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: H.() -> Unit
): H = register(H(1, id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.h1(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: H.() -> Unit
): H = register(
    H(1, id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.h2(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: H.() -> Unit
): H = register(H(2, id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.h2(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: H.() -> Unit
): H = register(
    H(2, id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.h3(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: H.() -> Unit
): H = register(H(3, id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.h3(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: H.() -> Unit
): H = register(
    H(3, id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.h4(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: H.() -> Unit
): H = register(H(4, id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.h4(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: H.() -> Unit
): H = register(
    H(4, id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.h5(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: H.() -> Unit
): H = register(H(5, id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.h5(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: H.() -> Unit
): H = register(
    H(5, id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.h6(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: H.() -> Unit
): H = register(H(6, id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.h6(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: H.() -> Unit
): H = register(
    H(6, id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.iframe(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: IFrame.() -> Unit
): IFrame = register(IFrame(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.iframe(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: IFrame.() -> Unit
): IFrame = register(
    IFrame(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.img(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Img.() -> Unit
): Img = register(Img(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.img(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Img.() -> Unit
): Img = register(
    Img(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.input(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Input.() -> Unit
): Input = register(Input(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.input(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Input.() -> Unit
): Input = register(
    Input(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.li(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Li.() -> Unit
): Li = register(Li(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.li(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Li.() -> Unit
): Li = register(
    Li(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.label(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Label.() -> Unit
): Label = register(Label(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.label(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Label.() -> Unit
): Label = register(
    Label(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.legend(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Legend.() -> Unit
): Legend = register(Legend(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.legend(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Legend.() -> Unit
): Legend = register(
    Legend(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.map(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Map.() -> Unit
): Map = register(Map(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.map(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Map.() -> Unit
): Map = register(
    Map(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.audio(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Audio.() -> Unit
): Audio = register(Audio(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.audio(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Audio.() -> Unit
): Audio = register(
    Audio(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.video(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Video.() -> Unit
): Video = register(Video(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.video(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Video.() -> Unit
): Video = register(
    Video(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.meter(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Meter.() -> Unit
): Meter = register(Meter(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.meter(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Meter.() -> Unit
): Meter = register(
    Meter(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.ins(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Ins.() -> Unit
): Ins = register(Ins(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.ins(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Ins.() -> Unit
): Ins = register(
    Ins(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.del(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Del.() -> Unit
): Del = register(Del(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.del(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Del.() -> Unit
): Del = register(
    Del(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.ol(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Ol.() -> Unit
): Ol = register(Ol(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.ol(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Ol.() -> Unit
): Ol = register(
    Ol(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.`object`(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Object.() -> Unit
): Object = register(Object(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.`object`(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Object.() -> Unit
): Object = register(
    Object(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.optgroup(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Optgroup.() -> Unit
): Optgroup = register(Optgroup(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.optgroup(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Optgroup.() -> Unit
): Optgroup = register(
    Optgroup(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.option(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Option.() -> Unit
): Option = register(Option(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.option(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Option.() -> Unit
): Option = register(
    Option(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.output(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Output.() -> Unit
): Output = register(Output(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.output(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Output.() -> Unit
): Output = register(
    Output(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.p(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: P.() -> Unit
): P = register(P(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.p(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: P.() -> Unit
): P = register(
    P(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.param(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Param.() -> Unit
): Param = register(Param(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.param(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Param.() -> Unit
): Param = register(
    Param(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.picture(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Picture.() -> Unit
): Picture = register(Picture(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.picture(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Picture.() -> Unit
): Picture = register(
    Picture(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.pre(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Pre.() -> Unit
): Pre = register(Pre(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.pre(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Pre.() -> Unit
): Pre = register(
    Pre(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.progress(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Progress.() -> Unit
): Progress = register(Progress(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.progress(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Progress.() -> Unit
): Progress = register(
    Progress(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.quote(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Quote.() -> Unit
): Quote = register(Quote(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.quote(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Quote.() -> Unit
): Quote = register(
    Quote(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.script(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Script.() -> Unit
): Script = register(Script(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.script(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Script.() -> Unit
): Script = register(
    Script(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.select(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Select.() -> Unit
): Select = register(Select(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.select(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Select.() -> Unit
): Select = register(
    Select(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.span(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Span.() -> Unit
): Span = register(Span(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.span(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Span.() -> Unit
): Span = register(
    Span(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.caption(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Caption.() -> Unit
): Caption = register(Caption(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.caption(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Caption.() -> Unit
): Caption = register(
    Caption(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.th(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Th.() -> Unit
): Th = register(Th(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.th(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Th.() -> Unit
): Th = register(
    Th(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.td(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Td.() -> Unit
): Td = register(Td(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.td(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Td.() -> Unit
): Td = register(
    Td(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.col(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Col.() -> Unit
): Col = register(Col(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.col(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Col.() -> Unit
): Col = register(
    Col(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.colgroup(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Colgroup.() -> Unit
): Colgroup = register(Colgroup(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.colgroup(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Colgroup.() -> Unit
): Colgroup = register(
    Colgroup(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.table(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Table.() -> Unit
): Table = register(Table(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.table(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Table.() -> Unit
): Table = register(
    Table(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.tr(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Tr.() -> Unit
): Tr = register(Tr(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.tr(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Tr.() -> Unit
): Tr = register(
    Tr(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.tfoot(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TFoot.() -> Unit
): TFoot = register(TFoot(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.tfoot(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TFoot.() -> Unit
): TFoot = register(
    TFoot(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.thead(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: THead.() -> Unit
): THead = register(THead(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.thead(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: THead.() -> Unit
): THead = register(
    THead(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.tbody(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TBody.() -> Unit
): TBody = register(TBody(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.tbody(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TBody.() -> Unit
): TBody = register(
    TBody(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.textarea(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextArea.() -> Unit
): TextArea = register(TextArea(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.textarea(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextArea.() -> Unit
): TextArea = register(
    TextArea(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.time(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Time.() -> Unit
): Time = register(Time(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.time(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Time.() -> Unit
): Time = register(
    Time(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.track(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Track.() -> Unit
): Track = register(Track(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.track(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Track.() -> Unit
): Track = register(
    Track(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.ul(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Ul.() -> Unit
): Ul = register(Ul(id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.ul(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: Ul.() -> Unit
): Ul = register(
    Ul(id, (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name, job),
    content
)

fun TagContext.address(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("address", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.address(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "address",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.article(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("article", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.article(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "article",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.aside(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("aside", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.aside(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "aside",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.bdi(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("bdi", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.bdi(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "bdi",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.details(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("details", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.details(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "details",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.dialog(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("dialog", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.dialog(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "dialog",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.figcaption(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("figcaption", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.figcaption(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "figcaption",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.figure(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("figure", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.figure(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "figure",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.footer(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("footer", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.footer(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "footer",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.header(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("header", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.header(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "header",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.main(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("main", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.main(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "main",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.mark(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("mark", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.mark(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "mark",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.nav(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("nav", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.nav(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "nav",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.noscript(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("noscript", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.noscript(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "noscript",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.progress(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("progress", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.progress(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "progress",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.rp(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("rp", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.rp(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "rp",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.rt(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("rt", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.rt(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "rt",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.ruby(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("ruby", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.ruby(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "ruby",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.section(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("section", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.section(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "section",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.summary(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("summary", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.summary(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "summary",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.time(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("time", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.time(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "time",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.wbr(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("wbr", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.wbr(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "wbr",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.blockquote(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("blockquote", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.blockquote(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "blockquote",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.em(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("em", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.em(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "em",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.strong(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("strong", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.strong(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "strong",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.small(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("small", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.small(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "small",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.s(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("s", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.s(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "s",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.cite(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("cite", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.cite(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "cite",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.q(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("q", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.q(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "q",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.dfn(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("dfn", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.dfn(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "dfn",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.abbr(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("abbr", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.abbr(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "abbr",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.code(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("code", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.code(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "code",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.`var`(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("var", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.`var`(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "var",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.samp(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("samp", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.samp(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "samp",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.kbd(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("kbd", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.kbd(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "kbd",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.sub(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("sub", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.sub(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "sub",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.sup(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("sup", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.sup(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "sup",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.i(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("i", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.i(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "i",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.b(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("b", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.b(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "b",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.u(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("u", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.u(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "u",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.bdo(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement =
    register(TextElement("bdo", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job), content)

fun TagContext.bdo(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "bdo",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)

fun TagContext.command(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement("command", id, (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name, job),
    content
)

fun TagContext.command(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "command",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job
    ), content
)