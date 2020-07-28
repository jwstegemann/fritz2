package dev.fritz2.identification

import java.util.*

/**
 * creates a real UUID on the jvm
 */
actual fun uniqueId(): String = UUID.randomUUID().toString()