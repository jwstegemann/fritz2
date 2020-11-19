import dev.fritz2.styling.theme.*


interface ExtendedTheme : Theme {
    interface MyProp {
        val a: Property
        val b: Property
    }

    val test: MyProp

    val teaserText: PredefinedBasicStyle
}

open class SmallFonts : ExtendedTheme, DefaultTheme() {
    override val name = "small Fonts"

    override val test = object : ExtendedTheme.MyProp {
        override val a: Property = space.normal
        override val b: Property = "b"
    }

    override val teaserText: PredefinedBasicStyle = {
        fontWeight { semiBold }
        textTransform { uppercase }
        fontSize { smaller }
        letterSpacing { large }
        textShadow { glowing }
        color { info }
    }
}


class LargeFonts : SmallFonts() {
    override val name = "large Fonts"

    override val fontSizes = ScaledValue(
        smaller = "1.125rem",
        small = "1.25rem",
        normal = "1.5rem",
        large = "1.875rem",
        larger = "2.25rem",
        huge = "3rem"
    )
}

