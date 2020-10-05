import dev.fritz2.styling.params.and
import dev.fritz2.styling.params.rgba
import dev.fritz2.styling.params.shadow
import dev.fritz2.styling.theme.*

/**
 * defines the default values and scales for fritz2
 */
open class DefaultTheme : Theme {
    final override val breakPoints = ResponsiveValue("30em", "48em", "62em", "80em")

    override val mediaQueryMd: String = "@media screen and (min-width: ${breakPoints.md})"
    override val mediaQueryLg: String = "@media screen and (min-width: ${breakPoints.lg})"
    override val mediaQueryXl: String = "@media screen and (min-width: ${breakPoints.xl})"

    override val space = ScaledValue(
        none = "0",
        tiny = "0.25rem",
        smaller = "0.5rem",
        small = "0.75rem",
        normal = "1rem",
        large = "1.25rem",
        larger = "1.5rem",
        huge = "2rem",
        giant = "3rem",
        full = "4rem"
    )

    override val position = space

    override val gaps = space

    override val fontSizes = ScaledValue(
        smaller = "0.75rem",
        small = "0.875rem",
        normal = "1rem",
        large = "1.25rem",
        larger = "1.5rem",
        huge = "2rem",
        giant = "3rem",
        full = "4rem"
    )

    override val colors = object : Colors {
        override val primary = "#3d405b"
        override val secondary = "#e07a5f"
        override val tertiary = "#81b29a"
        override val success = "#28a745"
        override val danger = "#dc3545"
        override val warning = "#ffc107"
        override val info = "#17a2b8"
        override val light = "#f8f9fa"
        override val dark = "#343a40"
        override val disabled = "#6c757d"
    }

    override val fonts = object : Fonts {
        override val body =
            """-apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol" """
        override val heading =
            """-apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol" """
        override val mono = """SFMono-Regular,Menlo,Monaco,Consolas,"Liberation Mono","Courier New",monospace"""
    }

    override val lineHeights = ScaledValue(
        normal = "normal",
        tiny = "1",
        smaller = "1.25",
        small = "1.375",
        large = "1.5",
        larger = "1.625",
        huge = "2",
        giant = "2.25",
        full = "3"
    )

    override val letterSpacings = ScaledValue(
        smaller = "-0.05em",
        small = "-0.025em",
        normal = "0",
        large = "0.025em",
        larger = "0.05em",
        huge = "0.1em"
    )

    override val sizes = Sizes(
        tiny = "5rem",
        smaller = "8rem",
        small = "13rem",
        normal = "auto",
        large = "21rem",
        larger = "34rem",
        huge = "55rem",
        giant = "89rem",
        full = "100%"
    )

    override val borderWidths = Thickness<Property>(
        normal = "2px",
        thin = "1px",
        fat = "4px",
        hair = "0.1px"
    )

    override val radii = ScaledValue(
        none = "0",
        small = "0.125rem",
        normal = "0.25rem",
        large = "0.5rem",
        full = "9999px"
    )

    override val shadows = Shadows(
        flat = shadow("0", "1px", "3px", color = rgba(0, 0, 0, 0.12))
                and shadow("0", "1px", "2px", rgba(0, 0, 0, 0.24)),
        raised = shadow("0", "14px", "28px", rgba(0, 0, 0, 0.25))
                and shadow(" 0", "10px", "10px", rgba(0, 0, 0, 0.22)),
        raisedFurther = shadow("0", "14px", "28px", rgba(0, 0, 0, 0.25))
                and shadow("0", "10px", "10px", rgba(0, 0, 0, 0.22)),
        top = shadow("0", "19px", "38px", rgba(0, 0, 0, 0.30))
                and shadow("0", "15px", "12px", rgba(0, 0, 0, 0.22)),
        lowered = shadow("0", "2px", "4px", color = rgba(0, 0, 0, 0.06), inset = true),
        glowing = shadow("0", "0", "2px", color = rgba(0, 0, 255, 0.5))
    )

    override val zIndices = ZIndices(1, 100, 2, 200, 300, 2, 400, 2)

    override val opacities = WeightedValue(
        normal = "0.5"
    )
}
