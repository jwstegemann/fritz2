package dev.fritz2.components


import dev.fritz2.binding.RootStore
import dev.fritz2.dom.html.Label
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.ColorProperty
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take

// todo on hold, will be revisited after context redesign

internal object SwitchFoundation {
    val css = staticStyle(
        "switch",
        """
        """
    )
}

fun RenderContext.f2NewSwitch(
    state: Flow<Boolean>,
    styles: Style<BasicParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: SwitchVariants.() -> SwitchVariantsInterface = { toggle },
    id: String = uniqueId(),
    init: SwitchContext.() -> Any
): Label {
    return label(
//        baseClass = "${SwitchFoundation.css} ${
//            use(
//                //SwitchFoundation.color(color) +
//                    SwitchVariants.variant().label +
//                    styles,
//                "label"
//            )
//        }",
        id = "switch-label-$id"
    ) {
        state.take(1).render { initialState ->
            div {
                val stateStore = RootStore(initialState)
                //stateStore.data.map { isChecked ->
                div {
//                        var styleFun = use(
//                            SwitchVariants.variant().input +
//                                    styles,
//                            "input"
//                        )
//                        if (isChecked) {
//                            styleFun = dev.fritz2.styling.params.use(
//                                SwitchVariants.variant().input,
//                                SwitchVariants.variant().inputChecked,
//                                styles, "input"
//                            )
//                    }
                        input(
                            //styleFun,
                            id = "switch-input-$id"
                        ) {
                            name("input-$id")
                            type("checkbox")
                            checked(state)
                            changes.states() handledBy stateStore.update
                        }

//                        styleFun = use(
//                            SwitchVariants.variant().span +
//                                styles
//                            , "span"
//                        )
//                        if (isChecked) {
//                            styleFun = dev.fritz2.styling.params.use(
//                                SwitchVariants.variant().span,
//                                SwitchVariants.variant().spanChecked,
//                                styles, "span"
//                            )
//                        }
                        span(
                            // styleFun,
                            id = "switch-span-$id"
                        ) {}

                        SwitchContext(
                            stateStore.data
                        ).init()
                    }
                //}() // stateStore data map
            }
        }
    }
}


interface SwitchVariantsInterface {
    val label: Style<BasicParams>
    val input: Style<BasicParams>
    val inputChecked: Style<BasicParams>
    val span: Style<BasicParams>
    val spanChecked: Style<BasicParams>
    val variant: String
}

object SwitchVariants {
    // --- Variant Toggle: Checking happens in SPAN ---
    val toggle = object : SwitchVariantsInterface {
        override val variant: String = "toggle"
        override val label: Style<BasicParams> = {
            css(
                """ 
                position: relative;
                display: inline-block;
                width: 10em;
                height: 3.5em;
                """
            )
        }
        override val input: Style<BasicParams> = {
            display { none }
        }
        override val inputChecked: Style<BasicParams> = {}
        override val span: Style<BasicParams> = { // unchecked
            css(
                """
                position: absolute;
                cursor: pointer;
                top: 1.5em;
                left: 2em;
                width: 4em;
                height: 2em;
                border-radius: 1em;
                transition: all .3s ease-in-out;
                background-color: #c32e04;
                """
            )
            before {
                css(
                    """
                    position: absolute;
                    content: "";
                    height: 1.6em;
                    width: 1.6em;
                    left: .2em;
                    bottom: .2em;
                    border-radius: 50%;
                    transition: all .3s ease-in-out;
                    background-color: white;
                    """
                )
            }
        }
        override val spanChecked: Style<BasicParams> = {
            css(
                """
                background-color: #5a9900;
                """
            )
            before {
                css(
                    """
                    transform: translateX(1.9em);
                    """
                )
            }
        }
    }

    // todo Sachen, die vom Theme kommen, hier raus nehmen
    // --- Variant Checkbox: Checking happens in INPUT ---
    val checkbox = object : SwitchVariantsInterface {
        override val variant: String = "checkbox"
        override val label: Style<BasicParams> = {}
        override val input: Style<BasicParams> = {

        }
        override val inputChecked: Style<BasicParams> = {}
        override val span: Style<BasicParams> = {} // unchecked
        override val spanChecked: Style<BasicParams> = {}
    }
}

class SwitchContext(val state: Flow<Boolean>)

// todo label, inactive, einbauen
// todo chakra benutzt controlbox zum styling

//fun RenderContext.f2Switch(
//    styles: Style<BasicParams> = {},
//    state: Flow<Boolean>,
//    variant: SwitchVariants.() -> SwitchVariantsInterface = { toggle },
//    id: String = uniqueId(),
//    init: SwitchContext.() -> Any
//): Label {
//    return label(
//        use(SwitchVariants.variant().label, styles, "label"),
//        id = "switch-label-$id"
//    ) {
//        state.take(1).map { initialState ->
//            div {
//                val stateStore = RootStore(initialState)
//                stateStore.data.map { isChecked ->
//                    div {
//                        var styleFun = use(
//                            SwitchVariants.variant().input,
//                            styles, "input"
//                        )
//                        if (isChecked)
//                            styleFun = use(
//                                SwitchVariants.variant().input,
//                                SwitchVariants.variant().inputChecked,
//                                styles, "input"
//                            )
//
//                        input(
//                            styleFun,
//                            id = "switch-input-$id"
//                        ) {
//                            name("input-$id")
//                            type("checkbox")
//                            checked = state
//                            changes.states() handledBy stateStore.update
//                        }
//
//                        styleFun = use(
//                            SwitchVariants.variant().span,
//                            styles, "span"
//                        )
//                        if (isChecked) {
//                            styleFun = use(
//                                SwitchVariants.variant().span,
//                                SwitchVariants.variant().spanChecked,
//                                styles, "span"
//                            )
//                        }
//                        span(
//                            styleFun,
//                            id = "switch-span-$id"
//                        ) {}
//
//                        SwitchContext(
//                            stateStore.data
//                        ).init()
//                    }
//                }()
//            }
//        }()
//    }
//}