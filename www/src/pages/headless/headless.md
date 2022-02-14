---
title: Headless
layout: layouts/docs.njk 
permalink: /headless/ 
eleventyNavigation:
    key: headless 
    title: Headless Components 
    order: 20
    icon: puzzle
    classes: "font-bold capitalize"
---

Hier steht dann der Willkommentext für die Headless-Komponenten

## Allgemeine Syntax Konventionen

- Aufruf einer Fabrik Funktion / Baustein-Funktion:
  - ``component("Some CSS classes", tag = RenderContext::div) { /* Attribute / Tags / Bausteine */ }``

| Parameter    | Typ                       | Default                            | Description                                    |
|--------------|---------------------------|------------------------------------|------------------------------------------------|
| `classes`    | `String?`                 | `null`                             | Beliebige CSS Klassen                          |
| `id`         | `String?`                 | `null`                             | Expliziter Identifikator                       |
| `scope`      | `ScopeContext.() -> Unit` | `{}`                               | Ausdruck zum Setzen von Scope Key-Value-Paaren |
| `tag`        | `TagFactory<Tag<C>>`      | einfach: `button` mit Label: `div` | Tag-Fabrik                                     |
| `initialize` | `Switch<C>.() -> Unit`    | -                                  | Scope für die Komponente                       |


## Properties
 beschreiben


## Hooks
 beschreiben


## Databinding

Einige Headless-Komponenten unterstützen Databinding. Das bedeutet, dass...
Hier beschreiben, wie die Property-aufgebaut ist, die unterschiedlichen Verwendungsarten, etc, so dass an der Komponenten nur noch der Datentyp, etc. genannt werden muss und ob DB unterstützt wird oder nicht.

## Closable Content - OpenClose

Einige der Headless Komponenten können geöffnet und geschlossen werden, beispielsweise indem im geöffneten Zustand content ausklappt (`disclosure`) oder ein PopUp erscheint (`popOver`). Diese Komponenten implementieren die abstrakte Klasse `OpenClose`.

Im Scope von dieser Komponenten stehen verschiedene `Flow`s und `Handler` bereit, um auf den Öffnungszustand der Komponente zu reagieren oder diesen zu beeinflussen:

- `opened: Flow<Boolean>` beschreibt, ob die Komponente geöffnet oder geschlossen ist
- `open : Handler<Unit>` öffnet die Komponente
- `close : Handler<Unit>` schließt die Komponente
- `toggle : Handler<Unit>` schließt die Komponente, wenn sie geöffnet ist und andersherum

Der Öffnungszustand einer solchen Komponente kann über die Property `openClose` per Databinding z.B. an einen externen `Store` oder `Flow` gebunden werden. Über diesen kann z.B. die Sichtbarkeit der Auswahlliste einer `listbox` dann unabhängig vom Standardverhalten gesteuert werden, also z.B. immer offen gehalten werden:

```kotlin
listbox<String> {
    //...

    listboxItems {
        openClose(data = flowOf(true))
        
        characters.forEach { entry ->
            listboxItem(entry) {
                //...
            }
        }
    }
}
```

## Floating Content - PopUpPanel

Einige Bausteine der Headless Komponenten (z.B. das `popOverPanel` oder die `listboxItem`) werden dynamisch positioniert und schweben über dem übrigen Inhalt. Oft werden diese auch dynamisch ein- und ausgeblendet.

Diese Bausteine sind mit Hilfe der Bibliothek [Popper.js]("https://popper.js.org/") realisiert. Dementsprechend bieten sie eine einheitliche Konfigurationsschnittstelle, um die wichtigsten Attribute  

Im Scope eines solchen Bausteins, der die abstrakte Klasse `PopUpPanel` implementiert, stehen folgende Konfigurationen zur Verfügung, um die Positionierung des Inhaltes zu beeinflussen:

- `placement` definiert die Position des Bausteins, z.B. `Placement.top`, `Placement.bottomRight`, etc. Standardwert ist `Placement.auto`. Hierbei wird die vermutlich beste Position automatisch anhand des zur Verfügung stehenden sichtbaren Platzes bestimmt.
- `strategy` legt fest, ob der Baustein `absolute` positioniert werden soll (default) oder `fixed`.
- `flip` kommt der Baustein zu nah an den Rand des sichtbaren Bereichs, wechselt die Position automatisch auf die jeweils andere Seite, wenn dort mehr Platz zur Verfügung steht.
- `distance` definiert den Abstand der Auswahlliste vom Referenzelement in Pixeln. Der Standardwert ist 10.
- `skidding` definiert die Verschiebung der Auswahlliste entlang des Referenzelements in Pixeln. Der Standardwert ist 0.

