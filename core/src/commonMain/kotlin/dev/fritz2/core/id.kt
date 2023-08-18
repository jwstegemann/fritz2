package dev.fritz2.core

object Id {
    private const val defaultLength = 6
    private val chars = "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray()

    @Suppress("UnusedPrivateProperty")
    fun next(length: Int = defaultLength) = buildString {
        for (i in 0 until length) {
            append(chars.random())
        }
    }
}
