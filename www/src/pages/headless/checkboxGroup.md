---
title: CheckboxGroup 
layout: layouts/headlessWithContentNav.njk 
permalink: /headless/checkboxgroup/ 
eleventyNavigation:
    key: checkbox
    title: CheckboxGroup
    parent: headless 
    order: 10 
demoHash: checkboxGroup 
teaser: "Eine CheckboxGroup dient als Basis für die Mehrfachauswahl von beliebigen Elementen."
---

## Einfaches Beispiel

CheckboxGroups werden mittels der *generischen* Komponenten Factory Funktion `fun <T> checkboxGroup()` erzeugt. 
Der Typ-Parameter `T` kann dabei durch einen beliegen Typen z.B. einer Domänen-Klasse ersetzt werden.

Durch einen Mausklick auf eine Option oder durch die [[Space]]-Taste, sofern die Option fokussiert ist, kann diese
Option selektiert oder deselektiert werden. Es könne beliebig viele Optionen selektiert werden.

Als Datenbindung über die Property `value` ist daher zwingend eine `List<T>` als Datenstrom oder gar als Store anzugeben.
Die Komponente unterstützt two-way-databinding, d.h. sie übernimmt sowohl von außen auf einem `Flow<List<T>>` 
vorhandene Elemente als selektiert, sendet aber natürlich auch die aktuelle Selektion an einen zu übergebenden
Handler.

Die verfügbaren Optionen werden nicht direkt als Parameter in die Komponente, sondern jeweils einzeln 
in die Bausteinfabrik `checkboxGroupOption` als erster Parameter hinein gereicht. Ein typisches Muster ist daher
die Verwendung einer Schleife, in welcher diese Fabrik Funktion entsprechend aufgerufen wird.

Für das Selektieren oder Deselektieren muss zwingend ein `Tag` mittels `checkboxGroupOptionToggle` angelegt werden.

```kotlin
// some domain type for this example, a collection to choose from, and an external store
data class Newsletter(val id: Int, val title: String, val description: String, val users: Int)
val mailingList = listOf<Newsletter>(/* ... */)
val subscriptions = storeOf(emptyList<Newsletter>())

checkboxGroup<HTMLFieldSetElement, Newsletter>(tag = RenderContext::fieldset) {
    // set up (two-way-)data-binding
    value(subscriptions)
    
    // using a loop is a typical pattern to create the options
    mailingList.forEach { option ->
        checkboxGroupOption(option) { // needs to be created for each selectable option!
            checkboxGroupOptionToggle(tag = RenderContext::label) {
                +option.title
            }
        }
    }
}
```

## Das selektierte Element stylen

Da eine headless Komponente kein Styling mitliefert, unterstützt die Komponente den Anwender damit, auf den aktuellen
Selektions-Zustand einer Option zu reagieren.

Innerhalb des Scopes der `checkboxGroupOption` Fabrik bietet die Komponente den booleschen Datenstrom `selected` an. 
Über diesen kann entsprechend abgefragt werden, ob diese Option aktuell selektiert ist oder nicht.

Ein verbreitetes Muster ist es, dynamisch CSS-Klassen zu setzen oder zu entfernen. Natürlich können auch ganze Elemente
hinzugefügt oder entfernt werden, je nach Status auf dem Datenstrom.

```kotlin
checkboxGroup<HTMLFieldSetElement, Newsletter>(tag = RenderContext::fieldset) {
    value(subscriptions)
    mailingList.forEach { option ->
        checkboxGroupOption(option) {
            checkboxGroupOptionToggle(tag = RenderContext::label) {
                // use `selected`-Flow with `className` to react to state changes
                className(selected.map {
                    if (it) "ring-2 ring-indigo-500 border-transparent"
                    else "border-gray-300"
                })
            }
            // render some Icon to support the visual "selected" impression only if option is selected
            selected.render {
                if (it) {
                    svg("h-5 w-5 text-indigo-600") {
                        content(HeroIcons.check_circle)
                        fill("currentColor")
                    }
                }
            }
        }
    }
}
```

## Beschriftung hinzufügen

Die CheckboxGroup kann mittels `checkboxGroupLabel` mit einem Label, die einzelnen Optionen per
`checkboxGroupOptionLabel` und `checkboxGroupOptionDescription` mit einem Label und einer Beschreibung angereichert
werden.

```kotlin
checkboxGroup<HTMLFieldSetElement, Newsletter>(tag = RenderContext::fieldset) {
    value(subscriptions)
    
    // describe the whole checkbox-group itself
    checkboxGroupLabel(tag = RenderContext::legend) { +"Select some mailing lists" }
    
    mailingList.forEach { option ->
        checkboxGroupOption(option) {
            checkboxGroupOptionToggle(tag = RenderContext::label) {
                div("flex-1 flex") {
                    div("flex flex-col") {
                        // enrich the single options with label and description(s)
                        checkboxGroupOptionLabel("block text-sm font-medium text-gray-900") {
                            +option.title
                        }
                        checkboxGroupOptionDescription("flex items-center text-sm text-gray-500") {
                            +option.description
                        }
                        checkboxGroupOptionDescription("mt-2 text-sm font-medium text-gray-900") {
                            +"${option.users} users"
                        }
                    }
                }
            }
        }
    }
}
```

## Validierung

Die Datenbindung erlaubt es der CheckboxGroup Komponente, die Validierungsnachrichten abzugreifen und einen eigenen 
Baustein `checkboxGroupValidationMessages` anzubieten, der nur dann gerendert wird, wenn Nachrichten vorliegen.
Diese Nachrichten werden in seinem Scope dem Anwender als Datenstrom `msgs` zur Verfügung gestellt.

```kotlin
checkboxGroup<HTMLFieldSetElement, Newsletter>(tag = RenderContext::fieldset) {
    value(subscriptions)
    mailingList.forEach { option ->
        checkboxGroupOption(option) { 
            checkboxGroupOptionToggle(tag = RenderContext::label) {
                +option.title
            }
        }
    }

    checkboxGroupValidationMessages(tag = RenderContext::ul) {
        msgs.renderEach { li { +it.message } }
    }
}
```

## Maus Interaction

Das Klicken auf ein mit ``checkboxGroupOptionToggle`` erzeugtes Element selektiert oder deselektiert die dahinter
liegende Option.

## Keyboard Interaction

| Command                                    | Description                                                |
|--------------------------------------------|------------------------------------------------------------|
| [[Space]] when an option-toggle is focused | Selektiert oder deselektiert die dahinter liegende Option. |

## API

### Summary / Sketch
```kotlin
checkboxGroup<T>() {
    val value: DatabindingPropert<List<T>>

    checkboxGroupLabel() { }
    checkboxGroupValidationMessages() {
        val msgs: Flow<List<ComponentValidationMessage>>
    }
    // for each T {
        checkboxGroupOption(option: T) {
            val selected: Flow<Boolean>
    
            checkboxGroupOptionToggle() { }
            checkboxGroupOptionLabel() { }
            checkboxGroupOptionDescription() { } // use multiple times
        }
    // }
}
```

### checkboxGroup

Parameter: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld | Typ                            | Description                                                                                          |
|------------|--------------------------------|------------------------------------------------------------------------------------------------------|
| `value`    | `DatabindingProperty<List<T>>` | Zwei-Wege-Datenbindung für eine beliebig große Anzahl an selektierten Optionen. Muss gesetzt werden! |


### checkboxGroupLabel

Verfügbar im Scope von: `checkboxGroup`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`


### checkboxGroupValidationMessages

Verfügbar im Scope von: `checkboxGroup`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld | Typ                                  | Description                                                   |
|------------|--------------------------------------|---------------------------------------------------------------|
| `msgs` | `Flow<List<ComponentValidationMessage>>` | stellt eine Liste von ``ComponentValidationMessage`` bereit   |


### checkboxGroupOption

Verfügbar im Scope von: `checkboxGroup`

Parameter:
- `option: T`: Das Optionsobjekt, welches dieser Option-Block verwalten soll, muss hier zwingend übergeben werden.
- `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld | Typ             | Description                                                                                                                                       |
|------------|-----------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| `selected` | `Flow<Boolean>` | Dieser Datenstrom liefert den Selektions-Status der verwalteten Option: `true` die Option ist Teil der selektierten Optionen, `false` wenn nicht. |


### checkboxGroupOptionToggle

Verfügbar im Scope von: `checkboxGroupOption`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`


### checkboxGroupOptionLabel

Verfügbar im Scope von: `checkboxGroupOption`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`


### checkboxGroupOptionDescription

Verfügbar im Scope von: `checkboxGroupOption`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `span`