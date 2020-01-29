package io.fritz2.examples.nestedmodel

import io.fritz2.binding.*
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import io.fritz2.binding.mapItems
import io.fritz2.optics.Optics
import io.fritz2.optics.withId
import io.fritz2.optics.buildLens

@Optics
data class Outer(val inner: Inner, val value: String, val seq: List<Element>) {
    companion object {
        val innerLens = buildLens<Outer,Inner>({p -> p.inner}, {p,v -> p.copy(inner = v)})
        val valueLens = buildLens<Outer,String>({p -> p.value}, {p,v -> p.copy(value = v)})
        val seqLens = buildLens<Outer,List<Element>>({p -> p.seq}, {p,v -> p.copy(seq = v)})
    }
}

data class Inner(val value: String) {
    companion object {
        val valueLens = buildLens<Inner,String>({p -> p.value}, {p,v -> p.copy(value = v)})
    }
}

data class Element(val value: String, override val id: String) : withId {
    companion object {
        val valueLens = buildLens<Element,String>({p -> p.value}, {p,v -> p.copy(value = v)})
    }
}



@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val model = Store(Outer(Inner("hello"), "world", listOf(
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
                    li {
                        input {
                            value = elementValue.data
                            elementValue.update <= changes
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