package dev.fritz2.headlessdemo

import dev.fritz2.core.RenderContext
import dev.fritz2.core.fill
import dev.fritz2.core.viewBox

fun RenderContext.fritz2(classes: String? = null, id: String? = null) = svg(classes, id) {
    viewBox("0 0 152 152")
    content(
        """<path fill="currentColor" d="M 75.63569,1.4806133e-5 A 75.731422,74.018906 0 0 0 7.5696619e-8,74.019268 75.731422,74.018906 0 0 0 75.731808,148.038 75.731422,74.018906 0 0 0 151.4631,74.019268 75.731422,74.018906 0 0 0 75.731808,1.4806133e-5 a 75.731422,74.018906 0 0 0 -0.09612,0 z M 111.16014,18.428347 c 3.52601,0.01826 6.37281,2.879733 6.35465,6.387206 l -0.13126,25.416536 c -0.0181,3.50747 -2.89477,6.338845 -6.4208,6.320544 -3.52602,-0.01826 -6.37285,-2.87922 -6.35465,-6.38669 l -12.775445,-0.06615 c -3.52603,-0.01826 -6.402596,2.813592 -6.420797,6.321062 l -0.03204,6.221842 h 5.896279 a 6.3606494,6.4906672 0 0 1 0.446484,-0.01601 6.3606494,6.4906672 0 0 1 6.360853,6.490557 6.3606494,6.4906672 0 0 1 -6.360853,6.490563 6.3606494,6.4906672 0 0 1 -0.516763,-0.0217 H 53.850976 v -0.02119 a 6.3606494,6.4732442 0 0 1 -0.301792,0.0078 6.3606494,6.4732442 0 0 1 -6.360853,-6.473505 6.3606494,6.4732442 0 0 1 6.360853,-6.472991 6.3606494,6.4732442 0 0 1 0.454237,0.01654 h 5.82445 l 0.06615,-12.708268 c 0.07267,-14.03637 11.57207,-25.357293 25.68267,-25.284244 l 19.163169,0.09922 c 0.0182,-3.507473 2.89426,-6.339322 6.42028,-6.32106 z m -51.430972,60.447988 25.551413,0.132292 -0.0987,19.062403 c -0.07268,14.03613 -11.57207,25.35724 -25.68267,25.28424 l -12.775445,-0.0661 c -0.01815,3.50747 -2.894788,6.33936 -6.420798,6.32106 -3.52603,-0.0183 -6.372852,-2.87974 -6.354651,-6.38721 L 34.08009,97.806447 c 0.01815,-3.50747 2.894258,-6.339361 6.420278,-6.321063 3.52601,0.01826 6.373368,2.879728 6.355168,6.387209 l 6.387724,0.03307 c 3.52602,0.01826 6.40208,-2.813582 6.420281,-6.321063 z" />"""
    )
    fill("none")
}

fun RenderContext.icon(classes: String? = null, id: String? = null, content: String) =
    svg(classes, id) {
        content(content)
        viewBox("0 0 24 24")
        fill("none")
    }

/**
 * Icon definitions from the fantastic [heroicons](https://heroicons.com)
 * License: https://github.com/tailwindlabs/heroicons/blob/master/LICENSE
 */
object HeroIcons {
    val academic_cap = """
		<path d="M12 14L21 9L12 4L3 9L12 14Z"/>
		<path d="M12 14L18.1591 10.5783C18.7017 11.9466 19 13.4384 19 14.9999C19 15.7013 18.9398 16.3885 18.8244 17.0569C16.2143 17.3106 13.849 18.4006 12 20.0555C10.151 18.4006 7.78571 17.3106 5.17562 17.0569C5.06017 16.3885 5 15.7012 5 14.9999C5 13.4384 5.29824 11.9466 5.84088 10.5782L12 14Z"/>
		<path d="M12 14L21 9L12 4L3 9L12 14ZM12 14L18.1591 10.5783C18.7017 11.9466 19 13.4384 19 14.9999C19 15.7013 18.9398 16.3885 18.8244 17.0569C16.2143 17.3106 13.849 18.4006 12 20.0555C10.151 18.4006 7.78571 17.3106 5.17562 17.0569C5.06017 16.3885 5 15.7012 5 14.9999C5 13.4384 5.29824 11.9466 5.84088 10.5782L12 14ZM8 19.9999V12.5L12 10.2778" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val adjustments = """
		<path d="M12 6V4M12 6C10.8954 6 10 6.89543 10 8C10 9.10457 10.8954 10 12 10M12 6C13.1046 6 14 6.89543 14 8C14 9.10457 13.1046 10 12 10M6 18C7.10457 18 8 17.1046 8 16C8 14.8954 7.10457 14 6 14M6 18C4.89543 18 4 17.1046 4 16C4 14.8954 4.89543 14 6 14M6 18V20M6 14V4M12 10V20M18 18C19.1046 18 20 17.1046 20 16C20 14.8954 19.1046 14 18 14M18 18C16.8954 18 16 17.1046 16 16C16 14.8954 16.8954 14 18 14M18 18V20M18 14V4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val `annotation` = """
		<path d="M7 8H17M7 12H11M12 20L8 16H5C3.89543 16 3 15.1046 3 14V6C3 4.89543 3.89543 4 5 4H19C20.1046 4 21 4.89543 21 6V14C21 15.1046 20.1046 16 19 16H16L12 20Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val archive = """
		<path d="M5 8H19M5 8C3.89543 8 3 7.10457 3 6C3 4.89543 3.89543 4 5 4H19C20.1046 4 21 4.89543 21 6C21 7.10457 20.1046 8 19 8M5 8L5 18C5 19.1046 5.89543 20 7 20H17C18.1046 20 19 19.1046 19 18V8M10 12H14" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_circle_down = """
		<path d="M15 13L12 16M12 16L9 13M12 16L12 8M12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_circle_left = """
		<path d="M11 15L8 12M8 12L11 9M8 12L16 12M3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_circle_right = """
		<path d="M13 9L16 12M16 12L13 15M16 12L8 12M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_circle_up = """
		<path d="M9 11L12 8M12 8L15 11M12 8L12 16M12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_down = """
		<path d="M19 14L12 21M12 21L5 14M12 21L12 3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_left = """
		<path d="M10 19L3 12M3 12L10 5M3 12L21 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_narrow_down = """
		<path d="M16 17L12 21M12 21L8 17M12 21L12 3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_narrow_left = """
		<path d="M7 16L3 12M3 12L7 8M3 12L21 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_narrow_right = """
		<path d="M17 8L21 12M21 12L17 16M21 12L3 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_narrow_up = """
		<path d="M8 7L12 3M12 3L16 7M12 3V21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_right = """
		<path d="M14 5L21 12M21 12L14 19M21 12L3 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_sm_down = """
		<path d="M17 13L12 18M12 18L7 13M12 18L12 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_sm_left = """
		<path d="M11 17L6 12M6 12L11 7M6 12L18 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_sm_right = """
		<path d="M13 7L18 12M18 12L13 17M18 12L6 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_sm_up = """
		<path d="M7 11L12 6M12 6L17 11M12 6V18" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrow_up = """
		<path d="M5 10L12 3M12 3L19 10M12 3V21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val arrows_expand = """
		<path d="M4 8V4M4 4H8M4 4L9 9M20 8V4M20 4H16M20 4L15 9M4 16V20M4 20H8M4 20L9 15M20 20L15 15M20 20V16M20 20H16" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val at_symbol = """
		<path d="M16 12C16 9.79086 14.2091 8 12 8C9.79086 8 8 9.79086 8 12C8 14.2091 9.79086 16 12 16C14.2091 16 16 14.2091 16 12ZM16 12V13.5C16 14.8807 17.1193 16 18.5 16C19.8807 16 21 14.8807 21 13.5V12C21 7.02944 16.9706 3 12 3C7.02944 3 3 7.02944 3 12C3 16.9706 7.02944 21 12 21M16.5 19.7942C15.0801 20.614 13.5296 21.0029 12 21.0015" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val backspace = """
		<path d="M12 14L14 12M14 12L16 10M14 12L12 10M14 12L16 14M3 12L9.41421 18.4142C9.78929 18.7893 10.298 19 10.8284 19H19C20.1046 19 21 18.1046 21 17V7C21 5.89543 20.1046 5 19 5H10.8284C10.298 5 9.78929 5.21071 9.41421 5.58579L3 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val badge_check = """
		<path d="M9.00012 12L11.0001 14L15.0001 10M7.83486 4.69705C8.55239 4.63979 9.23358 4.35763 9.78144 3.89075C11.0599 2.80123 12.9403 2.80123 14.2188 3.89075C14.7667 4.35763 15.4478 4.63979 16.1654 4.69705C17.8398 4.83067 19.1695 6.16031 19.3031 7.83474C19.3603 8.55227 19.6425 9.23346 20.1094 9.78132C21.1989 11.0598 21.1989 12.9402 20.1094 14.2187C19.6425 14.7665 19.3603 15.4477 19.3031 16.1653C19.1695 17.8397 17.8398 19.1693 16.1654 19.303C15.4479 19.3602 14.7667 19.6424 14.2188 20.1093C12.9403 21.1988 11.0599 21.1988 9.78144 20.1093C9.23358 19.6424 8.55239 19.3602 7.83486 19.303C6.16043 19.1693 4.83079 17.8397 4.69717 16.1653C4.63991 15.4477 4.35775 14.7665 3.89087 14.2187C2.80135 12.9402 2.80135 11.0598 3.89087 9.78132C4.35775 9.23346 4.63991 8.55227 4.69717 7.83474C4.83079 6.16031 6.16043 4.83067 7.83486 4.69705Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val ban = """
		<path d="M18.364 18.364C21.8787 14.8492 21.8787 9.15076 18.364 5.63604C14.8492 2.12132 9.15076 2.12132 5.63604 5.63604M18.364 18.364C14.8492 21.8787 9.15076 21.8787 5.63604 18.364C2.12132 14.8492 2.12132 9.15076 5.63604 5.63604M18.364 18.364L5.63604 5.63604" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val beaker = """
		<path d="M19.428 15.4282C19.1488 15.149 18.7932 14.9587 18.406 14.8812L16.0185 14.4037C14.7101 14.1421 13.3519 14.324 12.1585 14.9207L11.8411 15.0793C10.6477 15.676 9.28948 15.8579 7.98113 15.5963L6.04938 15.2099C5.39366 15.0788 4.71578 15.284 4.24294 15.7569M7.9998 4H15.9998L14.9998 5V10.1716C14.9998 10.702 15.2105 11.2107 15.5856 11.5858L20.5856 16.5858C21.8455 17.8457 20.9532 20 19.1714 20H4.82823C3.04642 20 2.15409 17.8457 3.41401 16.5858L8.41402 11.5858C8.78909 11.2107 8.9998 10.702 8.9998 10.1716V5L7.9998 4Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val bell = """
		<path d="M15 17H20L18.5951 15.5951C18.2141 15.2141 18 14.6973 18 14.1585V11C18 8.38757 16.3304 6.16509 14 5.34142V5C14 3.89543 13.1046 3 12 3C10.8954 3 10 3.89543 10 5V5.34142C7.66962 6.16509 6 8.38757 6 11V14.1585C6 14.6973 5.78595 15.2141 5.40493 15.5951L4 17H9M15 17V18C15 19.6569 13.6569 21 12 21C10.3431 21 9 19.6569 9 18V17M15 17H9" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val book_open = """
		<path d="M12 6.25278V19.2528M12 6.25278C10.8321 5.47686 9.24649 5 7.5 5C5.75351 5 4.16789 5.47686 3 6.25278V19.2528C4.16789 18.4769 5.75351 18 7.5 18C9.24649 18 10.8321 18.4769 12 19.2528M12 6.25278C13.1679 5.47686 14.7535 5 16.5 5C18.2465 5 19.8321 5.47686 21 6.25278V19.2528C19.8321 18.4769 18.2465 18 16.5 18C14.7535 18 13.1679 18.4769 12 19.2528" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val bookmark_alt = """
		<path d="M16 4V16L12 14L8 16V4M6 20H18C19.1046 20 20 19.1046 20 18V6C20 4.89543 19.1046 4 18 4H6C4.89543 4 4 4.89543 4 6V18C4 19.1046 4.89543 20 6 20Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val bookmark = """
		<path d="M5 5C5 3.89543 5.89543 3 7 3H17C18.1046 3 19 3.89543 19 5V21L12 17.5L5 21V5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val briefcase = """
		<path d="M21 13.2554C18.2207 14.3805 15.1827 15 12 15C8.8173 15 5.7793 14.3805 3 13.2554M16 6V4C16 2.89543 15.1046 2 14 2H10C8.89543 2 8 2.89543 8 4V6M12 12H12.01M5 20H19C20.1046 20 21 19.1046 21 18V8C21 6.89543 20.1046 6 19 6H5C3.89543 6 3 6.89543 3 8V18C3 19.1046 3.89543 20 5 20Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val cake = """
		<path d="M21 15.5458C20.4771 15.5458 19.9542 15.6972 19.5 16C18.5917 16.6056 17.4083 16.6056 16.5 16C15.5917 15.3944 14.4083 15.3944 13.5 16C12.5917 16.6056 11.4083 16.6056 10.5 16C9.59167 15.3944 8.40833 15.3944 7.5 16C6.59167 16.6056 5.40833 16.6056 4.5 16C4.04584 15.6972 3.52292 15.5458 3 15.5458M9 6V8M12 6V8M15 6V8M9 3H9.01M12 3H12.01M15 3H15.01M21 21V14C21 12.8954 20.1046 12 19 12H5C3.89543 12 3 12.8954 3 14V21H21ZM18 12V10C18 8.89543 17.1046 8 16 8H8C6.89543 8 6 8.89543 6 10V12H18Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val calculator = """
		<path d="M9 7H15M15 17V14M12 17H12.01M9 17H9.01M9 14H9.01M12 14H12.01M15 11H15.01M12 11H12.01M9 11H9.01M7 21H17C18.1046 21 19 20.1046 19 19V5C19 3.89543 18.1046 3 17 3H7C5.89543 3 5 3.89543 5 5V19C5 20.1046 5.89543 21 7 21Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val calendar = """
		<path d="M8 7V3M16 7V3M7 11H17M5 21H19C20.1046 21 21 20.1046 21 19V7C21 5.89543 20.1046 5 19 5H5C3.89543 5 3 5.89543 3 7V19C3 20.1046 3.89543 21 5 21Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val camera = """
		<path d="M3 9C3 7.89543 3.89543 7 5 7H5.92963C6.59834 7 7.2228 6.6658 7.59373 6.1094L8.40627 4.8906C8.7772 4.3342 9.40166 4 10.0704 4H13.9296C14.5983 4 15.2228 4.3342 15.5937 4.8906L16.4063 6.1094C16.7772 6.6658 17.4017 7 18.0704 7H19C20.1046 7 21 7.89543 21 9V18C21 19.1046 20.1046 20 19 20H5C3.89543 20 3 19.1046 3 18V9Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M15 13C15 14.6569 13.6569 16 12 16C10.3431 16 9 14.6569 9 13C9 11.3431 10.3431 10 12 10C13.6569 10 15 11.3431 15 13Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val cash = """
		<path d="M17 9V7C17 5.89543 16.1046 5 15 5H5C3.89543 5 3 5.89543 3 7V13C3 14.1046 3.89543 15 5 15H7M9 19H19C20.1046 19 21 18.1046 21 17V11C21 9.89543 20.1046 9 19 9H9C7.89543 9 7 9.89543 7 11V17C7 18.1046 7.89543 19 9 19ZM16 14C16 15.1046 15.1046 16 14 16C12.8954 16 12 15.1046 12 14C12 12.8954 12.8954 12 14 12C15.1046 12 16 12.8954 16 14Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val chart_bar = """
		<path d="M9 19V13C9 11.8954 8.10457 11 7 11H5C3.89543 11 3 11.8954 3 13V19C3 20.1046 3.89543 21 5 21H7C8.10457 21 9 20.1046 9 19ZM9 19V9C9 7.89543 9.89543 7 11 7H13C14.1046 7 15 7.89543 15 9V19M9 19C9 20.1046 9.89543 21 11 21H13C14.1046 21 15 20.1046 15 19M15 19V5C15 3.89543 15.8954 3 17 3H19C20.1046 3 21 3.89543 21 5V19C21 20.1046 20.1046 21 19 21H17C15.8954 21 15 20.1046 15 19Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val chart_pie = """
		<path d="M11 3.05493C6.50005 3.55238 3 7.36745 3 12C3 16.9706 7.02944 21 12 21C16.6326 21 20.4476 17.5 20.9451 13H11V3.05493Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M20.4878 9H15V3.5123C17.5572 4.41613 19.5839 6.44285 20.4878 9Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val chart_square_bar = """
		<path d="M16 8V16M12 11V16M8 14V16M6 20H18C19.1046 20 20 19.1046 20 18V6C20 4.89543 19.1046 4 18 4H6C4.89543 4 4 4.89543 4 6V18C4 19.1046 4.89543 20 6 20Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val chat_alt_2 = """
		<path d="M17 8H19C20.1046 8 21 8.89543 21 10V16C21 17.1046 20.1046 18 19 18H17V22L13 18H9C8.44772 18 7.94772 17.7761 7.58579 17.4142M7.58579 17.4142L11 14H15C16.1046 14 17 13.1046 17 12V6C17 4.89543 16.1046 4 15 4H5C3.89543 4 3 4.89543 3 6V12C3 13.1046 3.89543 14 5 14H7V18L7.58579 17.4142Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val chat_alt = """
		<path d="M8 10H8.01M12 10H12.01M16 10H16.01M9 16H5C3.89543 16 3 15.1046 3 14V6C3 4.89543 3.89543 4 5 4H19C20.1046 4 21 4.89543 21 6V14C21 15.1046 20.1046 16 19 16H14L9 21V16Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val chat = """
		<path d="M8 12H8.01M12 12H12.01M16 12H16.01M21 12C21 16.4183 16.9706 20 12 20C10.4607 20 9.01172 19.6565 7.74467 19.0511L3 20L4.39499 16.28C3.51156 15.0423 3 13.5743 3 12C3 7.58172 7.02944 4 12 4C16.9706 4 21 7.58172 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val check_circle = """
		<path d="M9 12L11 14L15 10M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val check = """
		<path d="M5 13L9 17L19 7" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val chevron_double_down = """
		<path d="M19 13L12 20L5 13M19 5L12 12L5 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val chevron_double_left = """
		<path d="M11 19L4 12L11 5M19 19L12 12L19 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val chevron_double_right = """
		<path d="M13 5L20 12L13 19M5 5L12 12L5 19" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val chevron_double_up = """
		<path d="M5 11L12 4L19 11M5 19L12 12L19 19" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val chevron_down = """
		<path d="M19 9L12 16L5 9" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val chevron_left = """
		<path d="M15 19L8 12L15 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val chevron_right = """
		<path d="M9 5L16 12L9 19" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val chevron_up = """
		<path d="M5 15L12 8L19 15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val chip = """
		<path d="M9 3V5M15 3V5M9 19V21M15 19V21M5 9H3M5 15H3M21 9H19M21 15H19M7 19H17C18.1046 19 19 18.1046 19 17V7C19 5.89543 18.1046 5 17 5H7C5.89543 5 5 5.89543 5 7V17C5 18.1046 5.89543 19 7 19ZM9 9H15V15H9V9Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val clipboard_check = """
		<path d="M9 5H7C5.89543 5 5 5.89543 5 7V19C5 20.1046 5.89543 21 7 21H17C18.1046 21 19 20.1046 19 19V7C19 5.89543 18.1046 5 17 5H15M9 5C9 6.10457 9.89543 7 11 7H13C14.1046 7 15 6.10457 15 5M9 5C9 3.89543 9.89543 3 11 3H13C14.1046 3 15 3.89543 15 5M9 14L11 16L15 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val clipboard_copy = """
		<path d="M8 5H6C4.89543 5 4 5.89543 4 7V19C4 20.1046 4.89543 21 6 21H16C17.1046 21 18 20.1046 18 19V18M8 5C8 6.10457 8.89543 7 10 7H12C13.1046 7 14 6.10457 14 5M8 5C8 3.89543 8.89543 3 10 3H12C13.1046 3 14 3.89543 14 5M14 5H16C17.1046 5 18 5.89543 18 7V10M20 14H10M10 14L13 11M10 14L13 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val clipboard_list = """
		<path d="M9 5H7C5.89543 5 5 5.89543 5 7V19C5 20.1046 5.89543 21 7 21H17C18.1046 21 19 20.1046 19 19V7C19 5.89543 18.1046 5 17 5H15M9 5C9 6.10457 9.89543 7 11 7H13C14.1046 7 15 6.10457 15 5M9 5C9 3.89543 9.89543 3 11 3H13C14.1046 3 15 3.89543 15 5M12 12H15M12 16H15M9 12H9.01M9 16H9.01" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val clipboard = """
		<path d="M9 5H7C5.89543 5 5 5.89543 5 7V19C5 20.1046 5.89543 21 7 21H17C18.1046 21 19 20.1046 19 19V7C19 5.89543 18.1046 5 17 5H15M9 5C9 6.10457 9.89543 7 11 7H13C14.1046 7 15 6.10457 15 5M9 5C9 3.89543 9.89543 3 11 3H13C14.1046 3 15 3.89543 15 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val clock = """
		<path d="M12 8V12L15 15M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val cloud_download = """
		<path d="M7 16C4.79086 16 3 14.2091 3 12C3 10.0929 4.33457 8.4976 6.12071 8.09695C6.04169 7.74395 6 7.37684 6 7C6 4.23858 8.23858 2 11 2C13.4193 2 15.4373 3.71825 15.9002 6.00098C15.9334 6.00033 15.9666 6 16 6C18.7614 6 21 8.23858 21 11C21 13.419 19.2822 15.4367 17 15.9M9 19L12 22M12 22L15 19M12 22V10" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val cloud_upload = """
		<path d="M7 16C4.79086 16 3 14.2091 3 12C3 10.0929 4.33457 8.4976 6.12071 8.09695C6.04169 7.74395 6 7.37684 6 7C6 4.23858 8.23858 2 11 2C13.4193 2 15.4373 3.71825 15.9002 6.00098C15.9334 6.00033 15.9666 6 16 6C18.7614 6 21 8.23858 21 11C21 13.419 19.2822 15.4367 17 15.9M15 13L12 10M12 10L9 13M12 10L12 22" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val cloud = """
		<path d="M3 15C3 17.2091 4.79086 19 7 19H16C18.7614 19 21 16.7614 21 14C21 11.2386 18.7614 9 16 9C15.9666 9 15.9334 9.00033 15.9002 9.00098C15.4373 6.71825 13.4193 5 11 5C8.23858 5 6 7.23858 6 10C6 10.3768 6.04169 10.7439 6.12071 11.097C4.33457 11.4976 3 13.0929 3 15Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val code = """
		<path d="M10 20L14 4M18 8L22 12L18 16M6 16L2 12L6 8" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val cog = """
		<path d="M10.3246 4.31731C10.751 2.5609 13.249 2.5609 13.6754 4.31731C13.9508 5.45193 15.2507 5.99038 16.2478 5.38285C17.7913 4.44239 19.5576 6.2087 18.6172 7.75218C18.0096 8.74925 18.5481 10.0492 19.6827 10.3246C21.4391 10.751 21.4391 13.249 19.6827 13.6754C18.5481 13.9508 18.0096 15.2507 18.6172 16.2478C19.5576 17.7913 17.7913 19.5576 16.2478 18.6172C15.2507 18.0096 13.9508 18.5481 13.6754 19.6827C13.249 21.4391 10.751 21.4391 10.3246 19.6827C10.0492 18.5481 8.74926 18.0096 7.75219 18.6172C6.2087 19.5576 4.44239 17.7913 5.38285 16.2478C5.99038 15.2507 5.45193 13.9508 4.31731 13.6754C2.5609 13.249 2.5609 10.751 4.31731 10.3246C5.45193 10.0492 5.99037 8.74926 5.38285 7.75218C4.44239 6.2087 6.2087 4.44239 7.75219 5.38285C8.74926 5.99037 10.0492 5.45193 10.3246 4.31731Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M15 12C15 13.6569 13.6569 15 12 15C10.3431 15 9 13.6569 9 12C9 10.3431 10.3431 9 12 9C13.6569 9 15 10.3431 15 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val collection = """
		<path d="M19 11H5M19 11C20.1046 11 21 11.8954 21 13V19C21 20.1046 20.1046 21 19 21H5C3.89543 21 3 20.1046 3 19V13C3 11.8954 3.89543 11 5 11M19 11V9C19 7.89543 18.1046 7 17 7M5 11V9C5 7.89543 5.89543 7 7 7M7 7V5C7 3.89543 7.89543 3 9 3H15C16.1046 3 17 3.89543 17 5V7M7 7H17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val color_swatch = """
		<path d="M7 21C4.79086 21 3 19.2091 3 17V5C3 3.89543 3.89543 3 5 3H9C10.1046 3 11 3.89543 11 5V17C11 19.2091 9.20914 21 7 21ZM7 21H19C20.1046 21 21 20.1046 21 19V15C21 13.8954 20.1046 13 19 13H16.6569M11 7.34312L12.6569 5.68629C13.4379 4.90524 14.7042 4.90524 15.4853 5.68629L18.3137 8.51472C19.0948 9.29577 19.0948 10.5621 18.3137 11.3431L9.82843 19.8284M7 17H7.01" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val credit_card = """
		<path d="M3 10H21M7 15H8M12 15H13M6 19H18C19.6569 19 21 17.6569 21 16V8C21 6.34315 19.6569 5 18 5H6C4.34315 5 3 6.34315 3 8V16C3 17.6569 4.34315 19 6 19Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val cube_transparent = """
		<path d="M14 10L12 11M12 11L10 10M12 11V13.5M20 7L18 8M20 7L18 6M20 7V9.5M14 4L12 3L10 4M4 7L6 6M4 7L6 8M4 7V9.5M12 21L10 20M12 21L14 20M12 21V18.5M6 18L4 17V14.5M18 18L20 17V14.5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val cube = """
		<path d="M20 7L12 3L4 7M20 7L12 11M20 7V17L12 21M12 11L4 7M12 11V21M4 7V17L12 21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val currency_bangladeshi = """
		<path d="M11 11V9C11 7.89543 10.1046 7 9 7M11 11V15C11 16.1046 11.8954 17 13 17C14.1046 17 15 16.1046 15 15V14M11 11H9M11 11H15M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val currency_dollar = """
		<path d="M12 8C10.3431 8 9 8.89543 9 10C9 11.1046 10.3431 12 12 12C13.6569 12 15 12.8954 15 14C15 15.1046 13.6569 16 12 16M12 8C13.1104 8 14.0799 8.4022 14.5987 9M12 8V7M12 8L12 16M12 16L12 17M12 16C10.8896 16 9.92008 15.5978 9.40137 15M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val currency_euro = """
		<path d="M14.1213 15.5355C12.9497 17.4882 11.0503 17.4882 9.87868 15.5355C8.70711 13.5829 8.70711 10.4171 9.87868 8.46447C11.0503 6.51184 12.9497 6.51184 14.1213 8.46447M8 10.5H12M8 13.5H12M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val currency_pound = """
		<path d="M15 9C15 7.89543 14.1046 7 13 7C11.8954 7 11 7.89543 11 9V14C11 15.1046 10.1046 16 9 16H15M9 12H13M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val currency_rupee = """
		<path d="M9 8H15M10 8C11.6569 8 13 9.34315 13 11C13 12.6569 11.6569 14 10 14H9L12 17M9 11H15M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val currency_yen = """
		<path d="M9 8L12 13M12 13L15 8M12 13V17M9 12H15M9 15H15M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val cursor_click = """
		<path d="M14.9998 15L12.9998 20L8.99983 9L19.9998 13L14.9998 15ZM14.9998 15L19.9998 20M7.18806 2.23853L7.96452 5.1363M5.13606 7.96472L2.23828 7.18826M13.9495 4.05029L11.8282 6.17161M6.17146 11.8284L4.05014 13.9497" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val database = """
		<path d="M4 7V17C4 19.2091 7.58172 21 12 21C16.4183 21 20 19.2091 20 17V7M4 7C4 9.20914 7.58172 11 12 11C16.4183 11 20 9.20914 20 7M4 7C4 4.79086 7.58172 3 12 3C16.4183 3 20 4.79086 20 7M20 12C20 14.2091 16.4183 16 12 16C7.58172 16 4 14.2091 4 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val desktop_computer = """
		<path d="M9.75 17L9 20L8 21H16L15 20L14.25 17M3 13H21M5 17H19C20.1046 17 21 16.1046 21 15V5C21 3.89543 20.1046 3 19 3H5C3.89543 3 3 3.89543 3 5V15C3 16.1046 3.89543 17 5 17Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val device_mobile = """
		<path d="M12 18H12.01M8 21H16C17.1046 21 18 20.1046 18 19V5C18 3.89543 17.1046 3 16 3H8C6.89543 3 6 3.89543 6 5V19C6 20.1046 6.89543 21 8 21Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val device_tablet = """
		<path d="M12 18H12.01M7 21H17C18.1046 21 19 20.1046 19 19V5C19 3.89543 18.1046 3 17 3H7C5.89543 3 5 3.89543 5 5V19C5 20.1046 5.89543 21 7 21Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val document_add = """
		<path d="M9 13H15M12 10L12 16M17 21H7C5.89543 21 5 20.1046 5 19V5C5 3.89543 5.89543 3 7 3H12.5858C12.851 3 13.1054 3.10536 13.2929 3.29289L18.7071 8.70711C18.8946 8.89464 19 9.149 19 9.41421V19C19 20.1046 18.1046 21 17 21Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val document_download = """
		<path d="M12 10V16M12 16L9 13M12 16L15 13M17 21H7C5.89543 21 5 20.1046 5 19V5C5 3.89543 5.89543 3 7 3H12.5858C12.851 3 13.1054 3.10536 13.2929 3.29289L18.7071 8.70711C18.8946 8.89464 19 9.149 19 9.41421V19C19 20.1046 18.1046 21 17 21Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val document_duplicate = """
		<path d="M8 7V15C8 16.1046 8.89543 17 10 17H16M8 7V5C8 3.89543 8.89543 3 10 3H14.5858C14.851 3 15.1054 3.10536 15.2929 3.29289L19.7071 7.70711C19.8946 7.89464 20 8.149 20 8.41421V15C20 16.1046 19.1046 17 18 17H16M8 7H6C4.89543 7 4 7.89543 4 9V19C4 20.1046 4.89543 21 6 21H14C15.1046 21 16 20.1046 16 19V17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val document_remove = """
		<path d="M9 13H15M17 21H7C5.89543 21 5 20.1046 5 19V5C5 3.89543 5.89543 3 7 3H12.5858C12.851 3 13.1054 3.10536 13.2929 3.29289L18.7071 8.70711C18.8946 8.89464 19 9.149 19 9.41421V19C19 20.1046 18.1046 21 17 21Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val document_report = """
		<path d="M9 17V15M12 17V13M15 17V11M17 21H7C5.89543 21 5 20.1046 5 19V5C5 3.89543 5.89543 3 7 3H12.5858C12.851 3 13.1054 3.10536 13.2929 3.29289L18.7071 8.70711C18.8946 8.89464 19 9.149 19 9.41421V19C19 20.1046 18.1046 21 17 21Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val document_search = """
		<path d="M10 21H17C18.1046 21 19 20.1046 19 19V9.41421C19 9.149 18.8946 8.89464 18.7071 8.70711L13.2929 3.29289C13.1054 3.10536 12.851 3 12.5858 3H7C5.89543 3 5 3.89543 5 5V16M5 21L9.87868 16.1213M9.87868 16.1213C10.4216 16.6642 11.1716 17 12 17C13.6569 17 15 15.6569 15 14C15 12.3431 13.6569 11 12 11C10.3431 11 9 12.3431 9 14C9 14.8284 9.33579 15.5784 9.87868 16.1213Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val document_text = """
		<path d="M9 12H15M9 16H15M17 21H7C5.89543 21 5 20.1046 5 19V5C5 3.89543 5.89543 3 7 3H12.5858C12.851 3 13.1054 3.10536 13.2929 3.29289L18.7071 8.70711C18.8946 8.89464 19 9.149 19 9.41421V19C19 20.1046 18.1046 21 17 21Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val document = """
		<path d="M7 21H17C18.1046 21 19 20.1046 19 19V9.41421C19 9.149 18.8946 8.89464 18.7071 8.70711L13.2929 3.29289C13.1054 3.10536 12.851 3 12.5858 3H7C5.89543 3 5 3.89543 5 5V19C5 20.1046 5.89543 21 7 21Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val dots_circle_horizontal = """
		<path d="M8 12H8.01M12 12H12.01M16 12H16.01M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val dots_horizontal = """
		<path d="M5 12H5.01M12 12H12.01M19 12H19.01M6 12C6 12.5523 5.55228 13 5 13C4.44772 13 4 12.5523 4 12C4 11.4477 4.44772 11 5 11C5.55228 11 6 11.4477 6 12ZM13 12C13 12.5523 12.5523 13 12 13C11.4477 13 11 12.5523 11 12C11 11.4477 11.4477 11 12 11C12.5523 11 13 11.4477 13 12ZM20 12C20 12.5523 19.5523 13 19 13C18.4477 13 18 12.5523 18 12C18 11.4477 18.4477 11 19 11C19.5523 11 20 11.4477 20 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val dots_vertical = """
		<path d="M12 5L12 5.01M12 12L12 12.01M12 19L12 19.01M12 6C11.4477 6 11 5.55228 11 5C11 4.44772 11.4477 4 12 4C12.5523 4 13 4.44772 13 5C13 5.55228 12.5523 6 12 6ZM12 13C11.4477 13 11 12.5523 11 12C11 11.4477 11.4477 11 12 11C12.5523 11 13 11.4477 13 12C13 12.5523 12.5523 13 12 13ZM12 20C11.4477 20 11 19.5523 11 19C11 18.4477 11.4477 18 12 18C12.5523 18 13 18.4477 13 19C13 19.5523 12.5523 20 12 20Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val download = """
		<path d="M4 16L4 17C4 18.6569 5.34315 20 7 20L17 20C18.6569 20 20 18.6569 20 17L20 16M16 12L12 16M12 16L8 12M12 16L12 4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val duplicate = """
		<path d="M8 16H6C4.89543 16 4 15.1046 4 14V6C4 4.89543 4.89543 4 6 4H14C15.1046 4 16 4.89543 16 6V8M10 20H18C19.1046 20 20 19.1046 20 18V10C20 8.89543 19.1046 8 18 8H10C8.89543 8 8 8.89543 8 10V18C8 19.1046 8.89543 20 10 20Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val emoji_happy = """
		<path d="M14.8284 14.8284C13.2663 16.3905 10.7337 16.3905 9.17157 14.8284M9 10H9.01M15 10H15.01M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val emoji_sad = """
		<path d="M9.17163 16.1716C10.7337 14.6095 13.2664 14.6095 14.8285 16.1716M9 10H9.01M15 10H15.01M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val exclamation_circle = """
		<path d="M12 8V12M12 16H12.01M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val exclamation = """
		<path d="M12 9V11M12 15H12.01M5.07183 19H18.9282C20.4678 19 21.4301 17.3333 20.6603 16L13.7321 4C12.9623 2.66667 11.0378 2.66667 10.268 4L3.33978 16C2.56998 17.3333 3.53223 19 5.07183 19Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val external_link = """
		<path d="M10 6H6C4.89543 6 4 6.89543 4 8V18C4 19.1046 4.89543 20 6 20H16C17.1046 20 18 19.1046 18 18V14M14 4H20M20 4V10M20 4L10 14" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val eye_off = """
		<path d="M13.8749 18.8246C13.2677 18.9398 12.6411 19 12.0005 19C7.52281 19 3.73251 16.0571 2.45825 12C2.80515 10.8955 3.33851 9.87361 4.02143 8.97118M9.87868 9.87868C10.4216 9.33579 11.1716 9 12 9C13.6569 9 15 10.3431 15 12C15 12.8284 14.6642 13.5784 14.1213 14.1213M9.87868 9.87868L14.1213 14.1213M9.87868 9.87868L6.58916 6.58916M14.1213 14.1213L17.4112 17.4112M3 3L6.58916 6.58916M6.58916 6.58916C8.14898 5.58354 10.0066 5 12.0004 5C16.4781 5 20.2684 7.94291 21.5426 12C20.8357 14.2507 19.3545 16.1585 17.4112 17.4112M17.4112 17.4112L21 21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val eye = """
		<path d="M14.9998 12C14.9998 13.6569 13.6566 15 11.9998 15C10.3429 15 8.99976 13.6569 8.99976 12C8.99976 10.3431 10.3429 9 11.9998 9C13.6566 9 14.9998 10.3431 14.9998 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M2.45801 12C3.73228 7.94288 7.52257 5 12.0002 5C16.4778 5 20.2681 7.94291 21.5424 12C20.2681 16.0571 16.4778 19 12.0002 19C7.52256 19 3.73226 16.0571 2.45801 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val fast_forward = """
		<path d="M11.9333 12.8C12.4667 12.4 12.4667 11.6 11.9333 11.2L6.6 7.19998C5.94076 6.70556 5 7.17594 5 7.99998L5 16C5 16.824 5.94076 17.2944 6.6 16.8L11.9333 12.8Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M19.9333 12.8C20.4667 12.4 20.4667 11.6 19.9333 11.2L14.6 7.19998C13.9408 6.70556 13 7.17594 13 7.99998L13 16C13 16.824 13.9408 17.2944 14.6 16.8L19.9333 12.8Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val film = """
		<path d="M7 4V20M17 4V20M3 8H7M17 8H21M3 12H21M3 16H7M17 16H21M4 20H20C20.5523 20 21 19.5523 21 19V5C21 4.44772 20.5523 4 20 4H4C3.44772 4 3 4.44772 3 5V19C3 19.5523 3.44772 20 4 20Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val filter = """
		<path d="M3 4C3 3.44772 3.44772 3 4 3H20C20.5523 3 21 3.44772 21 4V6.58579C21 6.851 20.8946 7.10536 20.7071 7.29289L14.2929 13.7071C14.1054 13.8946 14 14.149 14 14.4142V17L10 21V14.4142C10 14.149 9.89464 13.8946 9.70711 13.7071L3.29289 7.29289C3.10536 7.10536 3 6.851 3 6.58579V4Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val finger_print = """
		<path d="M11.9999 11C11.9999 14.5172 10.9911 17.7988 9.24707 20.5712M5.80688 18.5304C5.82459 18.5005 5.84273 18.4709 5.8613 18.4413C7.2158 16.2881 7.99991 13.7418 7.99991 11C7.99991 8.79086 9.79077 7 11.9999 7C14.209 7 15.9999 8.79086 15.9999 11C15.9999 12.017 15.9307 13.0186 15.7966 14M13.6792 20.8436C14.2909 19.6226 14.7924 18.3369 15.1707 17M19.0097 18.132C19.6547 15.8657 20 13.4732 20 11C20 6.58172 16.4183 3 12 3C10.5429 3 9.17669 3.38958 8 4.07026M3 15.3641C3.64066 14.0454 4 12.5646 4 11C4 9.54285 4.38958 8.17669 5.07026 7" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val fire = """
		<path d="M17.6569 18.6568C14.5327 21.781 9.46734 21.781 6.34315 18.6568C4.78105 17.0947 4 15.0474 4 13C4 10.9526 4.78105 8.90523 6.34315 7.34313C6.34315 7.34313 7.00004 8.99995 9.00004 9.99995C9.00004 7.99995 9.50004 4.99996 11.9859 3C14 5 16.0912 5.77745 17.6569 7.34313C19.219 8.90523 20 10.9526 20 13C20 15.0474 19.2189 17.0947 17.6569 18.6568Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M9.87868 16.1213C11.0503 17.2928 12.9497 17.2928 14.1213 16.1213C14.7071 15.5355 15 14.7677 15 14C15 13.2322 14.7071 12.4644 14.1213 11.8786C13.5392 11.2965 12.7775 11.0037 12.0146 11L10.9999 13.9999L9 14C9.00001 14.7677 9.2929 15.5355 9.87868 16.1213Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val flag = """
		<path d="M3 21V17M3 17V5C3 3.89543 3.89543 3 5 3H11.5L12.5 4H21L18 10L21 16H12.5L11.5 15H5C3.89543 15 3 15.8954 3 17ZM12 3.5V9" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val folder_add = """
		<path d="M9 13H15M12 10V16M3 17V7C3 5.89543 3.89543 5 5 5H11L13 7H19C20.1046 7 21 7.89543 21 9V17C21 18.1046 20.1046 19 19 19H5C3.89543 19 3 18.1046 3 17Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val folder_download = """
		<path d="M12 10V16M12 16L9 13M12 16L15 13M3 17V7C3 5.89543 3.89543 5 5 5H11L13 7H19C20.1046 7 21 7.89543 21 9V17C21 18.1046 20.1046 19 19 19H5C3.89543 19 3 18.1046 3 17Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val folder_open = """
		<path d="M5 19C3.89543 19 3 18.1046 3 17V7C3 5.89543 3.89543 5 5 5H9L11 7H15C16.1046 7 17 7.89543 17 9V10M5 19H19C20.1046 19 21 18.1046 21 17V12C21 10.8954 20.1046 10 19 10H9C7.89543 10 7 10.8954 7 12V17C7 18.1046 6.10457 19 5 19Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val folder_remove = """
		<path d="M9 13H15M3 17V7C3 5.89543 3.89543 5 5 5H11L13 7H19C20.1046 7 21 7.89543 21 9V17C21 18.1046 20.1046 19 19 19H5C3.89543 19 3 18.1046 3 17Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val folder = """
		<path d="M3 7V17C3 18.1046 3.89543 19 5 19H19C20.1046 19 21 18.1046 21 17V9C21 7.89543 20.1046 7 19 7H13L11 5H5C3.89543 5 3 5.89543 3 7Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val gift = """
		<path d="M12 8V21M12 8C12 8 12 6.50722 12 6C12 4.89543 12.8954 4 14 4C15.1046 4 16 4.89543 16 6C16 7.10457 15.1046 8 14 8C13.4027 8 12 8 12 8ZM12 8C12 8 12 6.06291 12 5.5C12 4.11929 10.8807 3 9.5 3C8.11929 3 7 4.11929 7 5.5C7 6.88071 8.11929 8 9.5 8C10.3178 8 12 8 12 8ZM5 12H19M5 12C3.89543 12 3 11.1046 3 10C3 8.89543 3.89543 8 5 8H19C20.1046 8 21 8.89543 21 10C21 11.1046 20.1046 12 19 12M5 12L5 19C5 20.1046 5.89543 21 7 21H17C18.1046 21 19 20.1046 19 19V12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val globe_alt = """
		<path d="M21 12C21 16.9706 16.9706 21 12 21M21 12C21 7.02944 16.9706 3 12 3M21 12H3M12 21C7.02944 21 3 16.9706 3 12M12 21C13.6569 21 15 16.9706 15 12C15 7.02944 13.6569 3 12 3M12 21C10.3431 21 9 16.9706 9 12C9 7.02944 10.3431 3 12 3M3 12C3 7.02944 7.02944 3 12 3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val globe = """
		<path d="M3.05493 11H5C6.10457 11 7 11.8954 7 13V14C7 15.1046 7.89543 16 9 16C10.1046 16 11 16.8954 11 18V20.9451M8 3.93552V5.5C8 6.88071 9.11929 8 10.5 8H11C12.1046 8 13 8.89543 13 10C13 11.1046 13.8954 12 15 12C16.1046 12 17 11.1046 17 10C17 8.89543 17.8954 8 19 8L20.0645 8M15 20.4879V18C15 16.8954 15.8954 16 17 16H20.0645M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val hand = """
		<path d="M7 11.5V14M7 11.5V5.5C7 4.67157 7.67157 4 8.5 4C9.32843 4 10 4.67157 10 5.5M7 11.5C7 10.6716 6.32843 10 5.5 10C4.67157 10 4 10.6716 4 11.5V13.5C4 17.6421 7.35786 21 11.5 21C15.6421 21 19 17.6421 19 13.5V8.5C19 7.67157 18.3284 7 17.5 7C16.6716 7 16 7.67157 16 8.5M10 5.5V11M10 5.5V4.5C10 3.67157 10.6716 3 11.5 3C12.3284 3 13 3.67157 13 4.5V5.5M13 5.5V11M13 5.5C13 4.67157 13.6716 4 14.5 4C15.3284 4 16 4.67157 16 5.5V8.5M16 8.5V11" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val hashtag = """
		<path d="M7 20L11 4M13 20L17 4M6 9H20M4 15H18" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val heart = """
		<path d="M4.31802 6.31802C2.56066 8.07538 2.56066 10.9246 4.31802 12.682L12.0001 20.364L19.682 12.682C21.4393 10.9246 21.4393 8.07538 19.682 6.31802C17.9246 4.56066 15.0754 4.56066 13.318 6.31802L12.0001 7.63609L10.682 6.31802C8.92462 4.56066 6.07538 4.56066 4.31802 6.31802Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val home = """
		<path d="M3 12L5 10M5 10L12 3L19 10M5 10V20C5 20.5523 5.44772 21 6 21H9M19 10L21 12M19 10V20C19 20.5523 18.5523 21 18 21H15M9 21C9.55228 21 10 20.5523 10 20V16C10 15.4477 10.4477 15 11 15H13C13.5523 15 14 15.4477 14 16V20C14 20.5523 14.4477 21 15 21M9 21H15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val identification = """
		<path d="M10 6H5C3.89543 6 3 6.89543 3 8V17C3 18.1046 3.89543 19 5 19H19C20.1046 19 21 18.1046 21 17V8C21 6.89543 20.1046 6 19 6H14M10 6V5C10 3.89543 10.8954 3 12 3C13.1046 3 14 3.89543 14 5V6M10 6C10 7.10457 10.8954 8 12 8C13.1046 8 14 7.10457 14 6M9 14C10.1046 14 11 13.1046 11 12C11 10.8954 10.1046 10 9 10C7.89543 10 7 10.8954 7 12C7 13.1046 7.89543 14 9 14ZM9 14C10.3062 14 11.4174 14.8348 11.8292 16M9 14C7.69378 14 6.58249 14.8348 6.17065 16M15 11H18M15 15H17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val inbox_in = """
		<path d="M8 4H6C4.89543 4 4 4.89543 4 6V18C4 19.1046 4.89543 20 6 20H18C19.1046 20 20 19.1046 20 18V6C20 4.89543 19.1046 4 18 4H16M12 3V11M12 11L15 8M12 11L9 8M4 13H6.58579C6.851 13 7.10536 13.1054 7.29289 13.2929L9.70711 15.7071C9.89464 15.8946 10.149 16 10.4142 16H13.5858C13.851 16 14.1054 15.8946 14.2929 15.7071L16.7071 13.2929C16.8946 13.1054 17.149 13 17.4142 13H20" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val inbox = """
		<path d="M20 13V6C20 4.89543 19.1046 4 18 4H6C4.89543 4 4 4.89543 4 6V13M20 13V18C20 19.1046 19.1046 20 18 20H6C4.89543 20 4 19.1046 4 18V13M20 13H17.4142C17.149 13 16.8946 13.1054 16.7071 13.2929L14.2929 15.7071C14.1054 15.8946 13.851 16 13.5858 16H10.4142C10.149 16 9.89464 15.8946 9.70711 15.7071L7.29289 13.2929C7.10536 13.1054 6.851 13 6.58579 13H4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val information_circle = """
		<path d="M13 16H12V12H11M12 8H12.01M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val key = """
		<path d="M15 7C16.1046 7 17 7.89543 17 9M21 9C21 12.3137 18.3137 15 15 15C14.3938 15 13.8087 14.9101 13.2571 14.7429L11 17H9V19H7V21H4C3.44772 21 3 20.5523 3 20V17.4142C3 17.149 3.10536 16.8946 3.29289 16.7071L9.25707 10.7429C9.08989 10.1914 9 9.60617 9 9C9 5.68629 11.6863 3 15 3C18.3137 3 21 5.68629 21 9Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val library = """
		<path d="M8 14V17M12 14V17M16 14V17M3 21H21M3 10H21M3 7L12 3L21 7M4 10H20V21H4V10Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val light_bulb = """
		<path d="M9.66347 17H14.3364M11.9999 3V4M18.3639 5.63604L17.6568 6.34315M21 11.9999H20M4 11.9999H3M6.34309 6.34315L5.63599 5.63604M8.46441 15.5356C6.51179 13.5829 6.51179 10.4171 8.46441 8.46449C10.417 6.51187 13.5829 6.51187 15.5355 8.46449C17.4881 10.4171 17.4881 13.5829 15.5355 15.5356L14.9884 16.0827C14.3555 16.7155 13.9999 17.5739 13.9999 18.469V19C13.9999 20.1046 13.1045 21 11.9999 21C10.8954 21 9.99995 20.1046 9.99995 19V18.469C9.99995 17.5739 9.6444 16.7155 9.01151 16.0827L8.46441 15.5356Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val lightning_bolt = """
		<path d="M13 10V3L4 14H11L11 21L20 10L13 10Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val link = """
		<path d="M13.8284 10.1716C12.2663 8.60948 9.73367 8.60948 8.17157 10.1716L4.17157 14.1716C2.60948 15.7337 2.60948 18.2663 4.17157 19.8284C5.73367 21.3905 8.26633 21.3905 9.82843 19.8284L10.93 18.7269M10.1716 13.8284C11.7337 15.3905 14.2663 15.3905 15.8284 13.8284L19.8284 9.82843C21.3905 8.26633 21.3905 5.73367 19.8284 4.17157C18.2663 2.60948 15.7337 2.60948 14.1716 4.17157L13.072 5.27118" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val location_marker = """
		<path d="M17.6569 16.6569C16.7202 17.5935 14.7616 19.5521 13.4138 20.8999C12.6327 21.681 11.3677 21.6814 10.5866 20.9003C9.26234 19.576 7.34159 17.6553 6.34315 16.6569C3.21895 13.5327 3.21895 8.46734 6.34315 5.34315C9.46734 2.21895 14.5327 2.21895 17.6569 5.34315C20.781 8.46734 20.781 13.5327 17.6569 16.6569Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M15 11C15 12.6569 13.6569 14 12 14C10.3431 14 9 12.6569 9 11C9 9.34315 10.3431 8 12 8C13.6569 8 15 9.34315 15 11Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val lock_closed = """
		<path d="M12 15V17M6 21H18C19.1046 21 20 20.1046 20 19V13C20 11.8954 19.1046 11 18 11H6C4.89543 11 4 11.8954 4 13V19C4 20.1046 4.89543 21 6 21ZM16 11V7C16 4.79086 14.2091 3 12 3C9.79086 3 8 4.79086 8 7V11H16Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val lock_open = """
		<path d="M8 11V7C8 4.79086 9.79086 3 12 3C14.2091 3 16 4.79086 16 7M12 15V17M6 21H18C19.1046 21 20 20.1046 20 19V13C20 11.8954 19.1046 11 18 11H6C4.89543 11 4 11.8954 4 13V19C4 20.1046 4.89543 21 6 21Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val login = """
		<path d="M11 16L7 12M7 12L11 8M7 12L21 12M16 16V17C16 18.6569 14.6569 20 13 20H6C4.34315 20 3 18.6569 3 17V7C3 5.34315 4.34315 4 6 4H13C14.6569 4 16 5.34315 16 7V8" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val logout = """
		<path d="M17 16L21 12M21 12L17 8M21 12L7 12M13 16V17C13 18.6569 11.6569 20 10 20H6C4.34315 20 3 18.6569 3 17V7C3 5.34315 4.34315 4 6 4H10C11.6569 4 13 5.34315 13 7V8" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val mail_open = """
		<path d="M3 19V10.0704C3 9.40165 3.3342 8.77718 3.8906 8.40625L10.8906 3.73959C11.5624 3.29172 12.4376 3.29172 13.1094 3.73959L20.1094 8.40625C20.6658 8.77718 21 9.40165 21 10.0704V19M3 19C3 20.1046 3.89543 21 5 21H19C20.1046 21 21 20.1046 21 19M3 19L9.75 14.5M21 19L14.25 14.5M3 9.99999L9.75 14.5M21 9.99999L14.25 14.5M14.25 14.5L13.1094 15.2604C12.4376 15.7083 11.5624 15.7083 10.8906 15.2604L9.75 14.5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val mail = """
		<path d="M3 8L10.8906 13.2604C11.5624 13.7083 12.4376 13.7083 13.1094 13.2604L21 8M5 19H19C20.1046 19 21 18.1046 21 17V7C21 5.89543 20.1046 5 19 5H5C3.89543 5 3 5.89543 3 7V17C3 18.1046 3.89543 19 5 19Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val map = """
		<path d="M9 20L3.55279 17.2764C3.214 17.107 3 16.7607 3 16.382V5.61803C3 4.87465 3.78231 4.39116 4.44721 4.72361L9 7M9 20L15 17M9 20V7M15 17L19.5528 19.2764C20.2177 19.6088 21 19.1253 21 18.382V7.61803C21 7.23926 20.786 6.893 20.4472 6.72361L15 4M15 17V4M15 4L9 7" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val menu_alt_1 = """
		<path d="M4 6H20M4 12H12M4 18H20" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val menu_alt_2 = """
		<path d="M4 6H20M4 12H20M4 18H11" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val menu_alt_3 = """
		<path d="M4 6H20M4 12H20M13 18H20" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val menu_alt_4 = """
		<path d="M4 8H20M4 16H20" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val menu = """
		<path d="M4 6H20M4 12H20M4 18H20" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val microphone = """
		<path d="M19 11C19 14.866 15.866 18 12 18M12 18C8.13401 18 5 14.866 5 11M12 18V22M12 22H8M12 22H16M12 14C10.3431 14 9 12.6569 9 11V5C9 3.34315 10.3431 2 12 2C13.6569 2 15 3.34315 15 5V11C15 12.6569 13.6569 14 12 14Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val minus_circle = """
		<path d="M15 12H9M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val minus_sm = """
		<path d="M18 12H6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val minus = """
		<path d="M20 12H4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val moon = """
		<path d="M20.3542 15.3542C19.3176 15.7708 18.1856 16.0001 17 16.0001C12.0294 16.0001 8 11.9706 8 7.00006C8 5.81449 8.22924 4.68246 8.64581 3.64587C5.33648 4.9758 3 8.21507 3 12.0001C3 16.9706 7.02944 21.0001 12 21.0001C15.785 21.0001 19.0243 18.6636 20.3542 15.3542Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val music_note = """
		<path d="M9 19V6L21 3V16M9 19C9 20.1046 7.65685 21 6 21C4.34315 21 3 20.1046 3 19C3 17.8954 4.34315 17 6 17C7.65685 17 9 17.8954 9 19ZM21 16C21 17.1046 19.6569 18 18 18C16.3431 18 15 17.1046 15 16C15 14.8954 16.3431 14 18 14C19.6569 14 21 14.8954 21 16ZM9 10L21 7" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val newspaper = """
		<path d="M19 20H5C3.89543 20 3 19.1046 3 18L3 6C3 4.89543 3.89543 4 5 4L15 4C16.1046 4 17 4.89543 17 6V7M19 20C17.8954 20 17 19.1046 17 18L17 7M19 20C20.1046 20 21 19.1046 21 18V9C21 7.89543 20.1046 7 19 7L17 7M13 4L9 4M7 16H13M7 8H13V12H7V8Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val office_building = """
		<path d="M19 21V5C19 3.89543 18.1046 3 17 3H7C5.89543 3 5 3.89543 5 5V21M19 21L21 21M19 21H14M5 21L3 21M5 21H10M9 6.99998H10M9 11H10M14 6.99998H15M14 11H15M10 21V16C10 15.4477 10.4477 15 11 15H13C13.5523 15 14 15.4477 14 16V21M10 21H14" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val paper_airplane = """
		<path d="M12 19L21 21L12 3L3 21L12 19ZM12 19V11" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val paper_clip = """
		<path d="M15.1716 7L8.58579 13.5858C7.80474 14.3668 7.80474 15.6332 8.58579 16.4142C9.36684 17.1953 10.6332 17.1953 11.4142 16.4142L17.8284 9.82843C19.3905 8.26633 19.3905 5.73367 17.8284 4.17157C16.2663 2.60948 13.7337 2.60948 12.1716 4.17157L5.75736 10.7574C3.41421 13.1005 3.41421 16.8995 5.75736 19.2426C8.1005 21.5858 11.8995 21.5858 14.2426 19.2426L20.5 13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val pause = """
		<path d="M10 9V15M14 9V15M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val pencil_alt = """
		<path d="M11 5H6C4.89543 5 4 5.89543 4 7V18C4 19.1046 4.89543 20 6 20H17C18.1046 20 19 19.1046 19 18V13M17.5858 3.58579C18.3668 2.80474 19.6332 2.80474 20.4142 3.58579C21.1953 4.36683 21.1953 5.63316 20.4142 6.41421L11.8284 15H9L9 12.1716L17.5858 3.58579Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val pencil = """
		<path d="M15.2322 5.23223L18.7677 8.76777M16.7322 3.73223C17.7085 2.75592 19.2914 2.75592 20.2677 3.73223C21.244 4.70854 21.244 6.29146 20.2677 7.26777L6.5 21.0355H3V17.4644L16.7322 3.73223Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val phone_incoming = """
		<path d="M21 3L15 9M15 9V4M15 9H20M5 3C3.89543 3 3 3.89543 3 5V6C3 14.2843 9.71573 21 18 21H19C20.1046 21 21 20.1046 21 19V15.7208C21 15.2903 20.7246 14.9082 20.3162 14.7721L15.8228 13.2743C15.3507 13.1169 14.8347 13.3306 14.6121 13.7757L13.4835 16.033C11.0388 14.9308 9.06925 12.9612 7.96701 10.5165L10.2243 9.38787C10.6694 9.16531 10.8831 8.64932 10.7257 8.17721L9.22792 3.68377C9.09181 3.27543 8.70967 3 8.27924 3H5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val phone_missed_call = """
		<path d="M16 8L18 6M18 6L20 4M18 6L16 4M18 6L20 8M5 3C3.89543 3 3 3.89543 3 5V6C3 14.2843 9.71573 21 18 21H19C20.1046 21 21 20.1046 21 19V15.7208C21 15.2903 20.7246 14.9082 20.3162 14.7721L15.8228 13.2743C15.3507 13.1169 14.8347 13.3306 14.6121 13.7757L13.4835 16.033C11.0388 14.9308 9.06925 12.9612 7.96701 10.5165L10.2243 9.38787C10.6694 9.16531 10.8831 8.64932 10.7257 8.17721L9.22792 3.68377C9.09181 3.27543 8.70967 3 8.27924 3H5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val phone_outgoing = """
		<path d="M16 3H21M21 3V8M21 3L15 9M5 3C3.89543 3 3 3.89543 3 5V6C3 14.2843 9.71573 21 18 21H19C20.1046 21 21 20.1046 21 19V15.7208C21 15.2903 20.7246 14.9082 20.3162 14.7721L15.8228 13.2743C15.3507 13.1169 14.8347 13.3306 14.6121 13.7757L13.4835 16.033C11.0388 14.9308 9.06925 12.9612 7.96701 10.5165L10.2243 9.38787C10.6694 9.16531 10.8831 8.64932 10.7257 8.17721L9.22792 3.68377C9.09181 3.27543 8.70967 3 8.27924 3H5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val phone = """
		<path d="M3 5C3 3.89543 3.89543 3 5 3H8.27924C8.70967 3 9.09181 3.27543 9.22792 3.68377L10.7257 8.17721C10.8831 8.64932 10.6694 9.16531 10.2243 9.38787L7.96701 10.5165C9.06925 12.9612 11.0388 14.9308 13.4835 16.033L14.6121 13.7757C14.8347 13.3306 15.3507 13.1169 15.8228 13.2743L20.3162 14.7721C20.7246 14.9082 21 15.2903 21 15.7208V19C21 20.1046 20.1046 21 19 21H18C9.71573 21 3 14.2843 3 6V5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val photograph = """
		<path d="M4 16L8.58579 11.4142C9.36683 10.6332 10.6332 10.6332 11.4142 11.4142L16 16M14 14L15.5858 12.4142C16.3668 11.6332 17.6332 11.6332 18.4142 12.4142L20 14M14 8H14.01M6 20H18C19.1046 20 20 19.1046 20 18V6C20 4.89543 19.1046 4 18 4H6C4.89543 4 4 4.89543 4 6V18C4 19.1046 4.89543 20 6 20Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val play = """
		<path d="M14.7519 11.1679L11.5547 9.03647C10.8901 8.59343 10 9.06982 10 9.86852V14.1315C10 14.9302 10.8901 15.4066 11.5547 14.9635L14.7519 12.8321C15.3457 12.4362 15.3457 11.5638 14.7519 11.1679Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val plus_circle = """
		<path d="M12 9V12M12 12V15M12 12H15M12 12H9M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val plus_sm = """
		<path d="M12 6V12M12 12V18M12 12H18M12 12L6 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val plus = """
		<path d="M12 4V20M20 12L4 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val presentation_chart_bar = """
		<path d="M8 13V12M12 13V10M16 13V8M8 21L12 17L16 21M3 4H21M4 4H20V16C20 16.5523 19.5523 17 19 17H5C4.44772 17 4 16.5523 4 16V4Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val presentation_chart_line = """
		<path d="M7 12L10 9L13 12L17 8M8 21L12 17L16 21M3 4H21M4 4H20V16C20 16.5523 19.5523 17 19 17H5C4.44772 17 4 16.5523 4 16V4Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val printer = """
		<path d="M17 17H19C20.1046 17 21 16.1046 21 15V11C21 9.89543 20.1046 9 19 9H5C3.89543 9 3 9.89543 3 11V15C3 16.1046 3.89543 17 5 17H7M9 21H15C16.1046 21 17 20.1046 17 19V15C17 13.8954 16.1046 13 15 13H9C7.89543 13 7 13.8954 7 15V19C7 20.1046 7.89543 21 9 21ZM17 9V5C17 3.89543 16.1046 3 15 3H9C7.89543 3 7 3.89543 7 5V9H17Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val puzzle = """
		<path d="M11 4C11 2.89543 11.8954 2 13 2C14.1046 2 15 2.89543 15 4V5C15 5.55228 15.4477 6 16 6H19C19.5523 6 20 6.44772 20 7V10C20 10.5523 19.5523 11 19 11H18C16.8954 11 16 11.8954 16 13C16 14.1046 16.8954 15 18 15H19C19.5523 15 20 15.4477 20 16V19C20 19.5523 19.5523 20 19 20H16C15.4477 20 15 19.5523 15 19V18C15 16.8954 14.1046 16 13 16C11.8954 16 11 16.8954 11 18V19C11 19.5523 10.5523 20 10 20H7C6.44772 20 6 19.5523 6 19V16C6 15.4477 5.55228 15 5 15H4C2.89543 15 2 14.1046 2 13C2 11.8954 2.89543 11 4 11H5C5.55228 11 6 10.5523 6 10V7C6 6.44772 6.44772 6 7 6H10C10.5523 6 11 5.55228 11 5V4Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val qrcode = """
		<path d="M12 4V5M18 16H20M14 16H12V20M12 9V12M12 12H12.01M12 12H16.01M16 20H20M4 12H8M20 12H20.01M5 8H7C7.55228 8 8 7.55228 8 7V5C8 4.44772 7.55228 4 7 4H5C4.44772 4 4 4.44772 4 5V7C4 7.55228 4.44772 8 5 8ZM17 8H19C19.5523 8 20 7.55228 20 7V5C20 4.44772 19.5523 4 19 4H17C16.4477 4 16 4.44772 16 5V7C16 7.55228 16.4477 8 17 8ZM5 20H7C7.55228 20 8 19.5523 8 19V17C8 16.4477 7.55228 16 7 16H5C4.44772 16 4 16.4477 4 17V19C4 19.5523 4.44772 20 5 20Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val question_mark_circle = """
		<path d="M8.22766 9C8.77678 7.83481 10.2584 7 12.0001 7C14.2092 7 16.0001 8.34315 16.0001 10C16.0001 11.3994 14.7224 12.5751 12.9943 12.9066C12.4519 13.0106 12.0001 13.4477 12.0001 14M12 17H12.01M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val receipt_refund = """
		<path d="M16 15V14C16 11.7909 14.2091 10 12 10H8M8 10L11 13M8 10L11 7M20 21V5C20 3.89543 19.1046 3 18 3H6C4.89543 3 4 3.89543 4 5V21L8 19L12 21L16 19L20 21Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val receipt_tax = """
		<path d="M9 14L15 8M9.50003 8.5H9.51003M14.5 13.5H14.51M19 21V5C19 3.89543 18.1046 3 17 3H7C5.89543 3 5 3.89543 5 5V21L8.5 19L12 21L15.5 19L19 21ZM10 8.5C10 8.77614 9.77614 9 9.5 9C9.22386 9 9 8.77614 9 8.5C9 8.22386 9.22386 8 9.5 8C9.77614 8 10 8.22386 10 8.5ZM15 13.5C15 13.7761 14.7761 14 14.5 14C14.2239 14 14 13.7761 14 13.5C14 13.2239 14.2239 13 14.5 13C14.7761 13 15 13.2239 15 13.5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val refresh = """
		<path d="M4 4V9H4.58152M19.9381 11C19.446 7.05369 16.0796 4 12 4C8.64262 4 5.76829 6.06817 4.58152 9M4.58152 9H9M20 20V15H19.4185M19.4185 15C18.2317 17.9318 15.3574 20 12 20C7.92038 20 4.55399 16.9463 4.06189 13M19.4185 15H15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val reply = """
		<path d="M3 10H13C17.4183 10 21 13.5817 21 18V20M3 10L9 16M3 10L9 4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val rewind = """
		<path d="M12.0665 11.2C11.5332 11.6 11.5332 12.4 12.0665 12.8L17.3998 16.8C18.0591 17.2944 18.9998 16.824 18.9998 16V7.99999C18.9998 7.17594 18.0591 6.70556 17.3998 7.19999L12.0665 11.2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M4.0665 11.2C3.53317 11.6 3.53317 12.4 4.0665 12.8L9.39984 16.8C10.0591 17.2944 10.9998 16.824 10.9998 16V7.99998C10.9998 7.17594 10.0591 6.70556 9.39984 7.19998L4.0665 11.2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val rss = """
		<path d="M6 5C13.1797 5 19 10.8203 19 18M6 11C9.86599 11 13 14.134 13 18M7 18C7 18.5523 6.55228 19 6 19C5.44772 19 5 18.5523 5 18C5 17.4477 5.44772 17 6 17C6.55228 17 7 17.4477 7 18Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val save_as = """
		<path d="M17 16V18C17 19.1046 16.1046 20 15 20H5C3.89543 20 3 19.1046 3 18V11C3 9.89543 3.89543 9 5 9H7M10 5H9C7.89543 5 7 5.89543 7 7V14C7 15.1046 7.89543 16 9 16H19C20.1046 16 21 15.1046 21 14V7C21 5.89543 20.1046 5 19 5H18M17 9L14 12M14 12L11 9M14 12L14 3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val save = """
		<path d="M8 7H5C3.89543 7 3 7.89543 3 9V18C3 19.1046 3.89543 20 5 20H19C20.1046 20 21 19.1046 21 18V9C21 7.89543 20.1046 7 19 7H16M15 11L12 14M12 14L9 11M12 14L12 4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val scale = """
		<path d="M3 6L6 7M6 7L3 16C4.77253 17.3334 7.22866 17.3334 9.00119 16M6 7L9.00006 16M6 7L12 5M18 7L21 6M18 7L15 16C16.7725 17.3334 19.2287 17.3334 21.0012 16M18 7L21.0001 16M18 7L12 5M12 3V5M12 21V5M12 21H9M12 21H15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val scissors = """
		<path d="M14.1213 14.1213L19 19M12 12L19 5M12 12L9.12132 14.8787M12 12L9.12132 9.12132M9.12132 14.8787C8.57843 14.3358 7.82843 14 7 14C5.34315 14 4 15.3431 4 17C4 18.6569 5.34315 20 7 20C8.65685 20 10 18.6569 10 17C10 16.1716 9.66421 15.4216 9.12132 14.8787ZM9.12132 9.12132C9.66421 8.57843 10 7.82843 10 7C10 5.34315 8.65685 4 7 4C5.34315 4 4 5.34315 4 7C4 8.65685 5.34315 10 7 10C7.82843 10 8.57843 9.66421 9.12132 9.12132Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val search_circle = """
		<path d="M8 16L10.8787 13.1213M10.8787 13.1213C11.4216 13.6642 12.1716 14 13 14C14.6569 14 16 12.6569 16 11C16 9.34315 14.6569 8 13 8C11.3431 8 10 9.34315 10 11C10 11.8284 10.3358 12.5784 10.8787 13.1213ZM21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val search = """
		<path d="M21 21L15 15M17 10C17 13.866 13.866 17 10 17C6.13401 17 3 13.866 3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val selector = """
		<path d="M8 9L12 5L16 9M16 15L12 19L8 15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val server = """
		<path d="M5 12H19M5 12C3.89543 12 3 11.1046 3 10V6C3 4.89543 3.89543 4 5 4H19C20.1046 4 21 4.89543 21 6V10C21 11.1046 20.1046 12 19 12M5 12C3.89543 12 3 12.8954 3 14V18C3 19.1046 3.89543 20 5 20H19C20.1046 20 21 19.1046 21 18V14C21 12.8954 20.1046 12 19 12M17 8H17.01M17 16H17.01" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val share = """
		<path d="M8.68387 13.3419C8.88616 12.9381 9 12.4824 9 12C9 11.5176 8.88616 11.0619 8.68387 10.6581M8.68387 13.3419C8.19134 14.3251 7.17449 15 6 15C4.34315 15 3 13.6569 3 12C3 10.3431 4.34315 9 6 9C7.17449 9 8.19134 9.67492 8.68387 10.6581M8.68387 13.3419L15.3161 16.6581M8.68387 10.6581L15.3161 7.34193M15.3161 7.34193C15.8087 8.32508 16.8255 9 18 9C19.6569 9 21 7.65685 21 6C21 4.34315 19.6569 3 18 3C16.3431 3 15 4.34315 15 6C15 6.48237 15.1138 6.93815 15.3161 7.34193ZM15.3161 16.6581C15.1138 17.0619 15 17.5176 15 18C15 19.6569 16.3431 21 18 21C19.6569 21 21 19.6569 21 18C21 16.3431 19.6569 15 18 15C16.8255 15 15.8087 15.6749 15.3161 16.6581Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val shield_check = """
		<path d="M9 12L11 14L15 10M20.6179 5.98434C20.4132 5.99472 20.2072 5.99997 20 5.99997C16.9265 5.99997 14.123 4.84453 11.9999 2.94434C9.87691 4.84446 7.07339 5.99985 4 5.99985C3.79277 5.99985 3.58678 5.9946 3.38213 5.98422C3.1327 6.94783 3 7.95842 3 9.00001C3 14.5915 6.82432 19.2898 12 20.622C17.1757 19.2898 21 14.5915 21 9.00001C21 7.95847 20.8673 6.94791 20.6179 5.98434Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val shield_exclamation = """
		<path d="M20.6179 5.98434C20.4132 5.99472 20.2072 5.99997 20 5.99997C16.9265 5.99997 14.123 4.84453 11.9999 2.94434C9.87691 4.84446 7.07339 5.99985 4 5.99985C3.79277 5.99985 3.58678 5.9946 3.38213 5.98422C3.1327 6.94783 3 7.95842 3 9.00001C3 14.5915 6.82432 19.2898 12 20.622C17.1757 19.2898 21 14.5915 21 9.00001C21 7.95847 20.8673 6.94791 20.6179 5.98434Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M12 9V11M12 15H12.01" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val shopping_bag = """
		<path d="M16 11V7C16 4.79086 14.2091 3 12 3C9.79086 3 8 4.79086 8 7V11M5 9H19L20 21H4L5 9Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val shopping_cart = """
		<path d="M3 3H5L5.4 5M7 13H17L21 5H5.4M7 13L5.4 5M7 13L4.70711 15.2929C4.07714 15.9229 4.52331 17 5.41421 17H17M17 17C15.8954 17 15 17.8954 15 19C15 20.1046 15.8954 21 17 21C18.1046 21 19 20.1046 19 19C19 17.8954 18.1046 17 17 17ZM9 19C9 20.1046 8.10457 21 7 21C5.89543 21 5 20.1046 5 19C5 17.8954 5.89543 17 7 17C8.10457 17 9 17.8954 9 19Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val sort_ascending = """
		<path d="M3 4H16M3 8H12M3 12H9M13 12L17 8M17 8L21 12M17 8V20" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val sort_descending = """
		<path d="M3 4H16M3 8H12M3 12H12M17 8V20M17 20L13 16M17 20L21 16" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val sparkles = """
		<path d="M5 3V7M3 5H7M6 17V21M4 19H8M13 3L15.2857 9.85714L21 12L15.2857 14.1429L13 21L10.7143 14.1429L5 12L10.7143 9.85714L13 3Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val speakerphone = """
		<path d="M11 5.88218V19.2402C11 20.2121 10.2121 21 9.24018 21C8.49646 21 7.83302 20.5325 7.58288 19.8321L5.43647 13.6829M18 13C19.6569 13 21 11.6569 21 10C21 8.34315 19.6569 7 18 7M5.43647 13.6829C4.0043 13.0741 3 11.6543 3 10C3 7.79086 4.79086 6 6.99999 6H8.83208C12.9327 6 16.4569 4.7659 18 3L18 17C16.4569 15.2341 12.9327 14 8.83208 14L6.99998 14C6.44518 14 5.91677 13.887 5.43647 13.6829Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val star = """
		<path d="M11.0489 2.92707C11.3483 2.00576 12.6517 2.00576 12.9511 2.92707L14.4697 7.60083C14.6035 8.01285 14.9875 8.29181 15.4207 8.29181H20.335C21.3037 8.29181 21.7065 9.53143 20.9228 10.1008L16.947 12.9894C16.5965 13.244 16.4499 13.6954 16.5838 14.1074L18.1024 18.7812C18.4017 19.7025 17.3472 20.4686 16.5635 19.8992L12.5878 17.0107C12.2373 16.756 11.7627 16.756 11.4122 17.0107L7.43647 19.8992C6.65276 20.4686 5.59828 19.7025 5.89763 18.7812L7.41623 14.1074C7.5501 13.6954 7.40344 13.244 7.05296 12.9894L3.07722 10.1008C2.2935 9.53143 2.69628 8.29181 3.665 8.29181H8.57929C9.01251 8.29181 9.39647 8.01285 9.53034 7.60083L11.0489 2.92707Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val status_offline = """
		<path d="M18.364 5.63605C21.8787 9.15077 21.8787 14.8493 18.364 18.364M18.364 18.364L15.5355 15.5355M18.364 18.364L21 21M15.5355 8.46447C17.4882 10.4171 17.4882 13.5829 15.5355 15.5355M15.5355 15.5355L12.7071 12.7071M8.46447 15.5355C7.66839 14.7395 7.19687 13.7417 7.04991 12.7068M5.63604 18.364C3.13732 15.8652 2.41501 12.2628 3.46913 9.12598M11.2929 11.2929C11.4739 11.1119 11.7239 11 12 11C12.5523 11 13 11.4477 13 12C13 12.2761 12.8881 12.5261 12.7071 12.7071M11.2929 11.2929L3 3M11.2929 11.2929L12.7071 12.7071" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val status_online = """
		<path d="M5.63604 18.3639C2.12132 14.8492 2.12132 9.15071 5.63604 5.63599M18.364 5.63599C21.8787 9.15071 21.8787 14.8492 18.364 18.3639M8.46447 15.5355C6.51184 13.5829 6.51184 10.417 8.46447 8.46441M15.5355 8.46441C17.4882 10.417 17.4882 13.5829 15.5355 15.5355M13 11.9999C13 12.5522 12.5523 12.9999 12 12.9999C11.4477 12.9999 11 12.5522 11 11.9999C11 11.4477 11.4477 10.9999 12 10.9999C12.5523 10.9999 13 11.4477 13 11.9999Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val stop = """
		<path d="M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M9 10C9 9.44772 9.44772 9 10 9H14C14.5523 9 15 9.44772 15 10V14C15 14.5523 14.5523 15 14 15H10C9.44772 15 9 14.5523 9 14V10Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val sun = """
		<path d="M12 3V4M12 20V21M21 12H20M4 12H3M18.364 18.364L17.6569 17.6569M6.34315 6.34315L5.63604 5.63604M18.364 5.63609L17.6569 6.3432M6.3432 17.6569L5.63609 18.364M16 12C16 14.2091 14.2091 16 12 16C9.79086 16 8 14.2091 8 12C8 9.79086 9.79086 8 12 8C14.2091 8 16 9.79086 16 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val support = """
		<path d="M18.364 5.63604L14.8284 9.17157M14.8284 14.8284L18.364 18.364M9.17157 9.17157L5.63604 5.63604M9.17157 14.8284L5.63604 18.364M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12ZM16 12C16 14.2091 14.2091 16 12 16C9.79086 16 8 14.2091 8 12C8 9.79086 9.79086 8 12 8C14.2091 8 16 9.79086 16 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val switch_horizontal = """
		<path d="M8 7L20 7M20 7L16 3M20 7L16 11M16 17L4 17M4 17L8 21M4 17L8 13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val switch_vertical = """
		<path d="M7 16V4M7 4L3 8M7 4L11 8M17 8V20M17 20L21 16M17 20L13 16" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val table = """
		<path d="M3 10H21M3 14H21M12 10V18M5 18H19C20.1046 18 21 17.1046 21 16V8C21 6.89543 20.1046 6 19 6H5C3.89543 6 3 6.89543 3 8V16C3 17.1046 3.89543 18 5 18Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val tag = """
		<path d="M7 7H7.01M7 3H12C12.5119 2.99999 13.0237 3.19525 13.4142 3.58579L20.4143 10.5858C21.1953 11.3668 21.1953 12.6332 20.4143 13.4142L13.4142 20.4142C12.6332 21.1953 11.3668 21.1953 10.5858 20.4142L3.58579 13.4142C3.19526 13.0237 3 12.5118 3 12V7C3 4.79086 4.79086 3 7 3Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val template = """
		<path d="M4 5C4 4.44772 4.44772 4 5 4H19C19.5523 4 20 4.44772 20 5V7C20 7.55228 19.5523 8 19 8H5C4.44772 8 4 7.55228 4 7V5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M4 13C4 12.4477 4.44772 12 5 12H11C11.5523 12 12 12.4477 12 13V19C12 19.5523 11.5523 20 11 20H5C4.44772 20 4 19.5523 4 19V13Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M16 13C16 12.4477 16.4477 12 17 12H19C19.5523 12 20 12.4477 20 13V19C20 19.5523 19.5523 20 19 20H17C16.4477 20 16 19.5523 16 19V13Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val terminal = """
		<path d="M8 9L11 12L8 15M13 15H16M5 20H19C20.1046 20 21 19.1046 21 18V6C21 4.89543 20.1046 4 19 4H5C3.89543 4 3 4.89543 3 6V18C3 19.1046 3.89543 20 5 20Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val thumb_down = """
		<path d="M9.99991 14H5.23598C3.74922 14 2.78222 12.4354 3.44712 11.1056L6.94712 4.10557C7.28591 3.42801 7.97843 3 8.73598 3H12.7537C12.9172 3 13.0801 3.02005 13.2388 3.05972L16.9999 4M9.99991 14V19C9.99991 20.1046 10.8953 21 11.9999 21H12.0954C12.5949 21 12.9999 20.595 12.9999 20.0955C12.9999 19.3812 13.2113 18.6828 13.6076 18.0885L16.9999 13V4M9.99991 14H11.9999M16.9999 4H18.9999C20.1045 4 20.9999 4.89543 20.9999 6V12C20.9999 13.1046 20.1045 14 18.9999 14H16.4999" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val thumb_up = """
		<path d="M14 10H18.7639C20.2507 10 21.2177 11.5646 20.5528 12.8944L17.0528 19.8944C16.714 20.572 16.0215 21 15.2639 21H11.2462C11.0827 21 10.9198 20.9799 10.7611 20.9403L7 20M14 10V5C14 3.89543 13.1046 3 12 3H11.9045C11.405 3 11 3.40497 11 3.90453C11 4.61883 10.7886 5.31715 10.3923 5.91149L7 11V20M14 10H12M7 20H5C3.89543 20 3 19.1046 3 18V12C3 10.8954 3.89543 10 5 10H7.5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val ticket = """
		<path d="M15 5V7M15 11V13M15 17V19M5 5C3.89543 5 3 5.89543 3 7V10C4.10457 10 5 10.8954 5 12C5 13.1046 4.10457 14 3 14V17C3 18.1046 3.89543 19 5 19H19C20.1046 19 21 18.1046 21 17V14C19.8954 14 19 13.1046 19 12C19 10.8954 19.8954 10 21 10V7C21 5.89543 20.1046 5 19 5H5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val translate = """
		<path d="M3 5H15M9 3V5M10.0482 14.5C8.52083 12.9178 7.28073 11.0565 6.41187 9M12.5 18H19.5M11 21L16 11L21 21M12.7511 5C11.7831 10.7702 8.06969 15.6095 3 18.129" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val trash = """
		<path d="M19 7L18.1327 19.1425C18.0579 20.1891 17.187 21 16.1378 21H7.86224C6.81296 21 5.94208 20.1891 5.86732 19.1425L5 7M10 11V17M14 11V17M15 7V4C15 3.44772 14.5523 3 14 3H10C9.44772 3 9 3.44772 9 4V7M4 7H20" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val trending_down = """
		<path d="M13 17H21M21 17V9M21 17L13 9L9 13L3 7" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val trending_up = """
		<path d="M13 7H21M21 7V15M21 7L13 15L9 11L3 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val truck = """
		<path d="M9 17C9 18.1046 8.10457 19 7 19C5.89543 19 5 18.1046 5 17C5 15.8954 5.89543 15 7 15C8.10457 15 9 15.8954 9 17Z"/>
		<path d="M19 17C19 18.1046 18.1046 19 17 19C15.8954 19 15 18.1046 15 17C15 15.8954 15.8954 15 17 15C18.1046 15 19 15.8954 19 17Z"/>
		<path d="M13 16V6C13 5.44772 12.5523 5 12 5H4C3.44772 5 3 5.44772 3 6V16C3 16.5523 3.44772 17 4 17H5M13 16C13 16.5523 12.5523 17 12 17H9M13 16L13 8C13 7.44772 13.4477 7 14 7H16.5858C16.851 7 17.1054 7.10536 17.2929 7.29289L20.7071 10.7071C20.8946 10.8946 21 11.149 21 11.4142V16C21 16.5523 20.5523 17 20 17H19M13 16C13 16.5523 13.4477 17 14 17H15M5 17C5 18.1046 5.89543 19 7 19C8.10457 19 9 18.1046 9 17M5 17C5 15.8954 5.89543 15 7 15C8.10457 15 9 15.8954 9 17M15 17C15 18.1046 15.8954 19 17 19C18.1046 19 19 18.1046 19 17M15 17C15 15.8954 15.8954 15 17 15C18.1046 15 19 15.8954 19 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val upload = """
		<path d="M4 16L4 17C4 18.6569 5.34315 20 7 20L17 20C18.6569 20 20 18.6569 20 17L20 16M16 8L12 4M12 4L8 8M12 4L12 16" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val user_add = """
		<path d="M18 9V12M18 12V15M18 12H21M18 12H15M13 7C13 9.20914 11.2091 11 9 11C6.79086 11 5 9.20914 5 7C5 4.79086 6.79086 3 9 3C11.2091 3 13 4.79086 13 7ZM3 20C3 16.6863 5.68629 14 9 14C12.3137 14 15 16.6863 15 20V21H3V20Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val user_circle = """
		<path d="M5.12104 17.8037C7.15267 16.6554 9.4998 16 12 16C14.5002 16 16.8473 16.6554 18.879 17.8037M15 10C15 11.6569 13.6569 13 12 13C10.3431 13 9 11.6569 9 10C9 8.34315 10.3431 7 12 7C13.6569 7 15 8.34315 15 10ZM21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val user_group = """
		<path d="M17 20H22V18C22 16.3431 20.6569 15 19 15C18.0444 15 17.1931 15.4468 16.6438 16.1429M17 20H7M17 20V18C17 17.3438 16.8736 16.717 16.6438 16.1429M7 20H2V18C2 16.3431 3.34315 15 5 15C5.95561 15 6.80686 15.4468 7.35625 16.1429M7 20V18C7 17.3438 7.12642 16.717 7.35625 16.1429M7.35625 16.1429C8.0935 14.301 9.89482 13 12 13C14.1052 13 15.9065 14.301 16.6438 16.1429M15 7C15 8.65685 13.6569 10 12 10C10.3431 10 9 8.65685 9 7C9 5.34315 10.3431 4 12 4C13.6569 4 15 5.34315 15 7ZM21 10C21 11.1046 20.1046 12 19 12C17.8954 12 17 11.1046 17 10C17 8.89543 17.8954 8 19 8C20.1046 8 21 8.89543 21 10ZM7 10C7 11.1046 6.10457 12 5 12C3.89543 12 3 11.1046 3 10C3 8.89543 3.89543 8 5 8C6.10457 8 7 8.89543 7 10Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val user_remove = """
		<path d="M13 7C13 9.20914 11.2091 11 9 11C6.79086 11 5 9.20914 5 7C5 4.79086 6.79086 3 9 3C11.2091 3 13 4.79086 13 7Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M9 14C5.68629 14 3 16.6863 3 20V21H15V20C15 16.6863 12.3137 14 9 14Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M21 12L15 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val user = """
		<path d="M16 7C16 9.20914 14.2091 11 12 11C9.79086 11 8 9.20914 8 7C8 4.79086 9.79086 3 12 3C14.2091 3 16 4.79086 16 7Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M12 14C8.13401 14 5 17.134 5 21H19C19 17.134 15.866 14 12 14Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val users = """
		<path d="M12 4.35418C12.7329 3.52375 13.8053 3 15 3C17.2091 3 19 4.79086 19 7C19 9.20914 17.2091 11 15 11C13.8053 11 12.7329 10.4762 12 9.64582M15 21H3V20C3 16.6863 5.68629 14 9 14C12.3137 14 15 16.6863 15 20V21ZM15 21H21V20C21 16.6863 18.3137 14 15 14C13.9071 14 12.8825 14.2922 12 14.8027M13 7C13 9.20914 11.2091 11 9 11C6.79086 11 5 9.20914 5 7C5 4.79086 6.79086 3 9 3C11.2091 3 13 4.79086 13 7Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val variable = """
		<path d="M4.87104 4C3.67341 6.40992 3 9.12632 3 12C3 14.8737 3.67341 17.5901 4.87104 20M19.0001 20C20.1977 17.5901 20.8711 14.8737 20.8711 12C20.8711 9.12632 20.1977 6.40992 19.0001 4M9 9H10.2457C10.6922 9 11.0846 9.29598 11.2072 9.72528L12.7928 15.2747C12.9154 15.704 13.3078 16 13.7543 16H15M16 9H15.9199C15.336 9 14.7813 9.25513 14.4014 9.69842L9.59864 15.3016C9.21868 15.7449 8.66398 16 8.08013 16H8" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val video_camera = """
		<path d="M15 10L19.5528 7.72361C20.2177 7.39116 21 7.87465 21 8.61803V15.382C21 16.1253 20.2177 16.6088 19.5528 16.2764L15 14M5 18H13C14.1046 18 15 17.1046 15 16V8C15 6.89543 14.1046 6 13 6H5C3.89543 6 3 6.89543 3 8V16C3 17.1046 3.89543 18 5 18Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val view_boards = """
		<path d="M9 17V7M9 17C9 18.1046 8.10457 19 7 19H5C3.89543 19 3 18.1046 3 17V7C3 5.89543 3.89543 5 5 5H7C8.10457 5 9 5.89543 9 7M9 17C9 18.1046 9.89543 19 11 19H13C14.1046 19 15 18.1046 15 17M9 7C9 5.89543 9.89543 5 11 5H13C14.1046 5 15 5.89543 15 7M15 17V7M15 17C15 18.1046 15.8954 19 17 19H19C20.1046 19 21 18.1046 21 17V7C21 5.89543 20.1046 5 19 5H17C15.8954 5 15 5.89543 15 7" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val view_grid_add = """
		<path d="M17 14V20M14 17H20M6 10H8C9.10457 10 10 9.10457 10 8V6C10 4.89543 9.10457 4 8 4H6C4.89543 4 4 4.89543 4 6V8C4 9.10457 4.89543 10 6 10ZM16 10H18C19.1046 10 20 9.10457 20 8V6C20 4.89543 19.1046 4 18 4H16C14.8954 4 14 4.89543 14 6V8C14 9.10457 14.8954 10 16 10ZM6 20H8C9.10457 20 10 19.1046 10 18V16C10 14.8954 9.10457 14 8 14H6C4.89543 14 4 14.8954 4 16V18C4 19.1046 4.89543 20 6 20Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val view_grid = """
		<path d="M4 6C4 4.89543 4.89543 4 6 4H8C9.10457 4 10 4.89543 10 6V8C10 9.10457 9.10457 10 8 10H6C4.89543 10 4 9.10457 4 8V6Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M14 6C14 4.89543 14.8954 4 16 4H18C19.1046 4 20 4.89543 20 6V8C20 9.10457 19.1046 10 18 10H16C14.8954 10 14 9.10457 14 8V6Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M4 16C4 14.8954 4.89543 14 6 14H8C9.10457 14 10 14.8954 10 16V18C10 19.1046 9.10457 20 8 20H6C4.89543 20 4 19.1046 4 18V16Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M14 16C14 14.8954 14.8954 14 16 14H18C19.1046 14 20 14.8954 20 16V18C20 19.1046 19.1046 20 18 20H16C14.8954 20 14 19.1046 14 18V16Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val view_list = """
		<path d="M4 6H20M4 10H20M4 14H20M4 18H20" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val volume_off = """
		<path fill-rule="evenodd" clip-rule="evenodd" d="M5.58579 15.0001H4C3.44772 15.0001 3 14.5523 3 14.0001V10.0001C3 9.44777 3.44772 9.00005 4 9.00005H5.58579L10.2929 4.29294C10.9229 3.66298 12 4.10915 12 5.00005V19.0001C12 19.891 10.9229 20.3371 10.2929 19.7072L5.58579 15.0001Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M17 14L19 12M19 12L21 10M19 12L17 10M19 12L21 14" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val volume_up = """
		<path d="M15.5355 8.46448C17.4881 10.4171 17.4881 13.5829 15.5355 15.5355M18.364 5.63599C21.8787 9.15071 21.8787 14.8492 18.364 18.3639M5.58579 15.0001H4C3.44772 15.0001 3 14.5523 3 14.0001V10.0001C3 9.44777 3.44772 9.00005 4 9.00005H5.58579L10.2929 4.29294C10.9229 3.66298 12 4.10915 12 5.00005V19.0001C12 19.891 10.9229 20.3371 10.2929 19.7072L5.58579 15.0001Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val wifi = """
		<path d="M8.11107 16.4039C10.259 14.256 13.7414 14.256 15.8892 16.4039M12.0002 20H12.0102M4.92913 12.9289C8.83437 9.02371 15.166 9.0237 19.0713 12.9289M1.39355 9.3934C7.25142 3.53553 16.7489 3.53553 22.6068 9.3934" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val x_circle = """
		<path d="M10 14L12 12M12 12L14 10M12 12L10 10M12 12L14 14M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val x = """
		<path d="M6 18L18 6M6 6L18 18" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val zoom_in = """
		<path d="M21 21L15 15M17 10C17 13.866 13.866 17 10 17C6.13401 17 3 13.866 3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M10 7V10M10 10V13M10 10H13M10 10H7" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()

    val zoom_out = """
		<path d="M21 21L15 15M17 10C17 13.866 13.866 17 10 17C6.13401 17 3 13.866 3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
		<path d="M13 10H7" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
	""".trimIndent()
}


