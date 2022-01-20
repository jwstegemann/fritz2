package dev.fritz2.components

import dev.fritz2.components.data.File
import dev.fritz2.components.file.FileSelectionBaseComponent
import dev.fritz2.components.file.MultiFileSelectionComponent
//import dev.fritz2.components.file.MultiFileSelectionComponent
import dev.fritz2.components.file.SingleFileSelectionComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import kotlinx.coroutines.flow.*


/**
 * This factory generates a single file selection context.
 *
 * In there you can create a button with a label, an icon, the position of the icon and access its events.
 * For a detailed overview about the possible properties of the button component object itself, have a look at
 * [PushButtonComponent]
 *
 * The [File] function then returns a [Flow] of [File] in order
 * to combine the [Flow] directly to a fitting _handler_ which accepts a [File]:
 * ```
 * val textFileStore = object : RootStore<String>("") {
 *     val upload = handle<File> { _, file -> file.content }
 * }
 * file {
 *   accept("text/plain")
 *   encoding("utf-8")
 *   button(id = "myFile") {
 *     text("Select a file")
 *   }
 * } handledBy textFileStore.upload
 * ```
 *
 * @see FileSelectionBaseComponent
 * @see SingleFileSelectionComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [FileSelectionBaseComponent]
 * @return a [Flow] that offers the selected [File]
 */
fun RenderContext.file(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "file",
    build: FileSelectionBaseComponent.() -> Unit = {}
): Flow<File> = SingleFileSelectionComponent().apply(build).render(this, styling, baseClass, id, prefix)


/**
 * This factory generates a multiple file selection context.
 *
 * In there you can create a button with a label, an icon, the position of the icon and access its events.
 * For a detailed overview about the possible properties of the button component object itself, have a look at
 * [PushButtonComponent].
 *
 * The [File] function then returns a [Flow] of a [List] of [File]s in order
 * to combine the [Flow] directly to a fitting _handler_ which accepts a [List] of [File]s:
 * ```
 * val textFileStore = object : RootStore<List<String>>(emptyList()) {
 *   val upload = handle<List<File>>{ _, files -> files.map { it.content } }
 * }
 * files {
 *   accept("text/plain")
 *   encoding("utf-8")
 *   button(id = "myFiles") {
 *     text("Select one or more files")
 *   }
 * } handledBy textFileStore.upload
 * ```
 * @see FileSelectionBaseComponent
 * @see SingleFileSelectionComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [FileSelectionBaseComponent]
 * @return a [Flow] that offers the selected [File]
 */
fun RenderContext.files(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "file",
    build: FileSelectionBaseComponent.() -> Unit = {}
): Flow<List<File>> = MultiFileSelectionComponent().apply(build).render(this, styling, baseClass, id, prefix)
