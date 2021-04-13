@file:JsModule("stylis")
@file:JsNonModule

package dev.fritz2.styling.stylis

internal external fun compile(css: String): Array<dynamic>

internal external fun serialize(children: dynamic, callback: dynamic): String

internal external fun stringify(element: dynamic, index: dynamic, children: dynamic, callback: dynamic): dynamic

internal external fun middleware(collection: dynamic): dynamic
