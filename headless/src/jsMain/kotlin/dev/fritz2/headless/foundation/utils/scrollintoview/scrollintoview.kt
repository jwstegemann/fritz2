package dev.fritz2.headless.foundation.utils.scrollintoview

import org.w3c.dom.Node

@JsModule("scroll-into-view-if-needed")
@JsNonModule
external fun scrollIntoView(node: Node, options: ScrollIntoViewOptions)