package dev.fritz2.components

import dev.fritz2.components.appFrame.AppFrameComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.theme.Theme


/**
 * This component implements a standard responsive layout for web-apps.
 *
 * It offers the following sections
 * - sidebar with brand, navbar section and optional complementary on the left
 * - header at the top with actions section on the right
 * - main section
 * - optional navigation tablist at the bottom of main section
 *
 * The sidebar is moved off-screen on small screens and can be opened by a hamburger-button,
 * that appears at the left edge of the header.
 *
 * The component is responsible for rendering all these section at the right sizes and places
 * at different viewport-sizes and on different devices.
 *
 * You can adopt the appearance of all sections by adjusting the [Theme].
 *
 * Example:
 * ```kotlin
 * appFrame {
 *     brand {
 *         //...
 *     }
 *     navbar {
 *         //...
 *     }
 *     complementary { //optional
 *         //...
 *     }
 *     header { //optional
 *         //...
 *     }
 *     actions { //optional
 *         //...
 *     }
 *     main {
 *         //...
 *     }
 *     tablist { //optional
 *         //...
 *     }
 *}
 * ```
 *
 * @see AppFrameComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself.
 */
fun RenderContext.appFrame(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "appFrame",
    build: AppFrameComponent.() -> Unit = {}
) {
    AppFrameComponent().apply(build).render(this, styling, baseClass, id, prefix)
}
