package dev.fritz2.dom.html

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.files.FileList

/**
 * Implementation of [HtmlTag] for [HTMLInputElement]
 */
class InputTag(
    id: String? = null,
    baseClass: String? = null,
    job: Job,
    scope: Scope
) : HtmlTag<HTMLInputElement>("input", id, baseClass, job, scope) {

    fun <X : Event> Flow<X>.values(): Flow<String> =
        this.map { it.target.unsafeCast<HTMLInputElement>().value }

    fun <X : Event> Flow<X>.valuesAsNumber(): Flow<Double> =
        this.map { it.target.unsafeCast<HTMLInputElement>().valueAsNumber }

    fun <X : Event> Flow<X>.valuesAsDate(): Flow<Double> =
        this.map { it.target.unsafeCast<HTMLInputElement>().valueAsDate }

    fun <X : Event> Flow<X>.files(): Flow<FileList?> =
        this.map { it.target.unsafeCast<HTMLInputElement>().files }

    fun <X : Event> Flow<X>.states(): Flow<Boolean> =
        this.map { it.target.unsafeCast<HTMLInputElement>().checked }
}

/**
 * Implementation of [HtmlTag] for [HTMLSelectElement]
 */
class SelectTag(
    id: String? = null,
    baseClass: String? = null,
    job: Job,
    scope: Scope
) : HtmlTag<HTMLSelectElement>("select", id, baseClass, job, scope) {

    fun <X : Event> Flow<X>.values(): Flow<String> =
        this.map { it.target.unsafeCast<HTMLSelectElement>().value }

    fun <X : Event> Flow<X>.selectedIndex(): Flow<Int> =
        this.map { it.target.unsafeCast<HTMLSelectElement>().selectedIndex }

    fun <X : Event> Flow<X>.selectedValue(): Flow<String> =
        this.map {
            val select = it.target.unsafeCast<HTMLSelectElement>()
            select.options[select.selectedIndex].unsafeCast<HTMLOptionElement>().value
        }

    fun <X : Event> Flow<X>.selectedText(): Flow<String> =
        this.map {
            val select = it.target.unsafeCast<HTMLSelectElement>()
            select.options[select.selectedIndex].unsafeCast<HTMLOptionElement>().text
        }
}

/**
 * Implementation of [HtmlTag] for [HTMLFieldSetElement]
 */
class FieldSetTag(
    id: String? = null,
    baseClass: String? = null,
    job: Job,
    scope: Scope
) : HtmlTag<HTMLFieldSetElement>("fieldset", id, baseClass, job, scope) {

    fun <X : Event> Flow<X>.values(): Flow<String> =
        this.map { it.target.unsafeCast<HTMLInputElement>().value }
}

/**
 * Implementation of [HtmlTag] for [HTMLTextAreaElement]
 */
class TextAreaTag(
    id: String? = null,
    baseClass: String? = null,
    job: Job,
    scope: Scope
) : HtmlTag<HTMLTextAreaElement>("textarea", id, baseClass, job, scope) {

    fun <X : Event> Flow<X>.values(): Flow<String> =
        this.map { it.target.unsafeCast<HTMLTextAreaElement>().value }
}