package io.fritz2.examples.nestedmodel

import io.fritz2.binding.RootStore
import io.fritz2.binding.eachStore
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import io.fritz2.dom.value
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val model = RootStore(Outer(Inner("hello"), "world", listOf(
        Element("one","n1"),
        Element("two","n2"),
        Element("three","n3")
    )))
    val outerValue = model.sub(Lenses.Outer.value)
    val innerValue = model.sub(Lenses.Outer.inner).sub(Lenses.Inner.value)
    val seq = model.sub(Lenses.Outer.seq)

    val myComponent = html {
        div {
            input {
                value = outerValue.data
                outerValue.update <= changes.value()
            }
            span {
                +"Outer.value = "
                +outerValue.data
            }
            input {
                value = innerValue.data
                innerValue.update <= changes.value()
            }
            span {
                +"Inner.value = "
                +innerValue.data
            }
            ul {
                seq.eachStore().map {
                    html {
                        val elementValue = it.sub(Lenses.Element.value)
                        li {
                            input {
                                attribute("id", it.id)
                                value = elementValue.data
                                elementValue.update <= changes.value()
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