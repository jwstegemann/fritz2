---
title: PopOver 
layout: layouts/headlessWithContentNav.njk 
permalink: /headless/popover/ 
eleventyNavigation:
    key: popover
    title: PopOver
    parent: headless 
    order: 70 
demoHash: popover 
teaser: "Ein schwebener Container für beliebigen Content, wie z.B. Navigations-Menüs, Hilfetexte, etc."
---

## Einfaches Beispiel

Ein PopOver wird durch die Factory Funktion `fun popOver()` erzeugt.

Durch einen Click auf den durch `popOverButton` erzeugten Baustein oder [[Space]] bei fokussiertem `popOverButton`, wird der durch `popOverPanel` erzeugte schwebende Container mit eingeblendet.

Durch [[Esc]] oder Click außerhalb des sichtbaren Containers wird das `popOverPanel` geschlossen.

```kotlin
data class Solution(val name: String, val description: String, val icon: String)

val solutions = listOf(
    Solution("Insights", "Measure actions your users take", HeroIcons.academic_cap),
    Solution("Automations", "Create your own targeted content", HeroIcons.adjustments),
    Solution("Reports", "Keep track of your growth", HeroIcons.archive)
)

popOver {
    popOverButton {
        span { +"Solutions" }
    }

    popOverPanel {
        solutions.forEach { item ->
            a {
                div {
                    svg { content(item.icon) }
                }
                div {
                    p { +item.name }
                    p { +item.description }
                }
            }
        }
    }
}
```

## Zustand des Containers

PopOver ist eine [`OpenClose`-Komponente](../#closable-content---openclose). In ihrem Scope stehen verschiedene `Flow`s und `Handler` wie `opened` zur Verfügung, um basierend auf Öffnungszustand der Auswahlliste zu steuern oder diesen zu verändern:

```kotlin
popOverButton {
    className(opened.map { if (it) "" else "text-opacity-90" })
    span { +"Solutions" }
}
```

Der Öffnungszustand des PopOvers kann per Databinding an einen externen `Store` gebunden werden um beispielsweise auf andere Trigger als einen Click auf den popOver-Button zu reagieren.


## Transitionen

Das Ein- und Ausblenden der Auswahlliste lässt sich mit Hilfe von `transition` einfach animieren:

```kotlin
popOverPanel {
    transition(
        opened,
        "transition ease-out duration-200",
        "opacity-0 translate-y-1",
        "opacity-100 translate-y-0",
        "transition ease-in duration-150",
        "opacity-100 translate-y-0",
        "opacity-0 translate-y-1"
    )

    //...    
}
```

## Position des Containers

`popOverPanel` ist ein [`PopUpPanel`](../#floating-content---popuppanel) und stellt in seinem Scope daher Konfigurationsmöglichkeiten zur Verfügung, um z.B. die Position oder den Abstand des Containers zum `popOverButton` als Referenzelement zu steuern:

```kotlin
popOverPanel {
    placement = Placement.Bottom
    distance = 20
    
    //...
}
```

## Pfeil

Ein Pfeil, der auf den `popOverButton` kann einfach hinzugefügt und bei Bedarf gestyled werden:

```kotlin
popOverPanel {
    arrow("h-3 w-3 bg-white")

    //...
}
```

## Focus Management

Um die Interaktion mit der restlichen Applikation auch per Tastensteuerung zu unterbinden, ist in das `popOverPanel`
von Haus aus eine sogenannte Fokus-Falle integriert. Daher zirkuliert der Fokus mittels [[Tab]] durch alle
fokussierbaren Elemente, ohne den Container zu verlassen.

Per default wird immer das erste fokussierbare Element mit dem Fokus versehen. Um ein bestimmtes Tag mit dem initialen Fokus zu versehen, kann die Funktion `setInitialFocus` in einem `Tag` aufgerufen werden.


## Maus Interaction

Ein Click auf den `popOverButton` schaltet den Zustand des Containers um. Ein Click außerhalb des geöffneten Containers schließt diesen.

## Keyboard Interaction

| Command                                             | Description      |
|-----------------------------------------------------|------------------|
| [[Enter]] [[Space]] when `popOverButton` is focused | Opens container  |
| [[Esc]] when container is open                      | Closes container |

## API

### Summary / Sketch

```kotlin
popOver {
    // inherited by `OpenClose`
    val openClose = DatabindingProperty<Boolean>()
    val opened: Flow<Boolean>
    val close: SimpleHandler<Unit>
    val open: SimpleHandler<Unit>
    val toggle: SimpleHandler<Unit>

    popOverButton() { }
    popOverPanel() {
        // inherited by `PopUpPanel`
        var placement: Placement
        var strategy: Strategy
        var flip: Boolean
        var skidding: Int
        var distance: int
    }
}
```

### popOver

Parameter: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld           | Typ                            | Description                                                                                                   |
|----------------------|--------------------------------|---------------------------------------------------------------------------------------------------------------|
| `openClose`          | `DatabindingProperty<Boolean>` | Zwei-Wege-Datenbindung für das Öffnen und Schließen. Muss gesetzt werden!                                     |
| `opened`             | `Flow<Boolean>`                | Datenstrom der bezogen auf den "geöffnet"-Status boolesche Werte liefert; im PopOver nutzlos, da immer `true` |
| `close`              | `SimpleHandler<Unit>`          | Handler zum Schließen des PopOver von innen heraus.                                                           |
| `open`               | `SimpleHandler<Unit>`          | Handler zum Öffnen; nicht sinnvoll im PopOver anzuwenden!                                                     |
| `toggle`             | `SimpleHandler<Unit>`          | Handler zum Wechseln zwischen Offen und Geschlossen; nicht sinnvoll im PopOver anzuwenden.                    |

### popOverButton

Verfügbar im Scope von: `popOver`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `button`

### popOverPanel

Verfügbar im Scope von: `popOver`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld  | Typ         | Description                                                                                                                                                                                                                                         |
|-------------|-------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `placement` | `Placement` | definiert die Position des Bausteins, z.B. `Placement.top`, `Placement.bottomRight`, etc. Standardwert ist `Placement.auto`. Hierbei wird die vermutlich beste Position automatisch anhand des zur Verfügung stehenden sichtbaren Platzes bestimmt. |
| `strategy`  | `Strategy`  | legt fest, ob der Baustein `absolute` positioniert werden soll (default) oder `fixed`.                                                                                                                                                              |
| `flip`      | `Boolean`   | kommt der Baustein zu nah an den Rand des sichtbaren Bereichs, wechselt die Position automatisch auf die jeweils andere Seite, wenn dort mehr Platz zur Verfügung steht.                                                                            |
| `skidding`  | `Int`       | definiert den Abstand der Auswahlliste vom Referenzelement in Pixeln. Der Standardwert ist 10.                                                                                                                                                      |
| `distance`  | `Int`       | definiert die Verschiebung der Auswahlliste entlang des Referenzelements in Pixeln. Der Standardwert ist 0.                                                                                                                                         |

