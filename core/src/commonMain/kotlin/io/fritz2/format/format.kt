package io.fritz2.format

/**
 * [parse]s and [format]s the given
 * [String] value from and to the target type.
 */
interface Format<T> {

    fun parse(value: String): T
    fun format(value: T): String

}