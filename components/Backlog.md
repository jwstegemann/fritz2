# Komponenten - Designentscheidungen

- Komponenten beginnen mit großem Buchstaben: ``Button``
- HTML Attribute-Werte werden nicht geändert oder erweitert
- keine Benennung anhand der Html-Komponenten, sondern Namen anhand der semantische Schnittstelle einer Komponente
  - Beispiel: ``SingleSelect`` statt ``Radiogroup``
- wo sinnvoll: _Convenience_ Funktionen für den simplen Standard-Use-Case
  - Beispiel: Aufruf von Komponenten-Factory nur mit Übergabe des ``Store``s, Anbindung an Standard-Handler ``update`` 
  wird dann intern durchgeführt.
  
## TODOs

- ``none`` bei _Background_ und _Border_ einbauen (Property mit getter)
- Aufteilung des ``Sizes`` Typs in zwei Unterelemente: ``element`` + ``container`` um Größen für kleine und große 
Elemente besser anzugeben.

## Eingabekomponenten

### Button (Jens)

- Varianten fürs Aussehen (reines Styling)
- überladene Funktion(en) für einfache Verwendung

### Input (Christian)

- ``type`` bleibt erhalten!
- ``variant`` für reines _Aussehen_ (Rahmen, Füllung, usw.)


## Auswahlkomponenten

- Trennung nach der Semantik der Auswahl → Hierarchie wird vertieft über die Darstellung (``representation`` als Parameter?)
- Elemente für...
  - An/Aus → ``Switch``
  - *einfache* Auswahl aus einer Liste → ``SingleSelect``
  - *mehrfache* Auswahl aus einer Liste → ``MultiSelect``

### Switch (Eva)

Variante / Repräsentation:
- checkbox
- switch
- radio

### SingleSelect (Eva)

Variante / Repräsentation:
- select
- radio

### MultiSelect (Eva)

Variante / Repräsentation:
- select
- checkbox
    
## Strukturierende Komponenten

### Box

### Flex

### Grid

### Stack

- Orientierung: vertical / horizontal
- Parameter oder getrennte Subkomponenten-Factory-Funktionen

### FormControl

- benötigt ``Input``
- zusammengesetzte / zusammensetzbare Komponente

## Weitere Komponenten

### Icon

- SVG-Icons per Theme
- **kein** Font
- benannte Varianten ``val safeIcon = ...`` → ``Icon { safeIcon }``

### Modal

- basiert auf _z-Index_
- ganz allgemeines Popup-"Fenster"