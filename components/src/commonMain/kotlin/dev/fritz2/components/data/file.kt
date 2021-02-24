package dev.fritz2.components.data

/**
 *
 * @author Jan Weidenhaupt
 * 09.02.2021
 */
open class File(val name: String, val type: String, val size: Long, val content: String) {
    override fun toString(): String = "File(name=$name, type=$type, size=$size, content: $content)"
}