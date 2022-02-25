---
layout: layouts/post.njk
title: Was wir über Komponenten und Wiederverwendung in der Web-Frontend-Entwicklung gelernt haben.
description: Wir schreiben darüber, wie wir bisher mit fritz2 Komponenten gebaut und ausgeliefert haben, welche Probleme wir dabei immer stärker bemerkt haben und wieso ein neuer Ansatz für uns die Lösung darstellt. Zugleich verabschieden wir uns damit auch von den bisher mit fritz2 ausgelieferten Komponenten und stellen den neuen Ersatz dafür vor.
date: 2022-03-01
---

## Historie

Seit Anfang 2020 entwickeln wir bei und für die [Öffentliche Versicherung](https://www.oeffentliche.de) ein Frontend für
die Web-UI-Entwicklung in Kotlin: **fritz2**.

fritz2 ist ein reaktives UI-Framework. Im Kern stehen daher Funktionen zur Verfügung, um ein Datenmodell reaktiv mit
einem dazugehörigen User Interface zu verbinden. Dass bedeutet, dass Änderungen am Datenmodell unmittelbar zu
Änderungen an der Oberfläche führen und Events vom Browser wiederum das Datenmodell ändern. fritz2 stellt dazu
die notwendigen Werkzeuge bereit, um die Verwaltung des Datenmodells, das UI und entsprechend die zyklische, reaktive
Verbindung zwischen beiden Aspekten mit einer deklarativ ausgeprägten Syntax zu implementieren.

fritz2 versteht sich als leichtgewichtiges Framework, weswegen der Kern sich auf wenige, für reaktive Programmierung
elementare Kotlin-Basis-Features wie [Coroutines](https://kotlinlang.org/docs/coroutines-basics.html) und
insbesondere [Flows](https://kotlinlang.org/docs/flow.html) stützt. Die zentralen fritz2 Konzepte, wie Stores, Handler,
Events und Tags stützen sich auf und integrieren diese Kotlin Bausteine, aber verstecken sie nicht, sondern exponieren
diese ganz im Gegenteil auch als Teil des Framework APIs. Ein Benutzer muss also nicht primär fritz2 lernen, sondern
viel mehr die universellen Kotlin Kern-Konzepte der Coroutines und Flows.

Durch den Fokus auf die reaktive Verbindung von Modell und UI, den wenigen dafür notwendigen Konzepten und dem daraus
resultierenden leichtgewichtigem API, ermöglicht es fritz2, einfach und elegant moderne Browser basierte UIs zu
entwickeln.

## Motivation für Wiederverwendung

Eine Web-Applikation basiert im Kern natürlich auf HTML und CSS. Allerdings benötigt man für viele Applikationen immer
wieder die gleichen oder sehr ähnliche UI-Muster und Strukturen, die man allgemein auch *Komponenten* nennt. Solche
Komponenten bilden in sich abgeschlossene Einheiten, die Funktion, Darstellung und teilweise Datenhandling kapseln.

Die Verwendung - und damit motiviert auch die Entwicklung von solchen Komponenten - lohnt sich daher aufgrund der
folgenden Aspekte:

- Knappe Ressourcen: Der Fokus einer Anwendung liegt in der Regel auf der Fachlichkeit und damit auf einem höheren Level
  als die kleinteilige Erzeugung einer guten und funktionalen DOM-Struktur. Wenn man mehrere Applikationen innerhalb
  einer Organisation entwickelt, lohnt es sich umso mehr, grundlegende Bausteine wiederzuverwenden.
- Minimierung der Wartung: Bei Fehlern oder neuen Funktionen müssen nicht alle Stellen in einer Applikation oder allen
  Applikationen angepasst werden, die so ein betroffenes UI-Feature implementieren, sondern es genügt, die Komponenten
  an einer zentralen Stelle zu verbessern.
- Einheitliche UX: Viele Organisationen wünschen sich oder fordern gar ein einheitliches Aussehen, Strukturen und
  Bedienung innerhalb ihrer eigenen Anwendungen. Zentral gebaute und gepflegte Komponenten sind daher ein einfacher und
  effizienter Weg, solche Ansprüche umzusetzen.

## Naiver Lösungsansatz

Es war für uns folglich naheliegend, eine solche Komponentenbibliothek auch direkt in fritz2 einzubauen, um sie als
Basis für die Applikationen der Öffentlichen Versicherung zu nutzen.

Mit der Version [0.8](https://github.com/jwstegemann/fritz2/releases/tag/v0.8) haben wir begonnen,
eine [Komponentenbibliothek](https://components.fritz2.dev/#Welcome) in fritz2 zu integrieren, die dann - inkl. einer
eigenen deklarativen Sprache für das Styling - in den folgenden Versionen ausgebaut worden ist.

Diese umfasst um die 20 verschiedene Komponenten, die wichtige Bereiche vom einfachen Layout, über typische, einfache
Formular-Elemente, bis hin zu komplexen generischen Daten-Tabellen und ganzen Seiten-Rahmen umfasst.

Ein Grundgedanke dabei war, dass jede Komponente für sich immer noch individuell stylebar sein muss, um sie an den
speziellen Kontext der Verwendung innerhalb der Applikation anpassen zu können.

Darüber hinaus haben wir versucht, eine Theme-Struktur als Basis für das grundlegende Aussehen zu integrieren. Damit war
es möglich, die Komponenten für alle Applikationen einer Organisation aufgrund eines zentralen Styling-Dokuments an ein
einheitliches CI-Design anzupassen. Darüber hinaus konnten wir sogar noch eine Stufe darunter ansetzen, und ableitend
vom zentralen Organisations-Theme, für verschiedene Unternehmensbereiche angepasste Themes zu erstellen und zu
verwenden. Damit konnten tatsächlich zwei verschiedene Designkonzepte mit denselben Komponenten umgesetzt werden.

## Probleme des naiven Ansatzes

Leider sehen wir diesen Ansatz mittlerweile als gescheitert an!

Folgende Probleme haben wir dabei identifiziert:

- Unsere Komponenten ermöglichen es über das Theme, ihre Darstellung anzupassen. Dies umfasst zum großen Teil Styling
  Informationen, die sich aber entsprechend auf *bestimmte* HTML-Tags oder eine ganze DOM-Unterstruktur beziehen. Durch
  diesen starken Bezug zwischen Struktur und Darstellung, ist es schwierig Komponenten wirklich flexibel zu gestalten:
  - Soll die Struktur in verschiedenen Varianten vorliegen, so muss es auch Styling-Definitionen für jede Variante
    geben. Dies erhöht die Komplexität der Komponenten und des Themes - auch wenn innerhalb einer Applikation nur
    jeweils eine Variante zum Einsatz kommt. Als Beispiel sei die Position des Labels bei einem FormControl genannt:
    Entweder immer oberhalb des Eingabe-Elements oder immer links davon.
  - Oftmals muss man Styling für einen speziellen Status an mehreren Tags anwenden, um den gewünschten Effekt zu
    erzielen. Ein Beispiel dafür ist die Deaktivierung von Form Elementen, wo man Label und Eingabe-Element auf
    verschiedene Art und Weise für einen solchen Effekt stylen muss. Die Gefahr ist groß, dass der Benutzer beim
    Customizing des Themes solche speziellen Stylings durch seine Angaben zunichtemacht.
  - Kompliziert wird es auch, wenn das Styling von einem Status abhängig ist. Dieser muss entsprechend in den
    Abschnitten im Theme zugänglich gemacht werden. Ein Komponenten spezifischer Typ kann dabei nicht direkt weiter
    gereicht werden, da dieser auf der Paket-Ebene des Themes nicht verfügbar ist.
  - Der Benutzer wird immer vor dem Problem stehen, dass er ohne fundierte Kenntnisse der finalen DOM-Struktur einer
    Komponenten nicht wirklich sinnvoll Anpassungen im Theme vornehmen kann, da er verstehen muss, auf welchen Tag
    ein Styling aus dem Theme dann auch wirklich angewendet wird. Dies ist alleine durch gute Namensgebung nicht
    zu erreichen und alles andere als trivial.
  - Wenn es zur Behebung von Fehlern oder zum Hinzufügen von neuen Funktionen kommt, bei dem das Styling im Theme 
    angepasst werden muss, so kann es schlimmsten Falls dazu führen, dass ein vom Benutzer angepasstes Theme nicht 
    mehr richtig funktioniert. Schließlich muss der Benutzer die Änderungen im Default-Theme genau analysieren, um 
    die neuen, relevanten Styling-Definitionen an sein eigenes Theme anzupassen.
- Die Komponenten bestehen aus einem relativ schwer verständlichem Code. Vieles ist, wie bereits angedeutet, abhängig
  vom Theme realisiert und somit erst durch das Nachlesen des dort ausgelagerten Codes nachvollziehbar.
- Trotz der von Grund auf flexibel und anpassbar geplanten und umgesetzten Komponenten, kam schnell die Notwendigkeit
  auf, „ganz andere“ Komponenten bauen zu müssen. Die Anpassbarkeit ist in der Praxis bei weitem nicht so hoch, wie wir
  vermutet hatten. (Trotz der Komplexität des Codes. Siehe vorheriges Item)
- Die Konzentration bei der Erstellung lag primär auf dem Design, was idR. keine Kernkompetenz der Entwickler ist,
  anstelle sich auf Usability zu konzentrieren (ARIA, Keyboard-Support, Standard-Funktionen, etc.).
- Es bedeutet einen immensen Aufwand, alleine die 20 Komponenten in dieser Flexibilität mit guten Defaults
  bereitzustellen und zu warten. Dabei gäbe es immer noch Bedarf an weiteren, elementaren Komponenten, die noch nicht
  einmal umgesetzt sind! Außerdem fehlen an vielen Komponenten noch dringend benötigte Funktionen oder Features
  (Keyboard-Support, ARIA-Attribute, uvm.)

Die wichtigste Erkenntnis lässt sich daraus wie folgt ableiten: Darstellung und Funktionalität von Komponenten sind
stark voneinander abhängig!

Der Versuch beide Aspekte so voneinander zu trennen, dass die Komponente die Funktionalität und die Struktur kapselt,
aber das Styling über ein öffentliches API im Theme vollkommen anpassbar exponiert, ist letztlich nur ein Trugschluss.
In Wahrheit sind beide Teile weiterhin stark gekoppelt und nicht unabhängig voneinander. 

Damit ist es aus unserer Sicht ausgeschlossen, Komponenten zu erzeugen, die auf der einen Seite perfekte Funktionalität
garantieren, auf der anderen Seite wirklich flexibel anpassbar sind.

Was aber ist die Lösung aus diesem Dilemma? Unzählige, starre Spezial-Komponenten bauen, was neben dem immensen 
initialen Aufwand auch eine hohe Code-Dopplung und damit noch höheren Aufwand bei der Wartung nach sich ziehen würde?
Ganz auf zentrale Komponenten verzichten, aber dafür für jede Applikation immer alles von Neuem bauen?

Ein von uns neu entdecktes Konzept scheint die Lösung für das Dilemma zu sein, beide extremen Alternativen auf
elegante Art und Weise zu vereinen: **Headless** Components!

## Headless

Auf der Suche nach einem Ersatz für die bisherige Komponentenbibliothek von fritz2, sind wir
auf das brillante [headlessui](https://headlessui.dev/)
gestoßen, was wir als Lösung für uns aufgegriffen haben und das uns zugleich als Inspiration und Vorlage dient.

Die Idee dahinter ist so simpel, wie der Nutzen schwer zu begreifen ist: 
::: info
Eine Komponente wird allein durch die Funktionalität bestimmt und **nicht** durch DOM-Struktur und Darstellung!
:::

Das bedarf einer Erklärung!

Wenn man im Kern nur die grundlegende Funktion betrachtet, aber die Darstellung, also die DOM-Struktur und das Styling,
dabei bewusst ignoriert, lassen sich die myriaden an UI-Komponenten da draußen auf einige wenige reduzieren wie z.B.:

- eine einfache Selektion aus einer Liste von Optionen
- eine mehrfache Auswahl aus einer Liste von Optionen
- das Ein- und Ausblenden von Panelen, wie Dialoge, PopOver oder Action-Menüs
- das Einblenden von zugehörigen Inhalten zu einer ausgewählten Option
- das Eingeben von Text

Das Headless-Konzept macht sich genau diesen Umstand zu Nutzen und definiert Komponenten alleine durch deren
Funktionalität. Damit reduziert sich die Anzahl von solchen disjunkten Komponenten auf relativ wenige: 
Aktuell haben wir 14 Stück identifiziert, die wir bereits implementiert haben oder noch implementieren wollen.

Diese Komponenten werden dabei aus kleineren, wohldefinierten Bausteinen zusammengesetzt. So lässt sich die minimale
funktionale Struktur einer einfachen Auswahl wie folgt zerteilen:

```text
- Einfachauswahl
    - two-way data-binding (→ Was ist selektiert? → Vorauswahl)
    // für alle Optionen
        - Option (→ Ist diese Option selektiert?)
            - Toggle-Schalter (→ ändert Selektion)
            - Label    
```

In der Praxis ergänzen wir die Funktionalität noch um ein paar oftmals sinnvolle, aber dennoch optionale
Zusatzfunktionen:

```text
- Einfachauswahl
    - two-way data-binding
    - Label (optional)
    - Beschreibung (optional)
    - Validierung (optional) (→ Ist diese Auswahl i.O.? → Meldungen)
    // für alle Optionen
        - Option
            - Toggle-Schalter
            - Label
            - Beschreibung (optional)
```

Mittels dieser Funktionen kann man eine klassische, auf nativen `<input type="radio">`-Tags basierte RadioGroup bauen,
aber auch beliebige [andere Varianten](https://tailwindui.com/components/application-ui/forms/radio-groups#component-45612766894822db447a2837d79355f5)
sind damit realisierbar.

Der Unterschied besteht einzig und allein in der Auswahl und Struktur der HTML-Tags und des Stylings!

fritz2's Headless Komponenten bestehen aus solchen Kontext sensitiven Komponenten- und Bausteinfunktionen. Diese müssen
lediglich auf die für den Anwendungsfall passende Art und Weise kombiniert und ggf. mit Standard HTML-Elementen ergänzt
werden, damit die gewünschte funktionierende Struktur entsteht. Über spezielle Felder werden Status und Funktionen im
Kontext des Bausteins manipulierbar oder nutzbar.

Folgende (teilweise reduzierte) Beispiele zeigen zwei mögliche Umsetzungen einer RadioGroup.

Das erste Beispiel nutzt alle per default erzeugten Tags, basiert auf dem nativen `<input type="radio">` Tag und gibt
sich allgemein minimal:

```kotlin
val frameworks = listOf("fritz2", "Spring", "flask")
val storedChoice = storeOf(frameworks.first())
// headless start
radioGroup<String>() { // <div>
    value(storedChoice) // databinding
    frameworks.forEach { option ->
        radioGroupOption(option) { // <div>
            radioGroupOptionToggle() { // <input type=radio>
                radioGroupOptionLabel() { // <label>
                    +option 
                }
            }
        }
    }
}
```

Als zweites Beispiel wird eine Auswahl gezeigt, die einige per default erzeugte Tags durch andere Typen ersetzt - u.a.
verzichtet sie auf `<input>`-Elemente als Basis für den Toggle - und zusätzlich einen speziellen Datentypen für die
Auswahl unterstützt:

```kotlin
data class Framework(val name: String, val info: String)
val frameworks = listOf(
  Framework("fritz2", "Kotlin based, reactive, frontend based web framework"),
  // ... and some more...
)
val storedChoice = storeOf(null)
// headless start
radioGroup<Framework?>() { // <div>
    value(storedChoice) // databinding
    radioGroupLabel() { // <label>
        +"Choose some framework please" 
    }
    frameworks.forEach { option ->
        radioGroupOption(option) { // <div>
            radioGroupOptionToggle(tag = RenderContext::div) { // <div>
                div { // mix HTML tags as custom structure inside!
                    radioGroupOptionLabel(tag = RenderContext::span) { // <span>
                        +option.name
                    }
                    +" * "
                    radioGroupOptionDescription(tag = RenderContext::span) { // <span>
                        +option.info
                    }                    
                    selected.render { // use some headless function: access current state!
                        if(it) span { +"(selected)" }
                    }
                }
            }
        }
    }
}
```

Die wesentliche Erkenntnis dabei ist folgende: Wir können mithilfe derselben Bausteine, zwei in Struktur und
Darstellung verschiedene Varianten einer Einfachauswahl aka RadioGroup bauen. Der Aufwand ist im Verhältnis zur
Funktionlität relativ gering: Zwischen grob zehn und 25 Zeilen Code sind lediglich notwendig. Dafür unterstützen beide
Komponenten die Bedienung mit der Tastatur, ARIA-Attribute und natürlich die garantierte Funktionalität der
Einfach-Auswahl mittels Übergabe eines `Stores`. Die einzelnen Bausteine helfen dem Benutzer dabei mit ihrer
expressiven Namensgestaltung und erleichtern damit auch das spätere Verständnis des Codes.

Zusätzlich müsste man im Code noch jeden Baustein entsprechend stylen, um eine fertige und voll funktionale
Oberflächeneinheit erzeugt zu haben, was hier aus Platzgründen weggelassen worden ist! Prinzipiell reicht man das
Styling analog zu den bekannten Tag-Fabrik-Funktionen als ersten Parameter in die Baustein-Funktionen hinein. Der
Quellcode
des [Beispiels zu einer RadioGroup](https://github.com/jwstegemann/fritz2/blob/master/headless-demo/src/jsMain/kotlin/dev/fritz2/headlessdemo/radioGroup.kt)
aus dem `headless-demo`-Projekt zeigt anschaulich, dass die Styling-Angaben das ganze nur unwesentlich komplexer machen.

Um es noch einmal zu betonen: Übergibt man den Baustein-Funktionen keinerlei Styling, so werden die daraus erzeugten
HTML-Tags auch ohne jegliches Styling gerendert! 

Der bewusste Verzicht eines internen Stylings bei den Headless Komponenten hat noch einen weiteren, im Ökosystem des
Webs immensen Vorteil: Solche Komponenten sind offen für jede Art von Styling und CSS-Framework! Sei
es [tailwindcss](https://tailwindcss.com/), [Bootstrap](https://getbootstrap.com/)
oder natives CSS-Styling, jeder Benutzer kann die für ihn gewohnte und passende Technologie dafür wählen.

## Und wo sind jetzt die Komponenten?

Wie aus den [einführend geschilderten Problemen](#probleme-des-naiven-ansatzes) leicht zu erahnen ist, liefert fritz2 ab
sofort keine gestylten, für die Verwendung fertig zusammengestellten Komponenten mehr mit aus!

Stattdessen stellt fritz2, wie zuvor beschrieben, im neuen Modul `headless` die [Headless Components](/headless) bereit,
die als Basis für die Erstellung eigener Komponenten verwendet werden können.

Um aus den Headless Komponenten und ihrer Bausteine eine fertig gestylte und wiederverwendbare Komponente zu entwickeln,
bietet fritz2 zusätzliche Hilfsmittel im package `dev.fritz2.headless.foundation` an, welches ebenso die Basis für die
Headless Komponenten selber bildet. Besonders hervor zu heben sind dabei die Konzepte von [*Properties* und 
*Hooks*](/headless/#basic-concepts-for-configuration). Für den Komponentenbau unterstützen diese Hilfsmittel den Benutzer
dabei, ein nach außen verständliches und zugleich komfortables API zu schaffen, dabei aber so viel Flexibilität und
Kontrolle bei der Komponente selber zu behalten.

Für die Konzeption einer eigenen, gestylten und gebrauchsfertigen Komponente, sollte man daher genau auf diese
Hilfsmittel setzen. Diese lassen sich zudem optimal mit den Headless Komponenten verbinden: Properties haben einen
dedizierten Mechanismus, Daten an eine zugrunde liegende Property einer Headless Komponenten weiterzureichen.

## Ausblick

Neben den bisher implementierten Headless Komponenten haben wir bereits zwei weitere in der Pipeline, die unser
Portfolio weiter ergänzen und fehlende Funktionalitäten abbilden:

- *Toasts*: Dienen der Anzeige von flüchtigen Nachrichten, deren Anzeige und (automatisches) Ausblenden von der
  Komponente selber gehandhabt wird.
- *DataLists*: Dienen als Basis für die explizite Darstellung von Daten. Dabei liegt der Fokus auf der dynamischen
  Manipulation wie *filtern* oder *sortieren*. Dies kann als Grundlage für das Erstellen
  von [Data-Tabellen](https://tailwindui.com/components/application-ui/lists/tables) oder
  [Stacked-Lists](https://tailwindui.com/components/application-ui/lists/stacked-lists) verwendet werden.

Wir hoffen, dass wir Dich neugierig gemacht haben und Du dem Konzept eine Chance gibst! Natürlich würden uns auch sehr
über Dein Feedback freuen.

Für den Einstieg verweisen wir zunächst auf die [Dokumentation](/headless) der Headless-Komponenten, die zudem einige
der relevanten Basiskonzepte erklärt. Darüber hinaus schau Dir
das [`headless-demo` Modul](https://github.com/jwstegemann/fritz2/tree/master/headless-demo/src/jsMain/kotlin/dev/fritz2/headlessdemo)
an, in dem sich die Demos aus der Doku befinden.

Wir planen für den leichteren Einstieg in die Kombination fritz2 und tailwindcss ein dediziertes Template-Projekt in
Ergänzung zum [alt bekannten](https://github.com/jwstegemann/fritz2-template) zu erstellen. Außerdem wird es zukünftig
auch weitere Blog-Artikel rund um das Thema "Komponentenbau mit Headless als Basis" geben oder ggf. auch neue Artikel
im [Rezepte-Bereich](/recipes).

## Warme Worte zu den ausgedienten Komponenten

Wie bereits deutlich erwähnt, sind die alten Komponenten (und das Styling API) kein Bestandteil mehr von fritz2. Der
Code ist aber nicht verloren! Solltest Du Interesse haben, so kannst Du gerne die beiden relevanten Module aus dem
Zweig `release/0.14` in ein neues Projekt extrahieren und an die API-Änderungen anpassen. Bei Fragen dazu wende Dich
jederzeit an uns! Wir sind gerne bereit, Dich bei so einem Unterfangen im Rahmen unserer Möglichkeiten zu unterstützen.
