package dev.fritz2.headless.foundation.utils.floatingui

fun <T> obj(apply: T.() -> Unit): T = (js("{}") as T).apply(apply)