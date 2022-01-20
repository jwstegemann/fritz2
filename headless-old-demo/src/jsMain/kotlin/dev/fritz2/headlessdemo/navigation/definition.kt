package dev.fritz2.headlessdemo.navigation

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.headlessdemo.demos.headlessListboxDemo
import dev.fritz2.headlessdemo.overview

fun navigationDefinition(): Navigation.() -> Unit = {
    defaultPage("overview")
    category("Welcome", displayInOverview = false) {
        page("Overview", "overview", RenderContext::overview)
    }

    category("Components") {
        page("Listbox", "headless-listbox", RenderContext::headlessListboxDemo)
        /*
        page("RadioGroup", "headless-radiogroup", RenderContext::headlessRadioDemo)
        page("CheckboxGroup", "headless-checkboxgroup", RenderContext::headlessCheckboxDemo)
        page("Switch", "headless-switch", RenderContext::headlessSwitchDemo)
        page("Disclosure", "headless-disclosure", RenderContext::headlessDisclsoureDemo)
        page("Modal", "headless-modal", RenderContext::headlessModalDemo)
        page("Tab", "headless-tab", RenderContext::headlessTabDemo)
        page("Input", "headless-input", RenderContext::headlessInputDemo)
        page("PopOver", "headless-popover", RenderContext::headlessPopOverDemo)
        page("Menu", "headless-menu", RenderContext::headlessMenuDemo)
        page("Tooltip", "headless-tooltip", RenderContext::headlessTooltipDemo)

         */
    }

}
