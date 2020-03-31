package io.fritz2.dom

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.Element
import kotlin.reflect.KProperty

@ExperimentalCoroutinesApi
@FlowPreview
//TODO: different datatypes for attributes (List of String for classes, etc.)
object AttributeDelegate {
    operator fun <X : Element, T: Any> getValue(thisRef: Tag<X>, property: KProperty<*>): Flow<T> = throw NotImplementedError()
    operator fun <X : Element, T: Any> setValue(thisRef: Tag<X>, property: KProperty<*>, values: Flow<T>) {
        thisRef.apply { values.map{ s -> s.toString()}.bindAttr(property.name) }
    }
}

/**
 * [ValueAttributeDelegate] is a special attribute delegate for
 * the html value attribute.
 */
@ExperimentalCoroutinesApi
@FlowPreview
object ValueAttributeDelegate {
    operator fun <X : Element> getValue(thisRef: Tag<X>, property: KProperty<*>): Flow<String> = throw NotImplementedError()
    operator fun <X : Element> setValue(thisRef: Tag<X>, property: KProperty<*>, values: Flow<String>) {
        ValueAttributeMountPoint(values, thisRef.domNode)
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
interface WithAttributes<out T : Element> : WithDomNode<T> {

    fun attr(name: String, value: String) = domNode.setAttribute(name, value)
    fun attr(name: String, values: List<String>) = domNode.setAttribute(name, values.joinToString(separator = " "))

    fun Flow<String>.bindAttr(name: String) = AttributeMountPoint(name, this, domNode)
    fun Flow<List<String>>.bindAttr(name: String) = AttributeMountPoint(name, this.map{ l -> l.joinToString(separator = " ")}, domNode)
    fun Flow<Map<String, Boolean>>.bindAttr(name: String) = AttributeMountPoint(name, this.map{ m ->
            m.filter { it.value }.keys.joinToString(" ")
    }, domNode)
}