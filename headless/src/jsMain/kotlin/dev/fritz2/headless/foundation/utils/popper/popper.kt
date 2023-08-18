@file:JsModule("@popperjs/core")
@file:JsNonModule

package dev.fritz2.headless.foundation.utils.popper

import org.w3c.dom.Element

external fun createPopper(reference: Element, popper: Element, options: PopperOptions): dynamic
