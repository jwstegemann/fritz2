package dev.fritz2.components

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.StyleClass.Companion.plus
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.File
import org.w3c.files.FileReader

open class FileRead(val name: String, val content: String, val type: String)

typealias ReadingStrategy = (File) -> Flow<FileRead>

@ComponentMarker
open class FileButtonComponent : PushButtonComponent() {

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

    val base64: ReadingStrategy = { file ->
        callbackFlow {
            val reader = FileReader()
            val listener: (Event) -> Unit = { _ ->
                var content = reader.result.toString()
                val index = content.indexOf("base64,")
                if (index > -1) content = content.substring(index + 7)
                offer(FileRead(file.name, content, file.type))
            }
            reader.addEventListener(eventName, listener)
            reader.readAsDataURL(file)
            awaitClose { reader.removeEventListener(eventName, listener) }
        }
    }

    val plainText: (String) -> ReadingStrategy = { encoding ->
        { file ->
            callbackFlow {
                val reader = FileReader()
                val listener: (Event) -> Unit = { _ ->
                    offer(FileRead(file.name, reader.result.toString(), file.type))
                }
                reader.addEventListener(eventName, listener)
                reader.readAsText(file, encoding)
                awaitClose { reader.removeEventListener(eventName, listener) }
            }
        }
    }

    var readingStrategy: ReadingStrategy = base64

    fun encoding(value: String) {
        readingStrategy = plainText(value)
    }
}

/**
 * This component generates a single file selection button.
 *
 * You can set the label, an icon, the position of the icon and access its events.
 * For a detailed overview about the possible properties of the component object itself, have a look at
 * [PushButtonComponent]
 *
 * In contrast to the [pushButton] component, this variant returns a [Flow] of [FileRead] in order
 * to combine the button declaration directly to a fitting _handler_ which expects a [FileRead]:
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
 * @return a [Flow] that offers the selected [FileRead]
 */
fun RenderContext.fileButton(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "file-button",
    build: FileButtonComponent.() -> Unit = {}
): Flow<FileRead> {
    var file: Flow<FileRead>? = null
    var inputNode: HTMLInputElement? = null
    val component = FileButtonComponent().apply(build).apply {
        inputNode = (::input.styled { display { none } }) {
            type("file")
            accept?.let { accept(it) }
            acceptFlow?.let { accept(it) }
            file = changes.events.mapNotNull {
                domNode.files?.item(0)
            }.flatMapLatest {
                domNode.value = "" // otherwise same file can't get loaded twice
                readingStrategy(it)
            }
        }.domNode
    }
    (::button.styled(styling, baseClass + PushButtonComponent.staticCss, id, prefix) {
        component.color()
        component.variant.value.invoke(Theme().button.variants)()
        component.size.value.invoke(Theme().button.sizes)()
    }) {
        component.element.value.invoke(this)
        disabled(component.disabled.values)
        if (component.label == null) {
            component.renderIcon(this, component.centerIconStyle, component.centerSpinnerStyle)
        } else {
            if (component.icon != null && component.iconPlacement.value(PushButtonComponent.iconPlacementContext) == PushButtonComponent.IconPlacement.left) {
                component.renderIcon(this, component.leftIconStyle, component.leftSpinnerStyle)
            }
            component.renderLabel(this)
            if (component.icon != null && component.iconPlacement.value(PushButtonComponent.iconPlacementContext) == PushButtonComponent.IconPlacement.right) {
                component.renderIcon(this, component.rightIconStyle, component.rightSpinnerStyle)
            }
        }
        component.events.value.invoke(this)
        domNode.onclick = {
            inputNode?.click()
        }
    }
    return file!!
}