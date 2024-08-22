package dev.fritz2.lens

internal fun String.lowerCamelCased() = "${first().lowercase()}${drop(1)}"