package io.fritz2.examples.validation

import io.fritz2.binding.*
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

data class ValMsg(override val id: String, override val severity: Severity, val text: String): WithSeverity

@ExperimentalCoroutinesApi
@FlowPreview
object EMailValidator: Validator<String, ValMsg, String>() {
    override fun validate(data: String, metadata: String): List<ValMsg> {
        val msgs = mutableListOf<ValMsg>()

        if(data.isEmpty()) {
            msgs.add(ValMsg("empty", Severity.Info, "Please provide some input"))
        }

        if(!data.matches("(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)")) {
            msgs.add(ValMsg("not_matched", Severity.Error, "Please correct the email address!"))
        }

        if(data.length > 20) {
            msgs.add(ValMsg("very_long", Severity.Warning, "Is it correct that your email address is that long?"))
        }
        return msgs
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val store = object : RootStore<String>(""), Validation<String, ValMsg, String> {
        override val validator = EMailValidator

        val updateWithValidation = Handler<String> { data, newData ->
            if (validate(newData, "update")) newData
            else data
        }
    }

    val myComponent = html {
        section {
            label {
                +"EMail"
                input {
                    value = store.data
                    store.updateWithValidation <= changes
                }
            }
            div {
                +"value: "
                store.data.bind()
            }
            hre{}
            ul {
                store.msgs().each().mapItems {
                    html {
                        li {
                            +it.text
                        }
                    }
                }.bind()
            }
        }
    }

    myComponent.mount("target")
}