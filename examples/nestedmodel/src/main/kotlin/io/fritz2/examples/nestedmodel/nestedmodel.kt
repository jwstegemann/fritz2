package io.fritz2.examples.nestedmodel

import io.fritz2.binding.*
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import io.fritz2.binding.mapItems


data class Outer(val inner: Inner, val value: String, val seq: List<Element>) {
    companion object {
        val innerLens = object : Lens<Outer, Inner> {
            override fun get(parent: Outer): Inner = parent.inner
            override fun set(parent: Outer, value: Inner): Outer = parent.copy(inner = value)
        }

        val valueLens = object : Lens<Outer, String> {
            override fun get(parent: Outer): String = parent.value
            override fun set(parent: Outer, value: String): Outer = parent.copy(value = value)
        }

        val seqLens = object : Lens<Outer, List<Element>> {
            override fun get(parent: Outer): List<Element> = parent.seq
            override fun set(parent: Outer, value: List<Element>): Outer = parent.copy(seq = value)
        }
    }
}

data class Inner(val value: String) {
    companion object {
        val valueLens = object : Lens<Inner, String> {
            override fun get(parent: Inner): String = parent.value
            override fun set(parent: Inner, value: String): Inner = parent.copy(value = value)
        }
    }
}

data class Element(val value: String, override val id: String) : WithId {
    companion object {
        val valueLens = object : Lens<Element, String> {
            override fun get(parent: Element): String = parent.value
            override fun set(parent: Element, value: String): Element = parent.copy(value = value)
        }
    }
}



@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val model = RootStore(Outer(Inner("hello"), "world", listOf(
        Element("one","n1"),
        Element("two","n2"),
        Element("three","n3")
    )))
    val outerValue = model.sub(Outer.valueLens)
    val innerValue = model.sub(Outer.innerLens).sub(Inner.valueLens)
    val seq = model.sub(Outer.seqLens)

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
            ul {
                seq.eachStore().mapItems {
                    val elementValue = it.sub(Element.valueLens)
                    //important to call html here, because otherwise children are added to dom directly and will not be removed, etc.
                    html {
                        li {
                            input {
                                value = elementValue.data
                                elementValue.update <= changes
                            }
                        }
                    }
                }.bind()
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