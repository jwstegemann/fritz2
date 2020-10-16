/*
@ExperimentalCoroutinesApi
fun HtmlElements.formControlDemo(): Div {
    return div {
        f2Flex {
            padding { "1rem" }
            direction { column }
        }.apply() {
            f2Text().apply() { +"FormControl" }
            f2FormControl {
                type { singleSelect }
                label { "SingleSelect:" }
                control {
                    f2Input().apply {
                        placeholder = const("Foo")
                    }
                }
            }.apply { }

            f2FormControl {
                type { input }
                invalid = true
                label { "Input:" }
                helperText {"Enter something!"}
                errorMessage {"Very serious error!"}
                control {
                    f2Input().apply {
                        placeholder = const("Foo")
                    }
                }
            }.apply { }
        }
    }
}

 */