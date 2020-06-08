package io.fritz2.identification

import java.util.*

actual fun createUUID(): String = UUID.randomUUID().toString()