package dev.fritz2.components

import dev.fritz2.components.popper.PopperComponent
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams


fun RenderContext.popper(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String = "fc2-popper-${randomId()}",
    prefix: String = "popper",
    build: PopperComponent.() -> Unit
) = PopperComponent().apply(build).render(this, styling, baseClass, id, prefix)
