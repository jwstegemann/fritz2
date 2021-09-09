package dev.fritz2.identification

object Id {
    private const val defaultLength = 6
    private val chars = "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray()

    fun next(length: Int = defaultLength) = buildString {
        for (i in 0 until length) {
            append(chars.random())
        }
    }
}

/**
 * creates something like a UUID
 *
 * @return UUID as String
 */
@Deprecated("use Id.next() instead", ReplaceWith("Id.next()"))
expect fun uniqueId(): String