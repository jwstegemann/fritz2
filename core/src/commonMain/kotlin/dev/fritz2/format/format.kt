package dev.fritz2.format

import dev.fritz2.lenses.Lens
import dev.fritz2.lenses.buildLens

/**
 * [Format] specifies how to convert a value of type [T]
 * to a [String] an vice versa.
 *
 * @param id sub id to append
 */
abstract class Format<T>(val id: String = "") {

    /**
     * parses the given [String] to the type [T]
     *
     * @param old last value of [T]
     * @param value new value to parse as [String]
     * @return [T]
     */
    abstract fun parse(old: T, value: String): T

    /**
     * formats the given value to [String]
     *
     * @param value to format of type [T]
     * @return [String]
     */
    abstract fun format(value: T): String

    /**
     * generates a [Lens] for this [Format]
     */
    val lens = buildLens(id, ::format, ::parse)
}