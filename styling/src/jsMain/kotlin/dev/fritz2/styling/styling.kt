package dev.fritz2.styling

import dev.fritz2.dom.html.*
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.StyleParamsImpl

/**
 * Evaluates the scope context and initializes a [ScopeContext]
 * for setting new entries to the scope.
 *
 * @param context to evaluate
 */
private inline fun RenderContext.evalScope(context: (ScopeContext.() -> Unit)): Scope =
    ScopeContext(this.scope).apply(context).scope

fun RenderContext.a(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: A.() -> Unit
): A = register(
    A(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.a(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: A.() -> Unit
): A = register(
    A(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.area(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Area.() -> Unit
): Area =
    register(
        Area(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.area(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Area.() -> Unit
): Area = register(
    Area(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.br(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Br.() -> Unit
): Br = register(
    Br(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.br(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Br.() -> Unit
): Br = register(
    Br(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.button(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Button.() -> Unit
): Button =
    register(
        Button(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.button(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Button.() -> Unit
): Button = register(
    Button(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.canvas(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Canvas.() -> Unit
): Canvas =
    register(
        Canvas(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.canvas(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Canvas.() -> Unit
): Canvas = register(
    Canvas(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.dl(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Dl.() -> Unit
): Dl = register(
    Dl(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.dl(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Dl.() -> Unit
): Dl = register(
    Dl(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.dt(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement =
    register(
        TextElement(
            "dt",
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ),
        content
    )

fun RenderContext.dt(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "dt",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.dd(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement =
    register(
        TextElement(
            "dd",
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ),
        content
    )

fun RenderContext.dd(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "dd",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.data(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Data.() -> Unit
): Data =
    register(
        Data(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.data(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Data.() -> Unit
): Data = register(
    Data(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.datalist(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: DataList.() -> Unit
): DataList =
    register(
        DataList(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.datalist(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: DataList.() -> Unit
): DataList = register(
    DataList(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.details(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Details.() -> Unit
): Details =
    register(
        Details(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.details(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Details.() -> Unit
): Details = register(
    Details(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.dialog(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Dialog.() -> Unit
): Dialog =
    register(
        Dialog(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.dialog(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Dialog.() -> Unit
): Dialog = register(
    Dialog(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.div(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Div.() -> Unit
): Div =
    register(
        Div(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.div(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Div.() -> Unit
): Div = register(
    Div(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.embed(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Embed.() -> Unit
): Embed =
    register(
        Embed(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.embed(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Embed.() -> Unit
): Embed = register(
    Embed(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.fieldset(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: FieldSet.() -> Unit
): FieldSet =
    register(
        FieldSet(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.fieldset(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: FieldSet.() -> Unit
): FieldSet = register(
    FieldSet(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.form(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Form.() -> Unit
): Form =
    register(
        Form(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.form(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Form.() -> Unit
): Form = register(
    Form(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.hr(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Hr.() -> Unit
): Hr = register(
    Hr(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.hr(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Hr.() -> Unit
): Hr = register(
    Hr(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.h1(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: H.() -> Unit
): H = register(
    H(
        1,
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.h1(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: H.() -> Unit
): H = register(
    H(
        1,
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.h2(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: H.() -> Unit
): H = register(
    H(
        2,
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.h2(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: H.() -> Unit
): H = register(
    H(
        2,
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.h3(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: H.() -> Unit
): H = register(
    H(
        3,
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.h3(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: H.() -> Unit
): H = register(
    H(
        3,
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.h4(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: H.() -> Unit
): H = register(
    H(
        4,
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.h4(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: H.() -> Unit
): H = register(
    H(
        4,
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.h5(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: H.() -> Unit
): H = register(
    H(
        5,
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.h5(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: H.() -> Unit
): H = register(
    H(
        5,
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.h6(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: H.() -> Unit
): H = register(
    H(
        6,
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.h6(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: H.() -> Unit
): H = register(
    H(
        6,
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.iframe(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: IFrame.() -> Unit
): IFrame =
    register(
        IFrame(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.iframe(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: IFrame.() -> Unit
): IFrame = register(
    IFrame(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.img(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Img.() -> Unit
): Img = register(
    Img(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.img(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Img.() -> Unit
): Img = register(
    Img(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.input(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Input.() -> Unit
): Input =
    register(
        Input(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.input(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Input.() -> Unit
): Input = register(
    Input(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.li(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Li.() -> Unit
): Li = register(
    Li(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.li(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Li.() -> Unit
): Li = register(
    Li(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.label(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Label.() -> Unit
): Label =
    register(
        Label(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.label(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Label.() -> Unit
): Label = register(
    Label(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.legend(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Legend.() -> Unit
): Legend =
    register(
        Legend(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.legend(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Legend.() -> Unit
): Legend = register(
    Legend(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.map(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Map.() -> Unit
): Map = register(
    Map(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.map(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Map.() -> Unit
): Map = register(
    Map(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.audio(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Audio.() -> Unit
): Audio =
    register(
        Audio(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.audio(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Audio.() -> Unit
): Audio = register(
    Audio(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.video(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Video.() -> Unit
): Video =
    register(
        Video(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.video(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Video.() -> Unit
): Video = register(
    Video(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.meter(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Meter.() -> Unit
): Meter =
    register(
        Meter(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.meter(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Meter.() -> Unit
): Meter = register(
    Meter(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.ins(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Ins.() -> Unit
): Ins = register(
    Ins(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.ins(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Ins.() -> Unit
): Ins = register(
    Ins(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.del(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Del.() -> Unit
): Del = register(
    Del(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.del(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Del.() -> Unit
): Del = register(
    Del(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.ol(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Ol.() -> Unit
): Ol = register(
    Ol(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.ol(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Ol.() -> Unit
): Ol = register(
    Ol(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.`object`(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Object.() -> Unit
): Object =
    register(
        Object(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.`object`(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Object.() -> Unit
): Object = register(
    Object(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.optgroup(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Optgroup.() -> Unit
): Optgroup =
    register(
        Optgroup(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.optgroup(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Optgroup.() -> Unit
): Optgroup = register(
    Optgroup(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.option(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Option.() -> Unit
): Option =
    register(
        Option(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.option(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Option.() -> Unit
): Option = register(
    Option(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.output(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Output.() -> Unit
): Output =
    register(
        Output(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.output(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Output.() -> Unit
): Output = register(
    Output(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.p(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: P.() -> Unit
): P = register(
    P(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.p(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: P.() -> Unit
): P = register(
    P(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.param(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Param.() -> Unit
): Param =
    register(
        Param(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.param(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Param.() -> Unit
): Param = register(
    Param(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.picture(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Picture.() -> Unit
): Picture =
    register(
        Picture(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.picture(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Picture.() -> Unit
): Picture = register(
    Picture(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.pre(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Pre.() -> Unit
): Pre = register(
    Pre(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.pre(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Pre.() -> Unit
): Pre = register(
    Pre(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.progress(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Progress.() -> Unit
): Progress =
    register(
        Progress(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.progress(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Progress.() -> Unit
): Progress = register(
    Progress(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.quote(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Quote.() -> Unit
): Quote =
    register(
        Quote(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.quote(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Quote.() -> Unit
): Quote = register(
    Quote(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.script(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Script.() -> Unit
): Script =
    register(
        Script(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.script(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Script.() -> Unit
): Script = register(
    Script(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.select(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Select.() -> Unit
): Select =
    register(
        Select(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.select(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Select.() -> Unit
): Select = register(
    Select(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.span(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Span.() -> Unit
): Span =
    register(
        Span(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.span(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Span.() -> Unit
): Span = register(
    Span(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.caption(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Caption.() -> Unit
): Caption =
    register(
        Caption(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.caption(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Caption.() -> Unit
): Caption = register(
    Caption(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.th(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Th.() -> Unit
): Th = register(
    Th(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.th(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Th.() -> Unit
): Th = register(
    Th(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.td(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Td.() -> Unit
): Td = register(
    Td(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.td(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Td.() -> Unit
): Td = register(
    Td(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.col(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Col.() -> Unit
): Col = register(
    Col(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.col(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Col.() -> Unit
): Col = register(
    Col(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.colgroup(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Colgroup.() -> Unit
): Colgroup =
    register(
        Colgroup(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.colgroup(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Colgroup.() -> Unit
): Colgroup = register(
    Colgroup(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.table(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Table.() -> Unit
): Table =
    register(
        Table(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.table(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Table.() -> Unit
): Table = register(
    Table(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.tr(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Tr.() -> Unit
): Tr = register(
    Tr(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.tr(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Tr.() -> Unit
): Tr = register(
    Tr(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.tfoot(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TFoot.() -> Unit
): TFoot =
    register(
        TFoot(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.tfoot(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TFoot.() -> Unit
): TFoot = register(
    TFoot(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.thead(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: THead.() -> Unit
): THead =
    register(
        THead(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.thead(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: THead.() -> Unit
): THead = register(
    THead(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.tbody(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TBody.() -> Unit
): TBody =
    register(
        TBody(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.tbody(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TBody.() -> Unit
): TBody = register(
    TBody(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.textarea(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextArea.() -> Unit
): TextArea =
    register(
        TextArea(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.textarea(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextArea.() -> Unit
): TextArea = register(
    TextArea(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.time(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Time.() -> Unit
): Time =
    register(
        Time(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.time(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Time.() -> Unit
): Time = register(
    Time(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.track(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Track.() -> Unit
): Track =
    register(
        Track(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.track(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Track.() -> Unit
): Track = register(
    Track(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.ul(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Ul.() -> Unit
): Ul = register(
    Ul(
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.ul(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Ul.() -> Unit
): Ul = register(
    Ul(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.address(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "address",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.address(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "address",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.article(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "article",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.article(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "article",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.aside(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "aside",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.aside(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "aside",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.bdi(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "bdi",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.bdi(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "bdi",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.details(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "details",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.details(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "details",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.dialog(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "dialog",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.dialog(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "dialog",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.figcaption(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "figcaption",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.figcaption(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "figcaption",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.figure(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "figure",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.figure(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "figure",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.footer(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "footer",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.footer(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "footer",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.header(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "header",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.header(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "header",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.main(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "main",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.main(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "main",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.mark(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "mark",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.mark(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "mark",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.nav(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "nav",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.nav(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "nav",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.noscript(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "noscript",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.noscript(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "noscript",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.progress(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "progress",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.progress(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "progress",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.rp(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "rp",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.rp(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "rp",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.rt(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "rt",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.rt(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "rt",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.ruby(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "ruby",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.ruby(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "ruby",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.section(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "section",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.section(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "section",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.summary(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "summary",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.summary(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "summary",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.time(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "time",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.time(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "time",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.wbr(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "wbr",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.wbr(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "wbr",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.blockquote(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "blockquote",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.blockquote(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "blockquote",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.em(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "em",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.em(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "em",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.strong(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "strong",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.strong(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "strong",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.small(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "small",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.small(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "small",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.s(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "s",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.s(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "s",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.cite(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "cite",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.cite(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "cite",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.q(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "q",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.q(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "q",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.dfn(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "dfn",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.dfn(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "dfn",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.abbr(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "abbr",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.abbr(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "abbr",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.code(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "code",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.code(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "code",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.`var`(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "var",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.`var`(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "var",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.samp(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "samp",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.samp(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "samp",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.kbd(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "kbd",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.kbd(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "kbd",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.submenu(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "sub",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.submenu(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "sub",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.sup(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "sup",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.sup(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "sup",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.i(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "i",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.i(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "i",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.b(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "b",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.b(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "b",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.u(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "u",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.u(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "u",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.bdo(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "bdo",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.bdo(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "bdo",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.command(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "command",
        id,
        (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.command(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: TextElement.() -> Unit
): TextElement = register(
    TextElement(
        "command",
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)

fun RenderContext.svg(
    style: Style<BoxParams>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Svg.() -> Unit
): Svg =
    register(
        Svg(
            id,
            (baseClass + StyleParamsImpl().apply(style).cssClasses(prefix)).name,
            job,
            evalScope(scope)
        ), content
    )

fun RenderContext.svg(
    style: Style<BoxParams>,
    parentStyling: Style<BoxParams> = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "css",
    scope: ScopeContext.() -> Unit = {},
    content: Svg.() -> Unit
): Svg = register(
    Svg(
        id,
        (baseClass + StyleParamsImpl().apply { style(); parentStyling() }.cssClasses(prefix)).name,
        job,
        evalScope(scope)
    ), content
)