package dev.fritz2.identification

import java.util.*

/**
 * creates a real UUID on the jvm
 */
@Deprecated("use Id.next() instead", ReplaceWith("Id.next()"))
actual fun uniqueId(): String = UUID.randomUUID().toString()