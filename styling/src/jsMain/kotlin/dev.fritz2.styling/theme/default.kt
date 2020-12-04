package dev.fritz2.styling.theme

import dev.fritz2.styling.params.*
import dev.fritz2.styling.params.BackgroundAttachments.inherit

/**
 * defines the default values and scales for fritz2
 */
open class DefaultTheme : Theme {
    override val name = "default"

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
        huge = "1.875rem",
        giant = "3rem",
        full = "4rem"
    )

    override val colors = object : Colors {
        override val secondary = "#b2f5ea" // rgb(178,245,234) new
        override val tertiary = "#718096" // rgb(61,64,92) formerly primary
        override val primary = "#319795" // rgb(49,151,149) formerly "#e07a5f" // rgb(224,122,95)
        override val success = "#28a745" // rgb(40,167,69)
        override val danger = "#dc3545" // rgb(220,53,69)
        override val warning = "#ffc107" // rgb(225,193,7)
        override val info = "#3182ce" // rgb(23,162,184)
        override val light = "#e2e8f0" // rgb(226,232,240)
        override val dark = "#2d3748" // rgb(45,55,72)
        override val base = "#ffffff" // rgb(255,255,255)
        override val primary_hover = "rgb(49,151,149, 0.3)"
        override val light_hover = "rgb(226,232,240, 0.5)"
        val alert = "feebc8" // rgb(254,235,200)
        override val disabled = light
        override val focus = "#3182ce"
        //color of focus of input elements: inner: #3182ce / rgb(49,130,206) outer: #acd2f2 / rgb(172,210,242)
    }

    override val fonts = object : Fonts {
        override val body =
            """Inter, sans-serif, -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol" """
        override val heading =
            """Inter, sans-serif, -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol" """
        override val mono = """SFMono-Regular,Menlo,Monaco,Consolas,"Liberation Mono","Courier New",monospace"""
    }

    override val lineHeights = ScaledValue(
        normal = "normal",
        tiny = "1.2",
        smaller = "1.25",
        small = "1.3",
        large = "1.5",
        larger = "1.7",
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
        tiny = "0.25rem",
        smaller = "0.5rem",
        small = "0.75rem",
        normal = "1rem",
        large = "1.25rem",
        larger = "1.5rem",
        huge = "1.75rem",
        giant = "2rem",
        full = "100%",
        wide = ScaledValue(
            tiny = "5rem",
            smaller = "8rem",
            small = "13rem",
            normal = "auto",
            large = "21rem",
            larger = "34rem",
            huge = "55rem",
            giant = "89rem",
        )
    )

    override val borderWidths = Thickness(
        none = "0px",
        normal = "2px",
        thin = "1px",
        fat = "4px",
        hair = "0.1px"
    )

    override val radii = ScaledValue(
        none = "0",
        smaller = "0.125rem",
        small = "0.225rem",
        normal = "0.375rem",
        large = "0.5rem",
        larger = "12px",
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
        glowing = shadow("0", "0", "2px", color = rgba(0, 0, 255, 0.5)),
        outline = shadow(
            "0",
            "0",
            "0",
            "3px",
            color = colors.secondary
        ), // changed by mkempa-np: formerly rgba(66, 153, 225, 0.6)
        danger = shadow("0", "0", "0", "1px", color = colors.danger)
    )

    override val zIndices = ZIndices(1, 100, 2, 200, 300, 2, 400, 2)

    override val opacities = WeightedValue(
        normal = "0.5"
    )

    override val icons = object : Icons {

        /*
         * Most of our provided icons are taken from the [MONO Icons](https://icons.mono.company/) Projekt, that
         * provides a solid set of useful icons. Some icons might be slightly modified and some icons
         * (like the outstanding fritz2 logo itself) are made by ourselves.
         */

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val add = IconDefinition(
            "add",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C12.5523 4 13 4.44772 13 5V11H19C19.5523 11 20 11.4477 20 12C20 12.5523 19.5523 13 19 13H13V19C13 19.5523 12.5523 20 12 20C11.4477 20 11 19.5523 11 19V13H5C4.44772 13 4 12.5523 4 12C4 11.4477 4.44772 11 5 11H11V5C11 4.44772 11.4477 4 12 4Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val all = IconDefinition(
            "all",
            svg = """
                
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val archive = IconDefinition(
            "archive",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 5C2 3.89543 2.89543 3 4 3H20C21.1046 3 22 3.89543 22 5V7C22 7.74708 21.5904 8.39848 20.9835 8.74188C20.9944 8.82638 21 8.91253 21 9V19C21 20.1046 20.1046 21 19 21H5C3.89543 21 3 20.1046 3 19V9C3 8.91253 3.00561 8.82638 3.0165 8.74188C2.40961 8.39848 2 7.74707 2 7V5ZM20 7V5H4V7H20ZM5 9V19H19V9H5ZM8 12C8 11.4477 8.44772 11 9 11H15C15.5523 11 16 11.4477 16 12C16 12.5523 15.5523 13 15 13H9C8.44772 13 8 12.5523 8 12Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val arrowDown = IconDefinition(
            "arrowDown",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C12.5523 4 13 4.44772 13 5V16.5858L17.2929 12.2929C17.6834 11.9024 18.3166 11.9024 18.7071 12.2929C19.0976 12.6834 19.0976 13.3166 18.7071 13.7071L12.7071 19.7071C12.3166 20.0976 11.6834 20.0976 11.2929 19.7071L5.29289 13.7071C4.90237 13.3166 4.90237 12.6834 5.29289 12.2929C5.68342 11.9024 6.31658 11.9024 6.70711 12.2929L11 16.5858V5C11 4.44772 11.4477 4 12 4Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val arrowLeftDown = IconDefinition(
            "arrowLeftDown",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M17.7071 6.29289C18.0976 6.68342 18.0976 7.31658 17.7071 7.70711L9.41421 16L15 16C15.5523 16 16 16.4477 16 17C16 17.5523 15.5523 18 15 18L7 18C6.44772 18 6 17.5523 6 17L6 9C6 8.44771 6.44772 8 7 8C7.55228 8 8 8.44771 8 9L8 14.5858L16.2929 6.29289C16.6834 5.90237 17.3166 5.90237 17.7071 6.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val arrowLeftUp = IconDefinition(
            "arrowLeftUp",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M8 9.41421L8 15C8 15.5523 7.55228 16 7 16C6.44772 16 6 15.5523 6 15L6 7C6 6.44772 6.44772 6 7 6L15 6C15.5523 6 16 6.44772 16 7C16 7.55228 15.5523 8 15 8L9.41421 8L17.7071 16.2929C18.0976 16.6834 18.0976 17.3166 17.7071 17.7071C17.3166 18.0976 16.6834 18.0976 16.2929 17.7071L8 9.41421Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val arrowLeft = IconDefinition(
            "arrowLeft",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M11.7071 5.29289C12.0976 5.68342 12.0976 6.31658 11.7071 6.70711L7.41421 11H19C19.5523 11 20 11.4477 20 12C20 12.5523 19.5523 13 19 13H7.41421L11.7071 17.2929C12.0976 17.6834 12.0976 18.3166 11.7071 18.7071C11.3166 19.0976 10.6834 19.0976 10.2929 18.7071L4.29289 12.7071C4.10536 12.5196 4 12.2652 4 12C4 11.7348 4.10536 11.4804 4.29289 11.2929L10.2929 5.29289C10.6834 4.90237 11.3166 4.90237 11.7071 5.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val arrowRightDown = IconDefinition(
            "arrowRightDown",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M6.29289 6.29289C6.68342 5.90237 7.31658 5.90237 7.70711 6.29289L16 14.5858L16 9C16 8.44772 16.4477 8 17 8C17.5523 8 18 8.44772 18 9L18 17C18 17.5523 17.5523 18 17 18H9C8.44771 18 8 17.5523 8 17C8 16.4477 8.44771 16 9 16H14.5858L6.29289 7.70711C5.90237 7.31658 5.90237 6.68342 6.29289 6.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val arrowRightUp = IconDefinition(
            "arrowRightUp",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M8 7C8 6.44772 8.44772 6 9 6L17 6C17.5523 6 18 6.44772 18 7V15C18 15.5523 17.5523 16 17 16C16.4477 16 16 15.5523 16 15V9.41421L7.70711 17.7071C7.31658 18.0976 6.68342 18.0976 6.29289 17.7071C5.90237 17.3166 5.90237 16.6834 6.29289 16.2929L14.5858 8L9 8C8.44772 8 8 7.55228 8 7Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val arrowRight = IconDefinition(
            "arrowRight",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12.2929 5.29289C12.6834 4.90237 13.3166 4.90237 13.7071 5.29289L19.7071 11.2929C19.8946 11.4804 20 11.7348 20 12C20 12.2652 19.8946 12.5196 19.7071 12.7071L13.7071 18.7071C13.3166 19.0976 12.6834 19.0976 12.2929 18.7071C11.9024 18.3166 11.9024 17.6834 12.2929 17.2929L16.5858 13L5 13C4.44772 13 4 12.5523 4 12C4 11.4477 4.44772 11 5 11L16.5858 11L12.2929 6.70711C11.9024 6.31658 11.9024 5.68342 12.2929 5.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val arrowUp = IconDefinition(
            "arrowUp",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C12.2652 4 12.5196 4.10536 12.7071 4.29289L18.7071 10.2929C19.0976 10.6834 19.0976 11.3166 18.7071 11.7071C18.3166 12.0976 17.6834 12.0976 17.2929 11.7071L13 7.41421L13 19C13 19.5523 12.5523 20 12 20C11.4477 20 11 19.5523 11 19L11 7.41421L6.70711 11.7071C6.31658 12.0976 5.68342 12.0976 5.29289 11.7071C4.90237 11.3166 4.90237 10.6834 5.29289 10.2929L11.2929 4.29289C11.4804 4.10536 11.7348 4 12 4Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val attachment = IconDefinition(
            "attachment",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M19.4628 5.57572C18.7755 4.82596 17.5344 4.77976 16.7071 5.60711L8.60706 13.7071C8.39758 13.9166 8.39758 14.1834 8.60706 14.3929C8.81653 14.6024 9.08337 14.6024 9.29284 14.3929L15.9928 7.69289C16.3834 7.30237 17.0165 7.30237 17.4071 7.69289C17.7976 8.08342 17.7976 8.71658 17.4071 9.10711L10.7071 15.8071C9.71653 16.7976 8.18337 16.7976 7.19284 15.8071C6.20232 14.8166 6.20232 13.2834 7.19284 12.2929L15.2928 4.19289C16.8604 2.62536 19.4077 2.57435 20.9223 4.2082C22.4746 5.77661 22.5203 8.31156 20.8928 9.82132L11.4071 19.3071C9.21653 21.4976 5.78337 21.4976 3.59284 19.3071C1.40232 17.1166 1.40232 13.6834 3.59284 11.4929L11.6928 3.39289C12.0834 3.00237 12.7165 3.00237 13.1071 3.39289C13.4976 3.78342 13.4976 4.41658 13.1071 4.80711L5.00706 12.9071C3.59758 14.3166 3.59758 16.4834 5.00706 17.8929C6.41653 19.3024 8.58337 19.3024 9.99284 17.8929L19.4928 8.39289C19.5031 8.38265 19.5135 8.37263 19.5242 8.36284C20.274 7.67556 20.3202 6.43445 19.4928 5.60711C19.4826 5.59686 19.4726 5.5864 19.4628 5.57572Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val ban = IconDefinition(
            "ban",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M5.68014 7.09436C4.62708 8.44904 4 10.1513 4 12C4 16.4183 7.58172 20 12 20C13.8487 20 15.551 19.3729 16.9056 18.3199L5.68014 7.09436ZM7.09436 5.68014L18.3199 16.9056C19.3729 15.551 20 13.8487 20 12C20 7.58172 16.4183 4 12 4C10.1513 4 8.44904 4.62708 7.09436 5.68014ZM2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val barChartAlt = IconDefinition(
            "barChartAlt",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C12.5523 4 13 4.44772 13 5V19C13 19.5523 12.5523 20 12 20C11.4477 20 11 19.5523 11 19V5C11 4.44772 11.4477 4 12 4ZM17 8C17.5523 8 18 8.44772 18 9V19C18 19.5523 17.5523 20 17 20C16.4477 20 16 19.5523 16 19V9C16 8.44772 16.4477 8 17 8ZM7 12C7.55228 12 8 12.4477 8 13V19C8 19.5523 7.55228 20 7 20C6.44772 20 6 19.5523 6 19V13C6 12.4477 6.44772 12 7 12Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val barChart = IconDefinition(
            "barChart",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M3 5C3 3.89543 3.89543 3 5 3H19C20.1046 3 21 3.89543 21 5V19C21 20.1046 20.1046 21 19 21H5C3.89543 21 3 20.1046 3 19V5ZM19 5H5V19H19V5ZM12 7C12.5523 7 13 7.44772 13 8V16C13 16.5523 12.5523 17 12 17C11.4477 17 11 16.5523 11 16V8C11 7.44772 11.4477 7 12 7ZM16 9C16.5523 9 17 9.44772 17 10V16C17 16.5523 16.5523 17 16 17C15.4477 17 15 16.5523 15 16V10C15 9.44772 15.4477 9 16 9ZM8 11C8.55228 11 9 11.4477 9 12V16C9 16.5523 8.55228 17 8 17C7.44772 17 7 16.5523 7 16V12C7 11.4477 7.44772 11 8 11Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val board = IconDefinition(
            "board",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 5C2 3.89543 2.89543 3 4 3H20C21.1046 3 22 3.89543 22 5V19C22 20.1046 21.1046 21 20 21H4C2.89543 21 2 20.1046 2 19V5ZM8 5H4V19H8V5ZM10 5V19H14V5H10ZM16 5V19H20V5H16Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val book = IconDefinition(
            "book",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 6.6335V17.5129C4.85122 17.2485 6.03984 17 7.5 17C8.96016 17 10.1488 17.2485 11 17.5129V6.6335C10.8601 6.57696 10.692 6.51452 10.4971 6.45153C9.80787 6.22874 8.78699 6 7.5 6C6.21301 6 5.19213 6.22874 4.50288 6.45153C4.308 6.51452 4.13986 6.57696 4 6.6335ZM11.4914 6.86097L11.4924 6.86157L11.4939 6.86248L11.4952 6.86326C11.4953 6.86332 11.4944 6.86268 11.4914 6.86097ZM12 4.88528C11.7743 4.78519 11.4765 4.66622 11.1123 4.54847C10.2546 4.27126 9.02551 4 7.5 4C5.97449 4 4.74537 4.27126 3.88775 4.54847C3.45896 4.68707 3.12235 4.82736 2.88689 4.93656C2.76911 4.99119 2.67645 5.03812 2.61002 5.07329C2.5768 5.09089 2.55011 5.10555 2.5301 5.11679L2.5051 5.131L2.49638 5.13607L2.49297 5.13807L2.49151 5.13893C2.49119 5.13912 2.4902 5.13971 3 6L2.4902 5.13971C2.18633 5.31978 2 5.64679 2 6V19C2 19.3593 2.19275 19.691 2.50493 19.8689C2.8169 20.0466 3.20021 20.0435 3.50921 19.8606C3.50648 19.8622 3.50498 19.8631 3.50477 19.8632L3.50609 19.8625L3.50765 19.8616L3.50921 19.8606C3.51529 19.8572 3.52757 19.8505 3.54599 19.8408C3.58283 19.8213 3.64398 19.7901 3.72834 19.7509C3.89718 19.6726 4.15822 19.5629 4.50288 19.4515C5.19213 19.2287 6.21301 19 7.5 19C8.78699 19 9.80787 19.2287 10.4971 19.4515C10.8418 19.5629 11.1028 19.6726 11.2717 19.7509C11.356 19.7901 11.4172 19.8213 11.454 19.8408C11.4724 19.8505 11.4847 19.8573 11.4908 19.8608C11.4936 19.8623 11.495 19.8631 11.4952 19.8632M11.4952 19.8632L11.4939 19.8625L11.4924 19.8616L11.4908 19.8608C11.4908 19.8607 11.4909 19.8608 11.4908 19.8608C11.8048 20.0462 12.1954 20.0463 12.5092 19.8606C12.5076 19.8615 12.5064 19.8623 12.5057 19.8627L12.5076 19.8616L12.5092 19.8606C12.5153 19.8572 12.5276 19.8505 12.546 19.8408C12.5828 19.8213 12.644 19.7901 12.7283 19.7509C12.8972 19.6726 13.1582 19.5629 13.5029 19.4515C14.1921 19.2287 15.213 19 16.5 19C17.787 19 18.8079 19.2287 19.4971 19.4515C19.8418 19.5629 20.1028 19.6726 20.2717 19.7509C20.356 19.7901 20.4172 19.8213 20.454 19.8408C20.4724 19.8505 20.4847 19.8573 20.4908 19.8608L20.4919 19.8613C20.8007 20.0435 21.1835 20.0464 21.4951 19.8689C21.8072 19.691 22 19.3593 22 19V6C22 5.64679 21.8137 5.31978 21.5098 5.13971L21 6C21.5098 5.13971 21.5101 5.1399 21.5098 5.13971L21.5085 5.13893L21.507 5.13807L21.5036 5.13607L21.4949 5.131L21.4699 5.11679C21.4499 5.10555 21.4232 5.09089 21.39 5.07329C21.3236 5.03812 21.2309 4.99119 21.1131 4.93656C20.8776 4.82736 20.541 4.68707 20.1123 4.54847C19.2546 4.27126 18.0255 4 16.5 4C14.9745 4 13.7454 4.27126 12.8877 4.54847C12.5235 4.66622 12.2257 4.78519 12 4.88528M20 6.6335C19.8601 6.57696 19.692 6.51452 19.4971 6.45153C18.8079 6.22874 17.787 6 16.5 6C15.213 6 14.1921 6.22874 13.5029 6.45153C13.308 6.51452 13.1399 6.57696 13 6.6335V17.5129C13.8512 17.2485 15.0398 17 16.5 17C17.9602 17 19.1488 17.2485 20 17.5129V6.6335ZM12.5092 6.86075C12.5061 6.86246 12.5047 6.86332 12.5048 6.86326L12.5061 6.86248L12.5076 6.86157L12.5092 6.86075ZM20.4908 6.86075C20.4935 6.86228 20.495 6.86313 20.4952 6.86325L20.4939 6.86248L20.4908 6.86075ZM20.4908 19.8608C20.4923 19.8616 20.4934 19.8622 20.4941 19.8626L20.4919 19.8613" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val bookmark = IconDefinition(
            "bookmark",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 4C4 2.89543 4.89543 2 6 2H18C19.1046 2 20 2.89543 20 4V21C20 21.3746 19.7907 21.7178 19.4576 21.8892C19.1245 22.0606 18.7236 22.0315 18.4188 21.8137L12 17.2289L5.58124 21.8137C5.27642 22.0315 4.87549 22.0606 4.54242 21.8892C4.20935 21.7178 4 21.3746 4 21V4ZM18 4L6 4V19.0568L11.4188 15.1863C11.7665 14.9379 12.2335 14.9379 12.5812 15.1863L18 19.0568V4Z" fill="#0D0D0D"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val calendar = IconDefinition(
            "calendar",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M9 2C9.55228 2 10 2.44772 10 3V4H14V3C14 2.44772 14.4477 2 15 2C15.5523 2 16 2.44772 16 3V4H19C20.1046 4 21 4.89543 21 6V19C21 20.1046 20.1046 21 19 21H5C3.89543 21 3 20.1046 3 19V6C3 4.89543 3.89543 4 5 4H8V3C8 2.44772 8.44772 2 9 2ZM8 6H5V9H19V6H16V7C16 7.55228 15.5523 8 15 8C14.4477 8 14 7.55228 14 7V6H10V7C10 7.55228 9.55228 8 9 8C8.44772 8 8 7.55228 8 7V6ZM19 11H5V19H19V11Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val call = IconDefinition(
            "call",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M3.83302 4C3.52772 6.1111 3.2268 11.8126 7.7071 16.2929C12.1874 20.7732 17.8889 20.4723 20 20.167V15.8471L15.5641 15.1078L14.8944 16.4472C14.725 16.786 14.3788 17 14 17V16C14 17 13.9995 17 13.999 17L13.9979 17L13.9957 17L13.9907 17L13.9783 16.9998C13.9691 16.9997 13.9579 16.9994 13.9448 16.999C13.9186 16.9981 13.8847 16.9966 13.8436 16.9939C13.7615 16.9884 13.6506 16.9781 13.5148 16.9587C13.2435 16.9199 12.8719 16.8447 12.4338 16.6987C11.5541 16.4054 10.4151 15.8293 9.29289 14.7071C8.17073 13.5849 7.59455 12.4459 7.30131 11.5662C7.15527 11.1281 7.08005 10.7565 7.0413 10.4852C7.02191 10.3494 7.01159 10.2385 7.00612 10.1564C7.00338 10.1153 7.00185 10.0814 7.00101 10.0552C7.00058 10.0421 7.00033 10.0309 7.00018 10.0217L7.00004 10.0093L7.00001 10.0043L7 10.002L7 10.001C7 10.0005 7 10 8 10H7C7 9.66565 7.1671 9.35342 7.4453 9.16795L9.77018 7.61803L8.32296 4H3.83302ZM9.07364 10.4861L10.8796 9.28213C11.6665 8.75751 11.9784 7.75338 11.6271 6.87525L10.1799 3.25722C9.87619 2.4979 9.14077 2 8.32296 2H3.78077C2.87226 2 2.01708 2.63116 1.86794 3.6169C1.52863 5.8595 1.06678 12.481 6.29289 17.7071C11.519 22.9332 18.1405 22.4714 20.3831 22.1321C21.3688 21.9829 22 21.1277 22 20.2192V15.8471C22 14.8694 21.2932 14.0351 20.3288 13.8743L15.8929 13.135C15.0269 12.9907 14.1679 13.4281 13.7753 14.2134L13.4288 14.9064C13.3199 14.8796 13.1983 14.8453 13.0662 14.8013C12.4459 14.5946 11.5849 14.1707 10.7071 13.2929C9.82927 12.4151 9.40545 11.5541 9.19868 10.9338C9.14281 10.7662 9.10259 10.6154 9.07364 10.4861Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val camera = IconDefinition(
            "camera",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M8.29289 4.29289C8.48043 4.10536 8.73478 4 9 4H15C15.2652 4 15.5196 4.10536 15.7071 4.29289L17.4142 6H20C21.1046 6 22 6.89543 22 8V18C22 19.1046 21.1046 20 20 20H4C2.89543 20 2 19.1046 2 18V8C2 6.89543 2.89543 6 4 6H6.58579L8.29289 4.29289ZM9.41421 6L7.70711 7.70711C7.51957 7.89464 7.26522 8 7 8L4 8V18H20V8H17C16.7348 8 16.4804 7.89464 16.2929 7.70711L14.5858 6H9.41421ZM12 10.5C10.8954 10.5 10 11.3954 10 12.5C10 13.6046 10.8954 14.5 12 14.5C13.1046 14.5 14 13.6046 14 12.5C14 11.3954 13.1046 10.5 12 10.5ZM8 12.5C8 10.2909 9.79086 8.5 12 8.5C14.2091 8.5 16 10.2909 16 12.5C16 14.7091 14.2091 16.5 12 16.5C9.79086 16.5 8 14.7091 8 12.5Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val caretDown = IconDefinition(
            "caretDown",
            svg = """
                <path d="M17 10L12 16L7 10H17Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val caretLeft = IconDefinition(
            "caretLeft",
            svg = """
                <path d="M14 17L8 12L14 7L14 17Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val caretRight = IconDefinition(
            "caretRight",
            svg = """
                <path d="M10 7L16 12L10 17L10 7Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val caretUp = IconDefinition(
            "caretUp",
            svg = """
                <path d="M7 14L12 8L17 14L7 14Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val check = IconDefinition(
            "check",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M20.6644 5.25259C21.0772 5.61951 21.1143 6.25158 20.7474 6.66437L10.0808 18.6644C9.89099 18.8779 9.61898 19 9.33334 19C9.04771 19 8.7757 18.8779 8.58593 18.6644L3.2526 12.6644C2.88568 12.2516 2.92286 11.6195 3.33565 11.2526C3.74843 10.8857 4.3805 10.9229 4.74742 11.3356L9.33334 16.4948L19.2526 5.33564C19.6195 4.92286 20.2516 4.88568 20.6644 5.25259Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val chevronDoubleDown = IconDefinition(
            "chevronDoubleDown",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M5.29289 6.29289C5.68342 5.90237 6.31658 5.90237 6.70711 6.29289L12 11.5858L17.2929 6.2929C17.6834 5.90237 18.3166 5.90237 18.7071 6.2929C19.0976 6.68342 19.0976 7.31658 18.7071 7.70711L12.7071 13.7071C12.3166 14.0976 11.6834 14.0976 11.2929 13.7071L5.29289 7.70711C4.90237 7.31658 4.90237 6.68342 5.29289 6.29289ZM5.29289 12.2929C5.68342 11.9024 6.31658 11.9024 6.70711 12.2929L12 17.5858L17.2929 12.2929C17.6834 11.9024 18.3166 11.9024 18.7071 12.2929C19.0976 12.6834 19.0976 13.3166 18.7071 13.7071L12.7071 19.7071C12.3166 20.0976 11.6834 20.0976 11.2929 19.7071L5.29289 13.7071C4.90237 13.3166 4.90237 12.6834 5.29289 12.2929Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val chevronDoubleLeft = IconDefinition(
            "chevronDoubleLeft",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M17.7071 5.29289C18.0976 5.68342 18.0976 6.31658 17.7071 6.70711L12.4142 12L17.7071 17.2929C18.0976 17.6834 18.0976 18.3166 17.7071 18.7071C17.3166 19.0976 16.6834 19.0976 16.2929 18.7071L10.2929 12.7071C9.90237 12.3166 9.90237 11.6834 10.2929 11.2929L16.2929 5.29289C16.6834 4.90237 17.3166 4.90237 17.7071 5.29289ZM11.7071 5.29289C12.0976 5.68342 12.0976 6.31658 11.7071 6.70711L6.41421 12L11.7071 17.2929C12.0976 17.6834 12.0976 18.3166 11.7071 18.7071C11.3166 19.0976 10.6834 19.0976 10.2929 18.7071L4.29289 12.7071C4.10536 12.5196 4 12.2652 4 12C4 11.7348 4.10536 11.4804 4.29289 11.2929L10.2929 5.29289C10.6834 4.90237 11.3166 4.90237 11.7071 5.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val chevronDoubleRight = IconDefinition(
            "chevronDoubleRight",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12.2929 5.29289C12.6834 4.90237 13.3166 4.90237 13.7071 5.29289L19.7071 11.2929C20.0976 11.6834 20.0976 12.3166 19.7071 12.7071L13.7071 18.7071C13.3166 19.0976 12.6834 19.0976 12.2929 18.7071C11.9024 18.3166 11.9024 17.6834 12.2929 17.2929L17.5858 12L12.2929 6.70711C11.9024 6.31658 11.9024 5.68342 12.2929 5.29289ZM6.29289 5.29289C6.68342 4.90237 7.31658 4.90237 7.70711 5.29289L13.7071 11.2929C13.8946 11.4804 14 11.7348 14 12C14 12.2652 13.8946 12.5196 13.7071 12.7071L7.70711 18.7071C7.31658 19.0976 6.68342 19.0976 6.29289 18.7071C5.90237 18.3166 5.90237 17.6834 6.29289 17.2929L11.5858 12L6.29289 6.70711C5.90237 6.31658 5.90237 5.68342 6.29289 5.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val chevronDoubleUp = IconDefinition(
            "chevronDoubleUp",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M11.2929 4.29289C11.6834 3.90237 12.3166 3.90237 12.7071 4.29289L18.7071 10.2929C19.0976 10.6834 19.0976 11.3166 18.7071 11.7071C18.3166 12.0976 17.6834 12.0976 17.2929 11.7071L12 6.41421L6.70711 11.7071C6.31658 12.0976 5.68342 12.0976 5.29289 11.7071C4.90237 11.3166 4.90237 10.6834 5.29289 10.2929L11.2929 4.29289ZM12 12.4142L6.70711 17.7071C6.31658 18.0976 5.68342 18.0976 5.29289 17.7071C4.90237 17.3166 4.90237 16.6834 5.29289 16.2929L11.2929 10.2929C11.6834 9.90237 12.3166 9.90237 12.7071 10.2929L18.7071 16.2929C19.0976 16.6834 19.0976 17.3166 18.7071 17.7071C18.3166 18.0976 17.6834 18.0976 17.2929 17.7071L12 12.4142Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val chevronDown = IconDefinition(
            "chevronDown",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M5.29289 9.29289C5.68342 8.90237 6.31658 8.90237 6.70711 9.29289L12 14.5858L17.2929 9.29289C17.6834 8.90237 18.3166 8.90237 18.7071 9.29289C19.0976 9.68342 19.0976 10.3166 18.7071 10.7071L12.7071 16.7071C12.3166 17.0976 11.6834 17.0976 11.2929 16.7071L5.29289 10.7071C4.90237 10.3166 4.90237 9.68342 5.29289 9.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val chevronLeft = IconDefinition(
            "chevronLeft",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M14.7071 5.29289C15.0976 5.68342 15.0976 6.31658 14.7071 6.70711L9.41421 12L14.7071 17.2929C15.0976 17.6834 15.0976 18.3166 14.7071 18.7071C14.3166 19.0976 13.6834 19.0976 13.2929 18.7071L7.29289 12.7071C6.90237 12.3166 6.90237 11.6834 7.29289 11.2929L13.2929 5.29289C13.6834 4.90237 14.3166 4.90237 14.7071 5.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val chevronRight = IconDefinition(
            "chevronRight",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M9.29289 18.7071C8.90237 18.3166 8.90237 17.6834 9.29289 17.2929L14.5858 12L9.29289 6.70711C8.90237 6.31658 8.90237 5.68342 9.29289 5.29289C9.68342 4.90237 10.3166 4.90237 10.7071 5.29289L16.7071 11.2929C17.0976 11.6834 17.0976 12.3166 16.7071 12.7071L10.7071 18.7071C10.3166 19.0976 9.68342 19.0976 9.29289 18.7071Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val chevronUp = IconDefinition(
            "chevronUp",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M11.2929 7.29289C11.6834 6.90237 12.3166 6.90237 12.7071 7.29289L18.7071 13.2929C19.0976 13.6834 19.0976 14.3166 18.7071 14.7071C18.3166 15.0976 17.6834 15.0976 17.2929 14.7071L12 9.41421L6.70711 14.7071C6.31658 15.0976 5.68342 15.0976 5.29289 14.7071C4.90237 14.3166 4.90237 13.6834 5.29289 13.2929L11.2929 7.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val circleAdd = IconDefinition(
            "circleAdd",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C7.58172 4 4 7.58172 4 12C4 16.4183 7.58172 20 12 20C16.4183 20 20 16.4183 20 12C20 7.58172 16.4183 4 12 4ZM2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12ZM12 7C12.5523 7 13 7.44772 13 8V11H16C16.5523 11 17 11.4477 17 12C17 12.5523 16.5523 13 16 13H13V16C13 16.5523 12.5523 17 12 17C11.4477 17 11 16.5523 11 16V13H8C7.44772 13 7 12.5523 7 12C7 11.4477 7.44772 11 8 11H11V8C11 7.44772 11.4477 7 12 7Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val circleArrowDown = IconDefinition(
            "circleArrowDown",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C7.58172 4 4 7.58172 4 12C4 16.4183 7.58172 20 12 20C16.4183 20 20 16.4183 20 12C20 7.58172 16.4183 4 12 4ZM2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12ZM12 6C12.5523 6 13 6.44772 13 7V14.5858L15.2929 12.2929C15.6834 11.9024 16.3166 11.9024 16.7071 12.2929C17.0976 12.6834 17.0976 13.3166 16.7071 13.7071L12.7071 17.7071C12.3166 18.0976 11.6834 18.0976 11.2929 17.7071L7.29289 13.7071C6.90237 13.3166 6.90237 12.6834 7.29289 12.2929C7.68342 11.9024 8.31658 11.9024 8.70711 12.2929L11 14.5858V7C11 6.44772 11.4477 6 12 6Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val circleArrowLeft = IconDefinition(
            "circleArrowLeft",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C7.58172 4 4 7.58172 4 12C4 16.4183 7.58172 20 12 20C16.4183 20 20 16.4183 20 12C20 7.58172 16.4183 4 12 4ZM2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12ZM11.7071 7.29289C12.0976 7.68342 12.0976 8.31658 11.7071 8.70711L9.41421 11H17C17.5523 11 18 11.4477 18 12C18 12.5523 17.5523 13 17 13H9.41421L11.7071 15.2929C12.0976 15.6834 12.0976 16.3166 11.7071 16.7071C11.3166 17.0976 10.6834 17.0976 10.2929 16.7071L6.29289 12.7071C5.90237 12.3166 5.90237 11.6834 6.29289 11.2929L10.2929 7.29289C10.6834 6.90237 11.3166 6.90237 11.7071 7.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val circleArrowRight = IconDefinition(
            "circleArrowRight",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C7.58172 4 4 7.58172 4 12C4 16.4183 7.58172 20 12 20C16.4183 20 20 16.4183 20 12C20 7.58172 16.4183 4 12 4ZM2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12ZM12.2929 7.29289C12.6834 6.90237 13.3166 6.90237 13.7071 7.29289L17.7071 11.2929C18.0976 11.6834 18.0976 12.3166 17.7071 12.7071L13.7071 16.7071C13.3166 17.0976 12.6834 17.0976 12.2929 16.7071C11.9024 16.3166 11.9024 15.6834 12.2929 15.2929L14.5858 13H7C6.44772 13 6 12.5523 6 12C6 11.4477 6.44772 11 7 11H14.5858L12.2929 8.70711C11.9024 8.31658 11.9024 7.68342 12.2929 7.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val circleArrowUp = IconDefinition(
            "circleArrowUp",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C7.58172 4 4 7.58172 4 12C4 16.4183 7.58172 20 12 20C16.4183 20 20 16.4183 20 12C20 7.58172 16.4183 4 12 4ZM2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12ZM11 9.41421L8.70711 11.7071C8.31658 12.0976 7.68342 12.0976 7.29289 11.7071C6.90237 11.3166 6.90237 10.6834 7.29289 10.2929L11.2929 6.29289C11.6834 5.90237 12.3166 5.90237 12.7071 6.29289L16.7071 10.2929C17.0976 10.6834 17.0976 11.3166 16.7071 11.7071C16.3166 12.0976 15.6834 12.0976 15.2929 11.7071L13 9.41421V17C13 17.5523 12.5523 18 12 18C11.4477 18 11 17.5523 11 17V9.41421Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val circleCheck = IconDefinition(
            "circleCheck",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C7.58172 4 4 7.58172 4 12C4 16.4183 7.58172 20 12 20C16.4183 20 20 16.4183 20 12C20 7.58172 16.4183 4 12 4ZM2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12ZM16.6644 8.75259C17.0771 9.11951 17.1143 9.75158 16.7474 10.1644L11.4141 16.1644C11.2243 16.3779 10.9523 16.5 10.6667 16.5C10.381 16.5 10.109 16.3779 9.91926 16.1644L7.25259 13.1644C6.88567 12.7516 6.92285 12.1195 7.33564 11.7526C7.74842 11.3857 8.38049 11.4229 8.74741 11.8356L10.6667 13.9948L15.2526 8.83564C15.6195 8.42285 16.2516 8.38567 16.6644 8.75259Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val circleError = IconDefinition(
            "circleError",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C7.58172 4 4 7.58172 4 12C4 16.4183 7.58172 20 12 20C16.4183 20 20 16.4183 20 12C20 7.58172 16.4183 4 12 4ZM2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12ZM7.79289 7.79289C8.18342 7.40237 8.81658 7.40237 9.20711 7.79289L12 10.5858L14.7929 7.79289C15.1834 7.40237 15.8166 7.40237 16.2071 7.79289C16.5976 8.18342 16.5976 8.81658 16.2071 9.20711L13.4142 12L16.2071 14.7929C16.5976 15.1834 16.5976 15.8166 16.2071 16.2071C15.8166 16.5976 15.1834 16.5976 14.7929 16.2071L12 13.4142L9.20711 16.2071C8.81658 16.5976 8.18342 16.5976 7.79289 16.2071C7.40237 15.8166 7.40237 15.1834 7.79289 14.7929L10.5858 12L7.79289 9.20711C7.40237 8.81658 7.40237 8.18342 7.79289 7.79289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val circleHelp = IconDefinition(
            "circleHelp",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C7.58172 4 4 7.58172 4 12C4 16.4183 7.58172 20 12 20C16.4183 20 20 16.4183 20 12C20 7.58172 16.4183 4 12 4ZM2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12Z" fill="currentColor"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 14C11.4477 14 11 13.5523 11 13V12C11 11.4477 11.4477 11 12 11C12.5523 11 13 11.4477 13 12V13C13 13.5523 12.5523 14 12 14Z" fill="currentColor"/>
                <path d="M10.5 16.5C10.5 15.6716 11.1716 15 12 15C12.8284 15 13.5 15.6716 13.5 16.5C13.5 17.3284 12.8284 18 12 18C11.1716 18 10.5 17.3284 10.5 16.5Z" fill="currentColor"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12.3899 7.81137C11.4329 7.7658 10.6304 8.3004 10.4864 9.1644C10.3956 9.70917 9.88037 10.0772 9.3356 9.9864C8.79083 9.8956 8.42281 9.38037 8.51361 8.8356C8.86961 6.69961 10.8171 5.73421 12.4851 5.81363C13.3395 5.85432 14.2176 6.16099 14.8937 6.79278C15.5866 7.44027 16 8.36777 16 9.5C16 10.7913 15.4919 11.7489 14.6172 12.3321C13.8141 12.8675 12.8295 13 12 13C11.4477 13 11 12.5523 11 12C11 11.4477 11.4477 11 12 11C12.6705 11 13.1859 10.8825 13.5078 10.668C13.7581 10.5011 14 10.2087 14 9.5C14 8.88224 13.7884 8.49723 13.5282 8.2541C13.2512 7.99526 12.848 7.83318 12.3899 7.81137Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val circleInformation = IconDefinition(
            "circleInformation",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C7.58172 4 4 7.58172 4 12C4 16.4183 7.58172 20 12 20C16.4183 20 20 16.4183 20 12C20 7.58172 16.4183 4 12 4ZM2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12Z" fill="currentColor"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 10C12.5523 10 13 10.4477 13 11V17C13 17.5523 12.5523 18 12 18C11.4477 18 11 17.5523 11 17V11C11 10.4477 11.4477 10 12 10Z" fill="currentColor"/>
                <path d="M13.5 7.5C13.5 8.32843 12.8284 9 12 9C11.1716 9 10.5 8.32843 10.5 7.5C10.5 6.67157 11.1716 6 12 6C12.8284 6 13.5 6.67157 13.5 7.5Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val circleRemove = IconDefinition(
            "circleRemove",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C7.58172 4 4 7.58172 4 12C4 16.4183 7.58172 20 12 20C16.4183 20 20 16.4183 20 12C20 7.58172 16.4183 4 12 4ZM2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12ZM7 12C7 11.4477 7.44772 11 8 11H16C16.5523 11 17 11.4477 17 12C17 12.5523 16.5523 13 16 13H8C7.44772 13 7 12.5523 7 12Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val circleWarning = IconDefinition(
            "circleWarning",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C7.58172 4 4 7.58172 4 12C4 16.4183 7.58172 20 12 20C16.4183 20 20 16.4183 20 12C20 7.58172 16.4183 4 12 4ZM2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12Z" fill="currentColor"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 14C11.4477 14 11 13.5523 11 13L11 7C11 6.44772 11.4477 6 12 6C12.5523 6 13 6.44772 13 7L13 13C13 13.5523 12.5523 14 12 14Z" fill="currentColor"/>
                <path d="M10.5 16.5C10.5 15.6716 11.1716 15 12 15C12.8284 15 13.5 15.6716 13.5 16.5C13.5 17.3284 12.8284 18 12 18C11.1716 18 10.5 17.3284 10.5 16.5Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val clipboardCheck = IconDefinition(
            "clipboardCheck",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M8 3C8 2.44772 8.44772 2 9 2H15C15.5523 2 16 2.44772 16 3H18C19.1046 3 20 3.89543 20 5V20C20 21.1046 19.1046 22 18 22H6C4.89543 22 4 21.1046 4 20V5C4 3.89543 4.89543 3 6 3H8ZM8 5H6V20H18V5H16V6C16 6.55228 15.5523 7 15 7H9C8.44772 7 8 6.55228 8 6V5ZM14 4H10V5H14V4ZM15.7071 10.7929C16.0976 11.1834 16.0976 11.8166 15.7071 12.2071L11.7071 16.2071C11.3166 16.5976 10.6834 16.5976 10.2929 16.2071L8.29289 14.2071C7.90237 13.8166 7.90237 13.1834 8.29289 12.7929C8.68342 12.4024 9.31658 12.4024 9.70711 12.7929L11 14.0858L14.2929 10.7929C14.6834 10.4024 15.3166 10.4024 15.7071 10.7929Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val clipboardList = IconDefinition(
            "clipboardList",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M11 16.5C11 15.9477 11.4477 15.5 12 15.5H15C15.5523 15.5 16 15.9477 16 16.5C16 17.0523 15.5523 17.5 15 17.5H12C11.4477 17.5 11 17.0523 11 16.5Z" fill="currentColor"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M11 13.5C11 12.9477 11.4477 12.5 12 12.5H15C15.5523 12.5 16 12.9477 16 13.5C16 14.0523 15.5523 14.5 15 14.5H12C11.4477 14.5 11 14.0523 11 13.5Z" fill="currentColor"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M11 10.5C11 9.94772 11.4477 9.5 12 9.5H15C15.5523 9.5 16 9.94772 16 10.5C16 11.0523 15.5523 11.5 15 11.5H12C11.4477 11.5 11 11.0523 11 10.5Z" fill="currentColor"/>
                <path d="M10 10.5C10 11.0523 9.55228 11.5 9 11.5C8.44772 11.5 8 11.0523 8 10.5C8 9.94772 8.44772 9.5 9 9.5C9.55228 9.5 10 9.94772 10 10.5Z" fill="currentColor"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 5C4 3.89543 4.89543 3 6 3H9C9.55228 3 10 3.44772 10 4C10 4.55228 9.55228 5 9 5H6V20H18V5H15C14.4477 5 14 4.55228 14 4C14 3.44772 14.4477 3 15 3H18C19.1046 3 20 3.89543 20 5V20C20 21.1046 19.1046 22 18 22H6C4.89543 22 4 21.1046 4 20V5Z" fill="currentColor"/>
                <path d="M10 13.5C10 14.0523 9.55228 14.5 9 14.5C8.44772 14.5 8 14.0523 8 13.5C8 12.9477 8.44772 12.5 9 12.5C9.55228 12.5 10 12.9477 10 13.5Z" fill="currentColor"/>
                <path d="M10 16.5C10 17.0523 9.55228 17.5 9 17.5C8.44772 17.5 8 17.0523 8 16.5C8 15.9477 8.44772 15.5 9 15.5C9.55228 15.5 10 15.9477 10 16.5Z" fill="currentColor"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M8 3C8 2.44772 8.44772 2 9 2H15C15.5523 2 16 2.44772 16 3V6C16 6.55228 15.5523 7 15 7H9C8.44772 7 8 6.55228 8 6V3ZM10 4V5H14V4H10Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val clipboard = IconDefinition(
            "clipboard",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M8 3C8 2.44772 8.44772 2 9 2H15C15.5523 2 16 2.44772 16 3H18C19.1046 3 20 3.89543 20 5V20C20 21.1046 19.1046 22 18 22H6C4.89543 22 4 21.1046 4 20V5C4 3.89543 4.89543 3 6 3H8ZM8 5H6V20H18V5H16V6C16 6.55228 15.5523 7 15 7H9C8.44772 7 8 6.55228 8 6V5ZM14 4H10V5H14V4Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val clock = IconDefinition(
            "clock",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C7.58172 4 4 7.58172 4 12C4 16.4183 7.58172 20 12 20C16.4183 20 20 16.4183 20 12C20 7.58172 16.4183 4 12 4ZM2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12ZM12 6C12.5523 6 13 6.44772 13 7V11.5858L15.7071 14.2929C16.0976 14.6834 16.0976 15.3166 15.7071 15.7071C15.3166 16.0976 14.6834 16.0976 14.2929 15.7071L11.2929 12.7071C11.1054 12.5196 11 12.2652 11 12V7C11 6.44772 11.4477 6 12 6Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val close = IconDefinition(
            "close",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M5.29289 5.29289C5.68342 4.90237 6.31658 4.90237 6.70711 5.29289L12 10.5858L17.2929 5.29289C17.6834 4.90237 18.3166 4.90237 18.7071 5.29289C19.0976 5.68342 19.0976 6.31658 18.7071 6.70711L13.4142 12L18.7071 17.2929C19.0976 17.6834 19.0976 18.3166 18.7071 18.7071C18.3166 19.0976 17.6834 19.0976 17.2929 18.7071L12 13.4142L6.70711 18.7071C6.31658 19.0976 5.68342 19.0976 5.29289 18.7071C4.90237 18.3166 4.90237 17.6834 5.29289 17.2929L10.5858 12L5.29289 6.70711C4.90237 6.31658 4.90237 5.68342 5.29289 5.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val cloudDownload = IconDefinition(
            "cloudDownload",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M11 4C8.79082 4 7 5.79085 7 8C7 8.03242 7.00047 8.06627 7.00131 8.10224C7.01219 8.56727 6.70099 8.97839 6.25047 9.09416C4.95577 9.42685 4 10.6031 4 12C4 13.6569 5.34317 15 7 15H8C8.55228 15 9 15.4477 9 16C9 16.5523 8.55228 17 8 17H7C4.23861 17 2 14.7614 2 12C2 9.93746 3.2482 8.16845 5.02926 7.40373C5.32856 4.36995 7.88746 2 11 2C13.2236 2 15.1629 3.20934 16.199 5.00324C19.4207 5.10823 22 7.75289 22 11C22 14.3137 19.3138 17 16 17C15.4477 17 15 16.5523 15 16C15 15.4477 15.4477 15 16 15C18.2092 15 20 13.2091 20 11C20 8.79085 18.2092 7 16 7C15.8893 7 15.78 7.00447 15.6718 7.01322C15.2449 7.04776 14.8434 6.8066 14.6734 6.4135C14.0584 4.99174 12.6439 4 11 4ZM12 10C12.5523 10 13 10.4477 13 11L13 18.5858L13.2929 18.2929C13.6834 17.9024 14.3166 17.9024 14.7071 18.2929C15.0976 18.6834 15.0976 19.3166 14.7071 19.7071L12.7071 21.7071C12.5196 21.8946 12.2652 22 12 22C11.7348 22 11.4804 21.8946 11.2929 21.7071L9.29289 19.7071C8.90237 19.3166 8.90237 18.6834 9.29289 18.2929C9.68342 17.9024 10.3166 17.9024 10.7071 18.2929L11 18.5858L11 11C11 10.4477 11.4477 10 12 10Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val cloudUpload = IconDefinition(
            "cloudUpload",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M11 4C8.79082 4 7 5.79085 7 8C7 8.03242 7.00047 8.06627 7.00131 8.10224C7.01219 8.56727 6.70099 8.97839 6.25047 9.09416C4.95577 9.42685 4 10.6031 4 12C4 13.6569 5.34317 15 7 15H8C8.55228 15 9 15.4477 9 16C9 16.5523 8.55228 17 8 17H7C4.23861 17 2 14.7614 2 12C2 9.93746 3.2482 8.16845 5.02926 7.40373C5.32856 4.36995 7.88746 2 11 2C13.2236 2 15.1629 3.20934 16.199 5.00324C19.4207 5.10823 22 7.75289 22 11C22 14.3137 19.3138 17 16 17C15.4477 17 15 16.5523 15 16C15 15.4477 15.4477 15 16 15C18.2092 15 20 13.2091 20 11C20 8.79085 18.2092 7 16 7C15.8893 7 15.78 7.00447 15.6718 7.01322C15.2449 7.04776 14.8434 6.8066 14.6734 6.4135C14.0584 4.99174 12.6439 4 11 4ZM11.2929 9.29289C11.6834 8.90237 12.3166 8.90237 12.7071 9.29289L14.7071 11.2929C15.0976 11.6834 15.0976 12.3166 14.7071 12.7071C14.3166 13.0976 13.6834 13.0976 13.2929 12.7071L13 12.4142V20C13 20.5523 12.5523 21 12 21C11.4477 21 11 20.5523 11 20V12.4142L10.7071 12.7071C10.3166 13.0976 9.68342 13.0976 9.29289 12.7071C8.90237 12.3166 8.90237 11.6834 9.29289 11.2929L11.2929 9.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val cloud = IconDefinition(
            "cloud",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M5 10C5 6.68629 7.68629 4 11 4C13.627 4 15.858 5.68745 16.6713 8.03713C19.669 8.37085 22 10.9132 22 14C22 17.3137 19.3137 20 16 20H7C4.23858 20 2 17.7614 2 15C2 12.9436 3.24073 11.1787 5.01385 10.4103C5.00466 10.2746 5 10.1378 5 10ZM11 6C8.79086 6 7 7.79086 7 10C7 10.3029 7.03348 10.5967 7.09656 10.8785C7.21716 11.4173 6.8783 11.9519 6.33958 12.0727C5.00015 12.3732 4 13.571 4 15C4 16.6569 5.34315 18 7 18H16C18.2091 18 20 16.2091 20 14C20 11.7909 18.2091 10 16 10C15.9732 10 15.9465 10.0003 15.9198 10.0008C15.4368 10.0102 15.0161 9.67312 14.9201 9.19971C14.5499 7.37395 12.9343 6 11 6Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val computer = IconDefinition(
            "computer",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 5C2 3.89543 2.89543 3 4 3H20C21.1046 3 22 3.89543 22 5V16C22 17.1046 21.1046 18 20 18H13V20H16C16.5523 20 17 20.4477 17 21C17 21.5523 16.5523 22 16 22H8C7.44772 22 7 21.5523 7 21C7 20.4477 7.44772 20 8 20H11V18H4C2.89543 18 2 17.1046 2 16V5ZM20 16V5H4V16H20Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val copy = IconDefinition(
            "copy",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 4C2 2.89543 2.89543 2 4 2H14C15.1046 2 16 2.89543 16 4V8H20C21.1046 8 22 8.89543 22 10V20C22 21.1046 21.1046 22 20 22H10C8.89543 22 8 21.1046 8 20V16H4C2.89543 16 2 15.1046 2 14V4ZM10 16V20H20V10H16V14C16 15.1046 15.1046 16 14 16H10ZM14 14H4V4L14 4V14Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val creditCard = IconDefinition(
            "creditCard",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M22 6C22 4.89543 21.1046 4 20 4H4C2.89543 4 2 4.89543 2 6V18C2 19.1046 2.89543 20 4 20H20C21.1046 20 22 19.1046 22 18V6ZM20 8V6L4 6L4 8H20ZM4 11L4 18L20 18V11H4Z" fill="#0D0D0D"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val delete = IconDefinition(
            "delete",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M7 4C7 2.89543 7.89543 2 9 2H15C16.1046 2 17 2.89543 17 4V6H18.9897C18.9959 5.99994 19.0021 5.99994 19.0083 6H21C21.5523 6 22 6.44772 22 7C22 7.55228 21.5523 8 21 8H19.9311L19.0638 20.1425C18.989 21.1891 18.1182 22 17.0689 22H6.93112C5.88184 22 5.01096 21.1891 4.9362 20.1425L4.06888 8H3C2.44772 8 2 7.55228 2 7C2 6.44772 2.44772 6 3 6H4.99174C4.99795 5.99994 5.00414 5.99994 5.01032 6H7V4ZM9 6H15V4H9V6ZM6.07398 8L6.93112 20H17.0689L17.926 8H6.07398ZM10 10C10.5523 10 11 10.4477 11 11V17C11 17.5523 10.5523 18 10 18C9.44772 18 9 17.5523 9 17V11C9 10.4477 9.44772 10 10 10ZM14 10C14.5523 10 15 10.4477 15 11V17C15 17.5523 14.5523 18 14 18C13.4477 18 13 17.5523 13 17V11C13 10.4477 13.4477 10 14 10Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val documentAdd = IconDefinition(
            "documentAdd",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 4C4 2.89543 4.89543 2 6 2H14C14.2652 2 14.5196 2.10536 14.7071 2.29289L19.7071 7.29289C19.8946 7.48043 20 7.73478 20 8V20C20 21.1046 19.1046 22 18 22H6C4.89543 22 4 21.1046 4 20V4ZM17.5858 8H14V4.41421L17.5858 8ZM12 4V9C12 9.55228 12.4477 10 13 10H18V20H6V4L12 4ZM12 12C12.5523 12 13 12.4477 13 13V14H14C14.5523 14 15 14.4477 15 15C15 15.5523 14.5523 16 14 16H13V17C13 17.5523 12.5523 18 12 18C11.4477 18 11 17.5523 11 17V16H10C9.44772 16 9 15.5523 9 15C9 14.4477 9.44772 14 10 14H11V13C11 12.4477 11.4477 12 12 12Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val documentCheck = IconDefinition(
            "documentCheck",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 4C4 2.89543 4.89543 2 6 2H14C14.2652 2 14.5196 2.10536 14.7071 2.29289L19.7071 7.29289C19.8946 7.48043 20 7.73478 20 8V20C20 21.1046 19.1046 22 18 22H6C4.89543 22 4 21.1046 4 20V4ZM17.5858 8H14V4.41421L17.5858 8ZM12 4V9C12 9.55228 12.4477 10 13 10H18V20H6V4L12 4ZM15.7071 12.2929C16.0976 12.6834 16.0976 13.3166 15.7071 13.7071L11.7071 17.7071C11.3166 18.0976 10.6834 18.0976 10.2929 17.7071L8.29289 15.7071C7.90237 15.3166 7.90237 14.6834 8.29289 14.2929C8.68342 13.9024 9.31658 13.9024 9.70711 14.2929L11 15.5858L14.2929 12.2929C14.6834 11.9024 15.3166 11.9024 15.7071 12.2929Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val documentDownload = IconDefinition(
            "documentDownload",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 4C4 2.89543 4.89543 2 6 2H14C14.2652 2 14.5196 2.10536 14.7071 2.29289L19.7071 7.29289C19.8946 7.48043 20 7.73478 20 8V20C20 21.1046 19.1046 22 18 22H6C4.89543 22 4 21.1046 4 20V4ZM17.5858 8H14V4.41421L17.5858 8ZM12 4V9C12 9.55228 12.4477 10 13 10H18V20H6V4L12 4ZM12 11.5C12.5523 11.5 13 11.9477 13 12.5V15.0858L13.2929 14.7929C13.6834 14.4024 14.3166 14.4024 14.7071 14.7929C15.0976 15.1834 15.0976 15.8166 14.7071 16.2071L12.7071 18.2071C12.3166 18.5976 11.6834 18.5976 11.2929 18.2071L9.29289 16.2071C8.90237 15.8166 8.90237 15.1834 9.29289 14.7929C9.68342 14.4024 10.3166 14.4024 10.7071 14.7929L11 15.0858L11 12.5C11 11.9477 11.4477 11.5 12 11.5Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val documentEmpty = IconDefinition(
            "documentEmpty",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 4C4 2.89543 4.89543 2 6 2H14C14.2652 2 14.5196 2.10536 14.7071 2.29289L19.7071 7.29289C19.8946 7.48043 20 7.73478 20 8V20C20 21.1046 19.1046 22 18 22H6C4.89543 22 4 21.1046 4 20V4ZM17.5858 8H14V4.41421L17.5858 8ZM12 4V9C12 9.55228 12.4477 10 13 10H18V20H6V4L12 4Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val documentRemove = IconDefinition(
            "documentRemove",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 4C4 2.89543 4.89543 2 6 2H14C14.2652 2 14.5196 2.10536 14.7071 2.29289L19.7071 7.29289C19.8946 7.48043 20 7.73478 20 8V20C20 21.1046 19.1046 22 18 22H6C4.89543 22 4 21.1046 4 20V4ZM17.5858 8H14V4.41421L17.5858 8ZM12 4V9C12 9.55228 12.4477 10 13 10H18V20H6V4L12 4ZM9 15C9 14.4477 9.44772 14 10 14H14C14.5523 14 15 14.4477 15 15C15 15.5523 14.5523 16 14 16H10C9.44772 16 9 15.5523 9 15Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val document = IconDefinition(
            "document",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 4C4 2.89543 4.89543 2 6 2H14C14.2652 2 14.5196 2.10536 14.7071 2.29289L19.7071 7.29289C19.8946 7.48043 20 7.73478 20 8V20C20 21.1046 19.1046 22 18 22H6C4.89543 22 4 21.1046 4 20V4ZM17.5858 8H14V4.41421L17.5858 8ZM12 4V9C12 9.55228 12.4477 10 13 10H18V20H6V4L12 4ZM8 13C8 12.4477 8.44772 12 9 12H15C15.5523 12 16 12.4477 16 13C16 13.5523 15.5523 14 15 14H9C8.44772 14 8 13.5523 8 13ZM8 17C8 16.4477 8.44772 16 9 16H15C15.5523 16 16 16.4477 16 17C16 17.5523 15.5523 18 15 18H9C8.44772 18 8 17.5523 8 17Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val download = IconDefinition(
            "download",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 2C12.5523 2 13 2.44772 13 3V13.5858L15.2929 11.2929C15.6834 10.9024 16.3166 10.9024 16.7071 11.2929C17.0976 11.6834 17.0976 12.3166 16.7071 12.7071L12.7071 16.7071C12.3166 17.0976 11.6834 17.0976 11.2929 16.7071L7.29289 12.7071C6.90237 12.3166 6.90237 11.6834 7.29289 11.2929C7.68342 10.9024 8.31658 10.9024 8.70711 11.2929L11 13.5858V3C11 2.44772 11.4477 2 12 2ZM5 17C5.55228 17 6 17.4477 6 18V20H18V18C18 17.4477 18.4477 17 19 17C19.5523 17 20 17.4477 20 18V20C20 21.1046 19.1046 22 18 22H6C4.89543 22 4 21.1046 4 20V18C4 17.4477 4.44772 17 5 17Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val drag = IconDefinition(
            "drag",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 6C4 5.44772 4.44772 5 5 5H19C19.5523 5 20 5.44772 20 6C20 6.55228 19.5523 7 19 7H5C4.44772 7 4 6.55228 4 6ZM4 10C4 9.44772 4.44772 9 5 9H19C19.5523 9 20 9.44772 20 10C20 10.5523 19.5523 11 19 11H5C4.44772 11 4 10.5523 4 10ZM4 14C4 13.4477 4.44772 13 5 13H19C19.5523 13 20 13.4477 20 14C20 14.5523 19.5523 15 19 15H5C4.44772 15 4 14.5523 4 14ZM4 18C4 17.4477 4.44772 17 5 17H19C19.5523 17 20 17.4477 20 18C20 18.5523 19.5523 19 19 19H5C4.44772 19 4 18.5523 4 18Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val editAlt = IconDefinition(
            "editAlt",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M16.2929 3.29289C16.6834 2.90237 17.3166 2.90237 17.7071 3.29289L20.7071 6.29289C21.0976 6.68342 21.0976 7.31658 20.7071 7.70711L11.7071 16.7071C11.5196 16.8946 11.2652 17 11 17H8C7.44772 17 7 16.5523 7 16V13C7 12.7348 7.10536 12.4804 7.29289 12.2929L16.2929 3.29289ZM9 13.4142V15H10.5858L18.5858 7L17 5.41421L9 13.4142ZM3 7C3 5.89543 3.89543 5 5 5H10C10.5523 5 11 5.44772 11 6C11 6.55228 10.5523 7 10 7H5V19H17V14C17 13.4477 17.4477 13 18 13C18.5523 13 19 13.4477 19 14V19C19 20.1046 18.1046 21 17 21H5C3.89543 21 3 20.1046 3 19V7Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val edit = IconDefinition(
            "edit",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M16.2929 2.29289C16.6834 1.90237 17.3166 1.90237 17.7071 2.29289L21.7071 6.29289C22.0976 6.68342 22.0976 7.31658 21.7071 7.70711L8.70711 20.7071C8.51957 20.8946 8.26522 21 8 21H4C3.44772 21 3 20.5523 3 20V16C3 15.7348 3.10536 15.4804 3.29289 15.2929L13.2927 5.2931L16.2929 2.29289ZM14 7.41421L5 16.4142V19H7.58579L16.5858 10L14 7.41421ZM18 8.58579L15.4142 6L17 4.41421L19.5858 7L18 8.58579Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val email = IconDefinition(
            "email",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 6C2 4.89543 2.89543 4 4 4H20C21.1046 4 22 4.89543 22 6V18C22 19.1046 21.1046 20 20 20H4C2.89543 20 2 19.1046 2 18V6ZM5.51859 6L12 11.6712L18.4814 6H5.51859ZM20 7.32877L12.6585 13.7526C12.2815 14.0825 11.7185 14.0825 11.3415 13.7526L4 7.32877V18H20V7.32877Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val expand = IconDefinition(
            "expand",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M6 7.41421V9C6 9.55229 5.55228 10 5 10C4.44772 10 4 9.55229 4 9L4 5C4 4.44772 4.44772 4 5 4L9 4C9.55229 4 10 4.44772 10 5C10 5.55228 9.55229 6 9 6H7.41421L9.70711 8.29289C10.0976 8.68342 10.0976 9.31658 9.70711 9.70711C9.31658 10.0976 8.68342 10.0976 8.29289 9.70711L6 7.41421ZM15 6C14.4477 6 14 5.55229 14 5C14 4.44772 14.4477 4 15 4H19C19.5523 4 20 4.44772 20 5V9.00001C20 9.55229 19.5523 10 19 10C18.4477 10 18 9.55229 18 9.00001V7.41421L15.7071 9.70711C15.3166 10.0976 14.6834 10.0976 14.2929 9.70711C13.9024 9.31658 13.9024 8.68342 14.2929 8.29289L16.5858 6H15ZM5 14C5.55228 14 6 14.4477 6 15L6 16.5858L8.29289 14.2929C8.68342 13.9024 9.31658 13.9024 9.70711 14.2929C10.0976 14.6834 10.0976 15.3166 9.70711 15.7071L7.41421 18H9C9.55228 18 10 18.4477 10 19C10 19.5523 9.55228 20 9 20H5C4.73478 20 4.48043 19.8946 4.29289 19.7071C4.10536 19.5196 4 19.2652 4 19L4 15C4 14.4477 4.44772 14 5 14ZM14.2929 15.7071C13.9024 15.3166 13.9024 14.6834 14.2929 14.2929C14.6834 13.9024 15.3166 13.9024 15.7071 14.2929L18 16.5858V15C18 14.4477 18.4477 14 19 14C19.5523 14 20 14.4477 20 15V19C20 19.5523 19.5523 20 19 20H15C14.4477 20 14 19.5523 14 19C14 18.4477 14.4477 18 15 18H16.5858L14.2929 15.7071Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val export = IconDefinition(
            "export",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M11.2929 2.29289C11.6834 1.90237 12.3166 1.90237 12.7071 2.29289L16.7071 6.29289C17.0976 6.68342 17.0976 7.31658 16.7071 7.70711C16.3166 8.09763 15.6834 8.09763 15.2929 7.70711L13 5.41421V16C13 16.5523 12.5523 17 12 17C11.4477 17 11 16.5523 11 16V5.41421L8.70711 7.70711C8.31658 8.09763 7.68342 8.09763 7.29289 7.70711C6.90237 7.31658 6.90237 6.68342 7.29289 6.29289L11.2929 2.29289ZM5 17C5.55228 17 6 17.4477 6 18V20H18V18C18 17.4477 18.4477 17 19 17C19.5523 17 20 17.4477 20 18V20C20 21.1046 19.1046 22 18 22H6C4.89543 22 4 21.1046 4 20V18C4 17.4477 4.44772 17 5 17Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val externalLink = IconDefinition(
            "externalLink",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M14 5C13.4477 5 13 4.55228 13 4C13 3.44772 13.4477 3 14 3H20C20.2652 3 20.5196 3.10536 20.7071 3.29289C20.8946 3.48043 21 3.73478 21 4L21 10C21 10.5523 20.5523 11 20 11C19.4477 11 19 10.5523 19 10L19 6.41422L9.70711 15.7071C9.31658 16.0976 8.68342 16.0976 8.29289 15.7071C7.90237 15.3166 7.90237 14.6834 8.29289 14.2929L17.5858 5H14ZM3 7C3 5.89543 3.89543 5 5 5H10C10.5523 5 11 5.44772 11 6C11 6.55228 10.5523 7 10 7H5V19H17V14C17 13.4477 17.4477 13 18 13C18.5523 13 19 13.4477 19 14V19C19 20.1046 18.1046 21 17 21H5C3.89543 21 3 20.1046 3 19V7Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val eyeOff = IconDefinition(
            "eyeOff",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4.70711 3.29289C4.31658 2.90237 3.68342 2.90237 3.29289 3.29289C2.90237 3.68342 2.90237 4.31658 3.29289 4.70711L5.71706 7.13127C4.28639 8.20737 3.03925 9.68543 2.10557 11.5528C1.96481 11.8343 1.96481 12.1657 2.10557 12.4472C4.26379 16.7637 8.09687 19 12 19C13.5552 19 15.0992 18.645 16.5306 17.9448L19.2929 20.7071C19.6834 21.0976 20.3166 21.0976 20.7071 20.7071C21.0976 20.3166 21.0976 19.6834 20.7071 19.2929L4.70711 3.29289ZM15.0138 16.428L13.2934 14.7076C12.9018 14.8951 12.4631 15 12 15C10.3431 15 9 13.6569 9 12C9 11.5369 9.10495 11.0982 9.29237 10.7066L7.14838 8.56259C5.98778 9.3794 4.94721 10.5214 4.12966 12C5.99806 15.3792 9.03121 17 12 17C13.0134 17 14.0343 16.8112 15.0138 16.428Z" fill="#0D0D0D"/>
                <path d="M18.5523 13.8955C19.0353 13.3402 19.4784 12.7088 19.8703 12C18.0019 8.62078 14.9687 7 12 7C11.888 7 11.7759 7.00231 11.6637 7.00693L9.87939 5.22258C10.5774 5.07451 11.2875 5 12 5C15.9031 5 19.7362 7.23635 21.8944 11.5528C22.0352 11.8343 22.0352 12.1657 21.8944 12.4472C21.3504 13.5352 20.7 14.491 19.9689 15.3121L18.5523 13.8955Z" fill="#0D0D0D"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val eye = IconDefinition(
            "eye",
            svg = """
                <path d="M15 12C15 13.6569 13.6569 15 12 15C10.3431 15 9 13.6569 9 12C9 10.3431 10.3431 9 12 9C13.6569 9 15 10.3431 15 12Z" fill="currentColor"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M21.8944 11.5528C19.7362 7.23635 15.9031 5 12 5C8.09687 5 4.26379 7.23635 2.10557 11.5528C1.96481 11.8343 1.96481 12.1657 2.10557 12.4472C4.26379 16.7637 8.09687 19 12 19C15.9031 19 19.7362 16.7637 21.8944 12.4472C22.0352 12.1657 22.0352 11.8343 21.8944 11.5528ZM12 17C9.03121 17 5.99806 15.3792 4.12966 12C5.99806 8.62078 9.03121 7 12 7C14.9688 7 18.0019 8.62078 19.8703 12C18.0019 15.3792 14.9688 17 12 17Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val favorite = IconDefinition(
            "favorite",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 2.5C12.3788 2.5 12.725 2.714 12.8944 3.05279L15.4733 8.2106L21.1439 9.03541C21.5206 9.0902 21.8335 9.35402 21.9511 9.71599C22.0687 10.078 21.9706 10.4753 21.6981 10.741L17.571 14.7649L18.4994 20.4385C18.5607 20.8135 18.4043 21.1908 18.0956 21.4124C17.787 21.6339 17.3794 21.6614 17.0438 21.4834L12 18.8071L6.95621 21.4834C6.62059 21.6614 6.21303 21.6339 5.90437 21.4124C5.5957 21.1908 5.43927 20.8135 5.50062 20.4385L6.42903 14.7649L2.3019 10.741C2.02939 10.4753 1.93133 10.078 2.04894 9.71599C2.16655 9.35402 2.47943 9.0902 2.85606 9.03541L8.52667 8.2106L11.1056 3.05279C11.275 2.714 11.6212 2.5 12 2.5ZM12 5.73607L10.0819 9.57221C9.93558 9.86491 9.65528 10.0675 9.33144 10.1146L5.14839 10.723L8.1981 13.6965C8.43179 13.9243 8.53958 14.2519 8.48687 14.574L7.80001 18.7715L11.5313 16.7917C11.8244 16.6361 12.1756 16.6361 12.4687 16.7917L16.2 18.7715L15.5131 14.574C15.4604 14.2519 15.5682 13.9243 15.8019 13.6965L18.8516 10.723L14.6686 10.1146C14.3447 10.0675 14.0644 9.86491 13.9181 9.57221L12 5.73607Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val filterAlt = IconDefinition(
            "filterAlt",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 7C4 6.44772 4.44772 6 5 6H19C19.5523 6 20 6.44772 20 7C20 7.55228 19.5523 8 19 8H5C4.44772 8 4 7.55228 4 7ZM6 12C6 11.4477 6.44772 11 7 11H17C17.5523 11 18 11.4477 18 12C18 12.5523 17.5523 13 17 13H7C6.44772 13 6 12.5523 6 12ZM8 17C8 16.4477 8.44772 16 9 16H15C15.5523 16 16 16.4477 16 17C16 17.5523 15.5523 18 15 18H9C8.44772 18 8 17.5523 8 17Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val filter = IconDefinition(
            "filter",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M9 5C8.44772 5 8 5.44772 8 6C8 6.55228 8.44772 7 9 7C9.55228 7 10 6.55228 10 6C10 5.44772 9.55228 5 9 5ZM6.17071 5C6.58254 3.83481 7.69378 3 9 3C10.3062 3 11.4175 3.83481 11.8293 5H19C19.5523 5 20 5.44772 20 6C20 6.55228 19.5523 7 19 7H11.8293C11.4175 8.16519 10.3062 9 9 9C7.69378 9 6.58254 8.16519 6.17071 7H5C4.44772 7 4 6.55228 4 6C4 5.44772 4.44772 5 5 5H6.17071ZM15 11C14.4477 11 14 11.4477 14 12C14 12.5523 14.4477 13 15 13C15.5523 13 16 12.5523 16 12C16 11.4477 15.5523 11 15 11ZM12.1707 11C12.5825 9.83481 13.6938 9 15 9C16.3062 9 17.4175 9.83481 17.8293 11H19C19.5523 11 20 11.4477 20 12C20 12.5523 19.5523 13 19 13H17.8293C17.4175 14.1652 16.3062 15 15 15C13.6938 15 12.5825 14.1652 12.1707 13H5C4.44772 13 4 12.5523 4 12C4 11.4477 4.44772 11 5 11H12.1707ZM9 17C8.44772 17 8 17.4477 8 18C8 18.5523 8.44772 19 9 19C9.55228 19 10 18.5523 10 18C10 17.4477 9.55228 17 9 17ZM6.17071 17C6.58254 15.8348 7.69378 15 9 15C10.3062 15 11.4175 15.8348 11.8293 17H19C19.5523 17 20 17.4477 20 18C20 18.5523 19.5523 19 19 19H11.8293C11.4175 20.1652 10.3062 21 9 21C7.69378 21 6.58254 20.1652 6.17071 19H5C4.44772 19 4 18.5523 4 18C4 17.4477 4.44772 17 5 17H6.17071Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val folderAdd = IconDefinition(
            "folderAdd",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 6C2 4.89543 2.89543 4 4 4H9C9.26522 4 9.51957 4.10536 9.70711 4.29289L11.4142 6H20C21.1046 6 22 6.89543 22 8V18C22 19.1046 21.1046 20 20 20H4C2.89543 20 2 19.1046 2 18V6ZM8.58579 6L4 6V18H20V8H11C10.7348 8 10.4804 7.89464 10.2929 7.70711L8.58579 6ZM12 10C12.5523 10 13 10.4477 13 11V12H14C14.5523 12 15 12.4477 15 13C15 13.5523 14.5523 14 14 14H13V15C13 15.5523 12.5523 16 12 16C11.4477 16 11 15.5523 11 15V14H10C9.44772 14 9 13.5523 9 13C9 12.4477 9.44772 12 10 12H11V11C11 10.4477 11.4477 10 12 10Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val folderCheck = IconDefinition(
            "folderCheck",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 6C2 4.89543 2.89543 4 4 4H9C9.26522 4 9.51957 4.10536 9.70711 4.29289L11.4142 6H20C21.1046 6 22 6.89543 22 8V18C22 19.1046 21.1046 20 20 20H4C2.89543 20 2 19.1046 2 18V6ZM8.58579 6L4 6V18H20V8H11C10.7348 8 10.4804 7.89464 10.2929 7.70711L8.58579 6ZM15.7071 10.2929C16.0976 10.6834 16.0976 11.3166 15.7071 11.7071L11.7071 15.7071C11.5196 15.8946 11.2652 16 11 16C10.7348 16 10.4804 15.8946 10.2929 15.7071L8.29289 13.7071C7.90237 13.3166 7.90237 12.6834 8.29289 12.2929C8.68342 11.9024 9.31658 11.9024 9.70711 12.2929L11 13.5858L14.2929 10.2929C14.6834 9.90237 15.3166 9.90237 15.7071 10.2929Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val folderDownload = IconDefinition(
            "folderDownload",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 6.00002C2 4.89545 2.89543 4.00002 4 4.00002H9C9.26522 4.00002 9.51957 4.10537 9.70711 4.29291L11.4142 6.00002H20C21.1046 6.00002 22 6.89545 22 8.00002V18C22 19.1046 21.1046 20 20 20H4C2.89543 20 2 19.1046 2 18V6.00002ZM8.58579 6.00002L4 6.00002V18H20V8.00002H11C10.7348 8.00002 10.4804 7.89466 10.2929 7.70712L8.58579 6.00002ZM12 9.50002C12.5523 9.50002 13 9.94773 13 10.5V13.0858L13.2929 12.7929C13.6834 12.4024 14.3166 12.4024 14.7071 12.7929C15.0976 13.1834 15.0976 13.8166 14.7071 14.2071L12.7071 16.2071C12.3166 16.5976 11.6834 16.5976 11.2929 16.2071L9.29289 14.2071C8.90237 13.8166 8.90237 13.1834 9.29289 12.7929C9.68342 12.4024 10.3166 12.4024 10.7071 12.7929L11 13.0858V10.5C11 9.94773 11.4477 9.50002 12 9.50002Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val folderRemove = IconDefinition(
            "folderRemove",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 6C2 4.89543 2.89543 4 4 4H9C9.26522 4 9.51957 4.10536 9.70711 4.29289L11.4142 6H20C21.1046 6 22 6.89543 22 8V18C22 19.1046 21.1046 20 20 20H4C2.89543 20 2 19.1046 2 18V6ZM8.58579 6L4 6V18H20V8H11C10.7348 8 10.4804 7.89464 10.2929 7.70711L8.58579 6ZM9 13C9 12.4477 9.44772 12 10 12H14C14.5523 12 15 12.4477 15 13C15 13.5523 14.5523 14 14 14H10C9.44772 14 9 13.5523 9 13Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val folder = IconDefinition(
            "folder",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 6C2 4.89543 2.89543 4 4 4H9C9.26522 4 9.51957 4.10536 9.70711 4.29289L11.4142 6H20C21.1046 6 22 6.89543 22 8V18C22 19.1046 21.1046 20 20 20H4C2.89543 20 2 19.1046 2 18V6ZM8.58579 6L4 6V18H20V8H11C10.7348 8 10.4804 7.89464 10.2929 7.70711L8.58579 6Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val grid = IconDefinition(
            "grid",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M3 5C3 3.89543 3.89543 3 5 3H9C10.1046 3 11 3.89543 11 5V9C11 10.1046 10.1046 11 9 11H5C3.89543 11 3 10.1046 3 9V5ZM9 5H5V9H9V5ZM13 5C13 3.89543 13.8954 3 15 3H19C20.1046 3 21 3.89543 21 5V9C21 10.1046 20.1046 11 19 11H15C13.8954 11 13 10.1046 13 9V5ZM19 5H15V9H19V5ZM3 15C3 13.8954 3.89543 13 5 13H9C10.1046 13 11 13.8954 11 15V19C11 20.1046 10.1046 21 9 21H5C3.89543 21 3 20.1046 3 19V15ZM9 15H5V19H9V15ZM13 15C13 13.8954 13.8954 13 15 13H19C20.1046 13 21 13.8954 21 15V19C21 20.1046 20.1046 21 19 21H15C13.8954 21 13 20.1046 13 19V15ZM19 15H15V19H19V15Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val heart = IconDefinition(
            "heart",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4.52765C9.64418 2.41689 6.02125 2.49347 3.75736 4.75736C1.41421 7.1005 1.41421 10.8995 3.75736 13.2426L10.5858 20.0711C11.3668 20.8521 12.6332 20.8521 13.4142 20.0711L20.2426 13.2426C22.5858 10.8995 22.5858 7.1005 20.2426 4.75736C17.9787 2.49347 14.3558 2.41689 12 4.52765ZM10.8284 6.17157C9.26633 4.60948 6.73367 4.60948 5.17157 6.17157C3.60948 7.73367 3.60948 10.2663 5.17157 11.8284L12 18.6569L18.8284 11.8284C20.3905 10.2663 20.3905 7.73367 18.8284 6.17157C17.2663 4.60948 14.7337 4.60948 13.1716 6.17157L12.7071 6.63604C12.3166 7.02656 11.6834 7.02656 11.2929 6.63604L10.8284 6.17157Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val home = IconDefinition(
            "home",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M11.3356 2.25259C11.7145 1.9158 12.2855 1.9158 12.6644 2.25259L21.6644 10.2526C22.0772 10.6195 22.1143 11.2516 21.7474 11.6644C21.3805 12.0771 20.7484 12.1143 20.3356 11.7474L20 11.4491V19C20 20.1046 19.1046 21 18 21H6.00001C4.89544 21 4.00001 20.1046 4.00001 19V11.4491L3.66437 11.7474C3.25159 12.1143 2.61952 12.0771 2.2526 11.6644C1.88568 11.2516 1.92286 10.6195 2.33565 10.2526L11.3356 2.25259ZM6.00001 9.67129V19H9.00001V14C9.00001 13.4477 9.44773 13 10 13H14C14.5523 13 15 13.4477 15 14V19H18V9.67129L12 4.33795L6.00001 9.67129ZM13 19V15H11V19H13Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val image = IconDefinition(
            "image",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2.99998 5C2.99998 3.89543 3.89541 3 4.99998 3H19C20.1045 3 21 3.89543 21 5V19C21 20.1046 20.1045 21 19 21H4.99998C3.89541 21 2.99998 20.1046 2.99998 19V5ZM19 5H4.99998V19H19V5Z" fill="currentColor"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M8.37528 10.2191C8.7405 9.92696 9.25945 9.92696 9.62467 10.2191L13.9258 13.66L15.2929 12.2929C15.6834 11.9024 16.3166 11.9024 16.7071 12.2929L20.7071 16.2929C21.0976 16.6834 21.0976 17.3166 20.7071 17.7071C20.3166 18.0976 19.6834 18.0976 19.2929 17.7071L16 14.4142L14.7071 15.7071C14.3468 16.0674 13.7732 16.0992 13.3753 15.7809L8.99998 12.2806L4.62467 15.7809C4.19341 16.1259 3.56412 16.056 3.21911 15.6247C2.8741 15.1934 2.94402 14.5641 3.37528 14.2191L8.37528 10.2191Z" fill="currentColor"/>
                <path d="M17 8.5C17 9.32843 16.3284 10 15.5 10C14.6715 10 14 9.32843 14 8.5C14 7.67157 14.6715 7 15.5 7C16.3284 7 17 7.67157 17 8.5Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val inbox = IconDefinition(
            "inbox",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M3 5C3 3.89543 3.89543 3 5 3H19C20.1046 3 21 3.89543 21 5V19C21 20.1046 20.1046 21 19 21H5C3.89543 21 3 20.1046 3 19V5ZM5 14V19H19V14H16.7208L15.9487 16.3162C15.8126 16.7246 15.4304 17 15 17H9C8.56957 17 8.18743 16.7246 8.05132 16.3162L7.27924 14H5ZM19 12V5H5V12H7.27924C8.1401 12 8.90438 12.5509 9.17661 13.3675L9.72076 15H14.2792L14.8234 13.3675C15.0956 12.5509 15.8599 12 16.7208 12H19Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val laptop = IconDefinition(
            "laptop",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 5C2 3.89543 2.89543 3 4 3H20C21.1046 3 22 3.89543 22 5V16C22 17.1046 21.1046 18 20 18H4C2.89543 18 2 17.1046 2 16V5ZM20 5H4V16H20V5Z" fill="currentColor"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M22 20C22 20.5523 21.5523 21 21 21H3C2.44772 21 2 20.5523 2 20C2 19.4477 2.44772 19 3 19L21 19C21.5523 19 22 19.4477 22 20Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val linkAlt = IconDefinition(
            "linkAlt",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M16.2 6C16.2 5.44772 16.6477 5 17.2 5C17.8367 5 18.4624 5.12794 19.0714 5.37152C19.7341 5.63661 20.2442 6.03001 20.7071 6.49289C21.17 6.95578 21.5634 7.4659 21.8285 8.12861C22.0721 8.73756 22.2 9.36326 22.2 10C22.2 10.6367 22.0721 11.2624 21.8285 11.8714C21.5667 12.5258 21.1798 13.0314 20.7245 13.4897L19.7399 14.5727C19.7293 14.5844 19.7183 14.5959 19.7071 14.6071L16.5071 17.8071C16.0442 18.27 15.5341 18.6634 14.8714 18.9285C14.2624 19.1721 13.6367 19.3 13 19.3C12.3633 19.3 11.7376 19.1721 11.1286 18.9285C10.4659 18.6634 9.95578 18.27 9.49289 17.8071C9.03 17.3442 8.63661 16.8341 8.37152 16.1714C8.12326 15.5507 8 14.9244 8 14.2C8 13.5633 8.12794 12.9376 8.37152 12.3286C8.63661 11.6659 9.03 11.1558 9.49289 10.6929L10.5929 9.59289C10.9834 9.20237 11.6166 9.20237 12.0071 9.59289C12.3976 9.98342 12.3976 10.6166 12.0071 11.0071L10.9071 12.1071C10.57 12.4442 10.3634 12.7341 10.2285 13.0714C10.0721 13.4624 10 13.8367 10 14.2C10 14.6756 10.0767 15.0493 10.2285 15.4286C10.3634 15.7659 10.57 16.0558 10.9071 16.3929C11.2442 16.73 11.5341 16.9366 11.8714 17.0715C12.2624 17.2279 12.6367 17.3 13 17.3C13.3633 17.3 13.7376 17.2279 14.1286 17.0715C14.4659 16.9366 14.7558 16.73 15.0929 16.3929L18.2761 13.2097L19.2601 12.1273C19.2707 12.1156 19.2817 12.1041 19.2929 12.0929C19.63 11.7558 19.8366 11.4659 19.9715 11.1286C20.1279 10.7376 20.2 10.3633 20.2 10C20.2 9.63674 20.1279 9.26244 19.9715 8.87139C19.8366 8.5341 19.63 8.24422 19.2929 7.90711C18.9558 7.56999 18.6659 7.36339 18.3286 7.22848C17.9376 7.07206 17.5633 7 17.2 7C16.6477 7 16.2 6.55228 16.2 6ZM11.3 7.5C10.9367 7.5 10.5624 7.57206 10.1714 7.72848C9.8341 7.86339 9.54422 8.07 9.20711 8.40711L5.92393 11.6903L4.93994 12.7727C4.92927 12.7844 4.91832 12.7959 4.90711 12.8071C4.56999 13.1442 4.36339 13.4341 4.22848 13.7714C4.07206 14.1624 4 14.5367 4 14.9C4 15.2633 4.07206 15.6376 4.22848 16.0286C4.36339 16.3659 4.57 16.6558 4.90711 16.9929C5.24422 17.33 5.5341 17.5366 5.87139 17.6715C6.26244 17.8279 6.63674 17.9 7 17.9C7.55228 17.9 8 18.3477 8 18.9C8 19.4523 7.55228 19.9 7 19.9C6.36326 19.9 5.73756 19.7721 5.12861 19.5285C4.4659 19.2634 3.95578 18.87 3.49289 18.4071C3.03 17.9442 2.63661 17.4341 2.37152 16.7714C2.12794 16.1624 2 15.5367 2 14.9C2 14.2633 2.12794 13.6376 2.37152 13.0286C2.63328 12.3742 3.02016 11.8686 3.47551 11.4103L4.46006 10.3273C4.47073 10.3156 4.48168 10.3041 4.49289 10.2929L7.79289 6.99289C8.25578 6.53 8.7659 6.13661 9.42861 5.87152C10.0376 5.62794 10.6633 5.5 11.3 5.5C11.9367 5.5 12.5624 5.62794 13.1714 5.87152C13.8341 6.13661 14.3442 6.53 14.8071 6.99289C15.27 7.45578 15.6634 7.9659 15.9285 8.62861C16.1721 9.23756 16.3 9.86326 16.3 10.5C16.3 11.1367 16.1721 11.7624 15.9285 12.3714C15.6664 13.0265 15.279 13.5325 14.823 13.9912L13.7372 15.1757C13.364 15.5828 12.7314 15.6103 12.3243 15.2372C11.9172 14.864 11.8897 14.2314 12.2628 13.8243L13.3628 12.6243C13.3726 12.6136 13.3827 12.6031 13.3929 12.5929C13.73 12.2558 13.9366 11.9659 14.0715 11.6286C14.2279 11.2376 14.3 10.8633 14.3 10.5C14.3 10.1367 14.2279 9.76244 14.0715 9.37139C13.9366 9.0341 13.73 8.74422 13.3929 8.40711C13.0558 8.07 12.7659 7.86339 12.4286 7.72848C12.0376 7.57206 11.6633 7.5 11.3 7.5Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val link = IconDefinition(
            "link",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M8 8C5.75228 8 4 9.75228 4 12C4 14.2477 5.75228 16 8 16H10C10.5523 16 11 16.4477 11 17C11 17.5523 10.5523 18 10 18H8C4.64772 18 2 15.3523 2 12C2 8.64772 4.64772 6 8 6H10C10.5523 6 11 6.44772 11 7C11 7.55228 10.5523 8 10 8H8ZM13 7C13 6.44772 13.4477 6 14 6H16C19.3523 6 22 8.64772 22 12C22 15.3523 19.3523 18 16 18H14C13.4477 18 13 17.5523 13 17C13 16.4477 13.4477 16 14 16H16C18.2477 16 20 14.2477 20 12C20 9.75228 18.2477 8 16 8H14C13.4477 8 13 7.55228 13 7ZM7 12C7 11.4477 7.44772 11 8 11H16C16.5523 11 17 11.4477 17 12C17 12.5523 16.5523 13 16 13H8C7.44772 13 7 12.5523 7 12Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val list = IconDefinition(
            "list",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 7C4 6.44772 4.44772 6 5 6H6C6.55228 6 7 6.44772 7 7C7 7.55228 6.55228 8 6 8H5C4.44772 8 4 7.55228 4 7ZM9 7C9 6.44772 9.44772 6 10 6H19C19.5523 6 20 6.44772 20 7C20 7.55228 19.5523 8 19 8H10C9.44772 8 9 7.55228 9 7ZM4 12C4 11.4477 4.44772 11 5 11H6C6.55228 11 7 11.4477 7 12C7 12.5523 6.55228 13 6 13H5C4.44772 13 4 12.5523 4 12ZM9 12C9 11.4477 9.44772 11 10 11H19C19.5523 11 20 11.4477 20 12C20 12.5523 19.5523 13 19 13H10C9.44772 13 9 12.5523 9 12ZM4 17C4 16.4477 4.44772 16 5 16H6C6.55228 16 7 16.4477 7 17C7 17.5523 6.55228 18 6 18H5C4.44772 18 4 17.5523 4 17ZM9 17C9 16.4477 9.44772 16 10 16H19C19.5523 16 20 16.4477 20 17C20 17.5523 19.5523 18 19 18H10C9.44772 18 9 17.5523 9 17Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val location = IconDefinition(
            "location",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C8.68629 4 6 6.68629 6 10C6 11.0279 6.36394 12.1768 6.9815 13.3678C7.59457 14.5501 8.4236 15.7092 9.27193 16.7393C10.1178 17.7664 10.9665 18.6457 11.6048 19.2688C11.7483 19.4089 11.8809 19.5357 12 19.6481C12.1191 19.5357 12.2517 19.4089 12.3952 19.2688C13.0335 18.6457 13.8822 17.7664 14.7281 16.7393C15.5764 15.7092 16.4054 14.5501 17.0185 13.3678C17.6361 12.1768 18 11.0279 18 10C18 6.68629 15.3137 4 12 4ZM12 21C11.3492 21.7593 11.349 21.7591 11.3488 21.7589L11.3467 21.7571L11.3417 21.7528L11.3241 21.7376C11.3092 21.7246 11.2878 21.7059 11.2604 21.6818C11.2055 21.6334 11.1266 21.5632 11.0269 21.4727C10.8277 21.2918 10.5454 21.0296 10.2077 20.6999C9.5335 20.0418 8.63218 19.1086 7.72807 18.0107C6.8264 16.9158 5.90543 15.6374 5.206 14.2884C4.51106 12.9482 4 11.4721 4 10C4 5.58172 7.58172 2 12 2C16.4183 2 20 5.58172 20 10C20 11.4721 19.4889 12.9482 18.794 14.2884C18.0946 15.6374 17.1736 16.9158 16.2719 18.0107C15.3678 19.1086 14.4665 20.0418 13.7923 20.6999C13.4546 21.0296 13.1723 21.2918 12.9731 21.4727C12.8734 21.5632 12.7945 21.6334 12.7396 21.6818C12.7122 21.7059 12.6908 21.7246 12.6759 21.7376L12.6583 21.7528L12.6533 21.7571L12.6517 21.7585C12.6515 21.7586 12.6508 21.7593 12 21ZM12 21L11.3488 21.7589C11.7233 22.0799 12.2763 22.0802 12.6508 21.7593L12 21ZM12 8C10.8954 8 10 8.89543 10 10C10 11.1046 10.8954 12 12 12C13.1046 12 14 11.1046 14 10C14 8.89543 13.1046 8 12 8ZM8 10C8 7.79086 9.79086 6 12 6C14.2091 6 16 7.79086 16 10C16 12.2091 14.2091 14 12 14C9.79086 14 8 12.2091 8 10Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val lock = IconDefinition(
            "lock",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C10.3523 4 9 5.35228 9 7V10H15V7C15 5.35228 13.6477 4 12 4ZM17 10V7C17 4.24772 14.7523 2 12 2C9.24771 2 7 4.24772 7 7V10H6C4.89543 10 4 10.8954 4 12V20C4 21.1046 4.89543 22 6 22H18C19.1046 22 20 21.1046 20 20V12C20 10.8954 19.1046 10 18 10H17ZM6 12V20H18V12H6Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val logOut = IconDefinition(
            "logOut",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 6C2 4.89543 2.89543 4 4 4L13 4C14.1046 4 15 4.89543 15 6L15 8C15 8.55229 14.5523 9 14 9C13.4477 9 13 8.55228 13 8L13 6L4 6L4 18H13V16C13 15.4477 13.4477 15 14 15C14.5523 15 15 15.4477 15 16V18C15 19.1046 14.1046 20 13 20H4C2.89543 20 2 19.1046 2 18L2 6ZM17.2929 8.29289C17.6834 7.90237 18.3166 7.90237 18.7071 8.29289L21.7071 11.2929C22.0976 11.6834 22.0976 12.3166 21.7071 12.7071L18.7071 15.7071C18.3166 16.0976 17.6834 16.0976 17.2929 15.7071C16.9024 15.3166 16.9024 14.6834 17.2929 14.2929L18.5858 13H9C8.44772 13 8 12.5523 8 12C8 11.4477 8.44772 11 9 11L18.5858 11L17.2929 9.70711C16.9024 9.31658 16.9024 8.68342 17.2929 8.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val map = IconDefinition(
            "map",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M8.68377 3.05132C8.88904 2.98289 9.11096 2.98289 9.31623 3.05132L15 4.94591L19.3675 3.49006C20.6626 3.05837 22 4.02231 22 5.38743V17.2792C22 18.1401 21.4491 18.9044 20.6325 19.1766L15.3162 20.9487C15.111 21.0171 14.889 21.0171 14.6838 20.9487L9 19.0541L4.63246 20.5099C3.33739 20.9416 2 19.9777 2 18.6126V6.72076C2 5.8599 2.55086 5.09562 3.36754 4.82339L8.68377 3.05132ZM10 17.2792L14 18.6126V6.72076L10 5.38743V17.2792ZM8 5.38743V17.2792L4 18.6126V6.72076L8 5.38743ZM16 6.72076V18.6126L20 17.2792V5.38743L16 6.72076Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val megaphone = IconDefinition(
            "megaphone",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M17.5019 2.13509C17.8102 2.31401 18 2.64353 18 3V7C19.1848 7 20.2502 7.51635 20.9812 8.33309C21.614 9.0401 22 9.97617 22 11C22 12.0238 21.614 12.9599 20.9812 13.6669C20.2502 14.4836 19.1848 15 18 15V19C18 19.3565 17.8102 19.686 17.5019 19.8649C17.1936 20.0438 16.8134 20.0451 16.5039 19.8682L10 16.1518V21C10 21.5523 9.55228 22 9 22H5C4.44772 22 4 21.5523 4 21V16C2.89543 16 2 15.1046 2 14V8C2 6.89543 2.89543 6 4 6H9.73444L16.5039 2.13176C16.8134 1.9549 17.1936 1.95617 17.5019 2.13509ZM10 14C10.174 14 10.345 14.0454 10.4961 14.1318L16 17.2768V4.72318L10.4961 7.86824C10.345 7.95458 10.174 8 10 8H4V14H10ZM6 16V20H8V16H6ZM18 13V9C18.5922 9 19.1233 9.25615 19.491 9.66691C19.8083 10.0214 20 10.4871 20 11C20 11.5129 19.8083 11.9786 19.491 12.3331C19.1233 12.7438 18.5922 13 18 13Z" fill="#0D0D0D"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val menu = IconDefinition(
            "menu",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 7C4 6.44772 4.44772 6 5 6H19C19.5523 6 20 6.44772 20 7C20 7.55228 19.5523 8 19 8H5C4.44772 8 4 7.55228 4 7ZM4 12C4 11.4477 4.44772 11 5 11H19C19.5523 11 20 11.4477 20 12C20 12.5523 19.5523 13 19 13H5C4.44772 13 4 12.5523 4 12ZM4 17C4 16.4477 4.44772 16 5 16H19C19.5523 16 20 16.4477 20 17C20 17.5523 19.5523 18 19 18H5C4.44772 18 4 17.5523 4 17Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val messageAlt = IconDefinition(
            "messageAlt",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 6C2 4.89543 2.89543 4 4 4H20C21.1046 4 22 4.89543 22 6V17C22 18.1046 21.1046 19 20 19H15.4142L12.7071 21.7071C12.3166 22.0976 11.6834 22.0976 11.2929 21.7071L8.58579 19H4C2.89543 19 2 18.1046 2 17V6ZM20 6H4V17H9C9.26522 17 9.51957 17.1054 9.70711 17.2929L12 19.5858L14.2929 17.2929C14.4804 17.1054 14.7348 17 15 17H20V6Z" fill="currentColor"/>
                <path d="M13.5 11.5C13.5 12.3284 12.8284 13 12 13C11.1716 13 10.5 12.3284 10.5 11.5C10.5 10.6716 11.1716 10 12 10C12.8284 10 13.5 10.6716 13.5 11.5Z" fill="currentColor"/>
                <path d="M17.5 11.5C17.5 12.3284 16.8284 13 16 13C15.1716 13 14.5 12.3284 14.5 11.5C14.5 10.6716 15.1716 10 16 10C16.8284 10 17.5 10.6716 17.5 11.5Z" fill="currentColor"/>
                <path d="M9.5 11.5C9.5 12.3284 8.82843 13 8 13C7.17157 13 6.5 12.3284 6.5 11.5C6.5 10.6716 7.17157 10 8 10C8.82843 10 9.5 10.6716 9.5 11.5Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val message = IconDefinition(
            "message",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 6C2 4.89543 2.89543 4 4 4H20C21.1046 4 22 4.89543 22 6V17C22 18.1046 21.1046 19 20 19H15.4142L12.7071 21.7071C12.3166 22.0976 11.6834 22.0976 11.2929 21.7071L8.58579 19H4C2.89543 19 2 18.1046 2 17V6ZM20 6H4V17H9C9.26522 17 9.51957 17.1054 9.70711 17.2929L12 19.5858L14.2929 17.2929C14.4804 17.1054 14.7348 17 15 17H20V6ZM6 9.5C6 8.94772 6.44772 8.5 7 8.5H17C17.5523 8.5 18 8.94772 18 9.5C18 10.0523 17.5523 10.5 17 10.5H7C6.44772 10.5 6 10.0523 6 9.5ZM6 13.5C6 12.9477 6.44772 12.5 7 12.5H13C13.5523 12.5 14 12.9477 14 13.5C14 14.0523 13.5523 14.5 13 14.5H7C6.44772 14.5 6 14.0523 6 13.5Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val mobile = IconDefinition(
            "mobile",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M6 5C6 3.89543 6.89543 3 8 3H16C17.1046 3 18 3.89543 18 5V19C18 20.1046 17.1046 21 16 21H8C6.89543 21 6 20.1046 6 19V5ZM16 5H8V19H16V5Z" fill="currentColor"/>
                <path d="M13 17C13 17.5523 12.5523 18 12 18C11.4477 18 11 17.5523 11 17C11 16.4477 11.4477 16 12 16C12.5523 16 13 16.4477 13 17Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val moon = IconDefinition(
            "moon",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M9.36077 3.29291C9.6659 3.59803 9.74089 4.06445 9.54678 4.44984C9.04068 5.4547 8.75521 6.59035 8.75521 7.79557C8.75521 11.9097 12.0903 15.2448 16.2044 15.2448C17.4097 15.2448 18.5453 14.9593 19.5502 14.4532C19.9356 14.2591 20.402 14.3341 20.7071 14.6392C21.0122 14.9444 21.0872 15.4108 20.8931 15.7962C19.3396 18.8806 16.1428 21 12.4492 21C7.23056 21 3 16.7695 3 11.5508C3 7.85719 5.11941 4.6604 8.20384 3.1069C8.58923 2.91279 9.05565 2.98778 9.36077 3.29291ZM6.8217 6.6696C5.68637 7.97742 5 9.68431 5 11.5508C5 15.6649 8.33513 19 12.4492 19C14.3157 19 16.0226 18.3136 17.3304 17.1783C16.9611 17.2222 16.5853 17.2448 16.2044 17.2448C10.9858 17.2448 6.75521 13.0142 6.75521 7.79557C6.75521 7.41472 6.77779 7.03896 6.8217 6.6696Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val notificationOff = IconDefinition(
            "notificationOff",
            svg = """
                <path d="M19 10C19 6.77579 16.8202 4.06072 13.8539 3.24812C13.5567 2.51616 12.8386 2 12 2C11.1614 2 10.4433 2.51616 10.1461 3.24812C9.58605 3.40155 9.054 3.62281 8.55937 3.90252L10.051 5.39411C10.6499 5.14035 11.3086 5 12 5C14.7614 5 17 7.23858 17 10V12.3431L19 14.3431V10Z" fill="#0D0D0D"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M3.1753 17.4343L5 14.6972V9.99999C5 8.94987 5.23124 7.95375 5.64557 7.05978L3.29289 4.70711C2.90237 4.31658 2.90237 3.68342 3.29289 3.29289C3.68342 2.90237 4.31658 2.90237 4.70711 3.29289L20.7071 19.2929C21.0976 19.6834 21.0976 20.3166 20.7071 20.7071C20.3166 21.0976 19.6834 21.0976 19.2929 20.7071L17.5858 19H15.4646C15.2219 20.6961 13.7632 22 12 22C10.2368 22 8.77806 20.6961 8.53544 19H4.01296C3.91555 19.0014 3.81743 18.9885 3.72186 18.9608C3.59602 18.9244 3.48038 18.864 3.38026 18.7849C3.16669 18.616 3.02368 18.3618 3.00268 18.0738C2.9935 17.9509 3.0069 17.8258 3.04421 17.7051C3.07424 17.6076 3.11878 17.5165 3.1753 17.4343ZM5.86851 17H15.5858L7.19575 8.60997C7.0683 9.05126 7 9.51765 7 9.99999V15C7 15.1974 6.94156 15.3904 6.83205 15.5547L5.86851 17ZM10.5854 19C10.7913 19.5826 11.3469 20 12 20C12.6531 20 13.2087 19.5826 13.4146 19H10.5854Z" fill="#0D0D0D"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val notification = IconDefinition(
            "notification",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M10.1461 3.24812C10.4433 2.51616 11.1614 2 12 2C12.8386 2 13.5567 2.51616 13.8539 3.24812C16.8202 4.06072 19 6.77579 19 10V14.6972L20.8321 17.4453C21.0366 17.7522 21.0557 18.1467 20.8817 18.4719C20.7077 18.797 20.3688 19 20 19H15.4646C15.2219 20.6961 13.7632 22 12 22C10.2368 22 8.77806 20.6961 8.53545 19H4C3.63121 19 3.29235 18.797 3.11833 18.4719C2.94431 18.1467 2.96338 17.7522 3.16795 17.4453L5 14.6972V10C5 6.77579 7.17983 4.06072 10.1461 3.24812ZM10.5854 19C10.7913 19.5826 11.3469 20 12 20C12.6531 20 13.2087 19.5826 13.4146 19H10.5854ZM12 5C9.23858 5 7 7.23858 7 10V15C7 15.1974 6.94156 15.3904 6.83205 15.5547L5.86852 17H18.1315L17.168 15.5547C17.0584 15.3904 17 15.1974 17 15V10C17 7.23858 14.7614 5 12 5Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val optionsHorizontal = IconDefinition(
            "optionsHorizontal",
            svg = """
                <path d="M12 14C13.1046 14 14 13.1046 14 12C14 10.8954 13.1046 10 12 10C10.8954 10 10 10.8954 10 12C10 13.1046 10.8954 14 12 14Z" fill="currentColor"/>
                <path d="M6 14C7.10457 14 8 13.1046 8 12C8 10.8954 7.10457 10 6 10C4.89543 10 4 10.8954 4 12C4 13.1046 4.89543 14 6 14Z" fill="currentColor"/>
                <path d="M18 14C19.1046 14 20 13.1046 20 12C20 10.8954 19.1046 10 18 10C16.8954 10 16 10.8954 16 12C16 13.1046 16.8954 14 18 14Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val optionsVertical = IconDefinition(
            "optionsVertical",
            svg = """
                <path d="M10 12C10 13.1046 10.8954 14 12 14C13.1046 14 14 13.1046 14 12C14 10.8954 13.1046 10 12 10C10.8954 10 10 10.8954 10 12Z" fill="currentColor"/>
                <path d="M10 6C10 7.10457 10.8954 8 12 8C13.1046 8 14 7.10457 14 6C14 4.89543 13.1046 4 12 4C10.8954 4 10 4.89543 10 6Z" fill="currentColor"/>
                <path d="M10 18C10 19.1046 10.8954 20 12 20C13.1046 20 14 19.1046 14 18C14 16.8954 13.1046 16 12 16C10.8954 16 10 16.8954 10 18Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val pause = IconDefinition(
            "pause",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M9 6C9.55228 6 10 6.44772 10 7V17C10 17.5523 9.55228 18 9 18C8.44772 18 8 17.5523 8 17V7C8 6.44772 8.44772 6 9 6ZM15 6C15.5523 6 16 6.44772 16 7V17C16 17.5523 15.5523 18 15 18C14.4477 18 14 17.5523 14 17V7C14 6.44772 14.4477 6 15 6Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val percentage = IconDefinition(
            "percentage",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M18.7071 5.29289C19.0976 5.68342 19.0976 6.31658 18.7071 6.70711L6.70711 18.7071C6.31658 19.0976 5.68342 19.0976 5.29289 18.7071C4.90237 18.3166 4.90237 17.6834 5.29289 17.2929L17.2929 5.29289C17.6834 4.90237 18.3166 4.90237 18.7071 5.29289Z" fill="currentColor"/>
                <path d="M17 19C18.1046 19 19 18.1046 19 17C19 15.8954 18.1046 15 17 15C15.8954 15 15 15.8954 15 17C15 18.1046 15.8954 19 17 19Z" fill="currentColor"/>
                <path d="M7 9C8.10457 9 9 8.10457 9 7C9 5.89543 8.10457 5 7 5C5.89543 5 5 5.89543 5 7C5 8.10457 5.89543 9 7 9Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val pin = IconDefinition(
            "pin",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M6 10.5994V4C5.44772 4 5 3.55228 5 3C5 2.44772 5.44772 2 6 2H18C18.5523 2 19 2.44772 19 3C19 3.55228 18.5523 4 18 4V10.5994C18.9318 11.6201 19.4321 12.6341 19.6987 13.4338C19.8447 13.8719 19.9199 14.2435 19.9587 14.5148C19.9781 14.6506 19.9884 14.7615 19.9939 14.8436C19.9966 14.8847 19.9981 14.9186 19.999 14.9448L19.9996 14.9682L19.9998 14.9783L20 14.9907L20 14.9957L20 14.9979L20 14.999C20 14.999 19.9954 14.8677 20 15C20 15.5523 19.5523 16 19 16H13V21C13 21.5523 12.5523 22 12 22C11.4477 22 11 21.5523 11 21V16H5C4.44772 16 4 15.5523 4 15C4 14.5 4 14.999 4 14.999L4 14.9979L4.00001 14.9957L4.00004 14.9907L4.00019 14.9783C4.00033 14.9691 4.00059 14.9579 4.00101 14.9448C4.00185 14.9186 4.00338 14.8847 4.00612 14.8436C4.01159 14.7615 4.02191 14.6506 4.0413 14.5148C4.08006 14.2435 4.15528 13.8719 4.30132 13.4338C4.56788 12.6341 5.06824 11.6201 6 10.5994ZM16 4H8V11C8 11.2652 7.89464 11.5196 7.70711 11.7071C6.86009 12.5541 6.43577 13.3854 6.22128 14H17.7787C17.5642 13.3854 17.1399 12.5541 16.2929 11.7071C16.1054 11.5196 16 11.2652 16 11V4Z" fill="#0D0D0D"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val play = IconDefinition(
            "play",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M6 6.74105C6 5.19747 7.67443 4.23573 9.00774 5.01349L18.0231 10.2725C19.3461 11.0442 19.3461 12.9558 18.0231 13.7276L9.00774 18.9865C7.67443 19.7643 6 18.8026 6 17.259V6.74105ZM17.0154 12L8 6.74105V17.259L17.0154 12Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val refresh = IconDefinition(
            "refresh",
            svg = """
                <path d="M12.7929 2.29289C13.1834 1.90237 13.8166 1.90237 14.2071 2.29289L17.2071 5.29289C17.5976 5.68342 17.5976 6.31658 17.2071 6.70711L14.2071 9.70711C13.8166 10.0976 13.1834 10.0976 12.7929 9.70711C12.4024 9.31658 12.4024 8.68342 12.7929 8.29289L14.0858 7H12.5C8.95228 7 6 9.95228 6 13.5C6 17.0477 8.95228 20 12.5 20C16.0477 20 19 17.0477 19 13.5C19 12.9477 19.4477 12.5 20 12.5C20.5523 12.5 21 12.9477 21 13.5C21 18.1523 17.1523 22 12.5 22C7.84772 22 4 18.1523 4 13.5C4 8.84772 7.84772 5 12.5 5H14.0858L12.7929 3.70711C12.4024 3.31658 12.4024 2.68342 12.7929 2.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val remove = IconDefinition(
            "remove",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 12C4 11.4477 4.44772 11 5 11H19C19.5523 11 20 11.4477 20 12C20 12.5523 19.5523 13 19 13H5C4.44772 13 4 12.5523 4 12Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val search = IconDefinition(
            "search",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M10 4C6.68629 4 4 6.68629 4 10C4 13.3137 6.68629 16 10 16C13.3137 16 16 13.3137 16 10C16 6.68629 13.3137 4 10 4ZM2 10C2 5.58172 5.58172 2 10 2C14.4183 2 18 5.58172 18 10C18 11.8487 17.3729 13.551 16.3199 14.9056L21.7071 20.2929C22.0976 20.6834 22.0976 21.3166 21.7071 21.7071C21.3166 22.0976 20.6834 22.0976 20.2929 21.7071L14.9056 16.3199C13.551 17.3729 11.8487 18 10 18C5.58172 18 2 14.4183 2 10Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val select = IconDefinition(
            "select",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C12.2652 4 12.5196 4.10536 12.7071 4.29289L16.7071 8.29289C17.0976 8.68342 17.0976 9.31658 16.7071 9.70711C16.3166 10.0976 15.6834 10.0976 15.2929 9.70711L12 6.41421L8.70711 9.70711C8.31658 10.0976 7.68342 10.0976 7.29289 9.70711C6.90237 9.31658 6.90237 8.68342 7.29289 8.29289L11.2929 4.29289C11.4804 4.10536 11.7348 4 12 4ZM7.29289 14.2929C7.68342 13.9024 8.31658 13.9024 8.70711 14.2929L12 17.5858L15.2929 14.2929C15.6834 13.9024 16.3166 13.9024 16.7071 14.2929C17.0976 14.6834 17.0976 15.3166 16.7071 15.7071L12.7071 19.7071C12.3166 20.0976 11.6834 20.0976 11.2929 19.7071L7.29289 15.7071C6.90237 15.3166 6.90237 14.6834 7.29289 14.2929Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val send = IconDefinition(
            "send",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 2C12.3788 2 12.725 2.214 12.8944 2.55279L21.8944 20.5528C22.067 20.8978 22.0256 21.3113 21.7882 21.6154C21.5508 21.9195 21.1597 22.0599 20.7831 21.9762L12 20.0244L3.21694 21.9762C2.84035 22.0599 2.44921 21.9195 2.2118 21.6154C1.97439 21.3113 1.93306 20.8978 2.10558 20.5528L11.1056 2.55279C11.275 2.214 11.6212 2 12 2ZM13 18.1978L19.166 19.568L13 7.23607V18.1978ZM11 7.23607V18.1978L4.83402 19.568L11 7.23607Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val settings = IconDefinition(
            "settings",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C11.4477 4 11 4.44772 11 5C11 6.69226 8.95399 7.53974 7.75738 6.34314C7.36686 5.95261 6.73369 5.95261 6.34317 6.34314C5.95265 6.73366 5.95265 7.36683 6.34317 7.75735C7.53982 8.954 6.69223 11 5 11C4.44772 11 4 11.4477 4 12C4 12.5523 4.44772 13 5 13C6.69236 13 7.53964 15.0461 6.34311 16.2426C5.95258 16.6332 5.95258 17.2663 6.34311 17.6569C6.73363 18.0474 7.3668 18.0474 7.75732 17.6569C8.9539 16.4603 11 17.3077 11 19C11 19.5523 11.4477 20 12 20C12.5523 20 13 19.5523 13 19C13 17.3077 15.046 16.4602 16.2427 17.6568C16.6332 18.0474 17.2664 18.0474 17.6569 17.6568C18.0474 17.2663 18.0474 16.6332 17.6569 16.2426C16.4603 15.0461 17.3077 13 19 13C19.5523 13 20 12.5523 20 12C20 11.4477 19.5523 11 19 11C17.3078 11 16.4601 8.95405 17.6568 7.75737C18.0473 7.36684 18.0473 6.73368 17.6568 6.34315C17.2663 5.95263 16.6331 5.95263 16.2426 6.34315C15.046 7.53979 13 6.69219 13 5C13 4.44772 12.5523 4 12 4ZM9.00816 4.77703C9.12224 3.2243 10.4181 2 12 2C13.5819 2 14.8778 3.2243 14.9918 4.77703C16.1704 3.75977 17.9525 3.8104 19.071 4.92894C20.1896 6.04748 20.2402 7.82955 19.2229 9.00816C20.7757 9.12221 22 10.4181 22 12C22 13.5819 20.7757 14.8778 19.223 14.9918C20.2403 16.1704 20.1896 17.9525 19.0711 19.0711C17.9525 20.1896 16.1705 20.2402 14.9918 19.2229C14.8778 20.7757 13.5819 22 12 22C10.4181 22 9.12221 20.7757 9.00816 19.2229C7.82955 20.2402 6.04745 20.1896 4.92889 19.0711C3.81034 17.9525 3.75972 16.1704 4.77702 14.9918C3.2243 14.8778 2 13.5819 2 12C2 10.4181 3.22433 9.12221 4.77709 9.00816C3.75978 7.82955 3.81041 6.04747 4.92896 4.92892C6.0475 3.81038 7.82955 3.75975 9.00816 4.77703Z" fill="currentColor"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 10C11.4696 10 10.9609 10.2107 10.5858 10.5858C10.2107 10.9609 10 11.4696 10 12C10 12.5304 10.2107 13.0391 10.5858 13.4142C10.9609 13.7893 11.4696 14 12 14C12.5304 14 13.0391 13.7893 13.4142 13.4142C13.7893 13.0391 14 12.5304 14 12C14 11.4696 13.7893 10.9609 13.4142 10.5858C13.0391 10.2107 12.5304 10 12 10ZM9.17157 9.17157C9.92172 8.42143 10.9391 8 12 8C13.0609 8 14.0783 8.42143 14.8284 9.17157C15.5786 9.92172 16 10.9391 16 12C16 13.0609 15.5786 14.0783 14.8284 14.8284C14.0783 15.5786 13.0609 16 12 16C10.9391 16 9.92172 15.5786 9.17157 14.8284C8.42143 14.0783 8 13.0609 8 12C8 10.9391 8.42143 9.92172 9.17157 9.17157Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val share = IconDefinition(
            "share",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M11.2929 2.29289C11.6834 1.90237 12.3166 1.90237 12.7071 2.29289L15.7071 5.29289C16.0976 5.68342 16.0976 6.31658 15.7071 6.70711C15.3166 7.09763 14.6834 7.09763 14.2929 6.70711L13 5.41421V15C13 15.5523 12.5523 16 12 16C11.4477 16 11 15.5523 11 15V5.41421L9.70711 6.70711C9.31658 7.09763 8.68342 7.09763 8.29289 6.70711C7.90237 6.31658 7.90237 5.68342 8.29289 5.29289L11.2929 2.29289ZM4 11C4 9.89543 4.89543 9 6 9H8C8.55228 9 9 9.44772 9 10C9 10.5523 8.55228 11 8 11H6V20H18V11H16C15.4477 11 15 10.5523 15 10C15 9.44772 15.4477 9 16 9H18C19.1046 9 20 9.89543 20 11V20C20 21.1046 19.1046 22 18 22H6C4.89543 22 4 21.1046 4 20V11Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val shoppingCartAdd = IconDefinition(
            "shoppingCartAdd",
            svg = """
                <path d="M6.00913 16.1357L4.1421 4H3C2.44772 4 2 3.55228 2 3C2 2.44772 2.44772 2 3 2H4.9847C5.0806 1.99841 5.17434 2.01066 5.26367 2.03513C5.40561 2.07382 5.53497 2.143 5.64429 2.23519C5.76213 2.33437 5.85751 2.4609 5.91981 2.60698C5.95413 2.6872 5.97826 2.77281 5.99058 2.86219L6.31948 5H14V7H6.62717L7.85794 15H17.256L18.756 10H20.844L18.9578 16.2873C18.8309 16.7103 18.4416 17 18 17H7.01578C6.92133 17.0016 6.82899 16.9898 6.74088 16.9661C6.59074 16.9259 6.45453 16.8517 6.34104 16.7522C6.2371 16.6612 6.15157 16.5485 6.0921 16.4198C6.05113 16.3313 6.02271 16.2358 6.00913 16.1357Z" fill="#0D0D0D"/>
                <path d="M10 20C10 21.1046 9.10457 22 8 22C6.89543 22 6 21.1046 6 20C6 18.8954 6.89543 18 8 18C9.10457 18 10 18.8954 10 20Z" fill="#0D0D0D"/>
                <path d="M19 20C19 21.1046 18.1046 22 17 22C15.8954 22 15 21.1046 15 20C15 18.8954 15.8954 18 17 18C18.1046 18 19 18.8954 19 20Z" fill="#0D0D0D"/>
                <path d="M19 2C19.5523 2 20 2.44772 20 3V4H21C21.5523 4 22 4.44772 22 5C22 5.55228 21.5523 6 21 6H20V7C20 7.55228 19.5523 8 19 8C18.4477 8 18 7.55228 18 7V6H17C16.4477 6 16 5.55228 16 5C16 4.44772 16.4477 4 17 4H18V3C18 2.44772 18.4477 2 19 2Z" fill="#0D0D0D"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val shoppingCart = IconDefinition(
            "shoppingCart",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4.15291 4L6.01064 15.1464C6.02538 15.2469 6.05505 15.3425 6.09733 15.4309C6.15592 15.5538 6.23834 15.6615 6.33774 15.7493C6.45342 15.8516 6.59297 15.9276 6.74698 15.9677C6.83358 15.9904 6.92416 16.0017 7.01669 16H18C18.4304 16 18.8126 15.7246 18.9487 15.3162L21.9487 6.31623C22.0503 6.01128 21.9992 5.67606 21.8112 5.41529C21.6233 5.15452 21.3214 5 21 5H6.34716L5.98881 2.84987C5.97616 2.76586 5.95306 2.68527 5.92087 2.60947C5.85636 2.45701 5.75593 2.32588 5.63168 2.22473C5.45299 2.07926 5.22503 1.99578 4.98379 2H3C2.44772 2 2 2.44772 2 3C2 3.55228 2.44772 4 3 4H4.15291ZM7.84716 14L6.68049 7H19.6126L17.2792 14H7.84716Z" fill="#0D0D0D"/>
                <path d="M10 20C10 21.1046 9.10457 22 8 22C6.89543 22 6 21.1046 6 20C6 18.8954 6.89543 18 8 18C9.10457 18 10 18.8954 10 20Z" fill="#0D0D0D"/>
                <path d="M19 20C19 21.1046 18.1046 22 17 22C15.8954 22 15 21.1046 15 20C15 18.8954 15.8954 18 17 18C18.1046 18 19 18.8954 19 20Z" fill="#0D0D0D"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val sort = IconDefinition(
            "sort",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M6.29289 4.29289C6.68342 3.90237 7.31658 3.90237 7.70711 4.29289L11.7071 8.29289C12.0976 8.68342 12.0976 9.31658 11.7071 9.70711C11.3166 10.0976 10.6834 10.0976 10.2929 9.70711L8 7.41421V19C8 19.5523 7.55228 20 7 20C6.44772 20 6 19.5523 6 19V7.41421L3.70711 9.70711C3.31658 10.0976 2.68342 10.0976 2.29289 9.70711C1.90237 9.31658 1.90237 8.68342 2.29289 8.29289L6.29289 4.29289ZM16 16.5858V5C16 4.44772 16.4477 4 17 4C17.5523 4 18 4.44772 18 5V16.5858L20.2929 14.2929C20.6834 13.9024 21.3166 13.9024 21.7071 14.2929C22.0976 14.6834 22.0976 15.3166 21.7071 15.7071L17.7071 19.7071C17.5196 19.8946 17.2652 20 17 20C16.7348 20 16.4804 19.8946 16.2929 19.7071L12.2929 15.7071C11.9024 15.3166 11.9024 14.6834 12.2929 14.2929C12.6834 13.9024 13.3166 13.9024 13.7071 14.2929L16 16.5858Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val speakers = IconDefinition(
            "speakers",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 4C4 2.89543 4.89543 2 6 2H18C19.1046 2 20 2.89543 20 4V20C20 21.1046 19.1046 22 18 22H6C4.89543 22 4 21.1046 4 20V4ZM18 4L6 4V20H18V4Z" fill="#0D0D0D"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 12C10.8954 12 10 12.8954 10 14C10 15.1046 10.8954 16 12 16C13.1046 16 14 15.1046 14 14C14 12.8954 13.1046 12 12 12ZM8 14C8 11.7909 9.79086 10 12 10C14.2091 10 16 11.7909 16 14C16 16.2091 14.2091 18 12 18C9.79086 18 8 16.2091 8 14Z" fill="#0D0D0D"/>
                <path d="M13.5 7.5C13.5 8.32843 12.8284 9 12 9C11.1716 9 10.5 8.32843 10.5 7.5C10.5 6.67157 11.1716 6 12 6C12.8284 6 13.5 6.67157 13.5 7.5Z" fill="#0D0D0D"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val stop = IconDefinition(
            "stop",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M5 7C5 5.89543 5.89543 5 7 5H17C18.1046 5 19 5.89543 19 7V17C19 18.1046 18.1046 19 17 19H7C5.89543 19 5 18.1046 5 17V7ZM17 7L7 7V17H17V7Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val sun = IconDefinition(
            "sun",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 2C12.5523 2 13 2.44772 13 3V4C13 4.55228 12.5523 5 12 5C11.4477 5 11 4.55228 11 4V3C11 2.44772 11.4477 2 12 2ZM19.0711 4.92893C19.4616 5.31945 19.4616 5.95261 19.0711 6.34314L18.364 7.05025C17.9735 7.44077 17.3403 7.44077 16.9498 7.05025C16.5593 6.65972 16.5593 6.02656 16.9498 5.63603L17.6569 4.92893C18.0474 4.5384 18.6806 4.5384 19.0711 4.92893ZM4.92893 4.92893C5.31945 4.5384 5.95262 4.5384 6.34314 4.92893L7.05025 5.63603C7.44077 6.02656 7.44077 6.65972 7.05025 7.05025C6.65972 7.44077 6.02656 7.44077 5.63603 7.05025L4.92893 6.34314C4.5384 5.95262 4.5384 5.31945 4.92893 4.92893ZM12 8C9.79086 8 8 9.79086 8 12C8 14.2091 9.79086 16 12 16C14.2091 16 16 14.2091 16 12C16 9.79086 14.2091 8 12 8ZM6 12C6 8.68629 8.68629 6 12 6C15.3137 6 18 8.68629 18 12C18 15.3137 15.3137 18 12 18C8.68629 18 6 15.3137 6 12ZM2 12C2 11.4477 2.44772 11 3 11H4C4.55228 11 5 11.4477 5 12C5 12.5523 4.55228 13 4 13H3C2.44772 13 2 12.5523 2 12ZM19 12C19 11.4477 19.4477 11 20 11H21C21.5523 11 22 11.4477 22 12C22 12.5523 21.5523 13 21 13H20C19.4477 13 19 12.5523 19 12ZM5.63603 16.9497C6.02656 16.5592 6.65972 16.5592 7.05025 16.9497C7.44077 17.3403 7.44077 17.9734 7.05025 18.364L6.34314 19.0711C5.95262 19.4616 5.31945 19.4616 4.92893 19.0711C4.5384 18.6805 4.5384 18.0474 4.92893 17.6568L5.63603 16.9497ZM16.9498 18.364C16.5593 17.9734 16.5593 17.3403 16.9498 16.9497C17.3403 16.5592 17.9735 16.5592 18.364 16.9497L19.0711 17.6568C19.4616 18.0474 19.4616 18.6805 19.0711 19.0711C18.6806 19.4616 18.0474 19.4616 17.6569 19.0711L16.9498 18.364ZM12 19C12.5523 19 13 19.4477 13 20V21C13 21.5523 12.5523 22 12 22C11.4477 22 11 21.5523 11 21V20C11 19.4477 11.4477 19 12 19Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val switch = IconDefinition(
            "switch",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M14.2929 2.29289C14.6834 1.90237 15.3166 1.90237 15.7071 2.29289L19.7071 6.29289C20.0976 6.68342 20.0976 7.31658 19.7071 7.70711L15.7071 11.7071C15.3166 12.0976 14.6834 12.0976 14.2929 11.7071C13.9024 11.3166 13.9024 10.6834 14.2929 10.2929L16.5858 8L5 8C4.44772 8 4 7.55228 4 7C4 6.44771 4.44772 6 5 6L16.5858 6L14.2929 3.70711C13.9024 3.31658 13.9024 2.68342 14.2929 2.29289ZM9.70711 12.2929C10.0976 12.6834 10.0976 13.3166 9.70711 13.7071L7.41421 16H19C19.5523 16 20 16.4477 20 17C20 17.5523 19.5523 18 19 18H7.41421L9.70711 20.2929C10.0976 20.6834 10.0976 21.3166 9.70711 21.7071C9.31658 22.0976 8.68342 22.0976 8.29289 21.7071L4.29289 17.7071C4.10536 17.5196 4 17.2652 4 17C4 16.7348 4.10536 16.4804 4.29289 16.2929L8.29289 12.2929C8.68342 11.9024 9.31658 11.9024 9.70711 12.2929Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val table = IconDefinition(
            "table",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 5.5C2 4.39543 2.89543 3.5 4 3.5H20C21.1046 3.5 22 4.39543 22 5.5V18.5C22 19.6046 21.1046 20.5 20 20.5H4C2.89543 20.5 2 19.6046 2 18.5V5.5ZM11 5.5H4V8.5H11V5.5ZM13 5.5V8.5H20V5.5H13ZM20 10.5H13V13.5H20V10.5ZM20 15.5H13V18.5H20V15.5ZM11 18.5V15.5H4V18.5H11ZM4 13.5H11V10.5H4V13.5Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val tablet = IconDefinition(
            "tablet",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M4 4C4 2.89543 4.89543 2 6 2H18C19.1046 2 20 2.89543 20 4V20C20 21.1046 19.1046 22 18 22H6C4.89543 22 4 21.1046 4 20V4ZM18 4L6 4V20H18V4Z" fill="currentColor"/>
                <path d="M13 18C13 18.5523 12.5523 19 12 19C11.4477 19 11 18.5523 11 18C11 17.4477 11.4477 17 12 17C12.5523 17 13 17.4477 13 18Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val tag = IconDefinition(
            "tag",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2 3C2 2.44772 2.44772 2 3 2H11C11.2652 2 11.5196 2.10536 11.7071 2.29289L21.7071 12.2929C22.0976 12.6834 22.0976 13.3166 21.7071 13.7071L13.7071 21.7071C13.3166 22.0976 12.6834 22.0976 12.2929 21.7071L2.29289 11.7071C2.10536 11.5196 2 11.2652 2 11V3ZM4 4V10.5858L13 19.5858L19.5858 13L10.5858 4H4Z" fill="#0D0D0D"/>
                <path d="M9 7.5C9 8.32843 8.32843 9 7.5 9C6.67157 9 6 8.32843 6 7.5C6 6.67157 6.67157 6 7.5 6C8.32843 6 9 6.67157 9 7.5Z" fill="#0D0D0D"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val undo = IconDefinition(
            "undo",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12.2071 2.29289C12.5976 2.68342 12.5976 3.31658 12.2071 3.70711L10.9142 5H12.5C17.1523 5 21 8.84772 21 13.5C21 18.1523 17.1523 22 12.5 22C7.84772 22 4 18.1523 4 13.5C4 12.9477 4.44772 12.5 5 12.5C5.55228 12.5 6 12.9477 6 13.5C6 17.0477 8.95228 20 12.5 20C16.0477 20 19 17.0477 19 13.5C19 9.95228 16.0477 7 12.5 7H10.9142L12.2071 8.29289C12.5976 8.68342 12.5976 9.31658 12.2071 9.70711C11.8166 10.0976 11.1834 10.0976 10.7929 9.70711L7.79289 6.70711C7.40237 6.31658 7.40237 5.68342 7.79289 5.29289L10.7929 2.29289C11.1834 1.90237 11.8166 1.90237 12.2071 2.29289Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val unlock = IconDefinition(
            "unlock",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C10.3523 4 9 5.35228 9 7V10H18C19.1046 10 20 10.8954 20 12V20C20 21.1046 19.1046 22 18 22H6C4.89543 22 4 21.1046 4 20V12C4 10.8954 4.89543 10 6 10H7V7C7 4.24772 9.24771 2 12 2C14.7523 2 17 4.24772 17 7C17 7.55228 16.5523 8 16 8C15.4477 8 15 7.55228 15 7C15 5.35228 13.6477 4 12 4ZM6 12V20H18V12H6Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val userAdd = IconDefinition(
            "userAdd",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M10 4C7.79086 4 6 5.79086 6 8C6 10.2091 7.79086 12 10 12C12.2091 12 14 10.2091 14 8C14 5.79086 12.2091 4 10 4ZM4 8C4 4.68629 6.68629 2 10 2C13.3137 2 16 4.68629 16 8C16 11.3137 13.3137 14 10 14C6.68629 14 4 11.3137 4 8ZM19 11C19.5523 11 20 11.4477 20 12V13H21C21.5523 13 22 13.4477 22 14C22 14.5523 21.5523 15 21 15H20V16C20 16.5523 19.5523 17 19 17C18.4477 17 18 16.5523 18 16V15H17C16.4477 15 16 14.5523 16 14C16 13.4477 16.4477 13 17 13H18V12C18 11.4477 18.4477 11 19 11ZM6.5 18C5.24054 18 4 19.2135 4 21C4 21.5523 3.55228 22 3 22C2.44772 22 2 21.5523 2 21C2 18.3682 3.89347 16 6.5 16H13.5C16.1065 16 18 18.3682 18 21C18 21.5523 17.5523 22 17 22C16.4477 22 16 21.5523 16 21C16 19.2135 14.7595 18 13.5 18H6.5Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val userCheck = IconDefinition(
            "userCheck",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M10 4C7.79086 4 6 5.79086 6 8C6 10.2091 7.79086 12 10 12C12.2091 12 14 10.2091 14 8C14 5.79086 12.2091 4 10 4ZM4 8C4 4.68629 6.68629 2 10 2C13.3137 2 16 4.68629 16 8C16 11.3137 13.3137 14 10 14C6.68629 14 4 11.3137 4 8ZM21.6644 11.2526C22.0771 11.6195 22.1143 12.2516 21.7474 12.6644L19.0807 15.6644C18.891 15.8779 18.619 16 18.3333 16C18.0477 16 17.7757 15.8779 17.5859 15.6644L16.2526 14.1644C15.8857 13.7516 15.9229 13.1195 16.3356 12.7526C16.7484 12.3857 17.3805 12.4229 17.7474 12.8356L18.3333 13.4948L20.2526 11.3356C20.6195 10.9229 21.2516 10.8857 21.6644 11.2526ZM6.5 18C5.24054 18 4 19.2135 4 21C4 21.5523 3.55228 22 3 22C2.44772 22 2 21.5523 2 21C2 18.3682 3.89347 16 6.5 16H13.5C16.1065 16 18 18.3682 18 21C18 21.5523 17.5523 22 17 22C16.4477 22 16 21.5523 16 21C16 19.2135 14.7595 18 13.5 18H6.5Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val userRemove = IconDefinition(
            "userRemove",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M10 4C7.79086 4 6 5.79086 6 8C6 10.2091 7.79086 12 10 12C12.2091 12 14 10.2091 14 8C14 5.79086 12.2091 4 10 4ZM4 8C4 4.68629 6.68629 2 10 2C13.3137 2 16 4.68629 16 8C16 11.3137 13.3137 14 10 14C6.68629 14 4 11.3137 4 8ZM16 14C16 13.4477 16.4477 13 17 13H21C21.5523 13 22 13.4477 22 14C22 14.5523 21.5523 15 21 15H17C16.4477 15 16 14.5523 16 14ZM6.5 18C5.24054 18 4 19.2135 4 21C4 21.5523 3.55228 22 3 22C2.44772 22 2 21.5523 2 21C2 18.3682 3.89347 16 6.5 16H13.5C16.1065 16 18 18.3682 18 21C18 21.5523 17.5523 22 17 22C16.4477 22 16 21.5523 16 21C16 19.2135 14.7595 18 13.5 18H6.5Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val user = IconDefinition(
            "user",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 4C9.79086 4 8 5.79086 8 8C8 10.2091 9.79086 12 12 12C14.2091 12 16 10.2091 16 8C16 5.79086 14.2091 4 12 4ZM6 8C6 4.68629 8.68629 2 12 2C15.3137 2 18 4.68629 18 8C18 11.3137 15.3137 14 12 14C8.68629 14 6 11.3137 6 8ZM8 18C6.34315 18 5 19.3431 5 21C5 21.5523 4.55228 22 4 22C3.44772 22 3 21.5523 3 21C3 18.2386 5.23858 16 8 16H16C18.7614 16 21 18.2386 21 21C21 21.5523 20.5523 22 20 22C19.4477 22 19 21.5523 19 21C19 19.3431 17.6569 18 16 18H8Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val users = IconDefinition(
            "users",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M10 4C7.79086 4 6 5.79086 6 8C6 10.2091 7.79086 12 10 12C12.2091 12 14 10.2091 14 8C14 5.79086 12.2091 4 10 4ZM4 8C4 4.68629 6.68629 2 10 2C13.3137 2 16 4.68629 16 8C16 11.3137 13.3137 14 10 14C6.68629 14 4 11.3137 4 8ZM16.8284 3.75736C17.219 3.36683 17.8521 3.36683 18.2426 3.75736C20.5858 6.1005 20.5858 9.8995 18.2426 12.2426C17.8521 12.6332 17.219 12.6332 16.8284 12.2426C16.4379 11.8521 16.4379 11.219 16.8284 10.8284C18.3905 9.26633 18.3905 6.73367 16.8284 5.17157C16.4379 4.78105 16.4379 4.14788 16.8284 3.75736ZM17.5299 16.7575C17.6638 16.2217 18.2067 15.8959 18.7425 16.0299C20.0705 16.3618 20.911 17.2109 21.3944 18.1778C21.8622 19.1133 22 20.1571 22 21C22 21.5523 21.5523 22 21 22C20.4477 22 20 21.5523 20 21C20 20.3429 19.8878 19.6367 19.6056 19.0722C19.339 18.5391 18.9295 18.1382 18.2575 17.9701C17.7217 17.8362 17.3959 17.2933 17.5299 16.7575ZM6.5 18C5.24054 18 4 19.2135 4 21C4 21.5523 3.55228 22 3 22C2.44772 22 2 21.5523 2 21C2 18.3682 3.89347 16 6.5 16H13.5C16.1065 16 18 18.3682 18 21C18 21.5523 17.5523 22 17 22C16.4477 22 16 21.5523 16 21C16 19.2135 14.7595 18 13.5 18H6.5Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val volumeOff = IconDefinition(
            "volumeOff",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M13.4179 2.0915C13.7727 2.25469 14 2.60948 14 3.00001V21C14 21.3905 13.7727 21.7453 13.4179 21.9085C13.0631 22.0717 12.6457 22.0134 12.3492 21.7593L5.63008 16H3C2.44772 16 2 15.5523 2 15V9.00001C2 8.44773 2.44772 8.00001 3 8.00001H5.63008L12.3492 2.24076C12.6457 1.9866 13.0631 1.92832 13.4179 2.0915ZM12 5.17423L6.65079 9.75927C6.46955 9.91462 6.23871 10 6 10H4V14H6C6.23871 14 6.46955 14.0854 6.65079 14.2408L12 18.8258V5.17423ZM16.2929 9.29291C16.6834 8.90238 17.3166 8.90238 17.7071 9.29291L19 10.5858L20.2929 9.29291C20.6834 8.90238 21.3166 8.90238 21.7071 9.29291C22.0976 9.68343 22.0976 10.3166 21.7071 10.7071L20.4142 12L21.7071 13.2929C22.0976 13.6834 22.0976 14.3166 21.7071 14.7071C21.3166 15.0976 20.6834 15.0976 20.2929 14.7071L19 13.4142L17.7071 14.7071C17.3166 15.0976 16.6834 15.0976 16.2929 14.7071C15.9024 14.3166 15.9024 13.6834 16.2929 13.2929L17.5858 12L16.2929 10.7071C15.9024 10.3166 15.9024 9.68343 16.2929 9.29291Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val volumeUp = IconDefinition(
            "volumeUp",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M13.4179 2.0915C13.7727 2.25469 14 2.60948 14 3.00001V21C14 21.3905 13.7727 21.7453 13.4179 21.9085C13.0631 22.0717 12.6457 22.0134 12.3492 21.7593L5.63008 16H3C2.44772 16 2 15.5523 2 15V9.00001C2 8.44773 2.44772 8.00001 3 8.00001H5.63008L12.3492 2.24076C12.6457 1.9866 13.0631 1.92832 13.4179 2.0915ZM12 5.17423L6.65079 9.75927C6.46955 9.91462 6.23871 10 6 10H4V14H6C6.23871 14 6.46955 14.0854 6.65079 14.2408L12 18.8258V5.17423ZM17.2929 5.29291C17.6834 4.90238 18.3166 4.90238 18.7071 5.29291L18.7078 5.29362L18.7086 5.29438L18.7102 5.29605L18.7141 5.29996L18.724 5.31008C18.7315 5.31784 18.7409 5.32765 18.752 5.33951C18.7742 5.36322 18.8034 5.39513 18.8385 5.43526C18.9087 5.51549 19.0028 5.62871 19.1125 5.77501C19.3318 6.06748 19.6147 6.49329 19.8944 7.0528C20.4556 8.17509 21 9.82725 21 12C21 14.1728 20.4556 15.8249 19.8944 16.9472C19.6147 17.5067 19.3318 17.9326 19.1125 18.225C19.0028 18.3713 18.9087 18.4845 18.8385 18.5648C18.8034 18.6049 18.7742 18.6368 18.752 18.6605C18.7409 18.6724 18.7315 18.6822 18.724 18.69L18.7141 18.7001L18.7102 18.704L18.7086 18.7056L18.7078 18.7064L18.7071 18.7071C18.3166 19.0976 17.6834 19.0976 17.2929 18.7071C16.904 18.3183 16.9024 17.6889 17.2879 17.2979L17.2929 17.2926C17.3 17.2851 17.3138 17.2701 17.3334 17.2478C17.3725 17.203 17.4347 17.1287 17.5125 17.025C17.6682 16.8175 17.8853 16.4933 18.1056 16.0528C18.5444 15.1751 19 13.8272 19 12C19 10.1728 18.5444 8.82494 18.1056 7.94723C17.8853 7.50674 17.6682 7.18255 17.5125 6.97501C17.4347 6.87131 17.3725 6.79704 17.3334 6.75227C17.3138 6.72989 17.3 6.71493 17.2929 6.70739L17.2879 6.70208C16.9024 6.31117 16.904 5.68176 17.2929 5.29291ZM15.2929 8.29291C15.6834 7.90238 16.3166 7.90238 16.7071 8.29291L16.7085 8.29434L16.7101 8.29587L16.7134 8.29923L16.7211 8.30715L16.7408 8.32779C16.7557 8.34369 16.7741 8.36388 16.7955 8.38838C16.8384 8.43736 16.8934 8.50371 16.9563 8.58751C17.0818 8.75498 17.2397 8.99329 17.3944 9.3028C17.7056 9.92509 18 10.8272 18 12C18 13.1728 17.7056 14.0749 17.3944 14.6972C17.2397 15.0067 17.0818 15.2451 16.9563 15.4125C16.8934 15.4963 16.8384 15.5627 16.7955 15.6116C16.7741 15.6361 16.7557 15.6563 16.7408 15.6722L16.7211 15.6929L16.7134 15.7008L16.7101 15.7042L16.7085 15.7057L16.7078 15.7064C16.3173 16.0969 15.6834 16.0976 15.2929 15.7071C14.9057 15.32 14.9024 14.6943 15.2829 14.303C15.2843 14.3015 15.2868 14.2987 15.2904 14.2946C15.3022 14.2811 15.3253 14.2537 15.3562 14.2125C15.4182 14.13 15.5103 13.9933 15.6056 13.8028C15.7944 13.4251 16 12.8272 16 12C16 11.1728 15.7944 10.5749 15.6056 10.1972C15.5103 10.0067 15.4182 9.87005 15.3562 9.78751C15.3253 9.74631 15.3022 9.71892 15.2904 9.70539C15.2868 9.70132 15.2843 9.69852 15.2829 9.69701C14.9024 9.30574 14.9057 8.68008 15.2929 8.29291Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val warning = IconDefinition(
            "warning",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12 14C11.4477 14 11 13.5523 11 13V10C11 9.44772 11.4477 9 12 9C12.5523 9 13 9.44772 13 10V13C13 13.5523 12.5523 14 12 14Z" fill="currentColor"/>
                <path d="M10.5 16.5C10.5 15.6716 11.1716 15 12 15C12.8284 15 13.5 15.6716 13.5 16.5C13.5 17.3284 12.8284 18 12 18C11.1716 18 10.5 17.3284 10.5 16.5Z" fill="currentColor"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M10.2301 3.2156C10.98 1.79093 13.02 1.79092 13.7698 3.2156L22.1135 19.0685C22.8144 20.4003 21.8486 22 20.3436 22H3.65635C2.15133 22 1.18556 20.4003 1.88651 19.0685L10.2301 3.2156ZM20.3436 20L12 4.1471L3.65635 20L20.3436 20Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val zoomIn = IconDefinition(
            "zoomIn",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M10 4C6.68629 4 4 6.68629 4 10C4 13.3137 6.68629 16 10 16C13.3137 16 16 13.3137 16 10C16 6.68629 13.3137 4 10 4ZM2 10C2 5.58172 5.58172 2 10 2C14.4183 2 18 5.58172 18 10C18 11.8487 17.3729 13.551 16.3199 14.9056L21.7071 20.2929C22.0976 20.6834 22.0976 21.3166 21.7071 21.7071C21.3166 22.0976 20.6834 22.0976 20.2929 21.7071L14.9056 16.3199C13.551 17.3729 11.8487 18 10 18C5.58172 18 2 14.4183 2 10ZM10 7C10.5523 7 11 7.44772 11 8V9H12C12.5523 9 13 9.44772 13 10C13 10.5523 12.5523 11 12 11H11V12C11 12.5523 10.5523 13 10 13C9.44772 13 9 12.5523 9 12V11H8C7.44772 11 7 10.5523 7 10C7 9.44772 7.44772 9 8 9H9V8C9 7.44772 9.44772 7 10 7Z" fill="currentColor"/>
            """.trimIndent()
        )

        // source: [MONO Icons](https://icons.mono.company/) license: ``icons/LICENSE.md``
        override val zoomOut = IconDefinition(
            "zoomOut",
            svg = """
                <path fill-rule="evenodd" clip-rule="evenodd" d="M10 4C6.68629 4 4 6.68629 4 10C4 13.3137 6.68629 16 10 16C13.3137 16 16 13.3137 16 10C16 6.68629 13.3137 4 10 4ZM2 10C2 5.58172 5.58172 2 10 2C14.4183 2 18 5.58172 18 10C18 11.8487 17.3729 13.551 16.3199 14.9056L21.7071 20.2929C22.0976 20.6834 22.0976 21.3166 21.7071 21.7071C21.3166 22.0976 20.6834 22.0976 20.2929 21.7071L14.9056 16.3199C13.551 17.3729 11.8487 18 10 18C5.58172 18 2 14.4183 2 10ZM7 10C7 9.44772 7.44772 9 8 9H12C12.5523 9 13 9.44772 13 10C13 10.5523 12.5523 11 12 11H8C7.44772 11 7 10.5523 7 10Z" fill="currentColor"/>
            """.trimIndent()
        )

        override val fritz2 = IconDefinition(
            "fritz2",
            "0 0 152 152",
            svg = """
                <path fill="currentColor" d="M 75.63569,1.4806133e-5 A 75.731422,74.018906 0 0 0 7.5696619e-8,74.019268 75.731422,74.018906 0 0 0 75.731808,148.038 75.731422,74.018906 0 0 0 151.4631,74.019268 75.731422,74.018906 0 0 0 75.731808,1.4806133e-5 a 75.731422,74.018906 0 0 0 -0.09612,0 z M 111.16014,18.428347 c 3.52601,0.01826 6.37281,2.879733 6.35465,6.387206 l -0.13126,25.416536 c -0.0181,3.50747 -2.89477,6.338845 -6.4208,6.320544 -3.52602,-0.01826 -6.37285,-2.87922 -6.35465,-6.38669 l -12.775445,-0.06615 c -3.52603,-0.01826 -6.402596,2.813592 -6.420797,6.321062 l -0.03204,6.221842 h 5.896279 a 6.3606494,6.4906672 0 0 1 0.446484,-0.01601 6.3606494,6.4906672 0 0 1 6.360853,6.490557 6.3606494,6.4906672 0 0 1 -6.360853,6.490563 6.3606494,6.4906672 0 0 1 -0.516763,-0.0217 H 53.850976 v -0.02119 a 6.3606494,6.4732442 0 0 1 -0.301792,0.0078 6.3606494,6.4732442 0 0 1 -6.360853,-6.473505 6.3606494,6.4732442 0 0 1 6.360853,-6.472991 6.3606494,6.4732442 0 0 1 0.454237,0.01654 h 5.82445 l 0.06615,-12.708268 c 0.07267,-14.03637 11.57207,-25.357293 25.68267,-25.284244 l 19.163169,0.09922 c 0.0182,-3.507473 2.89426,-6.339322 6.42028,-6.32106 z m -51.430972,60.447988 25.551413,0.132292 -0.0987,19.062403 c -0.07268,14.03613 -11.57207,25.35724 -25.68267,25.28424 l -12.775445,-0.0661 c -0.01815,3.50747 -2.894788,6.33936 -6.420798,6.32106 -3.52603,-0.0183 -6.372852,-2.87974 -6.354651,-6.38721 L 34.08009,97.806447 c 0.01815,-3.50747 2.894258,-6.339361 6.420278,-6.321063 3.52601,0.01826 6.373368,2.879728 6.355168,6.387209 l 6.387724,0.03307 c 3.52602,0.01826 6.40208,-2.813582 6.420281,-6.321063 z"                  />
               """.trimIndent()
        )
    }

    override val input = object : InputFieldStyles {
        override val sizes = object : InputFieldSizes {
            override val small: Style<BasicParams> = {
                height { "2rem" }
                minWidth { "2.5rem" }
                fontSize { small }
                paddings {
                    horizontal { tiny }
                }
            }
            override val normal: Style<BasicParams> = {
                height { "2.5rem" }
                minWidth { "2.5rem" }
                fontSize { normal }
                paddings {
                    horizontal { small }
                }
            }

            override val large: Style<BasicParams> = {
                height { "3rem" }
                minWidth { "2.5rem" }
                fontSize { large }
                paddings {
                    horizontal { small }
                }
            }
        }

        override val variants = object : InputFieldVariants {
            override val outline: Style<BasicParams> = {
                // just leave the *foundation* CSS values untouched!
                // But we need a *name* for this variant, so we got to have this val!
            }

            override val filled: Style<BasicParams> = {
                background {
                    color { light }
                }

                hover {
                    css("filter: brightness(90%);")
                }

                focus {
                    zIndex { "1" }
                    background {
                        color { "transparent" }
                    }
                }
            }
        }
    }


    override val checkbox = object : CheckboxStyles {
        override val sizes = object : CheckboxSizes {
            private val basic: Style<BasicParams> = {

                display { inlineFlex }
                css("align-items: center;")


            }
            override val small: Style<BasicParams> = {
                basic()
                css("--cb-size: .75rem")
                css("--cb-svg-size: .50rem")
                css("--cb-radius:  ${radii.smaller}")
                fontSize { small }
                lineHeight { small }
                margins { right { tiny } }
            }
            override val normal: Style<BasicParams> = {
                basic()
                css("--cb-size: 1.0rem")
                css("--cb-svg-size: .75rem")
                css("--cb-radius:  ${radii.small}")
                fontSize { normal }
                lineHeight { normal }
                margins { right { smaller } }
            }
            override val large: Style<BasicParams> = {
                basic()
                css("--cb-size: 1.5rem")
                css("--cb-svg-size: 1.25rem")
                css("--cb-radius:  ${radii.normal}")
                fontSize { larger }
                lineHeight { larger }
                margins { right { small } }
            }
        }
        override val input: Style<BasicParams> = {
            children("&:focus + div") {
                border {
                    color { focus }
                }
                boxShadow { outline }
            }
            children("&[disabled] + div") {
                background {
                    color { disabled }
                }
            }
            children("&[disabled] ~ div") {
                opacity { ".5" }
            }
            children("&:not([checked]) + div > *") {
                css("visibility:hidden;")
            }
        }
        override val icon: Style<BasicParams> = {
            width { "var(--cb-svg-size)" }
            height { "var(--cb-svg-size)" }
            lineHeight { "var(--cb-svg-size)" }
            margins {
                top { ".0625rem" }
                left { ".0625rem" }
            }
        }
        override val label: Style<BasicParams> = {
            margins { left { tiny } }
            display { block }
        }
        override val default: Style<BasicParams> = {
            display { inlineFlex }
            flex {
                shrink { "0" }
            }
            width { "var(--cb-size)" }
            height { "var(--cb-size)" }
            background { color { base } }
            border {
                width { "1px" }
                style { solid }
                color { dark }
            }
            radius { "var(--cb-radius)" }
        }
        override val checked: Style<BasicParams> = {
            background { color { info } }
            color { base }
        }
    }

    override val radio = object : RadioStyles {
        override val sizes = object : RadioSizes {
            private val basic: Style<BasicParams> = {
                display { inlineFlex }
                css("align-items: center;")
            }
            override val small: Style<BasicParams> = {
                basic()
                css("--rb-size: .75rem")
                fontSize { small }
                lineHeight { small }
                margins { right { tiny } }
            }
            override val normal: Style<BasicParams> = {
                basic()
                css("--rb-size: 1.0rem")
                fontSize { normal }
                lineHeight { normal }
                margins { right { smaller } }
            }
            override val large: Style<BasicParams> = {
                basic()
                css("--rb-size: 1.5rem")
                fontSize { larger }
                lineHeight { larger }
                margins { right { small } }
            }
        }
        override val input: Style<BasicParams> = {
            children("&:focus + div") {
                border {
                    color { focus }
                }
                boxShadow { outline }
            }
            children("&[disabled] + div") {
                background {
                    color { disabled }
                }
            }
            children("&[disabled] ~ div") {
                opacity { ".5" }
            }
        }
        override val label: Style<BasicParams> = {
            margins { left { tiny } }
            display { block }
        }
        override val default: Style<BasicParams> = {
            display { inlineFlex }
            flex {
                shrink { "0" }
            }
            css("align-items:center;")
            css("justify-content:center;")
            width { "var(--rb-size)" }
            height { "var(--rb-size)" }
            background { color { "white" } }
            border {
                width { "2px" }
                style { solid }
                color { inherit }
            }
            radius { "9999px" }
        }
        override val selected: Style<BasicParams> = {
            background { color { info } }
            color { light }
            before {
                css("content:\"\";")
                display {
                    inlineBlock
                }
                position {
                    relative { }
                }
                width { "50%" }
                height { "50%" }
                radius { "50%" }
                background {
                    color { "currentColor" }
                }
            }
        }
    }

    override val switch = object : SwitchStyles {
        override val sizes = object : SwitchSizes {
            private val basic: Style<BasicParams> = {
                display { inlineFlex }
                css("align-items: center;")
            }
            override val small: Style<BasicParams> = {
                basic()
                css("--sw-width: 1.35rem")
                css("--sw-height: .75rem")
                fontSize { small }
                lineHeight { small }
                margins { right { tiny } }
            }
            override val normal: Style<BasicParams> = {
                basic()
                css("--sw-width: 1.85rem")
                css("--sw-height: 1rem")
                fontSize { normal }
                lineHeight { normal }
                margins { right { smaller } }
            }
            override val large: Style<BasicParams> = {
                basic()
                css("--sw-width: 2.875rem")
                css("--sw-height: 1.5rem")
                fontSize { larger }
                lineHeight { larger }
                margins { right { small } }
            }
        }
        override val input: Style<BasicParams> = {
            children("&:focus + div") {
                border {
                    color { focus }
                }
                boxShadow { outline }
            }

            children("&[checked] + div div") {
                css("transform:translateX(calc(var(--sw-width) - var(--sw-height)));")

            }

            children("&[disabled] + div") {
                background {
                    color { disabled }
                }
            }
            children("&[disabled] ~ div") {
                opacity { ".5" }
            }
        }
        override val dot: Style<BasicParams> = {
            width { "var(--sw-height)" }
            height { "var(--sw-height)" }
            radius { "9999px" }
            background {
                color { "white" }
            }
            css("transition: transform 250ms ease 0s;")


        }
        override val label: Style<BasicParams> = {
            margins { left { tiny } }
            display { block }
        }
        override val default: Style<BasicParams> = {
            display { inlineFlex }
            flex {
                shrink { "0" }
            }
            padding { "2px" }
            width { "var(--sw-width)" }
            height { "var(--sw-height)" }
            background { color { light } }
            radius { "9999px" }
            css("justify-content: flex-start;")
            css("box-sizing: content-box;")
            css("align-items: center;")
            css("transition: all 120ms ease 0s;")
        }
        override val checked: Style<BasicParams> = {
            background { color { success } }
        }
    }

    override val button = object : PushButtonStyles {
        override val variants = object : PushButtonVariants {
            private val basic: Style<BasicParams> = {
                lineHeight { smaller }
                radius { normal }
                fontWeight { semiBold }

                focus {
                    boxShadow { outline }
                }
            }

            override val solid: Style<BasicParams> = {
                basic()
                background { color { "var(--main-color)" } }
                color { base }
                hover {
                    css("filter: brightness(80%);") //132%
                }
                active {
                    css("filter: brightness(80%);")
                }
            }

            override val outline: Style<BasicParams> = {
                basic()
                color { "var(--main-color)" }
                border {
                    width { thin }
                    style { solid }
                    color { "var(--main-color)" }
                }
                hover {
                    css("background-opacity: 0.2;")
                    background {
                        color { primary_hover }
                    }

                }
            }

            override val ghost: Style<BasicParams> = {
                basic()
                color { "var(--main-color)" }
            }

            override val link: Style<BasicParams> = {
                basic()
                paddings { all { none } }
                height { auto }
                lineHeight { normal }
                color { "var(--main-color)" }
                hover {
                    textDecoration { underline }
                }
                active {
                    color { secondary }
                }
            }
        }

        override val sizes = object : PushButtonSizes {
            override val normal: Style<BasicParams> = {
                height { "2.5rem" }
                minWidth { "2.5rem" }
                fontSize { normal }
                paddings {
                    horizontal { normal }
                }
            }

            override val small: Style<BasicParams> = {
                height { "2rem" }
                minWidth { "2rem" }
                fontSize { small }
                paddings {
                    horizontal { smaller }
                }
            }

            override val large: Style<BasicParams> = {
                height { "3rem" }
                minWidth { "3rem" }
                fontSize { large }
                paddings {
                    horizontal { larger }
                }
            }

        }

    }

    override val modal = object : ModalStyles {

        override val overlay: Style<BasicParams> = {
            position {
                fixed {
                    vertical { none }
                    horizontal { none }
                }
            }
            background {
                color { rgba(0, 0, 0, 0.4) }
            }
        }

        override val sizes = object : ModalSizes {

            private val basic: Style<BasicParams> = {
                background {
                    color { "white" }
                }
                padding { normal }
                radius { tiny }
                boxShadow { raisedFurther }
            }

            override val full: Style<BasicParams> = {
                basic()
                width { "100%" }
                height { "100%" }
                position {
                    fixed {
                        horizontal { "0" }
                        vertical { "0" }
                    }
                }
            }

            override val large: Style<BasicParams> = {
                basic()
                position {
                    fixed {
                        left { "var(--main-level)" }
                        top { "var(--main-level)" }
                        //bottom {normal}
                        right { normal }
                    }
                }
                minHeight { wide.smaller }
            }

            override val normal: Style<BasicParams> = {
                basic()
                position {
                    fixed {
                        top { "var(--main-level)" }
                        left { "50%" }
                    }
                }
                minHeight { wide.smaller }
                minWidth { "50%" }
                css("transform: translateX(-50%);")
            }

            override val small: Style<BasicParams> = {
                basic()
                position {
                    fixed {
                        top { "var(--main-level)" }
                        left { "65%" }
                        bottom { normal }
                    }
                }
                minHeight { wide.smaller }
                minWidth { "35%" }
                css("transform: translateX(-90%);")
            }
        }

        override val variants = object : ModalVariants {
            override val auto: Style<BasicParams> = {
                position {
                    fixed {
                        bottom { auto }
                    }
                }
            }

            override val verticalFilled: Style<BasicParams> = {
                position {
                    fixed {
                        bottom { normal }
                    }
                }
            }

            override val centered: Style<BasicParams> = {
                position {
                    fixed {
                        top { "50%" }
                    }
                }
                // FIXME: does not work! overrides size-settings!
                css("transform: translatey(-50%);")
            }
        }
    }

    override val popover = object : PopoverStyles {
        override val size: PopoverSizes = object : PopoverSizes {
            private val basic: Style<BasicParams> = {
                background {
                    color { "white" }
                }
                paddings {
                    top { tiny }
                    bottom { tiny }
                    left { small }
                    right { small }
                }
                radius { tiny }
                boxShadow { flat }
                zIndex { "20" }

            }
            override val auto: Style<BasicParams> = {
                basic()
            }
            override val normal: Style<BasicParams> = {
                basic()
                width { "250px" }
            }
        }
        override val trigger: Style<BasicParams> = {
            display { "inline-block" }
        }
        override val header: Style<BasicParams> = {
            fontWeight { semiBold }
            borders {
                bottom {
                    width { thin }
                    style { solid }
                    color { inherit }
                }
            }
        }
        override val section: Style<BasicParams> = {
            paddings {
                top { tiny }
                bottom { tiny }
            }
        }
        override val footer: Style<BasicParams> = {
            borders {
                top {
                    width { thin }
                    style { solid }
                    color { inherit }
                }
            }
        }
        override val placement = object : PopoverPlacements {
            private val basic: Style<BasicParams> = {
                css("transition: transform .2s;")
                zIndex { "50" }
            }
            override val top: Style<BasicParams> = {
                basic()
                position {
                    absolute {
                        left { "50%" }
                        top { "-1rem" }
                    }
                }
                css("transform: translate(-50%, -100%) scale(1);")
            }
            override val right: Style<BasicParams> = {
                margins { top { "1rem" } }
                position {
                    absolute {
                        left { "calc(100% + 1rem)" }
                        top { "50%" }
                    }
                }
                css("transform: translate(0, -50%) scale(1);")
            }
            override val bottom: Style<BasicParams> = {
                position {
                    absolute {
                        left { "50%" }
                        top { "calc(100% + 1rem)" }
                    }
                }
                css("transform: translate(-50%, 0) scale(1);")
            }
            override val left: Style<BasicParams> = {
                margins { top { "1rem" } }
                position {
                    absolute {
                        left { "-1rem" }
                        top { "50%" }
                    }
                }
                css("transform: translate(-100%, -50%) scale(1);")
            }
        }
        override val arrowPlacement = object : PopoverArrowPlacements {
            private val basic: Style<BasicParams> = {
                css("transform: rotate(45deg);")
                width { "1rem" }
                height { "1rem" }
                position {
                    absolute {}
                }
                background {
                    color { inherit }
                }
                before {
                    zIndex { "-1" }
                    css("content:\"\";")
                    width { "1rem" }
                    height { "1rem" }
                    position {
                        absolute {
                        }
                    }
                }
            }
            override val top: Style<BasicParams> = {
                basic()
                css("left:calc(50% - 0.5rem);")
                position {
                    absolute { top { "-0.5rem" } }
                }
                before {
                    boxShadow {
                        "rgba(0, 0, 0, 0.1) -1px -1px 1px 0px"
                    }
                }
            }
            override val right: Style<BasicParams> = {
                basic()
                css("top: calc(50% - 1.5rem);")
                css("right: calc(-0.5rem - 0.5px);")
                position {
                    absolute {}
                }
                before {
                    boxShadow {
                        "rgba(0, 0, 0, 0.1) -1px 1px 1px 0px inset"
                    }
                }
            }
            override val bottom: Style<BasicParams> = {
                basic()
                css("left:calc(50% - 0.5rem);")
                position {
                    absolute { bottom { "-0.5rem" } }
                }
                before {
                    boxShadow {
                        "rgba(0, 0, 0, 0.1) -1px -1px 1px 0px inset"
                    }
                }
            }
            override val left: Style<BasicParams> = {
                basic()
                css("top:calc(50% - 1.5rem);")
                position {
                    absolute { left { "-0.5rem" } }
                }
                before {
                    boxShadow {
                        "rgba(0, 0, 0, 0.1) -1px 1px 1px 0px"
                    }
                }
            }
        }
        override val closeButton: Style<BasicParams> = {
            position {
                absolute {
                    right { "0.5rem" }
                    top { "0.5rem" }
                }
            }
            padding { tiny }
            width { "1rem" }
            height { "1rem" }
            minWidth { "1rem" }
            fontWeight { semiBold }
            lineHeight { "1rem" }
        }
    }

    override val tooltip = object : Tooltip {

        override fun write(vararg value: String): Style<BasicParams> {
            return write(*value) { top }
        }

        override fun write(
            vararg value: String,
            tooltipPlacement: TooltipPlacements.() -> Style<BasicParams>
        ): Style<BasicParams> {
            return {
                position {
                    relative {}
                }
                after {
                    css("content:\"${value.asList().joinToString("\\A")}\";")
                    background { color { dark } }
                    radius { small }
                    color { light }
                    display { none }
                    overflow { hidden }
                    opacity { "0" }
                    zIndex { "20" }
                    position {
                        absolute {
                            left { "50%" }
                            bottom { "100%" }
                        }
                    }
                    padding { tiny }
                    fontSize { smaller }
                    lineHeight { smaller }
                    css("text-overflow: ellipsis;")
                    css("transition: opacity .2s, transform .2s;")
                    css("white-space: pre;")
                }
                focus {
                    after {
                        display { block }
                        opacity { "1" }
                    }

                }
                hover {
                    after {
                        display { block }
                        opacity { "1" }
                    }
                }
                tooltipPlacement.invoke(placement)()
            }
        }

        override val placement = object : TooltipPlacements {
            override val top: Style<BasicParams> = {
                after {
                    css("transform: translate(-50%, 0.5rem);")
                }
                focus {
                    after {
                        css("transform: translate(-50%, -.5rem);")
                    }
                }
                hover {
                    after {
                        css("transform: translate(-50%, -.5rem);")
                    }
                }
            }
            override val right: Style<BasicParams> = {
                after {
                    position {
                        absolute {
                            bottom { "50%" }
                            left { "100%" }
                        }
                    }
                    css("transform: translate(-0.5rem, 50%);")
                }
                focus {
                    after {
                        css("transform: translate(0.5rem, 50%);")
                    }

                }
                hover {
                    after {
                        css("transform: translate(0.5rem, 50%);")
                    }
                }
            }
            override val bottom: Style<BasicParams> = {
                after {
                    position {
                        absolute {
                            bottom { auto }
                            top { "100%" }
                        }
                    }
                    css("transform: translate(-50%, -.5rem);")
                }
                focus {
                    after {
                        css("transform: translate(-50%, .5rem);")
                    }
                }
                hover {
                    after {
                        css("transform: translate(-50%, .5rem);")
                    }

                }
            }
            override val left: Style<BasicParams> = {
                after {
                    position {
                        absolute {
                            bottom { "50%" }
                            left { auto }
                            right { "100%" }
                        }
                    }
                    css("transform: translate(0.5rem, 50%);")
                }
                focus {
                    after {
                        css("transform: translate(-0.5rem, 50%);")
                    }

                }
                hover {
                    after {
                        css("transform: translate(-0.5rem, 50%);")
                    }
                }
            }
        }
    }

    override val reset: String by lazy {
        //from modern-normalize v1.0.0 | MIT License | https://github.com/sindresorhus/modern-normalize
        """
            *,::after,::before{box-sizing:border-box}:root{-moz-tab-size:4;tab-size:4}html{line-height:1.15;-webkit-text-size-adjust:100%}body{margin:0}body{font-family:system-ui,-apple-system,'Segoe UI',Roboto,Helvetica,Arial,sans-serif,'Apple Color Emoji','Segoe UI Emoji'}hr{height:0;color:inherit}abbr[title]{-webkit-text-decoration:underline dotted;text-decoration:underline dotted}b,strong{font-weight:bolder}code,kbd,pre,samp{font-family:ui-monospace,SFMono-Regular,Consolas,'Liberation Mono',Menlo,monospace;font-size:1em}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-.25em}sup{top:-.5em}table{text-indent:0;border-color:inherit}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0}button,select{text-transform:none}[type=button],[type=reset],[type=submit],button{-webkit-appearance:button}legend{padding:0}progress{vertical-align:baseline}::-webkit-inner-spin-button,::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px}::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit}summary{display:list-item}blockquote,dd,dl,figure,h1,h2,h3,h4,h5,h6,hr,p,pre{margin:0}button{background-color:transparent;background-image:none}fieldset{margin:0;padding:0}ol,ul{list-style:none;margin:0;padding:0}html{font-family:ui-sans-serif,system-ui,-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,"Noto Sans",sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji";line-height:1.5}body{font-family:inherit;line-height:inherit}*,::after,::before{box-sizing:border-box;border-width:0;border-style:solid;border-color:#e5e7eb}hr{border-top-width:1px}img{border-style:solid}textarea{resize:vertical}input::placeholder,textarea::placeholder{color:#9ca3af}[role=button],button{cursor:pointer}table{border-collapse:collapse}h1,h2,h3,h4,h5,h6{font-size:inherit;font-weight:inherit}a{color:inherit;text-decoration:inherit}button,input,optgroup,select,textarea{padding:0;line-height:inherit;color:inherit}code,kbd,pre,samp{font-family:ui-monospace,SFMono-Regular,Menlo,Monaco,Consolas,"Liberation Mono","Courier New",monospace}audio,canvas,embed,iframe,img,object,svg,video{display:block;vertical-align:middle}img,video{max-width:100%;height:auto}
        """ + """
            html {
              font-rendering: optimizeLegibilty;
              color: ${colors.dark}
            }
            body {
              margin: 0;
              line-height: ${lineHeights.large}
              font-family: Inter, sans-serif;
              font-feature-settings: "kern";
            }
            textarea {
              font-family: Inter, sans-serif;
              line-height: ${lineHeights.tiny}; /* 1 */
            }
            a {
              font-family: Inter, sans-serif;
              font-weight: 600;
              font-size: ${fontSizes.small}
            }
            p {
              font-family: Inter, sans-serif;
              font-size: ${fontSizes.normal};
              line-height: ${lineHeights.larger};
            }
            *::after {
              border-sizing: border-box;
              overflow-wrap: break-word;
            }
            h1 {
              font-family: Inter, sans-serif;
              line-height: ${lineHeights.tiny};
              font-weight: 700;
              font-size: ${fontSizes.huge};
              letter-spacing: ${letterSpacings.small};
              outline: 0;
            }
            h2 {
              font-family: Inter, sans-serif;
              line-height: ${lineHeights.small};
              font-weight: 600;
              font-size: ${fontSizes.larger};
              letter-spacing: ${letterSpacings.small};
            }
            h3 {
              font-family: Inter, sans-serif;
              line-height: smaller;
              font-weight: 600;
              font-size: ${fontSizes.large};
              letter-spacing: ${letterSpacings.small};
            }
            h4 {
              font-family: Inter, sans-serif;
              font-size: ${fontSizes.normal};
              font-weight: bold;
            }
            h5 {
              font-family: Inter, sans-serif;
              font-size: ${fontSizes.small};
              font-weight: bold;
            }
            h6 {
              font-family: Inter, sans-serif;
              font-size: ${fontSizes.smaller};
              font-weight: bold;
            }
        """.trimIndent()
    }

    override val textarea = object : TextAreaStyles {
        override val resize = object : TextAreaResize {
            override val none: Style<BasicParams> = {
                css("resize:none")
            }

            override val vertical: Style<BasicParams> = {
                css("resize:vertical")
            }

            override val horizontal: Style<BasicParams> = {
                css("resize:horizontal")
            }
        }

        override val size = object : TextAreaSize {

            override val small: Style<BasicParams> = {
                lineHeight { normal }
                height { "1rem" }
                width { "25%" }

                paddings {
                    vertical { "4px" }
                    horizontal { "0.5rem" }
                }
            }
            override val normal: Style<BasicParams> = {
                lineHeight { normal }
                height { "2rem" }
                width { "50%" }

                paddings {
                    vertical { "8px" }
                    horizontal { "0.75rem" }
                }
            }
            override val large: Style<BasicParams> = {
                lineHeight { normal }
                height { "2.5rem" }
                width { "100%" }

                paddings {
                    vertical { "8px" }
                    horizontal { "0.75rem" }
                }
            }
        }

    }

    override val select = object : SelectStyles {
        override val variant = object : SelectVariants {
            override val outline: Style<BasicParams> = {

                border {
                    width { thin }
                    style { solid }
                    color { light }
                }


            }

            override val filled: Style<BasicParams> = {
                background { color { light } }
            }

            override val flushed: Style<BasicParams> = {

                borders {
                    bottom {
                        width { thin }
                        style { solid }
                        color { light }
                    }
                }
            }

            override val unstyled: Style<BasicParams> = {

            }


        }

        override val size = object : SelectSize {
            override val small: Style<BasicParams> = {

                height { "32px" }
            }
            override val normal: Style<BasicParams> = {
                height { "40px" }
            }
            override val large: Style<BasicParams> = {
                height { "48px" }
            }

        }


    }
}


