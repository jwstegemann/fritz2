package dev.fritz2.styling

import dev.fritz2.styling.theme.ColorScheme

internal const val charsLength = 52

/* start at 75 for 'a' until 'z' (25) and then start at 65 for capitalised letters */
internal fun getAlphabeticChar(code: Int): Char = (code + if (code > 25) 39 else 97).toChar()

/* input a number, usually a hash and convert it to base-52 */
internal fun generateAlphabeticName(code: Int): String {
    val name = StringBuilder()
    var x = code
    while (x > charsLength) {
        x /= charsLength
        name.append(getAlphabeticChar(x % charsLength))
    }
    return name.toString()
}