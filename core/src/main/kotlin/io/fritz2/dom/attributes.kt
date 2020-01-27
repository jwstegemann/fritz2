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
        thisRef.attribute(property.name, values.map{s -> s.toString()})
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
interface WithAttributes<out T : Element> : WithDomNode<T> {

    fun attribute(name: String, value: String) = domNode.setAttribute(name, value)
    fun attribute(name: String, values: Flow<String>) = values.bind(name)
    //TODO: convenience-methods for data-attributes

    fun Flow<String>.bind(name: String) = AttributeMountPoint(name, this, domNode)
}