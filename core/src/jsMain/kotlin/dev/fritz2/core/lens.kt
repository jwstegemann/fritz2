package dev.fritz2.core

/**
 * Creates a [Lens] from [Byte] to [String]
 */
fun <P> Lens<P, Byte>.asString(): Lens<P, String> =
    this + lensOf(Byte::toString, String::toByte)

/**
 * Creates a [Lens] from [Byte] to [String]
 */
fun <P> Lens<P, Byte?>.asString(emptyValue: String = ""): Lens<P, String> =
    this + lensOf({ it?.toString() ?: emptyValue }, { if (it.isBlank()) null else it.toByte() })

/**
 * Creates a [Lens] from [Short] to [String]
 */
fun <P> Lens<P, Short>.asString(): Lens<P, String> =
    this + lensOf(Short::toString, String::toShort)

/**
 * Creates a [Lens] from [Short] to [String]
 */
fun <P> Lens<P, Short?>.asString(emptyValue: String = ""): Lens<P, String> =
    this + lensOf({ it?.toString() ?: emptyValue }, { if (it.isBlank()) null else it.toShort() })

/**
 * Creates a [Lens] from [Int] to [String]
 */
fun <P> Lens<P, Int>.asString(): Lens<P, String> =
    this + lensOf(Int::toString, String::toInt)

/**
 * Creates a [Lens] from [Int] to [String]
 */
fun <P> Lens<P, Int?>.asString(emptyValue: String = ""): Lens<P, String> =
    this + lensOf({ it?.toString() ?: emptyValue }, { if (it.isBlank()) null else it.toInt() })

/**
 * Creates a [Lens] from [Long] to [String]
 */
fun <P> Lens<P, Long>.asString(): Lens<P, String> =
    this + lensOf(Long::toString, String::toLong)

/**
 * Creates a [Lens] from [Long] to [String]
 */
fun <P> Lens<P, Long?>.asString(emptyValue: String = ""): Lens<P, String> =
    this + lensOf({ it?.toString() ?: emptyValue }, { if (it.isBlank()) null else it.toLong() })

/**
 * Creates a [Lens] from [Double] to [String]
 */
fun <P> Lens<P, Double>.asString(): Lens<P, String> =
    this + lensOf(Double::toString, String::toDouble)

/**
 * Creates a [Lens] from [Double] to [String]
 */
fun <P> Lens<P, Double?>.asString(emptyValue: String = ""): Lens<P, String> =
    this + lensOf({ it?.toString() ?: emptyValue }, { if (it.isBlank()) null else it.toDouble() })

/**
 * Creates a [Lens] from [Float] to [String]
 */
fun <P> Lens<P, Float>.asString(): Lens<P, String> =
    this + lensOf(Float::toString, String::toFloat)

/**
 * Creates a [Lens] from [Float] to [String]
 */
fun <P> Lens<P, Float?>.asString(emptyValue: String = ""): Lens<P, String> =
    this + lensOf({ it?.toString() ?: emptyValue }, { if (it.isBlank()) null else it.toFloat() })

/**
 * Creates a [Lens] from [Boolean] to [String]
 */
fun <P> Lens<P, Boolean>.asString(): Lens<P, String> =
    this + lensOf(Boolean::toString, String::toBoolean)

/**
 * Creates a [Lens] from [Boolean] to [String]
 */
fun <P> Lens<P, Boolean?>.asString(emptyValue: String = ""): Lens<P, String> =
    this + lensOf({ it?.toString() ?: emptyValue }, { if (it.isBlank()) null else it.toBoolean() })
