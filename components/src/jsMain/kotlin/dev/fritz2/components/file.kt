package dev.fritz2.components

import dev.fritz2.components.data.File
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.styled
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.FileReader
import org.w3c.files.File as jsFile

typealias FileReadingStrategy = (jsFile) -> Flow<File>

@ComponentMarker
open class FileButtonComponent {

    companion object {
        const val eventName = "loadend"
    }

    internal var accept: String? = null
    fun accept(value: String) {
        accept = value
    }

    internal var acceptFlow: Flow<String>? = null
    fun accept(value: Flow<String>) {
        acceptFlow = value
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

    var fileReadingStrategy: FileReadingStrategy = base64

    fun encoding(value: String) {
        fileReadingStrategy = plainText(value)
    }

    internal var renderContext: RenderContext.(HTMLInputElement) -> Unit = { input ->
        pushButton {
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
        renderContext = { input ->
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
 * This component generates a single file selection button.
 *
 * You can set the label, an icon, the position of the icon and access its events.
 * For a detailed overview about the possible properties of the component object itself, have a look at
 * [PushButtonComponent]
 *
 * In contrast to the [pushButton] component, this variant returns a [Flow] of [file] in order
 * to combine the button declaration directly to a fitting _handler_ which expects a [file]:
 * ```
 * val contentStore = object: RootStore<String>("") {
 *   val saveFile<FileRead> = { _, file -> file.content }
 * }
 * fileButton {
 *   text("Select a file")
 *   accept("text/plain")
 *   encoding("utf-8")
 * } handledBy contentStore.saveFile
 * ```
 *
 * @see PushButtonComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [PushButtonComponent]
 * @return a [Flow] that offers the selected [file]
 */
fun RenderContext.file(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "file",
    build: FileButtonComponent.() -> Unit = {}
): Flow<File> {
    var file: Flow<File>? = null
    val component = FileButtonComponent().apply(build)
    (::div.styled(styling, baseClass, id, prefix) {}) {
        val inputElement = (::input.styled { display { none } }) {
            type("file")
            component.accept?.let { accept(it) }
            component.acceptFlow?.let { accept(it) }
            file = changes.events.mapNotNull {
                domNode.files?.item(0)
            }.flatMapLatest {
                domNode.value = "" // otherwise same file can't get loaded twice
                component.fileReadingStrategy(it)
            }
        }.domNode
        component.renderContext(this, inputElement)
    }
    return file!!
}

fun RenderContext.files(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "file",
    build: FileButtonComponent.() -> Unit = {}
): Flow<List<File>> {
    var files: Flow<List<File>>? = null
    val component = FileButtonComponent().apply(build)
    (::div.styled(styling, baseClass, id, prefix) {}) {
        val inputElement = (::input.styled { display { none } }) {
            type("file")
            multiple(true)
            component.accept?.let { accept(it) }
            component.acceptFlow?.let { accept(it) }
            files = changes.events.mapNotNull {
                val list = domNode.files
                console.log("files: ${list?.length}}")
                if(list != null) {
                    buildList {
                        for (i in 0..list.length) {
                            val file = list.item(i)
                            if (file != null) add(component.fileReadingStrategy(file))
                        }
                    }
                } else null
            }.flatMapLatest { files ->
                domNode.value = "" // otherwise same files can't get loaded twice
                combine(files) { it.toList() }
            }
        }.domNode
        component.renderContext(this, inputElement)
    }
    return files!!
}