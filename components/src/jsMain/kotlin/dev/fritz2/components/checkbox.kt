package dev.fritz2.components

import dev.fritz2.binding.*
import dev.fritz2.dom.WithEvents
import dev.fritz2.dom.html.*
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.*
import dev.fritz2.styling.staticStyle
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLInputElement

// todo add checkmark, alternatively change disabled style

fun HtmlElements.checkbox(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "checkboxComponent",
    build: CheckboxComponent.() -> Unit = {}
) {
    val component = CheckboxComponent().apply(build)

    (::div.styled(
        baseClass = baseClass,
        id = id,
        prefix = prefix) {
        styling() // attach user styling to container only
    }) {
        (::input.styled(
            baseClass = CheckboxComponent.checkboxInputStaticCss,
            id = "$id-input",
            prefix = prefix) {
            CheckboxComponent.checkboxStyles()
            component.checkedBackgroundColor()

        }) {
            type = const("checkbox")
            checked = component.checked
            disabled = component.disabled
            component.events?.invoke(this)
        }
        (::label.styled(
            baseClass = CheckboxComponent.checkboxLabelStaticCss,
            id = "$id-label",
            extension = "$id-input", // for
            prefix = prefix) {
            component.checkboxSize()
            component.backgroundColor()
            component.borderColor()
        }) {
            component.text.bind()
        }
    }
}

class CheckboxComponent {

    var checkboxSize: Style<BasicParams> = { CheckboxSizes.small } // @label
    fun checkboxSize(value: CheckboxSizes.() -> Style<BasicParams>) {
        checkboxSize = CheckboxSizes.value()
    }

    var text: Flow<String> = const("") // @label
    fun text(value: Flow<String>) {
        text = value
    }

    var backgroundColor: Style<BasicParams> = {} // @label
    fun backgroundColor(value: () -> ColorProperty) {
        backgroundColor = {
            css("&::before { background-color: ${value()};}")
        }
    }

    var borderColor: Style<BasicParams> = {} // @label
    fun borderColor(value: () -> ColorProperty) {
        borderColor = {
            css("&::before { border-color: ${value()};}")
        }
    }

    var checkedBackgroundColor: Style<BasicParams> = {} // @input
    fun checkedBackgroundColor(value: () -> ColorProperty) {
        checkedBackgroundColor = {
            css("&:checked + label::before { background-color: ${value()}; }")
        }
    }

    var events: (WithEvents<HTMLInputElement>.() -> Unit)? = null // @input
    fun events(value: WithEvents<HTMLInputElement>.() -> Unit) {
        events = value
    }

    // todo: for user, these are only distinguished  from Input.xxx by signature
    var checked: Flow<Boolean> = const(false) // @input
    fun checked(value: () -> Flow<Boolean>) {
        checked = value()
    }
    var disabled: Flow<Boolean> = const(false) // @input
    fun disabled(value: () -> Flow<Boolean>) {
        disabled = value()
    }

    companion object {

        // todo when using with checkboxgroup, duplicated css code is passed instead of this
        // todo replace px with rem where not explicit (not due for 0.8 snapshot)
        object CheckboxSizes {
            val small: Style<BasicParams> = {
                fontSize { small }
                before {
                    height { "10px" }
                    width { "10px" }
                    border {
                        width { "1px" }
                    }
                    margins {
                        right { "4px" }
                    }
                    position {
                        relative {
                            bottom { "1px" }
                        }
                    }
                    radii {
                        top { smaller }
                        bottom { smaller }
                        left { smaller }
                        right { smaller }
                    }
                }
            }
            val normal: Style<BasicParams> = {
                fontSize { normal }
                before {
                    height { "20px" }
                    width { "20px" }
                    border {
                        width { "2px" }
                    }
                    margins {
                        right { "7px" }
                    }
                    position {
                        relative {
                            bottom { "2px" }
                        }
                    }
                    radii {
                        top { normal }
                        bottom { normal }
                        left { normal }
                        right { normal }
                    }
                }
            }
            val large: Style<BasicParams> = {
                fontSize { larger }
                before {
                    height { "30px" }
                    width { "30px" }
                    border {
                        width { "3px" }
                    }
                    margins {
                        right { "10px" }
                    }
                    position {
                        relative {
                            bottom { "3px" }
                        }
                    }
                    radii {
                        top { larger }
                        bottom { larger }
                        left { larger }
                        right { larger }
                    }
                }
            }
        }

        val checkboxStyles: Style<BasicParams> = {
            lineHeight { normal }
            radius { normal }
            fontWeight { normal }
            paddings { horizontal { small } }
            border {
                width { thin }
                style { solid }
                color { light }
            }
        }

        val checkboxInputStaticCss = staticStyle(
            "checkbox",
            """
                position: absolute;
                height: 1px;                
                width: 1px;                
                overflow: hidden;
                clip: rect(1px 1px 1px 1px); /* IE6, IE7 */
                clip: rect(1px, 1px, 1px, 1px);
                outline: none;
                &:checked + label::before {
                    border-style: solid;
                    outline: none;
                }
                &:focus + label::before {
                    box-shadow: 0 0px 4px #373737;
                }
                &:disabled + label {
                    color: #878787;
                    cursor: not-allowed;
                }
                &:disabled + label::before {
                    opacity: 0.3;
                    cursor: not-allowed;
                    boxShadow: none;
                    color: #575757;
                }
                &:focus{
                    outline: none;
                }
            """
        )

        val checkboxLabelStaticCss = staticStyle(
            "checkboxlabel",
            """
            display: block;
            position: relative;
            margin-right: 1.0rem;
            &::before {
                content: '';
                position: relative;
                display: inline-block;
                vertical-align: middle;
                box-shadow: 0 0 2px #575757 inset;
            }
            """
        )
    }
}