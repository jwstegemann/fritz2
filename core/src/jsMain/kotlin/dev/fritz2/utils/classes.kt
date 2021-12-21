package dev.fritz2.utils

fun classes(vararg classes: String?): String =
    classes.filter { !it.isNullOrBlank() }.joinToString(" ")
