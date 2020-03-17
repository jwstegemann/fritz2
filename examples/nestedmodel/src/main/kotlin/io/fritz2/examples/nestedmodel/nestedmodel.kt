package io.fritz2.examples.nestedmodel

import io.fritz2.binding.RootStore
import io.fritz2.binding.eachStore
import io.fritz2.dom.html.tags
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val model = RootStore(
        Outer(
            Inner("hello"), "world", listOf(
                Element("one", "n1"),
                Element("two", "n2"),
                Element("three", "n3")
            )
        )
    )
    val outerValue = model.sub(Lenses.Outer.value)
    val innerValue = model.sub(Lenses.Outer.inner).sub(Lenses.Inner.value)
    val seq = model.sub(Lenses.Outer.seq)

    val myComponent = tags {
        div {
            input {
                value = outerValue.data
                type = !"text"
                attribute("test", outerValue.data)
                attribute("test-abc", "abc")
                attributeData("extra", "abc")
                outerValue.update <= changes
            }
            span {
                text("Outer.value = ")
                text(outerValue.data)
            }
            input {
                value = innerValue.data
                innerValue.update <= changes
            }
            span {
                text("Inner.value = ")
                text(innerValue.data)
            }
            ul {
                bind(seq.eachStore().map {
                    tags {
                        val elementValue = it.sub(Lenses.Element.value)
                        li {
                            input {
                                attribute("id", it.id)
                                value = elementValue.data
                                elementValue.update <= changes
                            }
                        }
                    }
                })
            }
            div {
                text("Model = ")
                text(
                    model.data.map {
                        it.toString()
                    }
                )
            }
        }
    }

    myComponent.mount("target")

}