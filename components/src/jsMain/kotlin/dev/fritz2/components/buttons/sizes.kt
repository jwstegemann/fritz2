package dev.fritz2.components.buttons

import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style

object ButtonSizes {
    val normal: Style<BasicParams> = {
        height { "2.5rem" } //TODO: smallSizes in Theme
        minWidth { "2.5rem" }
        fontSize { normal }
        paddings {
            horizontal { normal }
        }
    }
}
