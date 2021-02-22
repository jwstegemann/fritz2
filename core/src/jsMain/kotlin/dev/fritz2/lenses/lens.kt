package dev.fritz2.lenses

/**
 * Creates a [Lens] from [Byte] to [String]
 */
fun <P> Lens<P, Byte>.asString(): Lens<P, String> =
    this + format(String::toByte, Byte::toString)

/**
 * Creates a [Lens] from [Byte] to [String]
 */
fun <P> Lens<P, Byte?>.asString(emptyValue: String = ""): Lens<P, String> =
    this + format({ if (it.isBlank()) null else it.toByte() }, { it?.toString() ?: emptyValue })

/**
 * Creates a [Lens] from [Short] to [String]
 */
fun <P> Lens<P, Short>.asString(): Lens<P, String> =
    this + format(String::toShort, Short::toString)

/**
 * Creates a [Lens] from [Short] to [String]
 */
fun <P> Lens<P, Short?>.asString(emptyValue: String = ""): Lens<P, String> =
    this + format({ if (it.isBlank()) null else it.toShort() }, { it?.toString() ?: emptyValue })

/**
 * Creates a [Lens] from [Int] to [String]
 */
fun <P> Lens<P, Int>.asString(): Lens<P, String> =
    this + format(String::toInt, Int::toString)

/**
 * Creates a [Lens] from [Int] to [String]
 */
fun <P> Lens<P, Int?>.asString(emptyValue: String = ""): Lens<P, String> =
    this + format({ if (it.isBlank()) null else it.toInt() }, { it?.toString() ?: emptyValue })

/**
 * Creates a [Lens] from [Long] to [String]
 */
fun <P> Lens<P, Long>.asString(): Lens<P, String> =
    this + format(String::toLong, Long::toString)

/**
 * Creates a [Lens] from [Long] to [String]
 */
fun <P> Lens<P, Long?>.asString(emptyValue: String = ""): Lens<P, String> =
    this + format({ if (it.isBlank()) null else it.toLong() }, { it?.toString() ?: emptyValue })

/**
 * Creates a [Lens] from [Double] to [String]
 */
fun <P> Lens<P, Double>.asString(): Lens<P, String> =
    this + format(String::toDouble, Double::toString)

/**
 * Creates a [Lens] from [Double] to [String]
 */
fun <P> Lens<P, Double?>.asString(emptyValue: String = ""): Lens<P, String> =
    this + format({ if (it.isBlank()) null else it.toDouble() }, { it?.toString() ?: emptyValue })

/**
 * Creates a [Lens] from [Float] to [String]
 */
fun <P> Lens<P, Float>.asString(): Lens<P, String> =
    this + format(String::toFloat, Float::toString)

/**
 * Creates a [Lens] from [Float] to [String]
 */
fun <P> Lens<P, Float?>.asString(emptyValue: String = ""): Lens<P, String> =
    this + format({ if (it.isBlank()) null else it.toFloat() }, { it?.toString() ?: emptyValue })

/**
 * Creates a [Lens] from [Boolean] to [String]
 */
fun <P> Lens<P, Boolean>.asString(): Lens<P, String> =
    this + format(String::toBoolean, Boolean::toString)

/**
 * Creates a [Lens] from [Boolean] to [String]
 */
fun <P> Lens<P, Boolean?>.asString(emptyValue: String = ""): Lens<P, String> =
    this + format({ if (it.isBlank()) null else it.toBoolean() }, { it?.toString() ?: emptyValue })

/**
 * Creates a [Lens] from [List] of [Byte] to [String]
 */
fun <P> Lens<P, List<Byte>>.asString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): Lens<P, String> =
    this + format(
        { it.removePrefix(prefix).removeSuffix(postfix).split(separator).map(String::toByte) },
        { it.joinToString(separator, prefix, postfix) }
    )

/**
 * Creates a [Lens] from [List] of [Short] to [String]
 */
fun <P> Lens<P, List<Short>>.asString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): Lens<P, String> =
    this + format(
        { it.removePrefix(prefix).removeSuffix(postfix).split(separator).map(String::toShort) },
        { it.joinToString(separator, prefix, postfix) }
    )

/**
 * Creates a [Lens] from [List] of [Int] to [String]
 */
fun <P> Lens<P, List<Int>>.asString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): Lens<P, String> =
    this + format(
        { it.removePrefix(prefix).removeSuffix(postfix).split(separator).map(String::toInt) },
        { it.joinToString(separator, prefix, postfix) }
    )

/**
 * Creates a [Lens] from [List] of [Long] to [String]
 */
fun <P> Lens<P, List<Long>>.asString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): Lens<P, String> =
    this + format(
        { it.removePrefix(prefix).removeSuffix(postfix).split(separator).map(String::toLong) },
        { it.joinToString(separator, prefix, postfix) }
    )

/**
 * Creates a [Lens] from [List] of [Double] to [String]
 */
fun <P> Lens<P, List<Double>>.asString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): Lens<P, String> =
    this + format(
        { it.removePrefix(prefix).removeSuffix(postfix).split(separator).map(String::toDouble) },
        { it.joinToString(separator, prefix, postfix) }
    )

/**
 * Creates a [Lens] from [List] of [Float] to [String]
 */
fun <P> Lens<P, List<Float>>.asString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): Lens<P, String> =
    this + format(
        { it.removePrefix(prefix).removeSuffix(postfix).split(separator).map(String::toFloat) },
        { it.joinToString(separator, prefix, postfix) }
    )

/**
 * Creates a [Lens] from [List] of [String] to [String]
 */
fun <P> Lens<P, List<String>>.asString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): Lens<P, String> =
    this + format(
        { it.removePrefix(prefix).removeSuffix(postfix).split(separator) },
        { it.joinToString(separator, prefix, postfix) }
    )