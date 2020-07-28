package dev.fritz2.identification

import kotlin.js.Date
import kotlin.random.Random

/**
 * create a unique id quite similar (but not really) to a UUID
 */
actual fun uniqueId(): String {
    inline fun createChar(dt: Int) = (dt + Random.nextInt(16)).toString(16)  //toString(16)
    inline fun createOther(dt: Int) = ((dt + Random.nextInt(16)) and 0x3 or 0x8).toString(16)

    var dt = Date().getUTCMilliseconds();
    val sb = StringBuilder(36)

    repeat(8) {
        sb.append(createChar(dt))
        dt /= 16
    }
    sb.append('-')

    repeat(4) {
        sb.append(createChar(dt))
        dt /= 16
    }
    sb.append('-', '4')

    repeat(3) {
        sb.append(createChar(dt))
        dt /= 16
    }
    sb.append('-')

    sb.append(createOther(dt))
    dt /= 16

    repeat(3) {
        sb.append(createChar(dt))
        dt /= 16
    }
    sb.append('-')

    repeat(12) {
        sb.append(createChar(dt))
        dt /= 16
    }

    return sb.toString();
}