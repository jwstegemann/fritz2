import dev.fritz2.styling.params.*
import dev.fritz2.styling.theme.*

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
        override val light = "#e2e8f0"
        override val dark = "#343a40"
        override val disabled = light
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

    override val borderWidths = Thickness(
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
        glowing = shadow("0", "0", "2px", color = rgba(0, 0, 255, 0.5)),
        outline = shadow("0", "0", "0", "3px", color = rgba(66, 153, 225, 0.6)),
        danger = shadow("0", "0", "0", "1px", color = colors.danger)
    )

    override val zIndices = ZIndices(1, 100, 2, 200, 300, 2, 400, 2)

    override val opacities = WeightedValue(
        normal = "0.5"
    )

    override val icons = object : Icons {
        override val copy = IconDefinition(
            "copy",
            svg = """
                <path fill="currentColor" d="M16 1H4c-1.1 0-2 .9-2 2v14h2V3h12V1zm3 4H8c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h11c1.1 0 2-.9 2-2V7c0-1.1-.9-2-2-2zm0 16H8V7h11v14z" />
            """.trimIndent()
        )
        override val search = IconDefinition(
            "search",
            svg = """
                <path fill="currentColor" d="M23.384,21.619,16.855,15.09a9.284,9.284,0,1,0-1.768,1.768l6.529,6.529a1.266,1.266,0,0,0,1.768,0A1.251,1.251,0,0,0,23.384,21.619ZM2.75,9.5a6.75,6.75,0,1,1,6.75,6.75A6.758,6.758,0,0,1,2.75,9.5Z">
            """.trimIndent()
        )
        override val search2 = IconDefinition(
            "search2",
            svg = """ 
                <path fill="currentColor" d="M23.414,20.591l-4.645-4.645a10.256,10.256,0,1,0-2.828,2.829l4.645,4.644a2.025,2.025,0,0,0,2.828,0A2,2,0,0,0,23.414,20.591ZM10.25,3.005A7.25,7.25,0,1,1,3,10.255,7.258,7.258,0,0,1,10.25,3.005Z">
            """.trimIndent()
        )
        override val moon = IconDefinition(
            "moon",
            svg = """ 
                <path fill="currentColor" d="M21.4,13.7C20.6,13.9,19.8,14,19,14c-5,0-9-4-9-9c0-0.8,0.1-1.6,0.3-2.4c0.1-0.3,0-0.7-0.3-1 c-0.3-0.3-0.6-0.4-1-0.3C4.3,2.7,1,7.1,1,12c0,6.1,4.9,11,11,11c4.9,0,9.3-3.3,10.6-8.1c0.1-0.3,0-0.7-0.3-1 C22.1,13.7,21.7,13.6,21.4,13.7z">
            """.trimIndent()
        )
        override val sun = IconDefinition(
            "sun",
            svg = """
                <g
                  strokeLinejoin="round"
                  strokeLinecap="round"
                  strokeWidth="2"
                  fill="none"
                  stroke="currentColor"
                >
                  <circle cx="12" cy="12" r="5" />
                  <path d="M12 1v2" />
                  <path d="M12 21v2" />
                  <path d="M4.22 4.22l1.42 1.42" />
                  <path d="M18.36 18.36l1.42 1.42" />
                  <path d="M1 12h2" />
                  <path d="M21 12h2" />
                  <path d="M4.22 19.78l1.42-1.42" />
                  <path d="M18.36 5.64l1.42-1.42" />
                </g>                
            """.trimIndent()
        )
        override val add = IconDefinition(
            "add",
            svg = """ 
                <path fill="currentColor" d="M0,12a1.5,1.5,0,0,0,1.5,1.5h8.75a.25.25,0,0,1,.25.25V22.5a1.5,1.5,0,0,0,3,0V13.75a.25.25,0,0,1,.25-.25H22.5a1.5,1.5,0,0,0,0-3H13.75a.25.25,0,0,1-.25-.25V1.5a1.5,1.5,0,0,0-3,0v8.75a.25.25,0,0,1-.25.25H1.5A1.5,1.5,0,0,0,0,12Z">
            """.trimIndent()
        )
        override val smallAdd = IconDefinition(
            "smallAdd",
            svg = """
                <path fill="currentColor" d="M14 9h-3V6c0-.55-.45-1-1-1s-1 .45-1 1v3H6c-.55 0-1 .45-1 1s.45 1 1 1h3v3c0 .55.45 1 1 1s1-.45 1-1v-3h3c.55 0 1-.45 1-1s-.45-1-1-1z" />
        """.trimIndent()
        )
        override val settings = IconDefinition(
            "settings",
            svg = """
                <path fill="currentColor" d="M14,7.77 L14,6.17 L12.06,5.53 L11.61,4.44 L12.49,2.6 L11.36,1.47 L9.55,2.38 L8.46,1.93 L7.77,0.01 L6.17,0.01 L5.54,1.95 L4.43,2.4 L2.59,1.52 L1.46,2.65 L2.37,4.46 L1.92,5.55 L0,6.23 L0,7.82 L1.94,8.46 L2.39,9.55 L1.51,11.39 L2.64,12.52 L4.45,11.61 L5.54,12.06 L6.23,13.98 L7.82,13.98 L8.45,12.04 L9.56,11.59 L11.4,12.47 L12.53,11.34 L11.61,9.53 L12.08,8.44 L14,7.75 L14,7.77 Z M7,10 C5.34,10 4,8.66 4,7 C4,5.34 5.34,4 7,4 C8.66,4 10,5.34 10,7 C10,8.66 8.66,10 7,10 Z" /> 
        """.trimIndent()
        )
        override val checkCircle = IconDefinition(
            "checkCircle",
            svg = """
                <path fill="currentColor" d="M12,0A12,12,0,1,0,24,12,12.014,12.014,0,0,0,12,0Zm6.927,8.2-6.845,9.289a1.011,1.011,0,0,1-1.43.188L5.764,13.769a1,1,0,1,1,1.25-1.562l4.076,3.261,6.227-8.451A1,1,0,1,1,18.927,8.2Z" /> 
        """.trimIndent()
        )
        override val lock = IconDefinition(
            "lock",
            svg = """ 
                <path fill="currentColor" d="M19.5,9.5h-.75V6.75a6.75,6.75,0,0,0-13.5,0V9.5H4.5a2,2,0,0,0-2,2V22a2,2,0,0,0,2,2h15a2,2,0,0,0,2-2V11.5A2,2,0,0,0,19.5,9.5Zm-9.5,6a2,2,0,1,1,3,1.723V19.5a1,1,0,0,1-2,0V17.223A1.994,1.994,0,0,1,10,15.5ZM7.75,6.75a4.25,4.25,0,0,1,8.5,0V9a.5.5,0,0,1-.5.5H8.25a.5.5,0,0,1-.5-.5Z" /> 
        """.trimIndent()
        )
        override val unlock = IconDefinition(
            "unlock",
            svg = """
                <path fill="currentColor" d="M19.5,9.5h-.75V6.75A6.751,6.751,0,0,0,5.533,4.811a1.25,1.25,0,1,0,2.395.717A4.251,4.251,0,0,1,16.25,6.75V9a.5.5,0,0,1-.5.5H4.5a2,2,0,0,0-2,2V22a2,2,0,0,0,2,2h15a2,2,0,0,0,2-2V11.5A2,2,0,0,0,19.5,9.5Zm-9.5,6a2,2,0,1,1,3,1.723V19.5a1,1,0,0,1-2,0V17.223A1.994,1.994,0,0,1,10,15.5Z" />
        """.trimIndent()
        )
        override val view = IconDefinition(
            "view",
            svg = """
                <g fill="currentColor">
                  <path d="M23.432,10.524C20.787,7.614,16.4,4.538,12,4.6,7.6,4.537,3.213,7.615.568,10.524a2.211,2.211,0,0,0,0,2.948C3.182,16.351,7.507,19.4,11.839,19.4h.308c4.347,0,8.671-3.049,11.288-5.929A2.21,2.21,0,0,0,23.432,10.524ZM7.4,12A4.6,4.6,0,1,1,12,16.6,4.6,4.6,0,0,1,7.4,12Z" />
                  <circle cx="12" cy="12" r="2" />
                </g>
        """.trimIndent()
        )
        override val viewOff = IconDefinition(
            "viewOff",
            svg = """
                <g fill="currentColor">
                    <path d="M23.2,10.549a20.954,20.954,0,0,0-4.3-3.6l4-3.995a1,1,0,1,0-1.414-1.414l-.018.018a.737.737,0,0,1-.173.291l-19.5,19.5c-.008.007-.018.009-.026.017a1,1,0,0,0,1.631,1.088l4.146-4.146a11.26,11.26,0,0,0,4.31.939h.3c4.256,0,8.489-2.984,11.051-5.8A2.171,2.171,0,0,0,23.2,10.549ZM16.313,13.27a4.581,4.581,0,0,1-3,3.028,4.3,4.3,0,0,1-3.1-.19.253.253,0,0,1-.068-.407l5.56-5.559a.252.252,0,0,1,.407.067A4.3,4.3,0,0,1,16.313,13.27Z" />
                    <path d="M7.615,13.4a.244.244,0,0,0,.061-.24A4.315,4.315,0,0,1,7.5,12,4.5,4.5,0,0,1,12,7.5a4.276,4.276,0,0,1,1.16.173.244.244,0,0,0,.24-.062l1.941-1.942a.254.254,0,0,0-.1-.421A10.413,10.413,0,0,0,12,4.75C7.7,4.692,3.4,7.7.813,10.549a2.15,2.15,0,0,0-.007,2.9,21.209,21.209,0,0,0,3.438,3.03.256.256,0,0,0,.326-.029Z" />
                </g>
        """.trimIndent()
        )
        override val download = IconDefinition(
            "download",
            "0 0 14 14",
            svg = """
            <path fill="currentColor" d="M11.2857,6.05714 L10.08571,4.85714 L7.85714,7.14786 L7.85714,1 L6.14286,1 L6.14286,7.14786 L3.91429,4.85714 L2.71429,6.05714 L7,10.42857 L11.2857,6.05714 Z M1,11.2857 L1,13 L13,13 L13,11.2857 L1,11.2857 Z" /> 
        """.trimIndent()
        )
        override val delete = IconDefinition(
            "delete",
            svg = """
            <path fill="currentColor" d="M19.452 7.5H4.547a.5.5 0 00-.5.545l1.287 14.136A2 2 0 007.326 24h9.347a2 2 0 001.992-1.819L19.95 8.045a.5.5 0 00-.129-.382.5.5 0 00-.369-.163zm-9.2 13a.75.75 0 01-1.5 0v-9a.75.75 0 011.5 0zm5 0a.75.75 0 01-1.5 0v-9a.75.75 0 011.5 0zM22 4h-4.75a.25.25 0 01-.25-.25V2.5A2.5 2.5 0 0014.5 0h-5A2.5 2.5 0 007 2.5v1.25a.25.25 0 01-.25.25H2a1 1 0 000 2h20a1 1 0 000-2zM9 3.75V2.5a.5.5 0 01.5-.5h5a.5.5 0 01.5.5v1.25a.25.25 0 01-.25.25h-5.5A.25.25 0 019 3.75z" /> 
        """.trimIndent()
        )
        override val repeat = IconDefinition(
            "repeat",
            svg = """
                <g fill="currentColor">
                  <path d="M10.319,4.936a7.239,7.239,0,0,1,7.1,2.252,1.25,1.25,0,1,0,1.872-1.657A9.737,9.737,0,0,0,9.743,2.5,10.269,10.269,0,0,0,2.378,9.61a.249.249,0,0,1-.271.178l-1.033-.13A.491.491,0,0,0,.6,9.877a.5.5,0,0,0-.019.526l2.476,4.342a.5.5,0,0,0,.373.248.43.43,0,0,0,.062,0,.5.5,0,0,0,.359-.152l3.477-3.593a.5.5,0,0,0-.3-.844L5.15,10.172a.25.25,0,0,1-.2-.333A7.7,7.7,0,0,1,10.319,4.936Z" />
                  <path d="M23.406,14.1a.5.5,0,0,0,.015-.526l-2.5-4.329A.5.5,0,0,0,20.546,9a.489.489,0,0,0-.421.151l-3.456,3.614a.5.5,0,0,0,.3.842l1.848.221a.249.249,0,0,1,.183.117.253.253,0,0,1,.023.216,7.688,7.688,0,0,1-5.369,4.9,7.243,7.243,0,0,1-7.1-2.253,1.25,1.25,0,1,0-1.872,1.656,9.74,9.74,0,0,0,9.549,3.03,10.261,10.261,0,0,0,7.369-7.12.251.251,0,0,1,.27-.179l1.058.127a.422.422,0,0,0,.06,0A.5.5,0,0,0,23.406,14.1Z" />
                </g>
        """.trimIndent()
        )
        override val repeatClock = IconDefinition(
            "repeatClock",
            svg = """
                <g fill="currentColor">
                  <path d="M12.965,6a1,1,0,0,0-1,1v5.5a1,1,0,0,0,1,1h5a1,1,0,0,0,0-2h-3.75a.25.25,0,0,1-.25-.25V7A1,1,0,0,0,12.965,6Z" />
                  <path d="M12.567,1.258A10.822,10.822,0,0,0,2.818,8.4a.25.25,0,0,1-.271.163L.858,8.309a.514.514,0,0,0-.485.213.5.5,0,0,0-.021.53l2.679,4.7a.5.5,0,0,0,.786.107l3.77-3.746a.5.5,0,0,0-.279-.85L5.593,9.007a.25.25,0,0,1-.192-.35,8.259,8.259,0,1,1,7.866,11.59,1.25,1.25,0,0,0,.045,2.5h.047a10.751,10.751,0,1,0-.792-21.487Z" />
                </g>
        """.trimIndent()
        )
        override val edit = IconDefinition(
            "edit",
            svg = """
                <g fill="none" stroke="currentColor" strokeLinecap="round" strokeWidth="2">
                  <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                  <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                </g>
        """.trimIndent()
        )
        override val chevronLeft = IconDefinition(
            "chevronLeft",
            svg = """
            <path fill="currentColor" d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z" /> 
        """.trimIndent()
        )
        override val chevronRight = IconDefinition(
            "chevronRight",
            svg = """
            <path fill="currentColor" d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z" /> 
        """.trimIndent()
        )
        override val chevronDown = IconDefinition(
            "chevronDown",
            svg = """
            <path fill="currentColor" d="M16.59 8.59L12 13.17 7.41 8.59 6 10l6 6 6-6z" /> 
        """.trimIndent()
        )
        override val chevronUp = IconDefinition(
            "chevronUp",
            svg = """
            <path fill="currentColor" d="M12 8l-6 6 1.41 1.41L12 10.83l4.59 4.58L18 14z" /> 
        """.trimIndent()
        )
        override val arrowBack = IconDefinition(
            "arrowBack",
            svg = """ 
                <path fill="currentColor" d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z" />
            """.trimIndent()
        )
        override val arrowForward = IconDefinition(
            "arrowForward",
            svg = """
                <path fill="currentColor" d="M12 4l-1.41 1.41L16.17 11H4v2h12.17l-5.58 5.59L12 20l8-8z" />
            """.trimIndent()
        )
        override val arrowUp = IconDefinition(
            "arrowUp",
            svg = """ 
                <path fill="currentColor" d="M12 4C12.2652 4 12.5196 4.10536 12.7071 4.29289L18.7071 10.2929C19.0976 10.6834 19.0976 11.3166 18.7071 11.7071C18.3166 12.0976 17.6834 12.0976 17.2929 11.7071L13 7.41421L13 19C13 19.5523 12.5523 20 12 20C11.4477 20 11 19.5523 11 19L11 7.41421L6.70711 11.7071C6.31658 12.0976 5.68342 12.0976 5.29289 11.7071C4.90237 11.3166 4.90237 10.6834 5.29289 10.2929L11.2929 4.29289C11.4804 4.10536 11.7348 4 12 4Z" />
            """.trimIndent()
        )
        override val arrowUpDown = IconDefinition(
            "arrowUpDown",
            "0 0 16 16",
            svg = """ 
                <path fill="currentColor" d="M11.891 9.992a1 1 0 1 1 1.416 1.415l-4.3 4.3a1 1 0 0 1-1.414 0l-4.3-4.3A1 1 0 0 1 4.71 9.992l3.59 3.591 3.591-3.591zm0-3.984L8.3 2.417 4.709 6.008a1 1 0 0 1-1.416-1.415l4.3-4.3a1 1 0 0 1 1.414 0l4.3 4.3a1 1 0 1 1-1.416 1.415z" />
            """.trimIndent()
        )
        override val arrowDown = IconDefinition(
            "arrowDown",
            svg = """ 
                <path fill="currentColor" d="M20 12l-1.41-1.41L13 16.17V4h-2v12.17l-5.58-5.59L4 12l8 8 8-8z" />
                """.trimIndent()
        )
        override val externalLink = IconDefinition(
            "externalLink",
            svg = """ 
                    <g fill="none" stroke="currentColor" strokeLinecap="round" strokeWidth="2">
                      <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6" />
                      <path d="M15 3h6v6" />
                      <path d="M10 14L21 3" />
                    </g>
                """.trimIndent()
        )
        override val link = IconDefinition(
            "link",
            svg = """ 
                <g fill="currentColor">
                  <path d="M10.458,18.374,7.721,21.11a2.853,2.853,0,0,1-3.942,0l-.892-.891a2.787,2.787,0,0,1,0-3.941l5.8-5.8a2.789,2.789,0,0,1,3.942,0l.893.892A1,1,0,0,0,14.94,9.952l-.893-.892a4.791,4.791,0,0,0-6.771,0l-5.8,5.8a4.787,4.787,0,0,0,0,6.77l.892.891a4.785,4.785,0,0,0,6.771,0l2.736-2.735a1,1,0,1,0-1.414-1.415Z" />
                  <path d="M22.526,2.363l-.892-.892a4.8,4.8,0,0,0-6.77,0l-2.905,2.9a1,1,0,0,0,1.414,1.414l2.9-2.9a2.79,2.79,0,0,1,3.941,0l.893.893a2.786,2.786,0,0,1,0,3.942l-5.8,5.8a2.769,2.769,0,0,1-1.971.817h0a2.766,2.766,0,0,1-1.969-.816,1,1,0,1,0-1.415,1.412,4.751,4.751,0,0,0,3.384,1.4h0a4.752,4.752,0,0,0,3.385-1.4l5.8-5.8a4.786,4.786,0,0,0,0-6.771Z" />
                </g>
                """.trimIndent()
        )
        override val plusSquare = IconDefinition(
            "plusSquare",
            svg = """ 
                <g fill="none" stroke="currentColor" strokeLinecap="round" strokeWidth="2">
                  <rect height="18" width="18" rx="2" ry="2" x="3" y="3" />
                  <path d="M12 8v8" />
                  <path d="M8 12h8" />
                </g>
                """.trimIndent()
        )
        override val calendar = IconDefinition(
            "calendar",
            "0 0 14 14",
            svg = """ 
                <path fill="currentColor" d="M10.8889,5.5 L3.11111,5.5 L3.11111,7.05556 L10.8889,7.05556 L10.8889,5.5 Z M12.4444,1.05556 L11.6667,1.05556 L11.6667,0 L10.1111,0 L10.1111,1.05556 L3.88889,1.05556 L3.88889,0 L2.33333,0 L2.33333,1.05556 L1.55556,1.05556 C0.692222,1.05556 0.00777777,1.75556 0.00777777,2.61111 L0,12.5 C0,13.3556 0.692222,14 1.55556,14 L12.4444,14 C13.3,14 14,13.3556 14,12.5 L14,2.61111 C14,1.75556 13.3,1.05556 12.4444,1.05556 Z M12.4444,12.5 L1.55556,12.5 L1.55556,3.94444 L12.4444,3.94444 L12.4444,12.5 Z M8.55556,8.61111 L3.11111,8.61111 L3.11111,10.1667 L8.55556,10.1667 L8.55556,8.61111 Z" />
                """.trimIndent()
        )
        override val chat = IconDefinition(
            "chat",
            "0 0 14 14",
            svg = """ 
                <path fill="currentColor" d="M0.913134,0.920639 C1.49851,0.331726 2.29348,0 3.12342,0 L10.8766,0 C11.7065,0 12.5015,0.331725 13.0869,0.920639 C13.6721,1.50939 14,2.30689 14,3.13746 L14,8.12943 C13.9962,8.51443 13.9059,8.97125 13.7629,9.32852 C13.6128,9.683 13.3552,10.0709 13.0869,10.3462 C12.813,10.6163 12.4265,10.8761 12.0734,11.0274 C11.7172,11.1716 11.2607,11.263 10.8766,11.2669 L10.1234,11.2669 L10.1234,12.5676 L10.1209,12.5676 C10.1204,12.793 10.0633,13.0791 9.97807,13.262 C9.8627,13.466 9.61158,13.7198 9.40818,13.8382 L9.40824,13.8383 C9.4077,13.8386 9.40716,13.8388 9.40661,13.8391 C9.40621,13.8393 9.4058,13.8396 9.40539,13.8398 L9.40535,13.8397 C9.22958,13.9254 8.94505,13.9951 8.75059,14 L8.74789,14 C8.35724,13.9963 7.98473,13.8383 7.71035,13.5617 L5.39553,11.2669 L3.12342,11.2669 C2.29348,11.2669 1.49851,10.9352 0.913134,10.3462 C0.644826,10.0709 0.387187,9.683 0.23711,9.32852 C0.0941235,8.97125 0.00379528,8.51443 0,8.12943 L0,3.13746 C0,2.30689 0.327915,1.50939 0.913134,0.920639 Z M3.12342,1.59494 C2.71959,1.59494 2.33133,1.75628 2.04431,2.04503 C1.75713,2.33395 1.59494,2.72681 1.59494,3.13746 L1.59494,8.12943 C1.59114,8.35901 1.62114,8.51076 1.71193,8.72129 C1.79563,8.9346 1.88065,9.06264 2.04431,9.22185 C2.33133,9.5106 2.71959,9.67195 3.12342,9.67195 L5.72383,9.67195 C5.93413,9.67195 6.13592,9.75502 6.28527,9.90308 L8.52848,12.1269 L8.52848,10.4694 C8.52848,10.029 8.88552,9.67195 9.32595,9.67195 L10.8766,9.67195 C11.1034,9.67583 11.2517,9.64614 11.4599,9.55518 C11.6712,9.47132 11.7976,9.38635 11.9557,9.22185 C12.1193,9.06264 12.2044,8.9346 12.2881,8.72129 C12.3789,8.51076 12.4089,8.35901 12.4051,8.12943 L12.4051,3.13746 C12.4051,2.72681 12.2429,2.33394 11.9557,2.04503 C11.6687,1.75628 11.2804,1.59494 10.8766,1.59494 L3.12342,1.59494 Z" />
                """.trimIndent()
        )
        override val time = IconDefinition(
            "time",
            svg = """ 
                <g fill="currentColor">
                  <path d="M12,0A12,12,0,1,0,24,12,12.014,12.014,0,0,0,12,0Zm0,22A10,10,0,1,1,22,12,10.011,10.011,0,0,1,12,22Z" />
                  <path d="M17.134,15.81,12.5,11.561V6.5a1,1,0,0,0-2,0V12a1,1,0,0,0,.324.738l4.959,4.545a1.01,1.01,0,0,0,1.413-.061A1,1,0,0,0,17.134,15.81Z" />
                </g>
                """.trimIndent()
        )
        override val arrowRight = IconDefinition(
            "arrowRight",
            svg = """
                <g fill="currentColor">
                    <path d="M13.584,12a2.643,2.643,0,0,1-.775,1.875L3.268,23.416a1.768,1.768,0,0,1-2.5-2.5l8.739-8.739a.25.25,0,0,0,0-.354L.768,3.084a1.768,1.768,0,0,1,2.5-2.5l9.541,9.541A2.643,2.643,0,0,1,13.584,12Z">
                    <path d="M23.75,12a2.643,2.643,0,0,1-.775,1.875l-9.541,9.541a1.768,1.768,0,0,1-2.5-2.5l8.739-8.739a.25.25,0,0,0,0-.354L10.934,3.084a1.768,1.768,0,0,1,2.5-2.5l9.541,9.541A2.643,2.643,0,0,1,23.75,12Z">
                </g>
            """.trimIndent()
        )
        override val arrowLeft = IconDefinition(
            "arrowLeft",
            svg = """
                <g fill="currentColor"> 
                    <path d="M10.416,12a2.643,2.643,0,0,1,.775-1.875L20.732.584a1.768,1.768,0,0,1,2.5,2.5l-8.739,8.739a.25.25,0,0,0,0,.354l8.739,8.739a1.768,1.768,0,0,1-2.5,2.5l-9.541-9.541A2.643,2.643,0,0,1,10.416,12Z">
                    <path d="M.25,12a2.643,2.643,0,0,1,.775-1.875L10.566.584a1.768,1.768,0,0,1,2.5,2.5L4.327,11.823a.25.25,0,0,0,0,.354l8.739,8.739a1.768,1.768,0,0,1-2.5,2.5L1.025,13.875A2.643,2.643,0,0,1,.25,12Z">
                </g>
            """.trimIndent()
        )
        override val atSign = IconDefinition(
            "atSign",
            svg = """ 
                <path fill="currentColor" d="M12,.5A11.634,11.634,0,0,0,.262,12,11.634,11.634,0,0,0,12,23.5a11.836,11.836,0,0,0,6.624-2,1.25,1.25,0,1,0-1.393-2.076A9.34,9.34,0,0,1,12,21a9.132,9.132,0,0,1-9.238-9A9.132,9.132,0,0,1,12,3a9.132,9.132,0,0,1,9.238,9v.891a1.943,1.943,0,0,1-3.884,0V12A5.355,5.355,0,1,0,12,17.261a5.376,5.376,0,0,0,3.861-1.634,4.438,4.438,0,0,0,7.877-2.736V12A11.634,11.634,0,0,0,12,.5Zm0,14.261A2.763,2.763,0,1,1,14.854,12,2.812,2.812,0,0,1,12,14.761Z" />
                """.trimIndent()
        )
        override val attachment = IconDefinition(
            "attachment",
            svg = """ 
                <path fill="currentColor" d="M21.843,3.455a6.961,6.961,0,0,0-9.846,0L1.619,13.832a5.128,5.128,0,0,0,7.252,7.252L17.3,12.653A3.293,3.293,0,1,0,12.646,8L7.457,13.184A1,1,0,1,0,8.871,14.6L14.06,9.409a1.294,1.294,0,0,1,1.829,1.83L7.457,19.67a3.128,3.128,0,0,1-4.424-4.424L13.411,4.869a4.962,4.962,0,1,1,7.018,7.018L12.646,19.67a1,1,0,1,0,1.414,1.414L21.843,13.3a6.96,6.96,0,0,0,0-9.846Z" />
                """.trimIndent()
        )
        override val upDown = IconDefinition(
            "upDown",
            "-1 -1 9 11",
            svg = """ 
                <path fill="currentColor" d="M 3.5 0L 3.98809 -0.569442L 3.5 -0.987808L 3.01191 -0.569442L 3.5 0ZM 3.5 9L 3.01191 9.56944L 3.5 9.98781L 3.98809 9.56944L 3.5 9ZM 0.488094 3.56944L 3.98809 0.569442L 3.01191 -0.569442L -0.488094 2.43056L 0.488094 3.56944ZM 3.01191 0.569442L 6.51191 3.56944L 7.48809 2.43056L 3.98809 -0.569442L 3.01191 0.569442ZM -0.488094 6.56944L 3.01191 9.56944L 3.98809 8.43056L 0.488094 5.43056L -0.488094 6.56944ZM 3.98809 9.56944L 7.48809 6.56944L 6.51191 5.43056L 3.01191 8.43056L 3.98809 9.56944Z" />
                """.trimIndent()
        )
        override val star = IconDefinition(
            "star",
            svg = """ 
                <path fill="currentColor" d="M23.555,8.729a1.505,1.505,0,0,0-1.406-.98H16.062a.5.5,0,0,1-.472-.334L13.405,1.222a1.5,1.5,0,0,0-2.81,0l-.005.016L8.41,7.415a.5.5,0,0,1-.471.334H1.85A1.5,1.5,0,0,0,.887,10.4l5.184,4.3a.5.5,0,0,1,.155.543L4.048,21.774a1.5,1.5,0,0,0,2.31,1.684l5.346-3.92a.5.5,0,0,1,.591,0l5.344,3.919a1.5,1.5,0,0,0,2.312-1.683l-2.178-6.535a.5.5,0,0,1,.155-.543l5.194-4.306A1.5,1.5,0,0,0,23.555,8.729Z" />
                """.trimIndent()
        )
        override val email = IconDefinition(
            "email",
            svg = """ 
                <g fill="currentColor">
                  <path d="M11.114,14.556a1.252,1.252,0,0,0,1.768,0L22.568,4.87a.5.5,0,0,0-.281-.849A1.966,1.966,0,0,0,22,4H2a1.966,1.966,0,0,0-.289.021.5.5,0,0,0-.281.849Z" />
                  <path d="M23.888,5.832a.182.182,0,0,0-.2.039l-6.2,6.2a.251.251,0,0,0,0,.354l5.043,5.043a.75.75,0,1,1-1.06,1.061l-5.043-5.043a.25.25,0,0,0-.354,0l-2.129,2.129a2.75,2.75,0,0,1-3.888,0L7.926,13.488a.251.251,0,0,0-.354,0L2.529,18.531a.75.75,0,0,1-1.06-1.061l5.043-5.043a.251.251,0,0,0,0-.354l-6.2-6.2a.18.18,0,0,0-.2-.039A.182.182,0,0,0,0,6V18a2,2,0,0,0,2,2H22a2,2,0,0,0,2-2V6A.181.181,0,0,0,23.888,5.832Z" />
                </g>
                """.trimIndent()
        )
        override val phone = IconDefinition(
            "phone",
            "0 0 14 14",
            svg = """ 
                <path fill="currentColor" d="M2.20731,0.0127209 C2.1105,-0.0066419 1.99432,-0.00664663 1.91687,0.032079 C0.871279,0.438698 0.212942,1.92964 0.0580392,2.95587 C-0.426031,6.28627 2.20731,9.17133 4.62766,11.0689 C6.77694,12.7534 10.9012,15.5223 13.3409,12.8503 C13.6507,12.5211 14.0186,12.037 13.9993,11.553 C13.9412,10.7397 13.186,10.1588 12.6051,9.71349 C12.1598,9.38432 11.2304,8.47427 10.6495,8.49363 C10.1267,8.51299 9.79754,9.05515 9.46837,9.38432 L8.88748,9.96521 C8.79067,10.062 7.55145,9.24878 7.41591,9.15197 C6.91248,8.8228 6.4284,8.45491 6.00242,8.04829 C5.57644,7.64167 5.18919,7.19632 4.86002,6.73161 C4.7632,6.59607 3.96933,5.41495 4.04678,5.31813 C4.04678,5.31813 4.72448,4.58234 4.91811,4.2919 C5.32473,3.67229 5.63453,3.18822 5.16982,2.45243 C4.99556,2.18135 4.78257,1.96836 4.55021,1.73601 C4.14359,1.34875 3.73698,0.942131 3.27227,0.612963 C3.02055,0.419335 2.59457,0.0708094 2.20731,0.0127209 Z" />
                """.trimIndent()
        )
        override val dragHandle = IconDefinition(
            "dragHandle",
            "0 0 10 10",
            svg = """ 
                <path fill="currentColor" d="M3,2 C2.44771525,2 2,1.55228475 2,1 C2,0.44771525 2.44771525,0 3,0 C3.55228475,0 4,0.44771525 4,1 C4,1.55228475 3.55228475,2 3,2 Z M3,6 C2.44771525,6 2,5.55228475 2,5 C2,4.44771525 2.44771525,4 3,4 C3.55228475,4 4,4.44771525 4,5 C4,5.55228475 3.55228475,6 3,6 Z M3,10 C2.44771525,10 2,9.55228475 2,9 C2,8.44771525 2.44771525,8 3,8 C3.55228475,8 4,8.44771525 4,9 C4,9.55228475 3.55228475,10 3,10 Z M7,2 C6.44771525,2 6,1.55228475 6,1 C6,0.44771525 6.44771525,0 7,0 C7.55228475,0 8,0.44771525 8,1 C8,1.55228475 7.55228475,2 7,2 Z M7,6 C6.44771525,6 6,5.55228475 6,5 C6,4.44771525 6.44771525,4 7,4 C7.55228475,4 8,4.44771525 8,5 C8,5.55228475 7.55228475,6 7,6 Z M7,10 C6.44771525,10 6,9.55228475 6,9 C6,8.44771525 6.44771525,8 7,8 C7.55228475,8 8,8.44771525 8,9 C8,9.55228475 7.55228475,10 7,10 Z" />
                """.trimIndent()
        )

        // TODO: Shows not up as expected!
        override val spinner = IconDefinition(
            "spinner",
            svg = """ 
                  <defs>
                    <linearGradient
                      x1="28.154%"
                      y1="63.74%"
                      x2="74.629%"
                      y2="17.783%"
                      id="a"
                    >
                      <stop stopColor="currentColor" offset="0%" />
                      <stop stopColor="#fff" stopOpacity="0" offset="100%" />
                    </linearGradient>
                  </defs>
                  <g transform="translate(2)" fill="none">
                    <circle stroke="url(#a)" strokeWidth="4" cx="10" cy="12" r="10" />
                    <path
                      d="M10 2C4.477 2 0 6.477 0 12"
                      stroke="currentColor"
                      strokeWidth="4"
                    />
                    <rect fill="currentColor" x="8" width="4" height="4" rx="8" />
                  </g>
                """.trimIndent()
        )
        override val close = IconDefinition(
            "close",
            svg = """ 
                <path fill="currentColor" d="M.439,21.44a1.5,1.5,0,0,0,2.122,2.121L11.823,14.3a.25.25,0,0,1,.354,0l9.262,9.263a1.5,1.5,0,1,0,2.122-2.121L14.3,12.177a.25.25,0,0,1,0-.354l9.263-9.262A1.5,1.5,0,0,0,21.439.44L12.177,9.7a.25.25,0,0,1-.354,0L2.561.44A1.5,1.5,0,0,0,.439,2.561L9.7,11.823a.25.25,0,0,1,0,.354Z" />
                """.trimIndent()
        )
        override val smallClose = IconDefinition(
            "smallClose",
            "0 0 16 16",
            svg = """ 
                <path
                  d="M9.41 8l2.29-2.29c.19-.18.3-.43.3-.71a1.003 1.003 0 0 0-1.71-.71L8 6.59l-2.29-2.3a1.003 1.003 0 0 0-1.42 1.42L6.59 8 4.3 10.29c-.19.18-.3.43-.3.71a1.003 1.003 0 0 0 1.71.71L8 9.41l2.29 2.29c.18.19.43.3.71.3a1.003 1.003 0 0 0 .71-1.71L9.41 8z"
                  fillRule="evenodd"
                  fill="currentColor"
                />
                """.trimIndent()
        )
        override val notAllowed = IconDefinition(
            "notAllowed",
            svg = """ 
                <path fill="currentColor" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.42 0-8-3.58-8-8 0-1.85.63-3.55 1.69-4.9L16.9 18.31C15.55 19.37 13.85 20 12 20zm6.31-3.1L7.1 5.69C8.45 4.63 10.15 4 12 4c4.42 0 8 3.58 8 8 0 1.85-.63 3.55-1.69 4.9z" />
                """.trimIndent()
        )
        override val triangleDown = IconDefinition(
            "triangleDown",
            svg = """ 
                <path fill="currentColor" d="M21,5H3C2.621,5,2.275,5.214,2.105,5.553C1.937,5.892,1.973,6.297,2.2,6.6l9,12 c0.188,0.252,0.485,0.4,0.8,0.4s0.611-0.148,0.8-0.4l9-12c0.228-0.303,0.264-0.708,0.095-1.047C21.725,5.214,21.379,5,21,5z" />
                """.trimIndent()
        )
        override val triangleUp = IconDefinition(
            "triangleUp",
            svg = """ 
                <path fill="currentColor" d="M12.8,5.4c-0.377-0.504-1.223-0.504-1.6,0l-9,12c-0.228,0.303-0.264,0.708-0.095,1.047 C2.275,18.786,2.621,19,3,19h18c0.379,0,0.725-0.214,0.895-0.553c0.169-0.339,0.133-0.744-0.095-1.047L12.8,5.4z" />
                """.trimIndent()
        )
        override val infoOutline = IconDefinition(
            "infoOutline",
            svg = """ 
                <g
                  fill="currentColor"
                  stroke="currentColor"
                  strokeLinecap="square"
                  strokeWidth="2"
                >
                  <circle cx="12" cy="12" fill="none" r="11" stroke="currentColor" />
                  <line fill="none" x1="11.959" x2="11.959" y1="11" y2="17" />
                  <circle cx="11.959" cy="7" r="1" stroke="none" />
                </g>
                """.trimIndent()
        )
        override val bell = IconDefinition(
            "bell",
            svg = """ 
                <path fill="currentColor" d="M12 22c1.1 0 2-.9 2-2h-4c0 1.1.89 2 2 2zm6-6v-5c0-3.07-1.64-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.63 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z" />
                """.trimIndent()
        )
        override val info = IconDefinition(
            "info",
            svg = """ 
                <path fill="currentColor" d="M12,0A12,12,0,1,0,24,12,12.013,12.013,0,0,0,12,0Zm.25,5a1.5,1.5,0,1,1-1.5,1.5A1.5,1.5,0,0,1,12.25,5ZM14.5,18.5h-4a1,1,0,0,1,0-2h.75a.25.25,0,0,0,.25-.25v-4.5a.25.25,0,0,0-.25-.25H10.5a1,1,0,0,1,0-2h1a2,2,0,0,1,2,2v4.75a.25.25,0,0,0,.25.25h.75a1,1,0,1,1,0,2Z" />
                """.trimIndent()
        )
        override val question = IconDefinition(
            "question",
            svg = """ 
                <path fill="currentColor" d="M12,0A12,12,0,1,0,24,12,12.013,12.013,0,0,0,12,0Zm0,19a1.5,1.5,0,1,1,1.5-1.5A1.5,1.5,0,0,1,12,19Zm1.6-6.08a1,1,0,0,0-.6.917,1,1,0,1,1-2,0,3,3,0,0,1,1.8-2.75A2,2,0,1,0,10,9.255a1,1,0,1,1-2,0,4,4,0,1,1,5.6,3.666Z" />
                """.trimIndent()
        )
        override val questionOutline = IconDefinition(
            "questionOutline",
            svg = """
                <g stroke="currentColor" strokeWidth="1.5">
                  <path
                    strokeLinecap="round"
                    fill="none"
                    d="M9,9a3,3,0,1,1,4,2.829,1.5,1.5,0,0,0-1,1.415V14.25"
                  />
                  <path
                    fill="none"
                    strokeLinecap="round"
                    d="M12,17.25a.375.375,0,1,0,.375.375A.375.375,0,0,0,12,17.25h0"
                  />
                  <circle fill="none" strokeMiterlimit="10" cx="12" cy="12" r="11.25" />
                </g>                 
                """.trimIndent()
        )
        override val warning = IconDefinition(
            "warning",
            svg = """ 
                <path fill="currentColor" d="M11.983,0a12.206,12.206,0,0,0-8.51,3.653A11.8,11.8,0,0,0,0,12.207,11.779,11.779,0,0,0,11.8,24h.214A12.111,12.111,0,0,0,24,11.791h0A11.766,11.766,0,0,0,11.983,0ZM10.5,16.542a1.476,1.476,0,0,1,1.449-1.53h.027a1.527,1.527,0,0,1,1.523,1.47,1.475,1.475,0,0,1-1.449,1.53h-.027A1.529,1.529,0,0,1,10.5,16.542ZM11,12.5v-6a1,1,0,0,1,2,0v6a1,1,0,1,1-2,0Z" />
                """.trimIndent()
        )
        override val warningTwo = IconDefinition(
            "warningTwo",
            svg = """ 
                <path fill="currentColor" d="M23.119,20,13.772,2.15h0a2,2,0,0,0-3.543,0L.881,20a2,2,0,0,0,1.772,2.928H21.347A2,2,0,0,0,23.119,20ZM11,8.423a1,1,0,0,1,2,0v6a1,1,0,1,1-2,0Zm1.05,11.51h-.028a1.528,1.528,0,0,1-1.522-1.47,1.476,1.476,0,0,1,1.448-1.53h.028A1.527,1.527,0,0,1,13.5,18.4,1.475,1.475,0,0,1,12.05,19.933Z" />
                """.trimIndent()
        )
        override val check = IconDefinition(
            "check",
            "0 0 14 14",
            svg = """ 
                <g fill="currentColor">
                  <polygon points="5.5 11.9993304 14 3.49933039 12.5 2 5.5 8.99933039 1.5 4.9968652 0 6.49933039" />
                </g>
                """.trimIndent()
        )
        override val minus = IconDefinition(
            "minus",
            svg = """ 
                <g fill="currentColor">
                  <rect height="4" width="20" x="2" y="10" />
                </g>
                """.trimIndent()
        )
        override val hamburger = IconDefinition(
            "hamburger",
            svg = """ 
                <path fill="currentColor" d="M 3 5 A 1.0001 1.0001 0 1 0 3 7 L 21 7 A 1.0001 1.0001 0 1 0 21 5 L 3 5 z M 3 11 A 1.0001 1.0001 0 1 0 3 13 L 21 13 A 1.0001 1.0001 0 1 0 21 11 L 3 11 z M 3 17 A 1.0001 1.0001 0 1 0 3 19 L 21 19 A 1.0001 1.0001 0 1 0 21 17 L 3 17 z" />
                """.trimIndent()
        )
    }

    override val input = object : InputFieldStyles {
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

    override val button = object : PushButtonStyles {
        override val variants = object : PushButtonVariants {
            private val basic: Style<BasicParams> = {
                lineHeight { smaller }
                radius { normal }
                fontWeight { FontWeights.semiBold }

                focus {
                    boxShadow { outline }
                }
            }

            override val solid: Style<BasicParams> = {
                basic()
                background { color { "var(--main-color)" } }
                color { light }

                hover {
                    css("filter: brightness(132%);")
                }

                active {
                    css("filter: brightness(132%);")
                }
            }

            override val outline: Style<BasicParams> = {
                basic()
                color { "var(--main-color)" }
                border {
                    width { thin }
                    style { BorderStyleValues.solid }
                    color { "var(--main-color)" }
                }

                hover {
                    background { color { light } }
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
                    textDecoration { TextDecorations.underline }
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
                minHeight { theme().sizes.smaller }
            }

            override val normal: Style<BasicParams> = {
                basic()
                position {
                    fixed {
                        top { "var(--main-level)" }
                        left { "50%" }
                    }
                }
                minHeight { theme().sizes.smaller }
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
                minHeight { theme().sizes.smaller }
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
                // TODO: Geht so nicht! Überschreibt ggf. das durch size gesetzte!
                // Man braucht aber X + Y!
                css("transform: translatey(-50%);")
            }
        }
    }

}
