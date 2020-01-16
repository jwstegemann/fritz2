package io.fritz2.dom

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Element
import kotlin.reflect.KProperty

@ExperimentalCoroutinesApi
@FlowPreview
//TODO: different datatypes for attributes (List of String for classes, etc.)
object AttributeDelegate {
    operator fun <X : Element> getValue(thisRef: Tag<X>, property: KProperty<*>): Flow<String> = throw NotImplementedError()
    operator fun <X : Element> setValue(thisRef: Tag<X>, property: KProperty<*>, values: Flow<String>) {
        thisRef.attribute(property.name, values)
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