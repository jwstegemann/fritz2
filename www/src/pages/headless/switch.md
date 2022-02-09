---
title: Switch
layout: layouts/headlessWithContentNav.njk
permalink: /headless/switch/
eleventyNavigation:
    key: switch
    title: Switch (Toggle)
    parent: headless
    order: 50
demoHash: switch
teaser: "Die Switch Komponente kann für das Umschalten zwischen zwei Zuständen verwendet werden, 
also an oder aus, ja oder nein usw. Die Semantik und Bedienung entspricht der einer einzelnen Checkbox."
---

## Einfaches Beispiel

Switches werden mittels der ``switch`` Fabrik Funktion gebaut. Es ist zwingend erforderlich, einen booleschen 
Datenstrom über die ``value`` Property in die Komponente zu injizieren.

Der aktuelle Zustand kann über den ``enabled`` Datenstrom abgefragt werden.

```kotlin
val switchState = storeOf(false)

switch {
    value(switchState)
    className(enabled.map { if (it) "bg-indigo-600" else "bg-gray-200" })
    span {
        className(enabled.map { if (it) "translate-x-5" else "translate-x-0" })
    }
}
```

## Verwendung mit Label und Beschreibung

Will man ein Switch mit einem Label oder einer zusätzlichen Beschreibung ausstatten, so muss man die Fabrik Funktion
``switchWithLabel`` anstellen von ``switch`` verwenden.

Innerhalb der headless Komponente existieren dann die beiden Bausteine ``switchToggle``, ``switchLabel`` und
``switchDescription``, um die einzelnen Teile zu erzeugen. Es können beliebig viele Beschreibungen angegeben werden.

```kotlin
    switchWithLabel {
        value(switchState)
        span {
            switchLabel(tag = RenderContext::span) { //default tag ist ``label``
                +"Available to hire"
            }
            switchDescription(tag = RenderContext::span) { //default tag ist ``p``
                +"Nulla amet tempus sit accumsan."
            }
        }
        switchToggle {
            className(enabled.map { if (it) "bg-indigo-600" else "bg-gray-200" })
            span {
                className(enabled.map { if (it) "translate-x-5" else "translate-x-0" })
            }
        }
    }
```

## Validierung

Die Datenbindung erlaubt es der Switch Komponente, die Validierungsnachrichten abzugreifen und einen eigenen Baustein
anzubieten, der nur dann gerendert wird, sofern Nachrichten vorliegen und diese in seinem Scope dem Anwender zur
Verfügung stellt.


```kotlin
val switchState = storeOf(false)

switch {
    value(switchState)
    className(enabled.map { if (it) "bg-indigo-600" else "bg-gray-200" })
    span {
        className(enabled.map { if (it) "translate-x-5" else "translate-x-0" })
    }
    switchValidationMessages(tag = RenderContext::ul) { messages ->
        messages.forEach { li { +it } }
    }
}
```

## Maus Interaction

Das Klicken auf ein mit ``switch``, ``switchWithLabel`` oder ``switchLabel`` erzeugtes Element, schaltet zwischen den
beiden Zuständen hin und her.

## Keyboard Interaction

| Command                              | Description                                        |
|--------------------------------------|----------------------------------------------------|
| [[Space]] when a `Switch` is focused | Schaltet zwischen den beiden Zuständen hin und her |

## API

### `switch` (Component Factory)

Parameter: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `button`

| Scope Feld | Typ                            | Description                                                                    |
|------------|--------------------------------|--------------------------------------------------------------------------------|
| `value`    | `DatabindingProperty<Boolean>` | Zwei-Wege-Datenbindung auf Basis eines booleschen Wertes. Muss gesetzt werden! |
| `enabled`  | `Flow<Boolean>`                | Aktueller Zustand. (Default `false`)                                           |


### `switchWithLabel` (Component Factory)

Parameter: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld | Typ                            | Description                                                                    |
|------------|--------------------------------|--------------------------------------------------------------------------------|
| `value`    | `DatabindingProperty<Boolean>` | Zwei-Wege-Datenbindung auf Basis eines booleschen Wertes. Muss gesetzt werden! |
| `enabled`  | `Flow<Boolean>`                | Aktueller Zustand. (Default `false`)                                           |


### `switchValidationMessages` (Baustein)

Verfügbar im Scope von: `switch`, `switchWithLabel`

Parameter: `classes`, `scope`, `tag`, `initialize`

| Scope Feld | Typ                                 | Description                                                   |
|------------|-------------------------------------|---------------------------------------------------------------|
| `messages` | `List<ComponentValidationMessage>`  | stellt eine Liste von ``ComponentValidationMessage`` bereit   |


### `switchToggle` (Baustein)

Verfügbar im Scope von: ``switchWithLabel``

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `button`


### `switchLabel` (Baustein)

Verfügbar im Scope von: ``switchWithLabel``

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`


### `switchDescription` (Baustein)

Verfügbar im Scope von: ``switchWithLabel``

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `span`
