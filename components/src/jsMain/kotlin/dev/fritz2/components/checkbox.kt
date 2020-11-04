package dev.fritz2.components

import dev.fritz2.binding.*
import dev.fritz2.dom.WithEvents
import dev.fritz2.dom.html.*
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.*
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLInputElement

// todo add checkmark

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
                CheckboxComponent.checkboxLabelStyles()
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

    var text: Flow<String> = const("CheckboxLabel") // @label
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

         // todo replace px with rem/theme values where not explicit (not due for 0.8 snapshot)
        object CheckboxSizes { // @ label
            val small: Style<BasicParams> = {
                fontSize { small }
                lineHeight { small }
                before {
                    height { "10px" }
                    width { "10px" }
                    before {
                        radii {
                            top { smaller }
                            bottom { smaller }
                            left { smaller }
                            right { smaller }
                        }
                    }
                    margins {
                        right { "4px" }
                    }
                    position {
                        relative {
                            bottom { "1px" }
                        }
                    }
                }
            }
            val normal: Style<BasicParams> = {
                fontSize { normal }
                lineHeight { normal }
                before {
                    height { "20px" }
                    width { "20px" }
                    margins {
                        right { "7px" }
                    }
                    position {
                        relative {
                            bottom { "2px" }
                        }
                    }
                }
            }
            val large: Style<BasicParams> = {
                fontSize { larger }
                lineHeight { larger }
                before {
                    height { "30px" }
                    width { "30px" }
                    margins {
                        right { "10px" }
                    }
                    position {
                        relative {
                            bottom { "3px" }
                        }
                    }
                }
            }
        }

        val checkboxLabelStyles: Style<BasicParams> = { // @label
            before {
                radii {// overwritten by CheckboxSizes.small only
                    top { normal }
                    bottom { normal }
                    left { normal }
                    right { normal }
                }
                border {
                    style { solid }
                    width { "0.1rem" }
                }
            }
            margins {
                right { "1.0rem" }
            }
        }

        // todo using theme colors in static styles probably does not work when changing themes
        val checkboxInputStaticCss = staticStyle( // @input
            "checkbox",
            """
            position: absolute;
            height: 1px;                
            width: 1px;                
            overflow: hidden;
            clip: rect(1px 1px 1px 1px); /* IE6, IE7 */
            clip: rect(1px, 1px, 1px, 1px);
            outline: none;
            &:focus + label::before {
                box-shadow: 0 0 1px ${theme().colors.dark}; 
            }
            &:disabled + label {
                color: ${theme().colors.disabled};
                cursor: not-allowed;
            }
            &:disabled + label::before {
                color: ${theme().colors.disabled};
                opacity: 0.3;
                cursor: not-allowed;
                boxShadow: none;
            }
            &:focus{
                outline: none;
            }
            """
        )

        val checkboxLabelStaticCss = staticStyle( // @input
            "checkboxlabel",
            """
            position: relative;            
            display: block;
            &::before {
                content: '';
                outline: none;
                position: relative;
                display: inline-block;
                box-shadow: 0 0 1px ${theme().colors.dark} inset;
                vertical-align: middle;
            }
            """
        )
    }
}