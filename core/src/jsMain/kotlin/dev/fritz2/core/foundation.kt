package dev.fritz2.core

fun classes(vararg classes: String?): String =
    classes.filter { !it.isNullOrBlank() }.joinToString(" ")

@JsName("Function")
internal external fun <T> nativeFunction(vararg params: String, block: String): T