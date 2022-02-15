---
title: RadioGroup 
layout: layouts/headlessWithContentNav.njk 
permalink: /headless/radioGroup/ 
eleventyNavigation:
    key: radioGroup
    title: RadioGroup
    parent: headless 
    order: 30 
demoHash: radioGroup 
teaser: "Eine RadioGroup dient als Basis für die Einfachauswahl von beliebigen Elementen."
---

## Einfaches Beispiel

RadioGroups werden mittels der *generischen* Komponenten Factory Funktion `fun <T> radioGroup()` erzeugt.
Der Typ-Parameter `T` kann dabei durch einen beliegen Typen z.B. einer Domänen-Klasse ersetzt werden.

Durch einen Mausklick auf eine Option oder durch die [[↑]] und [[↓]] Tasten, sofern die RadioGroup fokussiert ist, 
kann eine Option selektiert werden. Es kann immer nur eine Option selektiert werden; die zuvor selektierte Option
wird entsprechend automatisch deselektiert.

Als Datenbindung über die Property `value` ist daher zwingend ein `T` als Datenstrom oder gar als Store anzugeben.
Die Komponente unterstützt two-way-databinding, d.h. sie übernimmt sowohl von außen auf einem `Flow<T>`
vorhandenes Element als selektiert, sendet aber natürlich auch die aktuell selektierte Option an einen zu übergebenden
Handler.

Die verfügbaren Optionen werden nicht direkt als Parameter in die Komponente, sondern jeweils einzeln
in die Bausteinfabrik `radioGroupOption` als ersten Parameter hinein gereicht. Ein typisches Muster ist daher die
Verwendung einer Schleife, in welcher diese Fabrik Funktion entsprechend aufgerufen wird.

Für das Selektieren muss zwingend ein `Tag` mittels `radioGroupOptionToggle` angelegt werden.

```kotlin
// some domain type for this example, a collection to choose from, and an external store
data class Plan(val name: String, val ram: String, val cpus: String, val disk: String, val price: String)
val plans = listOf<Plan>(/* ... */)
val choice = storeOf<Plan?>(null)

radioGroup<HTMLFieldSetElement, Plan?>(tag = RenderContext::fieldset) {
    // set up (two-way-)data-binding
    value(choice)
    
    // using a loop is a typical pattern to create the options
    plans.forEach { option ->
        radioGroupOption(option) { // needs to be created for each selectable option!
            radioGroupOptionToggle {
                +option.name
            }
        }
    }
}
```

## Das selektierte Element stylen

Da eine headless Komponente kein Styling mitliefert, unterstützt die Komponente den Anwender damit, auf die aktuell
selektierte Option zu reagieren.

Innerhalb des Scopes der `radioGroupOption` Fabrik bietet die Komponente den booleschen Datenstrom `selected` an.
Über diesen kann entsprechend abgefragt werden, ob diese Option aktuell selektiert ist oder nicht.

Ein verbreitetes Muster ist es, dynamisch CSS-Klassen zu setzen oder zu entfernen. Natürlich können auch ganze Elemente
hinzugefügt oder entfernt werden, je nach Status auf dem Datenstrom.

```kotlin
radioGroup<HTMLFieldSetElement, Plan?>(tag = RenderContext::fieldset) {
    value(choice)
    plans.forEach { option ->
        radioGroupOption(option) {
            radioGroupOptionToggle {
                // use `selected`-Flow with `className` to react to state changes
                className(selected.map {
                    if (it) "bg-indigo-200" else "bg-white"
                })
                +option.name
            }
        }
    }
}
```

## Das aktive Element stylen

Eine RadioGroup bietet zusätzlich die Information, welche Option gerade *aktiv* ist, also den Fokus hat.

Dazu bietet der Scope von `radioGroupOption` den booleschen Datenstrom `active` an. Auch dieser kann (und sollte)
verwendet werden, um einen spezifischen Stil für den Zustand `true` vorzusehen.

Da oftmals sowohl der Status der Selektion, als auch der Fokus stilistisch dieselben Elemente betreffen, ist es ein
typisches Muster, beide Datenströme zu kombinieren. Dafür bietet sich die Verwendung der gleichnamigen `combine`
Methode auf `Flow`s an:

```kotlin
radioGroup<HTMLFieldSetElement, Plan?>(tag = RenderContext::fieldset) {
    value(choice)
    plans.forEach { option ->
        radioGroupOption(option) {
            radioGroupOptionToggle {
                // combine `selected` and `active`-Flow with `className` to react to state changes
                className(selected.combine(active) { sel, act ->
                    // use `classes` to attach both styling results
                    classes(
                        if (sel) "bg-indigo-200" else "bg-white",
                        if (act) "ring-2 ring-indigo-500 border-transparent" else "border-gray-300"
                    )
                })
                +option.name
            }
        }
    }
}
```

## Beschriftung hinzufügen

Die RadioGroup kann mittels `radioGroupLabel` mit einem Label, die einzelnen Optionen per
`radioGroupOptionLabel` und `radioGroupOptionDescription` mit einem Label und einer Beschreibung angereichert
werden.

```kotlin
radioGroup<HTMLFieldSetElement, Plan?>(tag = RenderContext::fieldset) {
    value(choice)

    // describe the whole radio-group itself
    radioGroupLabel("sr-only") { +"Server size" }
    
    plans.forEach { option ->
        radioGroupOption(option) {
            radioGroupOptionToggle {
                // enrich the single options with label and (multiple) description(s)                        
                div("flex items-center") {
                    div("text-sm") {
                        radioGroupOptionLabel("font-medium text-gray-900", tag = RenderContext::p) {
                            +option.name
                        }
                        radioGroupOptionDescription("text-gray-500", tag = RenderContext::div) {
                            p("sm:inline") { +option.cpus }
                            span("hidden sm:inline sm:mx-1") {
                                attr(Aria.hidden, "true")
                                +"·"
                            }
                            p("sm:inline") { +option.ram }
                        }
                    }
                }
                radioGroupOptionDescription(
                    "mt-2 flex text-sm sm:mt-0 sm:block sm:ml-4 sm:text-right",
                    tag = RenderContext::div
                ) {
                    div("font-medium text-gray-900") { +option.price }
                    div("ml-1 text-gray-500 sm:ml-0") { +"""/mo""" }
                }
            }
        }
    }
}
```

## Validierung

Die Datenbindung erlaubt es der CheckboxGroup Komponente, die Validierungsnachrichten abzugreifen und einen eigenen
Baustein `radioGroupValidationMessages` anzubieten, der nur dann gerendert wird, wenn Nachrichten vorliegen.
Diese Nachrichten werden in seinem Scope dem Anwender als Datenstrom `msgs` zur Verfügung gestellt.

```kotlin
radioGroup<HTMLFieldSetElement, Plan?>(tag = RenderContext::fieldset) {
    value(choice)
    plans.forEach { option ->
        radioGroupOption(option) {
            radioGroupOptionToggle {
                +option.name
            }
        }
    }

    checkboxGroupValidationMessages(tag = RenderContext::ul) {
        msgs.renderEach { li { +it.message } }
    }
}
```

## Maus Interaction

Das Klicken auf ein mit ``radioGroupOptionToggle`` erzeugtes Element selektiert die dahinter liegende Option und
deselektiert die zuvor gewählte Option.

## Keyboard Interaction

| Command                                          | Description                              |
|--------------------------------------------------|------------------------------------------|
| [[↑]] [[↓]] when an option-toggle is focused | Zyklische Selektion durch alle Optionen. |

## API

### Summary / Sketch
```kotlin
radioGroup<T>() {
    val value: DatabindingPropert<T>

    radioGroupLabel() { }
    radioGroupValidationMessages() {
        val msgs: Flow<List<ComponentValidationMessage>>
    }
    // for each T {
        radioGroupOption(option: T) {
            val selected: Flow<Boolean>
            val active: Flow<Boolean>
    
            radioGroupOptionToggle() { }
            radioGroupOptionLabel() { }
            radioGroupOptionDescription() { } // use multiple times
        }
    // }
}
```

### radioGroup

Parameter: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld | Typ                       | Description                                                                   |
|------------|---------------------------|-------------------------------------------------------------------------------|
| `value`    | `DatabindingProperty<T>`  | Zwei-Wege-Datenbindung für die Selektion einer Option. Muss gesetzt werden!   |


### radioGroupLabel

Verfügbar im Scope von: `radioGroup`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`


### radioGroupValidationMessages

Verfügbar im Scope von: `radioGroup`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld | Typ                                      | Description                                                   |
|------------|------------------------------------------|---------------------------------------------------------------|
| `msgs` | `Flow<List<ComponentValidationMessage>>` | stellt eine Liste von ``ComponentValidationMessage`` bereit   |


### radioGroupOption

Verfügbar im Scope von: `radioGroup`

Parameter:
- `option: T`: Das Optionsobjekt, welches dieser Option-Block verwalten soll, muss hier zwingend übergeben werden.
- `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld | Typ             | Description                                                                                                                                                    |
|------------|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `selected` | `Flow<Boolean>` | Dieser Datenstrom liefert den Selektions-Status der verwalteten Option: `true` die Option ist selektiert, `false` wenn nicht.                                  |
| `active`   | `Flow<Boolean>` | Dieser Datenstrom zeigt an, ob eine Option fokussiert ist: `true` die Option hat den Fokus, `false` wenn nicht. Es kann immer nur eine Option den Fokus haben. |


### radioGroupOptionToggle

Verfügbar im Scope von: `radioGroupOption`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`


### radioGroupOptionLabel

Verfügbar im Scope von: `radioGroupOption`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`


### radioGroupOptionDescription

Verfügbar im Scope von: `radioGroupOption`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `span`