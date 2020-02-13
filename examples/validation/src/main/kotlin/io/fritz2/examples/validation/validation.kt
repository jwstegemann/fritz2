package io.fritz2.examples.validation

import io.fritz2.binding.*
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map

enum class Severity {
    Info,
    Warning,
    Error
}

data class ValMsg(override val id: String, val severity: Severity, val text: String): Failable {
    override fun isFail(): Boolean = severity > Severity.Warning
}

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


        val updateWithValidation = handle<String> { data, newData ->
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
            div {
                +"state: "
                +store.validator.isValid.map{ v -> if(v) "valid" else "not valid"}
            }
            hre{}
            ul {
                store.msgs().each().map {
                    html {
                        li {
                            +it.text
                            `class` = !it.severity.name.toLowerCase()
                        }
                    }
                }.bind()
            }
        }
    }

    myComponent.mount("target")
}