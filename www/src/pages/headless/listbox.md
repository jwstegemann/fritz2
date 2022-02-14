---
title: Listbox 
layout: layouts/headlessWithContentNav.njk 
permalink: /headless/listbox/ 
eleventyNavigation:
    key: listbox
    title: Listbox (Select)
    parent: headless 
    order: 10 
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

Im Scope von `listboxItems` stehen verschiedene `Flow`s und `Handler` bereit, um auf den Zustand der Auswahlliste zu regieren oder diesen zu beeinflussen:

- `opened: Flow<Boolean>` beschreibt, ob die Auswahlliste sichtbar ist, oder nicht
- `open : Handler<Unit>` öffnet die Auswahlliste
- `close : Handler<Unit>` schließt die Auswahlliste
- `toggle : Handler<Unit>` schließt die Auswahlliste, wenn sie geöffnet ist und andersherum

Die Listbox verwaltet den Zustand der Auswahlliste standardmäßig intern. Alternativ
kann dieser Zustand über die Property `openClose` der `listboxItems` per Databinding z.B. an einen externen `Store` oder `Flow` gebunden werden. Über diesen kann die Sichtbarkeit der Auswahlliste dann unabhängig vom Standardverhalten gesteuert werden, also z.B. immer offen gehalten werden:

```kotlin
val alwaysOpen = storeOf(true)    
    
listbox<String> {
    //...

    listboxItems {
        openClose(alwaysOpen)

        characters.forEach { entry ->
            listboxItem(entry) {
                //...
            }
        }
    }
}
```

## Transitionen

Das Ein- und Ausblenden der Auswahlliste lässt sich mit Hilfe von `transition` einfach animieren:

```kotlin
listboxItems {
    transition(opened,
        "transition duration-100 ease-out",
        "opacity-0 scale-95",
        "opacity-100 scale-100",
        "transition duration-100 ease-in",
        "opacity-100 scale-100",
        "opacity-0 scale-95"
    )


    characters.forEach { (entry, disabledState) ->
        listboxItem(entry) {
            //...
        }
    }
}
```

## Auswahlliste positionieren

Im Scope von `listboxItems` stehen folgende Konfigurationen zur Verfügung, um die Positionierung der Liste zu beeinflussen:

- `placement` definiert die Position der Auswahlliste, z.B. Placement.top, Placement.bottomRight, etc. Standardwert ist `Placement.auto`. Hierbei wird die vermutlich besste Position der Liste automatisch bestimmt, je nachdem, wie viel Platz auf dem Bildschirm zur Verfügung steht. 
- `strategy` legt fest, ob die Auswahlliste `absolute` positioniert werden soll (default) oder `fixed` 
- `flip` kommt die Listbox zu nah an den Rand des sichtbaren Bereichs, wechselt die Auswahlliste automatisch auf die jeweils andere Seite, wenn dort mehr Platz zur Verfügung steht.
- `distance` definiert den Abstand der Auswahlliste vom `listboxButton` in Pixeln. Der Standardwert ist 10.
- `skidding` definiert die Verschiebung der Auswahlliste entlang des `listboxButton` in Pixeln. Der Standardwert ist 0.

Die Auswahlliste ist mit Hilfe der Bibliothek [Popper.js]("https://popper.js.org/") realisiert.

## Validierung

Die Datenbindung erlaubt es der Listbox Komponente, die Validierungsnachrichten abzugreifen und einen eigenen Baustein listboxValidationMessages anzubieten, der nur dann gerendert wird, wenn Nachrichten vorliegen. Diese Nachrichten werden in seinem Scope dem Anwender als Datenstrom messages zur Verfügung gestellt.

```kotlin

```

## Focus Management

Ist die Auswahlliste der Listbox geöffnet, erhält der Baustein `listboxItems` den Fokus. Verliert dieser den Fokus, wird die Auswahlliste geschlossen.

## Maus Interaction

Ein Click auf den `listboxButton` schaltet den Zustand der Auswahlliste um. Ein Click außerhalb der geöffneten Auswahlliste schließt diese. Wir die Maus in der geöffneten Liste über ein Item bewegt, wird dieses als aktiv gekennzeichnet. Ein Click auf ein Item bei geöffneter Liste wählt dieses aus und schließt die Liste.

## Keyboard Interaction

| Command                                             | Description                                          |
|-----------------------------------------------------|------------------------------------------------------|
| [[Enter]] [[Space]] when `listBoxButton` is focused | Opens listbox and focuses current selected item      |
| [[⬆︎]] [[⬇]] ︎when listbox is open                  | Activates previous / next item                       |
| [[Home︎]] [[End]] ︎when listbox is open             | Activates first / last item                          |
| [[A-Z]] [[a-z]] when listbox is open                | Activates first item beginning with according letter |
| [[Esc]] when listbox is open                | Closes listbox                                       |
| [[Enter]] [[Space]] when listbox is open            | Selectes active item                                 |

## API

### Summary / Sketch

```kotlin

```