package dev.fritz2.components.buttons

import dev.fritz2.styling.params.BasicStyleParams
import dev.fritz2.styling.params.Style

object ButtonSizes {
    val normal: Style<BasicStyleParams> = {
        height { "2.5rem" } //TODO: smallSizes in Theme
        minWidth { "2.5rem" }
        fontSize { normal }
        paddings {
            horizontal { normal }
        }
    }
}
