package dev.fritz2.core

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