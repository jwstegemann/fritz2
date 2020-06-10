package dev.fritz2.identification

import java.util.*

actual fun uniqueId(): String = UUID.randomUUID().toString()