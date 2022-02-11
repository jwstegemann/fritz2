---
title: Disclosure
layout: layouts/headlessWithContentNav.njk
permalink: /headless/disclosure/
eleventyNavigation:
    key: disclosure
    title: Disclosure
    parent: headless
    order: 70
demoHash: disclosure
teaser: "Ein Disclosure ermöglicht das Ein- und Ausblenden von Inhalten wie z.B. umschaltbare Akkordeon Menüs."
---

## Einfaches Beispiel

Ein Disclosure wird über die `disclosure` Komponenten Fabrik Funktion erzeugt. Innerhalb dieses Scopes müssen dann ein
Button durch `disclosureButton` und ein Panel via `disclosurePanel` erzeugt werden.

Der Button dient als Steuerelement zum Öffnen und wieder Schließen des Panel-Bereichs. Dies geschieht durch einen
Maus-Klick oder durch die [[Space]]-Taste, sofern der Button-Tag fokussiert ist.

```kotlin
disclosure {
    disclosureButton {
        +"When is being headless a good thing?"
    }
    disclosurePanel {
        +"As foundation for web components!"
    }
}
```

## Styling abhängig vom Zustand

Um das Styling oder auch ganze Strukturen abhängig vom Zustand des Disclosures zu gestalten, kann auf den booleschen
Datenstrom `opened` zugegriffen werden.

Typische Muster sind die Verwendung innerhalb von `className`, um verschiedene CSS-Klassen je nach Zustand zu setzen,
oder schlicht ein bedingtes Rendering.

```kotlin
disclosure {
    disclosureButton {
        +"When is being headless a good thing?"
        
        span("ml-6 h-7 flex items-center") {
            // add some Icon that reflects the state visually
            opened.render(into = this) {
                svg("h-6 w-6 transform") {
                    // change Icon depending on `opened` state
                    if (it) content(HeroIcons.chevron_up)
                    else content(HeroIcons.chevron_down)
                }
            }
        }
    }
    disclosurePanel {
        +"As foundation for web components!"
    }
}
```

## Öffnen und Schließen

Um das Öffnen auch programmatisch und unabhängig vom `disclosureButton` Element zu ermöglichen, kann optional eine
boolesche Datenbindung `openClose` in der Komponente gesetzt werden.

Zudem bietet ein Disclosure selber einen Handler `close` zum Schließen an, der man z.B. innerhalb des Panels an ein
Event koppeln kann, wie z.B. ein expliziter Schließen-Button.

```kotlin
val toggle = storeOf(true) // show Panel at start

disclosure {
    
    // establish two-way-data-binding
    openClose(toggle)
    
    disclosureButton {
        +"When is being headless a good thing?"
    }
    disclosurePanel {
        +"As foundation for web components!"
        button {
            +"Close"
            // use `close` Handler to explicitly close the panel from within
            clicks.map { false } handledBy close
        }
    }
}
```

## Schließen über das Panel

Sollte es gewünscht sein, dass eine Aktion auf dem Panel selber das Panel schließt, also ein Maus-Klick oder 
die [[Spabe]]-Taste, sofern das Panel fokussiert ist, so existiert ein dedizierter Baustein `disclosureCloseButton`
im Scope vom Panel.

```kotlin
disclosure {
    disclosureButton {
        +"When is being headless a good thing?"
    }
    disclosurePanel {
        disclosureCloseButton {
            +"As foundation for web components!"
            +"Simply click or press Space to close."
        }
    }
}
```

## Maus Interaction

Durch das Klicken auf einen `disclosureButton` wird das Panel geschlossen.

## Keyboard Interaction

| Command                                     | Description         |
|---------------------------------------------|---------------------|
| [[Space]] wenn Fokus auf `disclosureButton` | Schließt das Panel. |

## API

### Summary / Sketch
```kotlin
disclosure() {
    // Felder
    opened: Flow<Boolean>
    openClose: DatabindingProperty<Boolean> // optional
    close: SimpleHandler<Unit>

    // Bausteine
    disclosureButton() { }
    disclosurePanel() {

        // Bausteine
        disclosureCloseButton() {}
    }

}
```

### `disclosure`

Parameter: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope Feld  | Typ                            | Description                                                            |
|-------------|--------------------------------|------------------------------------------------------------------------|
| `opened`    | `Flow<Boolean>`                | Datenstrom vom Zustand des Disclose: `true` offen, `false` geschlossen |
| `openClose` | `DatabindingProperty<Boolean>` | Zwei-Wege-Datenbindung für das Öffnen und Schließen. Rein optional.    |
| `close`     | `SimpleHandler<Unit>`          | Handler zum Schließen des Disclosures von innen heraus                 |


### `disclosureButton`

Verfügbar im Scope von: `disclosure`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `button`

### `disclosurePanel`

Verfügbar im Scope von: `disclosure`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

### `disclosureCloseButton`

Verfügbar im Scope von: `disclosurePanel`

Parameter: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `button`