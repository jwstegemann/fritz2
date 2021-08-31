package dev.fritz2.components.appFrame

import dev.fritz2.dom.html.Scope

sealed class AppFrameScope(name: String) : Scope.Key<Boolean>("appFrame-$name") {
    object Header: AppFrameScope("header")
    object Brand: AppFrameScope("brand")
    object Actions: AppFrameScope("actions")
    object Navigation: AppFrameScope("navigation")
    object Content: AppFrameScope("content")
    object Complementary: AppFrameScope("complementary")
    object Tablist: AppFrameScope("tablist")
}