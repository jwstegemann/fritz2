package io.fritz2.examples.nestedmodel

import io.fritz2.binding.Lens
import io.fritz2.binding.Store
import io.fritz2.binding.each
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import io.fritz2.binding.map


data class Outer(val inner: Inner, val value: String) {
    companion object {
        val innerLens = object : Lens<Outer, Inner> {
            override fun get(parent: Outer): Inner = parent.inner
            override fun set(parent: Outer, value: Inner): Outer = parent.copy(inner = value)
            override fun map(parent: Outer, mapper: (Inner) -> Inner): Outer = parent.copy(inner = mapper(parent.inner))
        }

        val valueLens = object : Lens<Outer, String> {
            override fun get(parent: Outer): String = parent.value
            override fun set(parent: Outer, value: String): Outer = parent.copy(value = value)
            override fun map(parent: Outer, mapper: (String) -> String): Outer = parent.copy(value = mapper(parent.value))
        }
    }
}

data class Inner(val value: String) {
    companion object {
        val valueLens = object : Lens<Inner, String> {
            override fun get(parent: Inner): String = parent.value
            override fun set(parent: Inner, value: String): Inner = parent.copy(value = value)
            override fun map(parent: Inner, mapper: (String) -> String): Inner = parent.copy(value = mapper(parent.value))
        }
    }
}



@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val model = Store(Outer(Inner("hello"), "world"))
    val outerValue = model.sub(Outer.valueLens)
    val innerValue = model.sub(Outer.innerLens).sub(Inner.valueLens)

    val myComponent = html {
        div {
            input {
                value = outerValue.data
                outerValue.update <= changes
            }
            span {
                +"Outer.value = "
                +outerValue.data
            }
            input {
                value = innerValue.data
                innerValue.update <= changes
            }
            span {
                +"Inner.value = "
                +innerValue.data
            }
            div {
                +"Model = "
                + model.data.map {
                    it.toString()
                }
            }
        }
    }

    myComponent.mount("target")
}