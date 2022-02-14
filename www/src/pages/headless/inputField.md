---
title: InputField
layout: layouts/headlessWithContentNav.njk
permalink: /headless/inputfield/
eleventyNavigation:
    key: inputfield
    title: InputField
    parent: headless
    order: 80
demoHash: inputfield
teaser: "Ein InputField bietet eine einzeilige Texteingabe an."
---

## Einfaches Beispiel

Ein InputField wird mit der `inputField` Komponenten Fabrik Funktion erzeugt. Innerhalb des Scopes muss zwingend 
eine `String` basierte Datenbindung `value` initialisiert werden.

Optional kann ein Platzhaltertext mittels `placeholder` Attribut-Hook gesetzt werden.

Des Weiteren muss das eigentliche Eingabeelement mittels `inputTextfield` erzeugt werden.

```kotlin
val name = storeOf("")

inputField {
    value(name)
    placeholder("The name is...")
    inputTextfield { }
}
```

## Verwendung mit Label und Beschreibung

Innerhalb des Scopes existieren die beiden Baustein-Funktionen `inputLabel` und `inputDescription`, mit denen man
das Eingabefeld mit weiteren Beschriftungen versehen kann.

Wird beim Label das HTML `label` Tag verwendet (per default), so bewirkt ein Maus-Klick auf das Label, dass das
Eingabefeld fokussiert wird.

```kotlin
val name = storeOf("")

inputField {
    value(name)
    placeholder("The name is...")
    inputLabel {
        +"Enter the framework's name"
    }
    inputTextfield { }
    inputDescription {
        +"The name should reflect the concept of the whole framework."
    }
}
```

## Deaktivieren

Das InputField unterstützt das (dynamische) Deaktivieren und Aktivieren des Eingabefeldes. Dazu muss der boolesche 
Attribut-Hook `disabled` entsprechend gesetzt werden.

```kotlin
val toggle = storeOf(false) 

button {
    +"Enable / Disable"
    clicks.map{ !toggle.current } handledBy toggle.update
}

inputField {
    value(name)
    
    // values on the `FLow` will disable or enable the input field
    disabled(toggle.data)
    
    inputTextfield { }
}
```

## Validierung

Die Datenbindung erlaubt es der InputField Komponente, die Validierungsnachrichten abzugreifen und einen eigenen 
Baustein `inputValidationMessages` anzubieten, der nur dann gerendert wird, wenn Nachrichten vorliegen.
Diese Nachrichten werden in seinem Scope dem Anwender als Datenstrom `msgs` zur Verfügung gestellt.

```kotlin
inputField {
    value(name)
    inputTextfield { }
    
    inputValidationMessages(tag = RenderContext::ul) {
        msgs.renderEach { li { +it.message } }
    }
}
```

## API

### Summary / Sketch
```kotlin
inputField() {
    // Felder
    value: DatabindingProperty<String>
    placeHolder: AttributeHook<String>
    disabled: BooleanAttributeHook

    // Bausteine
    inputTextfield() { }
    inputLabel() { }
    inputDescription() { } // use multiple times
    inputValidationMessages() { 
        // Felder
        msgs: Flow<List<ComponentValidationMessage>>
    }
}
```

### inputField

Parameter: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld    | Typ                           | Description                                                                      |
|---------------|-------------------------------|----------------------------------------------------------------------------------|
| `value`       | `DatabindingProperty<String>` | Zwei-Wege-Datenbindung für den Inhalt der Eingabe. Muss zwingend gesetzt werden! |
| `placeHolder` | `AttributeHook<String>`       | Optionaler Hook zum (dynamischen) Setzen eines Platzhalters                      |
| `disabled`    | `BooleanAttributeHook`        | Optionaler Hook zum (dynamischen) Aktivieren oder Deaktivieren der Eingabe       |


### inputTextfield

Verfügbar im Scope von: `inputField`

Parameter: `classes`, `scope`, `tag`, `initialize`

Tag: `input` (nicht änderbar!)


### inputLabel

Verfügbar im Scope von: `inputField`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`


### inputDescription

Verfügbar im Scope von: `inputField`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `p`


### inputValidationMessages

Verfügbar im Scope von: `inputField`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`