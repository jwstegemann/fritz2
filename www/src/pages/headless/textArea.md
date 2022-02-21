---
title: TextArea
layout: layouts/headlessWithContentNav.njk
permalink: /headless/textarea/
eleventyNavigation:
    key: textarea
    title: TextArea
    parent: headless
    order: 110
demoHash: textarea
teaser: "Ein TextArea bietet eine mehrzeilige Texteingabe an."
---

## Einfaches Beispiel

Eine TextArea wird mit der `textArea` Komponenten Fabrik Funktion erzeugt. Innerhalb des Scopes muss zwingend
eine `String` basierte Datenbindung `value` initialisiert werden.

Optional kann ein Platzhaltertext mittels `placeholder` Attribut-Hook gesetzt werden.

Des Weiteren muss das eigentliche Eingabeelement mittels `textareaTextfield` erzeugt werden.

```kotlin
val name = storeOf("")

textArea {
    value(name)
    placeholder("The name is...")
    textareaTextfield { }
}
```

## Verwendung mit Label und Beschreibung

Innerhalb des Scopes existieren die beiden Baustein-Funktionen `textareaLabel` und `textareaDescription`, mit denen man
das Eingabefeld mit weiteren Beschriftungen versehen kann.

Wird beim Label das HTML `label` Tag verwendet (per default), so bewirkt ein Maus-Klick auf das Label, dass das
Eingabefeld fokussiert wird.

```kotlin
val name = storeOf("")

textArea {
    value(name)
    placeholder("The name is...")
    textareaLabel {
        +"Enter the framework's name"
    }
    textareaTextfield { }
    textareaDescription {
        +"The name should reflect the concept of the whole framework."
    }
}
```

## Deaktivieren

Das TextArea unterstützt das (dynamische) Deaktivieren und Aktivieren des Eingabefeldes. Dazu muss der boolesche
Attribut-Hook `disabled` entsprechend gesetzt werden.

```kotlin
val toggle = storeOf(false) 

button {
    +"Enable / Disable"
    clicks.map{ !toggle.current } handledBy toggle.update
}

textArea {
    value(name)
    
    // values on the `FLow` will disable or enable the textarea field
    disabled(toggle.data)
    
    textareaTextfield { }
}
```

## Validierung

Die Datenbindung erlaubt es der TextArea Komponente, die Validierungsnachrichten abzugreifen und einen eigenen
Baustein `textareaValidationMessages` anzubieten, der nur dann gerendert wird, wenn Nachrichten vorliegen.
Diese Nachrichten werden in seinem Scope dem Anwender als Datenstrom `msgs` zur Verfügung gestellt.

```kotlin
textArea {
    value(name)
    textareaTextfield { }
    
    textareaValidationMessages(tag = RenderContext::ul) {
        msgs.renderEach { li { +it.message } }
    }
}
```

## API

### Summary / Sketch
```kotlin
textArea() {
    val value: DatabindingProperty<String>
    val placeHolder: AttributeHook<String>
    val disabled: BooleanAttributeHook

    textareaTextfield() { }
    textareaLabel() { }
    textareaDescription() { } // use multiple times
    textareaValidationMessages() {
        val msgs: Flow<List<ComponentValidationMessage>>
    }
}
```

### textArea

Parameter: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld    | Typ                           | Description                                                                      |
|---------------|-------------------------------|----------------------------------------------------------------------------------|
| `value`       | `DatabindingProperty<String>` | Zwei-Wege-Datenbindung für den Inhalt der Eingabe. Muss zwingend gesetzt werden! |
| `placeHolder` | `AttributeHook<String>`       | Optionaler Hook zum (dynamischen) Setzen eines Platzhalters                      |
| `disabled`    | `BooleanAttributeHook`        | Optionaler Hook zum (dynamischen) Aktivieren oder Deaktivieren der Eingabe       |


### textareaTextfield

Verfügbar im Scope von: `textArea`

Parameter: `classes`, `scope`, `tag`, `initialize`

Tag: `textarea` (nicht änderbar!)


### textareaLabel

Verfügbar im Scope von: `textArea`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`


### textareaDescription

Verfügbar im Scope von: `textArea`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `p`


### textareaValidationMessages

Verfügbar im Scope von: `textArea`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`