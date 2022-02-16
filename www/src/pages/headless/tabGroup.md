---
title: TabGroup
layout: layouts/headlessWithContentNav.njk
permalink: /headless/tabgroup/
eleventyNavigation:
    key: tabgroup
    title: TabGroup
    parent: headless
    order: 100
demoHash: tabGroup
teaser: "Eine TabGroup ermöglicht das Umschalten von Inhalten über eine horizontale oder vertikale Liste von 
Tabulatoren."
---

## Einfaches Beispiel

TabGroups werden mit Hilfe der `tabGroup` Fabrik Funktion erzeugt. Es ist in zwei Bereiche unterteilt: In der
`tabList` werden die verfügbaren `tab`s dargestellt, im `tabPanel` der Inhalt des gerade aktiven Tabs.

Durch einen Mausklick auf einen Tab oder durch die Auswahl via Tastatur kann der aktuelle Tab gewählt und damit
auch der Inhalt des Panels umgeschaltet werden.

Die TabGroup ist vollständig agnostisch bezüglich des Typen eines Tabs als auch seines Inhalts. Darum benötigt die
Komponente auch keinerlei Informationen zu den verfügbaren Tabs. Die Tabs an sich werden intern über den *Index*
verwaltet, den sie beim Hinzufügen in die `tabList` inne hatten. Struktur und Inhalt von Tabs und Panels können
vollkommen frei gestaltet werden.

Eine [Datenbindung](#aktiven-tab-von-außen-setzen-und-abfragen) ist rein optional und muss nicht angegeben werden.

```kotlin
// Some domain type and a collection of data to be displayed inside a tab-group 
data class Posting(val id: Int, val title: String, val date: String, val commentCount: Int, val shareCount: Int)

val categories = mapOf(
    "Recent" to listOf(
        Posting(1, "Does drinking coffee make you smarter?", "5h ago", 5, 2),
        Posting(2, "So you've bought coffee... now what?", "2h ago", 3, 2)
    ), // ...
)

tabGroup {
    tabList {
        // typical pattern to use a loop to create the tabList
        categories.keys.forEach { category ->
            tab { +category }
        }
    }
    tabPanels {
        // for each tab there must be a corresponding panel
        categories.values.forEach { postings ->
            panel {
                ul {
                    postings.forEach { posting ->
                        li { +posting.title }
                    }
                }
            }
        }
    }
}
```

## Aktiven Tab stylen

Um den aktiven Tab bezüglich des Styles von den restlichen abzuheben, ist im Scope von `tab` der boolesche Datenstrom
`selected` verfügbar.

Dieser kann benutzt werden, um in Kombination mit `className` verschiedene Stile auf einen Tab anzuwenden oder sogar
ganze Elemente (z.B. ein Icon für den selektierten Tab) ein- und auszublenden.

```kotlin
tabGroup {
    tabList {
        categories.keys.forEach { category ->
            tab {                 
                // use `selected` flow in order to apply separate styling to the tabs 
                className(selected.map { sel ->
                    if (sel == index) "bg-white shadow"
                    else "text-blue-100 hover:bg-white/[0.12] hover:text-white"
                })
                
                +category 
            }
        }
    }
    tabPanels {
        // omitted
    }
}
```

## Aktiven Tab von außen setzen und abfragen

Wie bereits eingangs beschrieben, werden die Tabs lediglich über ihren Index (`0` basiert!) verwaltet.

Aus diesem Grund kann optional im Scope der `tabGroup` Fabrik-Funktion eine `Int` basierte Datenbindung `value` 
angegeben werden. Damit kann sowohl der initial aktive Tab bestimmt werden, als auch über einen anzugebenden Handler 
der aktuell gewählte Tab abgefragt werden.

Wird die Datenbindung nicht angegeben, so wird initial immer der erste, aktive Tab gewählt.

```kotlin
val currentIndex = storeOf(1) // preselect *second* tab (0-based as all collections in Kotlin)

currentIndex.data handledBy {
    console.log("Current Index is: $it")
}

tabGroup {
    
    // apply two-way-data-binding via index based store
    value(currentIndex)
    
    tabList {
        categories.keys.forEach { category ->
            tab { +category }
        }
    }
    tabPanels {
        // omitted
    }
}
```

## Deaktivieren von Tabs

Tabs können dynamisch aktiviert und auch deaktiviert werden. Deaktivierte Tabs können weder per Maus noch per Tastatur
aktiviert werden, noch werden sie als initialer Tab gewählt.

Per default ist jeder Tab zunächst immer aktiv.

Um einen Tab zu aktivieren oder zu deaktivieren, steht im Scope von `tab` der boolesche Handler `disable` zur Verfügung.

Der aktuelle Status kann über den booleschen Datenstrom `disabled` abgefragt werden. Letzteres ist primär für das
Styling relevant, denn ein deaktivierter Tab sollte sich optisch von aktiven abheben.

```kotlin
tabGroup {
    tabList {
        categories.keys.forEach { category ->
            tab {
                // reduce opacity for disabled tabs
                className(disabled.map { sel ->
                    if (sel == index) "opacity-50" else ""
                })
                
                +category
                
                // simply disable tab "Trending" forever
                if(category == "trending") disable(true)
                
                // toggle disable state of tab "Popular" every 5 seconds
                if(category == "Popular") {
                    generateSequence { listOf(true, false) }.flatten().asFlow()
                        .onEach { delay(5000) } handledBy disable
                }
            }
        }
    }
    tabPanels {
        // omitted
    }
}
```

## Vertikale TabGroup

Eine TabGroup kann sowohl horizontal (default) als auch vertikal dargestellt werden. Diese Unterscheidung ist an sich
nur abhängig von der optischen Gestaltung, aber ändert aber die Bedienung per Tastatur und muss dementsprechend der
Komponente explizit bekannt gemacht werden.

Bei einer horizontalen TabGroup kann man mittels den Pfeil-Tasten [[←]] und [[→]] zwischen den Tabs wechseln, bei einer
vertikalen TabGroup mit [[↑]] und [[↓]].

Dazu existiert die Property `orientation`, die die beiden Enum-Werte `Horizontal` oder `Vertical` annehmen kann.

```kotlin
tabGroup {
    
    // will be evaluated only once at the initial rendering, so it is not reactive!
    orientation = Orientation.Vertical
    
    tabList {
        // omitted        
    }
    tabPanels {
        // omitted
    }
}
```

## Maus Interaction

Das Klicken auf ein mit ``tab`` erzeugtes Element aktiviert den Tab und rendert das zugehörige Panel als Inhalt,
sofern der Tab nicht deaktiviert ist.

## Keyboard Interaction

| Command                                       | Description                                           |
|-----------------------------------------------|-------------------------------------------------------|
| [[←]] [[→]]                                   | Wählt zyklisch den vorherigen / nächsten aktiven Tab. |
| [[↑]] [[↓]] when `orientation` is `Vertical`  | Wählt zyklisch den vorherigen / nächsten aktiven Tab. |
| [[Home]] [[PageUp]]                           | Wählt den ersten aktiven Tab.                         |
| [[End]] [[PageDown]]                          | Wählt den letzten aktiven Tab.                        |


## API

### Summary / Sketch
```kotlin
tabGroup() {
    val value: DatabindingProperty<Int> // optional
    val selected: Flow<Int>
    var orientation: Orientation

    tabList() {
        // for each tab {
            tab() {
                val disabled: Flow<Int>
                val disable: SimpleHandler<Int>
            }
        // }
    }

    tabPanels() {
        // for each tab {
            panel() { }
        // }        
    }
}
```

### tabGroup

Parameter: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld    | Typ                        | Description                                                                           |
|---------------|----------------------------|---------------------------------------------------------------------------------------|
| `value`       | `DatabindingProperty<Int>` | Zwei-Wege-Datenbindung für das Setzen und Abfragen des aktuellen Index. Rein Optional |
| `selected`    | `Flow<Int>`                | Datenstrom mit dem aktuellen Tab-Index                                                |
| `orientation` | `Orientation`              | Feld zum Einstellen der Orientierung. Default ist `Horizontal`                        |


### tabList

Verfügbar im Scope von: `tabGroup`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`


### tab

Verfügbar im Scope von: `tabList`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld | Typ                      | Description                                                |
|------------|--------------------------|------------------------------------------------------------|
| `disabled` | `Flow<Boolean>`          | Datenstrom der angibt, ob ein Tab aktiv oder inaktiv ist.  |
| `disable`  | `SimpleHandler<Boolean>` | Handler für das Setzen des inaktiven Status.               |


### tabPanels

Verfügbar im Scope von: `tabGroup`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`


### panel

Verfügbar im Scope von: `tabPanels`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`