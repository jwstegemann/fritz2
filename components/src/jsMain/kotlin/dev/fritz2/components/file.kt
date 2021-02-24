package dev.fritz2.components

import dev.fritz2.components.data.File
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.DisplayValues
import dev.fritz2.styling.params.styled
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.FileReader
import org.w3c.files.File as jsFile

/**
 * function for reading the [jsFile] to a [File]
 * with [File.content] as a [String]
 */
typealias FileReadingStrategy = (jsFile) -> Flow<File>

/**
 * This abstract class is the base _configuration_ for file inputs.
 * It has two specific implementations:
 * - [SingleFileSelectionComponent] for handling one file input
 * - [MultiFileSelectionComponent] for handling an arbitrary amount of files
 *
 * Both specific implementations only differ in their rendering implementation, but share the same configuration
 * options, like creating a [button] which has the same options like a [pushButton].
 *
 * Much more important are the _configuration_ functions. You can configure the following aspects:
 *  - the [accept](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/file#accept) property
 *  - the [FileReadingStrategy] for interpreting the content of the file
 *
 * This can be done within a functional expression that is the last parameter of the two files functions, called
 * ``build``. It offers an initialized instance of this [FileSelectionBaseComponent] class as receiver, so every mutating
 * method can be called for configuring the desired state for rendering the button.
 *
 * The following example shows the usage ([SingleFileSelectionComponent]):
 * ```
 * file {
 *   accept("application/pdf")
 *   button({
 *     background { color { info } }
 *   }) {
 *     icon { fromTheme { document } }
 *     text("Accept only pdf files")
 *   }
 * }
 * ```
 */
@ComponentMarker
abstract class FileSelectionBaseComponent {

    companion object {
        const val eventName = "loadend"
    }

    protected var accept: (Input.() -> Unit)? = null

    fun accept(value: String) {
        accept = { attr("accept", value) }
    }

    fun accept(value: Flow<String>) {
        accept = { attr("accept", value) }
    }

    val base64: FileReadingStrategy = { file ->
        callbackFlow {
            val reader = FileReader()
            val listener: (Event) -> Unit = { _ ->
                var content = reader.result.toString()
                val index = content.indexOf("base64,")
                if (index > -1) content = content.substring(index + 7)
                offer(File(file.name, file.type, file.size.toLong(), content))
            }
            reader.addEventListener(eventName, listener)
            reader.readAsDataURL(file)
            awaitClose { reader.removeEventListener(eventName, listener) }
        }
    }

    val plainText: (String) -> FileReadingStrategy = { encoding ->
        { file ->
            callbackFlow {
                val reader = FileReader()
                val listener: (Event) -> Unit = { _ ->
                    offer(File(file.name, file.type, file.size.toLong(), reader.result.toString()))
                }
                reader.addEventListener(eventName, listener)
                reader.readAsText(file, encoding)
                awaitClose { reader.removeEventListener(eventName, listener) }
            }
        }
    }

    val fileReadingStrategy = ComponentProperty<FileSelectionBaseComponent.() -> FileReadingStrategy> { base64 }

    fun encoding(value: String) {
        fileReadingStrategy { plainText(value) }
    }

    protected var context: RenderContext.(HTMLInputElement) -> Unit = { input ->
        pushButton(prefix = "file-button") {
            icon { fromTheme { cloudUpload } }
            element {
                domNode.onclick = {
                    input.click()
                }
            }
        }
    }

    open fun button(
        styling: BasicParams.() -> Unit = {},
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = "file-button",
        build: PushButtonComponent.() -> Unit = {}
    ) {
        context = { input ->
            pushButton(styling, baseClass, id, prefix) {
                build()
                element {
                    domNode.onclick = {
                        input.click()
                    }
                }
            }
        }
    }
}

/**
 * Specific component for handling the upload for one file at once.
 *
 * For the common configuration options @see [FileSelectionBaseComponent].
 */
open class SingleFileSelectionComponent : FileSelectionBaseComponent(), Component<Flow<File>> {
    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass?,
        id: String?,
        prefix: String
    ): Flow<File> {
        var file: Flow<File>? = null
        context.apply {
            (::div.styled(styling, baseClass, id, prefix) {}) {
                val inputElement = (::input.styled { display { none } }) {
                    type("file")
                    accept?.invoke(this)
                    file = changes.events.mapNotNull {
                        domNode.files?.item(0)
                    }.flatMapLatest {
                        domNode.value = "" // otherwise same file can't get loaded twice
                        fileReadingStrategy.value(this@SingleFileSelectionComponent)(it)
                    }
                }.domNode
                context(this, inputElement)
            }
        }
        return file!!
    }
}

/**
 * Specific component for handling the upload for an arbitrary amount of files.
 *
 * For the common configuration options @see [FileSelectionBaseComponent].
 */
open class MultiFileSelectionComponent : FileSelectionBaseComponent(), Component<Flow<List<File>>> {
    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass?,
        id: String?,
        prefix: String
    ): Flow<List<File>> {
        var files: Flow<List<File>>? = null
        context.apply {
            (::div.styled(styling, baseClass, id, prefix) {}) {
                val inputElement = (::input.styled { display { none } }) {
                    type("file")
                    multiple(true)
                    accept?.invoke(this)
                    files = changes.events.mapNotNull {
                        val list = domNode.files
                        if (list != null) {
                            buildList {
                                for (i in 0..list.length) {
                                    val file = list.item(i)
                                    if (file != null) add(
                                        fileReadingStrategy.value(this@MultiFileSelectionComponent)(
                                            file
                                        )
                                    )
                                }
                            }
                        } else null
                    }.flatMapLatest { files ->
                        domNode.value = "" // otherwise same files can't get loaded twice
                        combine(files) { it.toList() }
                    }
                }.domNode
                context(this, inputElement)
            }
        }
        return files!!
    }
}


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
 * @see PushButtonComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [PushButtonComponent]
 * @return a [Flow] that offers the selected [File]
 */
fun RenderContext.file(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
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
 * @see PushButtonComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [PushButtonComponent]
 * @return a [Flow] that offers the selected [File]
 */
fun RenderContext.files(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "file",
    build: FileSelectionBaseComponent.() -> Unit = {}
): Flow<List<File>> = MultiFileSelectionComponent().apply(build).render(this, styling, baseClass, id, prefix)
