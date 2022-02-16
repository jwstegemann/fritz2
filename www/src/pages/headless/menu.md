---
title: Menu 
layout: layouts/headlessWithContentNav.njk 
permalink: /headless/menu/ 
eleventyNavigation:
    key: menu
    title: Menu
    parent: headless 
    order: 50 
demoHash: menu 
teaser: "Ein ausklappbares Menü zur Auswahl auszuführender Aktionen inkl. Keyboard-Navigation"
---

## Einfaches Beispiel

Ein Drop-Down-Menü wird durch die Factory Funktion `fun menu()` erzeugt.

Durch einen Click auf den durch `menuButton` erzeugten Baustein oder [[Space]] bei fokussierter `menuButton`, wird die Auswahlliste eingeblendet.

Innerhalb der Auswahlliste kann mit Hilfe der Tastatur navigiert. Durch [[Enter]], [[Space]] oder einen Mausklick kann ein Element ausgewählt werden. Verliert das Menü den Fokus oder wird außerhalb der Auswahlliste geclickt, wird diese ausgeblendet.

Die Auswahlliste wird durch die Bausteinfabrik `menuItems` erzeugt. Innerhalb dieses Scopes werden durch `menuItem` die einzelnen Optionen definiert.

```kotlin
data class MenuEntry(val label: String, val icon: String)

val entries = listOf(
    MenuEntry("Duplicate", HeroIcons.duplicate),
    MenuEntry("Archive", HeroIcons.archive),
    MenuEntry("Move", HeroIcons.share),
)

val action = storeOf("")

menu {
    menuButton {
        +"Options"
    }

    menuItems {
        entries.forEach { entry ->
            menuItem {
                svg { content(entry.icon) }
                +entry.label
                selected.map { entry.label } handledBy action.update
            }
        }
    }
}
```

## Das aktive Item stylen

Wird durch die Tastatur oder durch Mausbewegung zu einem `menuItem` navigiert ist es `active`.

Im Scope eines `menuItem` steht `active` als `Flow<Boolean>` zur Verfügung, um schnell in Abhängigkeit des Status das Styling anzupassen oder bestimmte Elemente zu rendern oder auszublenden:

```kotlin
menuItem {
    className(active.map { 
        if (it) "bg-violet-500 text-white"
        else "text-gray-900"
    })
    
    svg { content(entry.icon) }
    +entry.label
    selected.map { entry.label } handledBy action.update
}
```

## Items disablen

Die einzelnen Items der Auswahlliste können statisch oder dynamisch disabled werden. Im Scope eines `menuItem` steht dazu der `Handler<Boolean>` `disable` zur Verfügung. Dieser kann direkt aufgerufen werden oder dazu genutzt werden, den disabled-Zustand eines Items in Abhängigkeit von einem externen Datenmodell dynamisch zu setzen. Der disabled-Zustand des Items steht darüber hinaus als `Flow<Boolean>` zur Verfügung, um das Item beispielsweise abhängig davon zu stylen.

```kotlin
data class MenuEntry(val label: String, val icon: String, val disabled: Boolean = false)

val entries = listOf(
    MenuEntry("Duplicate", HeroIcons.duplicate, true),
    MenuEntry("Archive", HeroIcons.archive),
    MenuEntry("Move", HeroIcons.share),
    MenuEntry("Delete", HeroIcons.trash),
    MenuEntry("Edit", HeroIcons.pencil),
    MenuEntry("Copy", HeroIcons.clipboard_copy, true),
    MenuEntry("Encrypt", HeroIcons.key)
)

val action = storeOf("")

menu {
    menuButton { /* ... */ }

    menuItems {
        entries.forEach { entry ->
            menuItem {
                className(active.combine(disabled) { a, d ->
                    if (a && !d) "bg-violet-500 text-white"
                    else {
                        if (d) "text-gray-300" else "text-gray-900"
                    }
                })

                svg { content(entry.icon) }
                +entry.label

                if (entry.disabled) disable(true)

                selected.map { entry.label } handledBy action.update
            }
        }
    }
}    
```

## Zustand der Auswahlliste

Das Menü ist eine [`OpenClose`-Komponente](../#closable-content---openclose). In ihrem Scope stehen verschiedene `Flow`s und `Handler` wie `opened` zur Verfügung, um basierend auf Öffnungszustand der Auswahlliste zu steuern oder diesen zu verändern.

Der Öffnungszustand des Menüs kann per Databinding an einen externen `Store` gebunden werden, z.B. um die Auswahlliste immer anzuzeigen.


## Transitionen

Das Ein- und Ausblenden der Auswahlliste lässt sich mit Hilfe von `transition` einfach animieren:

```kotlin
menuItems {
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

`menuItems` ist ein [`PopUpPanel`](../#floating-content---popuppanel) und stellt in seinem Scope daher Konfigurationsmöglichkeiten zur Verfügung, um z.B. die Position oder den Abstand der Auswahlliste zum `menuButton` als Referenzelement zu steuern:

```kotlin
menuItems {
    placement = Placement.Top
    distance = 20
    
    menuItem {
        //...
    }
    
    //...
}
```

## Focus Management

Ist die Auswahlliste des Menüs geöffnet, erhält der Baustein `menuItems` den Fokus. Verliert dieser den Fokus, wird die Auswahlliste geschlossen und der Fokus kehrt zum `menuButton` zurück.

## Maus Interaction

Ein Click auf den `menuButton` schaltet den Zustand der Auswahlliste um. Ein Click außerhalb der geöffneten Auswahlliste schließt diese. Wird die Maus in der geöffneten Liste über ein Item bewegt, wird dieses als aktiv gekennzeichnet. Ein Click auf ein Item bei geöffneter Liste wählt dieses aus und schließt die Liste.

## Keyboard Interaction

| Command                                          | Description                                          |
|--------------------------------------------------|------------------------------------------------------|
| [[Enter]] [[Space]] when `menuButton` is focused | Opens menu and activates first not disabled item     |
| [[⬆︎]] [[⬇]] ︎when menu is open                  | Activates previous / next item                       |
| [[Home︎]] [[End]] ︎when menu is open             | Activates first / last item                          |
| [[A-Z]] [[a-z]] when menu is open                | Activates first item beginning with according letter |
| [[Esc]] when menu is open                        | Closes menu                                          |
| [[Enter]] [[Space]] when menu is open            | Selects active item                                  |

## API

### Summary / Sketch

```kotlin
menu {
    // inherited by `OpenClose`
    val openClose = DatabindingProperty<Boolean>()
    val opened: Flow<Boolean>
    val close: SimpleHandler<Unit>
    val open: SimpleHandler<Unit>
    val toggle: SimpleHandler<Unit>

    menuButton() { }
    menuItems() {
        // inherited by `PopUpPanel`
        var placement: Placement
        var strategy: Strategy
        var flip: Boolean
        var skidding: Int
        var distance: int
        
        // for each T {
            MenuItem {
                val selected: Flow<Boolean>
                val active: Flow<Boolean>
                val disabled: Flow<Boolean>
                val disable: SimpleHandler<Boolean>
            }
        // }
    }
}
```

### menu

Parameter: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld           | Typ                            | Description                                                                                                    |
|----------------------|--------------------------------|----------------------------------------------------------------------------------------------------------------|
| `openClose`          | `DatabindingProperty<Boolean>` | Zwei-Wege-Datenbindung für das Öffnen und Schließen. Muss gesetzt werden!                                      |
| `opened`             | `Flow<Boolean>`                | Datenstrom der bezogen auf den "geöffnet"-Status boolesche Werte liefert; im Menu nutzlos, da immer `true`     |
| `close`              | `SimpleHandler<Unit>`          | Handler zum Schließen des Menus von innen heraus, sollte nicht verwendet werden, da dies automatisch passiert! |
| `open`               | `SimpleHandler<Unit>`          | Handler zum Öffnen; nicht sinnvoll im Menu anzuwenden!                                                         |
| `toggle`             | `SimpleHandler<Unit>`          | Handler zum Wechseln zwischen Offen und Geschlossen; nicht sinnvoll im Menu anzuwenden                         |


### menuButton

Verfügbar im Scope von: `menu`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `button`

### menuItems

Verfügbar im Scope von: `menu`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld  | Typ         | Description                                                                                                                                                                                                                                         |
|-------------|-------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `placement` | `Placement` | definiert die Position des Bausteins, z.B. `Placement.top`, `Placement.bottomRight`, etc. Standardwert ist `Placement.auto`. Hierbei wird die vermutlich beste Position automatisch anhand des zur Verfügung stehenden sichtbaren Platzes bestimmt. |
| `strategy`  | `Strategy`  | legt fest, ob der Baustein `absolute` positioniert werden soll (default) oder `fixed`.                                                                                                                                                              |
| `flip`      | `Boolean`   | kommt der Baustein zu nah an den Rand des sichtbaren Bereichs, wechselt die Position automatisch auf die jeweils andere Seite, wenn dort mehr Platz zur Verfügung steht.                                                                            |
| `skidding`  | `Int`       | definiert den Abstand der Auswahlliste vom Referenzelement in Pixeln. Der Standardwert ist 10.                                                                                                                                                      |
| `distance`  | `Int`       | definiert die Verschiebung der Auswahlliste entlang des Referenzelements in Pixeln. Der Standardwert ist 0.                                                                                                                                         |


### menuItem

Verfügbar im Scope von: `menuItems`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld | Typ                      | Description                                                                                                                                                        |
|------------|--------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `selected` | `Flow<Boolean>`          | Dieser Datenstrom liefert den Selektions-Status der verwalteten Option: `true` die Option ist selektiert, `false` wenn nicht. Nicht sinnvoll im Menu zu verwenden! |
| `active`   | `Flow<Boolean>`          | Dieser Datenstrom zeigt an, ob ein Eintrag fokussiert ist: `true` die Option hat den Fokus, `false` wenn nicht. Es kann immer nur eine Option den Fokus haben.     |
| `disabled` | `Flow<Boolean>`          | Dieser Datenstrom zeigt an, ob ein Eintrag aktiv (`false`) oder inaktiv (`true`) ist.                                                                              |
| `disable`  | `SimpleHandler<Boolean>` | Dieser Handler aktiviert oder deaktiviert einen Eintrag                                                                                                            |
