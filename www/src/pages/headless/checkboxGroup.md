---
title: CheckboxGroup 
layout: layouts/headlessWithContentNav.njk 
permalink: /headless/checkboxgroup/ 
eleventyNavigation:
    key: checkbox
    title: CheckboxGroup
    parent: headless 
    order: 40 
demoHash: checkboxGroup 
teaser: "Ich bin der Teaser für die checkboxen"
---

## Usage

Dies ist ein Test

Mal sehen, ob [links](http://google.com) auch gehen.

## Another one

Dies ist ein Test


## Focus Management

Dies ist ein Test

Noch ein Absatz

## API

### Summary / Sketch
```kotlin
checkboxGroup() {
    // Felder
    value: DatabindingPropert<List<T>>

    // Bausteine
    checkboxGroupLabel() { }
    checkboxGroupValidationMessages() {
        // Felder    
        messages: Flow<List<ComponentValidationMessage>>
    }
    // for each T {
        checkboxGroupOption(option: T) {
            // Felder
            selected: Flow<Boolean>
    
            // Bausteine
            checkboxGroupOptionToggle() { }
            checkboxGroupOptionLabel() { }
            checkboxGroupOptionDescription() { } // use multiple times
        }
    // }
}
```