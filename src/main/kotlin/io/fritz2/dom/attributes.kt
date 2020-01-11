package io.fritz2.dom

import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Element
import kotlin.reflect.KProperty

object AttributeDelegate {
    operator fun getValue(thisRef: Tag, property: KProperty<*>): Flow<String> = throw NotImplementedError()
    operator fun setValue(thisRef: Tag, property: KProperty<*>, values: Flow<String>) {
        thisRef.attribute(property.name, values)
    }
}

interface WithAttributes<out T : Element> : WithDomNode<T> {

    fun attribute(name: String, value: String) = domNode.setAttribute(name, value)
    fun attribute(name: String, values: Flow<String>) = values.bind(name)
    //TODO: convenience-methods for data-attributes

    fun Flow<String>.bind(name: String) = AttributeMountPoint(name, this, domNode)
}