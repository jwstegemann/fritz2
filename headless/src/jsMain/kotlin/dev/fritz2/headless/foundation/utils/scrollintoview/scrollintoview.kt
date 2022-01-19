@file:JsModule("scroll-into-view-if-needed")
@file:JsNonModule

package dev.fritz2.headless.foundation.utils.scrollintoview

import org.w3c.dom.Node

@JsName("default")
external fun scrollIntoView(node: Node, options: ScrollIntoViewOptions)

