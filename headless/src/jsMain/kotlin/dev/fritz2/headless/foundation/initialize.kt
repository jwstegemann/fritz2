package dev.fritz2.headless.foundation
/**
 * Alias to improve the readability of the signature of the initialization parameter of a component factory function.
 *
 * ```
 * // signature without typealias:
 * fun RenderContext.myComponent(
 *      classes: String? = null,
 *      id: String? = null,
 *      init: MyComponent.() -> Unit // instead of this...
 * ): Div = InputField(MyComponent).run { render(classes, id) }
 *
 * // signature with typealias
 * fun RenderContext.myComponent(
 *      classes: String? = null,
 *      id: String? = null,
 *      init: Initializer<MyComponent> // ... use typealias
 * ): Div = InputField(MyComponent).run { render(classes, id) }
 * ```
 */
typealias Initialize<T> = T.() -> Unit
