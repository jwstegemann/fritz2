@file:JsModule("@floating-ui/dom")
@file:JsNonModule

package dev.fritz2.headless.foundation.utils.floatingui.dom

import dev.fritz2.headless.foundation.utils.floatingui.core.ComputePositionConfig
import dev.fritz2.headless.foundation.utils.floatingui.core.ComputePositionReturn
import dev.fritz2.headless.foundation.utils.floatingui.dom.AutoUpdateOptions
import org.w3c.dom.Element
import kotlin.js.Promise


external fun autoUpdate(
    reference: Element,
    floating: Element,
    update: () -> Unit,
    options: AutoUpdateOptions = definedExternally
): () -> Unit


external fun computePosition(
    reference: Element,
    floating: Element,
    config: ComputePositionConfig = definedExternally
): Promise<ComputePositionReturn>