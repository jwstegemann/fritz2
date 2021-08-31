package dev.fritz2.identification

object Id {
    const val defaultLength = 6
    private val chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray()

    fun next(length: Int = defaultLength) = buildString {
        for (i in 0 until length) {
            append(chars.random())
        }
    }
}

/**
 * creates something like an UUID
 *
 * @return UUID as String
 */
@Deprecated("use Id.next() instead", ReplaceWith("Id.next()"))
expect fun uniqueId(): String