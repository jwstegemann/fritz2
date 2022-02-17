package dev.fritz2.core

/**
 * Joins all given [classes] strings to one html-class-attribute [String]
 * by filtering all out which are null or blank.
 */
fun classes(vararg classes: String?): String =
    classes.filter { !it.isNullOrBlank() }.joinToString(" ")

/**
 * Helper function to call a native js function with concrete return type [T]
 */
@JsName("Function")
internal external fun <T> nativeFunction(vararg params: String, block: String): T