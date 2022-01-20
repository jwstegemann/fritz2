package dev.fritz2.components.file

import dev.fritz2.components.buttons.PushButtonComponent
import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.components.pushButton
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.div
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.staticStyle
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.File
import org.w3c.files.FileReader

/**
 * function for reading the [jsFile] to a [File]
 * with [File.content] as a [String]
 */
typealias FileReadingStrategy = (File) -> Flow<dev.fritz2.components.data.File>

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
abstract class FileSelectionBaseComponent {

    companion object {
        const val eventName = "loadend"
        val inputStyle = staticStyle("file-input", "display: none;")
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
                trySend(dev.fritz2.components.data.File(file.name, file.type, file.size.toLong(), content))
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
                    trySend(
                        dev.fritz2.components.data.File(
                            file.name,
                            file.type,
                            file.size.toLong(),
                            reader.result.toString()
                        )
                    )
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
            icon { cloudUpload }
            element {
                domNode.onclick = {
                    input.click()
                }
            }
        }
    }

    open fun button(
        styling: BasicParams.() -> Unit = {},
        baseClass: StyleClass = StyleClass.None,
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
open class SingleFileSelectionComponent : FileSelectionBaseComponent(),
    Component<Flow<dev.fritz2.components.data.File>> {
    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ): Flow<dev.fritz2.components.data.File> {
        var file: Flow<dev.fritz2.components.data.File>? = null
        context.apply {
            div({}, styling, baseClass, id, prefix) {
                val inputElement = input(inputStyle.name) {
                    type("file")
                    this@SingleFileSelectionComponent.accept?.invoke(this)
                    file = changes.events.mapNotNull {
                        domNode.files?.item(0)
                    }.flatMapLatest {
                        domNode.value = "" // otherwise same file can't get loaded twice
                        this@SingleFileSelectionComponent.fileReadingStrategy.value(this@SingleFileSelectionComponent)(
                            it
                        )
                    }
                }.domNode
                this@SingleFileSelectionComponent.context(this, inputElement)
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
open class MultiFileSelectionComponent : FileSelectionBaseComponent(),
    Component<Flow<List<dev.fritz2.components.data.File>>> {
    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ): Flow<List<dev.fritz2.components.data.File>> {
        var files: Flow<List<dev.fritz2.components.data.File>>? = null
        context.apply {
            div({}, styling, baseClass, id, prefix) {
                val inputElement = input(inputStyle.name) {
                    type("file")
                    multiple(true)
                    this@MultiFileSelectionComponent.accept?.invoke(this)
                    files = changes.events.mapNotNull<Event, List<Flow<dev.fritz2.components.data.File>>> {
                        domNode.files?.let { inputFiles ->
                            buildList {
                                for (i in 0..inputFiles.length) {
                                    val file = inputFiles.item(i)
                                    if (file != null) add(
                                        this@MultiFileSelectionComponent.fileReadingStrategy.value(this@MultiFileSelectionComponent)(
                                            file
                                        )
                                    )
                                }
                            }
                        }
                    }.flatMapLatest { files ->
                        domNode.value = "" // otherwise same files can't get loaded twice
                        combine(files) { it.toList() }
                    }
                }.domNode
                this@MultiFileSelectionComponent.context(this, inputElement)
            }
        }
        return files!!
    }
}
