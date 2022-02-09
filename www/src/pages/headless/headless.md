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