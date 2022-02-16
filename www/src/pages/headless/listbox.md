---
title: Listbox 
layout: layouts/headlessWithContentNav.njk 
permalink: /headless/listbox/ 
eleventyNavigation:
    key: listbox
    title: Listbox (Select)
    parent: headless 
    order: 40 
demoHash: listbox 
teaser: "Eine Listbox bietet dem Anwender die Möglichkeit, eine Option aus einer vorgegebenen Menge auszuwählen. Die Auswahloptionen sind im Normalfall nur sichtbar, wenn die Listbox aktiv ist."
---

## Einfaches Beispiel

Eine Listbox wird durch die Factory Funktion `fun <T> listbox()` erzeugt. `T` ist dabei der Datentyp der Auswahloptionen, die angeboten werden sollen, als z.B. ein Domänendatentyp wie Film-Charaktere.

Eine Listbox kann direkt an einen `Store<T>` gebunden werden. TODO: Bessere Formulierung für das Databinding. Sollte an einer Stelle beschrieben werden und dann an den Komponenten nur noch durch den Typ spezifiziert werden.

Durch einen Click auf den durch `listboxButton` erzeugten Baustein oder [[Space]] bei fokussierter `listboxButton`, wird die Auswahlliste eingeblendet. Der `listboxButton` zeigt häufig das aktuell gewählte Item an.

Innerhalb der Auswahlliste kann mit Hilfe der Tastatur navigiert. Durch [[Enter]], [[Space]] oder einen Mausklick kann ein Element ausgewählt werden. Verliert die Listbox den Fokus oder wird außerhalb der Auswahlliste geclickt, wird diese ausgeblendet.

Die Auswahlliste wird durch die Bausteinfabrik `listboxItems` erzeugt. Innerhalb dieses Scopes werden durch `listboxItem` die einzelnen Optionen definiert. `listboxItem` erhält dabei als ersten Parameter den Wert, den diese Option repräsentiert. Ein typisches Muster ist daher
die Verwendung einer Schleife, in welcher diese Fabrik Funktion entsprechend aufgerufen wird.

```kotlin
// some domain type for this example, a collection to choose from, and an external store
val characters = listOf("Luke", "Leia", "Han",)
val bestCharacter = storeOf("Luke")

listbox<String> {
    // set up (two-way-)data-binding
    value(bestCharacter)
    
    listboxButton {
        span { value.data.renderText() }
        svg { content(HeroIcons.selector) }
    }

    listboxItems {
        // using a loop is a typical pattern to create the options
        characters.forEach { entry ->
            listboxItem(entry) { // needs to be created for each selectable option
                span { +entry }
            }
        }
    }
}
```

## Das aktive und ausgewählte Item stylen  

Ein `listboxItem` besitzt zwei besondere Zustände:
- wurde durch die Tastatur oder durch Mausbewegung zu dem Item navigiert ist es `active`. 
- Ein Click, [[Enter]] oder [[Space]] wählen das aktive Item wählen es aus. Es ist dann `selected`.

Im Scope eines `listboxItem` stehen `active` und `selected` als `Flow<Boolean>` zur Verfügung, um schnell in Abhängigkeit des Status das Styling anzupassen oder bestimmte Elemente zu rendern oder auszublenden:

```kotlin
listboxItem(entry) {
    //change fore- und background-color is item is active 
    className(active.map {
        if (it) "text-amber-900 bg-amber-100" else "text-gray-300"
    })
    
    span { +entry }

    //render checked-icon only if item is selected
    selected.render {
        if (it) {
            svg { content(HeroIcons.check) }
        }
    }
}
```

## Items disablen

Die einzelnen Items der Auswahlliste können statisch oder dynamisch disabled werden. Im Scope eines `listboxItem` steht dazu der `Handler<Boolean>` `disable` zur Verfügung. Dieser kann direkt aufgerufen werden oder dazu genutzt werden, den disabled-Zustand eines Items in Abhängigkeit von einem externen Datenmodell dynamisch zu setzen. Der disabled-Zustand des Items steht darüber hinaus als `Flow<Boolean>` zur Verfügung, um das Item beispielsweise abhängig davon zu stylen.

```kotlin
val characters = listOf(
    "Luke" to true,
    "Leia" to false,
    "Chewbakka" to false,
    "Han" to false
)
    
val bestCharacter = storeOf("Luke")
    
listbox<String> {
    value(bestCharacter)
    
    //...

    listboxItems {
        characters.forEach { (entry, disabledState) ->
            listboxItem(entry) {
                //set disabled state
                disable(disabledState)
                
                //change text-color if item is disabled
                className(disabled.map {
                    if (it) "text-gray-300" else "text-gray-800"
                })

                span { +entry }
            }
        }
    }
}
```

## Beschriftung hinzufügen

Die Listbox kann mittels `listboxLabel` mit einem Label ergänzt werden, z.B. zur Nutzung in Formularen oder für einen Screen-Reader:

```kotlin
listbox<String> {
    //...
    listboxLabel {
        span { +"select best character of franchise" }
    }
    //...
}
```

## Zustand der Auswahlliste

Listbox ist eine [`OpenClose`-Komponente](../#closable-content---openclose). In ihrem Scope stehen verschiedene `Flow`s und `Handler` wie `opened` zur Verfügung, um basierend auf Öffnungszustand der Auswahlliste zu steuern oder diesen zu verändern.

Der Öffnungszustand der Listbox kann per Databinding an einen externen `Store` gebunden werden, z.B. um die Auswahlliste immer anzuzeigen.


## Transitionen

Das Ein- und Ausblenden der Auswahlliste lässt sich mit Hilfe von `transition` einfach animieren:

```kotlin
listboxItems {
    transition(opened,
        enter = "transition duration-100 ease-out",
        enterStart = "opacity-0 scale-95",
        enterEnd = "opacity-100 scale-100",
        leave = "transition duration-100 ease-in",
        leaveStart = "opacity-100 scale-100",
        leaveEnde = "opacity-0 scale-95"
    )
    
    characters.forEach { (entry, disabledState) ->
        listboxItem(entry) {
            //...
        }
    }
}
```

## Position der Auswahlliste

`listboxItems` ist ein [`PopUpPanel`](../#floating-content---popuppanel) und stellt in seinem Scope daher Konfigurationsmöglichkeiten zur Verfügung, um z.B. die Position oder den Abstand der Auswahlliste zum `listboxButton` als Referenzelement zu steuern:

```kotlin
listboxItems {
    placement = Placement.Top
    distance = 20
    
    characters.forEach { (entry, disabledState) ->
        listboxItem(entry) {
            //...
        }
    }
}
```


## Validierung

Die Datenbindung erlaubt es der Listbox Komponente, die Validierungsnachrichten abzugreifen und einen eigenen Baustein listboxValidationMessages anzubieten, der nur dann gerendert wird, wenn Nachrichten vorliegen. Diese Nachrichten werden in seinem Scope dem Anwender als Datenstrom messages zur Verfügung gestellt.

```kotlin
listbox<String> {
    value(bestCharacter)
    listboxButton { /* ... */ }

    listboxItems {
        characters.forEach { entry ->
            listboxItem(entry) {
                span { +entry }
            }
        }
    }

    listboxValidationMessages(tag = RenderContext::ul) {
        msgs.renderEach { li { +it.message } }
    }
}
```

## Focus Management

Ist die Auswahlliste der Listbox geöffnet, erhält der Baustein `listboxItems` den Fokus. Verliert dieser den Fokus, wird die Auswahlliste geschlossen und der Fokus kehrt zum `listboxButton` zurück.

## Maus Interaction

Ein Click auf den `listboxButton` schaltet den Zustand der Auswahlliste um. Ein Click außerhalb der geöffneten Auswahlliste schließt diese. Wir die Maus in der geöffneten Liste über ein Item bewegt, wird dieses als aktiv gekennzeichnet. Ein Click auf ein Item bei geöffneter Liste wählt dieses aus und schließt die Liste.

## Keyboard Interaction

| Command                                             | Description                                          |
|-----------------------------------------------------|------------------------------------------------------|
| [[Enter]] [[Space]] when `listBoxButton` is focused | Opens listbox and activates current selected item    |
| [[⬆︎]] [[⬇]] ︎when listbox is open                  | Activates previous / next item                       |
| [[Home︎]] [[End]] ︎when listbox is open             | Activates first / last item                          |
| [[A-Z]] [[a-z]] when listbox is open                | Activates first item beginning with according letter |
| [[Esc]] when listbox is open                | Closes listbox                                       |
| [[Enter]] [[Space]] when listbox is open            | Selects active item                                  |

## API

### Summary / Sketch

```kotlin
listbox<T>() {
    val value: DatabindingProperty<T>
    // inherited by `OpenClose`
    val openClose = DatabindingProperty<Boolean>()
    val opened: Flow<Boolean>
    val close: SimpleHandler<Unit>
    val open: SimpleHandler<Unit>
    val toggle: SimpleHandler<Unit>
    
    listboxButton() { }
    listboxLabel() { }
    listboxValidationMessages() {
        val msgs: Flow<List<ComponentValidationMessage>>
    }
    listboxItems() {
        // inherited by `PopUpPanel`
        var placement: Placement
        var strategy: Strategy
        var flip: Boolean
        var skidding: Int
        var distance: int
        
        // for each T {
            listboxItem(entry: T) {
                val index: Int
                val selected: Flow<Boolean>
                val active: Flow<Boolean>
                val disabled: Flow<Boolean>
                val disable: SimpleHandler<Boolean>
            }
        // }
    }
}
```


### listbox

Parameter: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld           | Typ                            | Description                                                                                                       |
|----------------------|--------------------------------|-------------------------------------------------------------------------------------------------------------------|
| `value`              | `DatabindingProperty<T>`       | Zwei-Wege-Datenbindung für ein selektiertes Entry. Muss gesetzt werden!                                           |
| `openClose`          | `DatabindingProperty<Boolean>` | Zwei-Wege-Datenbindung für das Öffnen und Schließen. Muss gesetzt werden!                                         |
| `opened`             | `Flow<Boolean>`                | Datenstrom der bezogen auf den "geöffnet"-Status boolesche Werte liefert; in der Listbox nutzlos, da immer `true` |
| `close`              | `SimpleHandler<Unit>`          | Handler zum Schließen der Listbox von innen heraus, sollte nicht verwendet werden, da dies automatisch passiert!  |
| `open`               | `SimpleHandler<Unit>`          | Handler zum Öffnen; nicht sinnvoll in der Listbox anzuwenden!                                                     |
| `toggle`             | `SimpleHandler<Unit>`          | Handler zum Wechseln zwischen Offen und Geschlossen; nicht sinnvoll in der Listbox anzuwenden                     |


### listboxButton

Verfügbar im Scope von: `listbox`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `button`

### listboxLabel

Verfügbar im Scope von: `listbox`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`

### listboxValidationMessages

Verfügbar im Scope von: `listbox`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld | Typ                                      | Description                                                   |
|------------|------------------------------------------|---------------------------------------------------------------|
| `msgs`     | `Flow<List<ComponentValidationMessage>>` | stellt eine Liste von ``ComponentValidationMessage`` bereit   |

### listboxItems

Verfügbar im Scope von: `listbox`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld  | Typ         | Description                                                                                                                                                                                                                                         |
|-------------|-------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `placement` | `Placement` | definiert die Position des Bausteins, z.B. `Placement.top`, `Placement.bottomRight`, etc. Standardwert ist `Placement.auto`. Hierbei wird die vermutlich beste Position automatisch anhand des zur Verfügung stehenden sichtbaren Platzes bestimmt. |
| `strategy`  | `Strategy`  | legt fest, ob der Baustein `absolute` positioniert werden soll (default) oder `fixed`.                                                                                                                                                              |
| `flip`      | `Boolean`   | kommt der Baustein zu nah an den Rand des sichtbaren Bereichs, wechselt die Position automatisch auf die jeweils andere Seite, wenn dort mehr Platz zur Verfügung steht.                                                                            |
| `skidding`  | `Int`       | definiert den Abstand der Auswahlliste vom Referenzelement in Pixeln. Der Standardwert ist 10.                                                                                                                                                      |
| `distance`  | `Int`       | definiert die Verschiebung der Auswahlliste entlang des Referenzelements in Pixeln. Der Standardwert ist 0.                                                                                                                                         |


### listboxItem

Verfügbar im Scope von: `listboxItems`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `button`

| Scope Feld | Typ                      | Description                                                                                                                                                    |
|------------|--------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `index`    | `Int`                    | Der Index innerhalb der Items.                                                                                                                                     |
| `selected` | `Flow<Boolean>`          | Dieser Datenstrom liefert den Selektions-Status der verwalteten Option: `true` die Option ist selektiert, `false` wenn nicht.                                  |
| `active`   | `Flow<Boolean>`          | Dieser Datenstrom zeigt an, ob ein Eintrag fokussiert ist: `true` die Option hat den Fokus, `false` wenn nicht. Es kann immer nur eine Option den Fokus haben. |
| `disabled` | `Flow<Boolean>`          | Dieser Datenstrom zeigt an, ob ein Eintrag aktiv (`false`) oder inaktiv (`true`) ist.                                                                          |
| `disable`  | `SimpleHandler<Boolean>` | Dieser Handler aktiviert oder deaktiviert einen Eintrag                                                                                                        |
