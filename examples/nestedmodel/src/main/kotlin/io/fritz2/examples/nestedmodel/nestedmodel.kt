package io.fritz2.examples.nestedmodel

import io.fritz2.binding.*
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map


@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val model = Store(Outer(Inner("hello"), "world", listOf(
        Element("one","n1"),
        Element("two","n2"),
        Element("three","n3")
    )))
    val outerValue = model.sub(OuterValueLens)
    val innerValue = model.sub(OuterInnerLens).sub(InnerValueLens)
    val seq = model.sub(OuterSeqLens)

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
                    html {
                        val elementValue = it.sub(ElementValueLens)
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