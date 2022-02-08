---
layout: layouts/docsWithContentNav.njk
title: Mount-Point
permalink: /docs/mount-point/
eleventyNavigation:
    key: mount-point
    parent: documentation
    title: Mount-Point
    order: 64
---

A mount-point in fritz2 is an anchor of a `Flow` somewhere in a structure like the DOM-tree. Afterwards, each value 
appearing on the mounted `Flow` will be put into the structure at exactly that position replacing the former value.

Most of the time you will use mount-points in the browser's DOM, allowing you to mount `Tag`s to some point in the 
html-structure you are building using for example the `someFlow.render {}` function.

Inside the `RenderContext` opened by `someFlow.render {}`, a new mount-point is created as a `<div>`-tag and 
added to the current parent-element. Whenever a new value appears on the `Flow`, the new content is rendered 
and replaces the old elements.

In this `RenderContext`, any number of root elements can be created (also none).
```kotlin
someIntFlow.render {
    if(it % 2 == 0) p { +"is even" }
    // nothing is rendered if odd        
}
// or multiple elements
someStringFlow.render { name ->
    h5 { +"Your name is:" }
    div { +name }
    hr {}
}
```
The latter example will result in the following DOM structure:
```html
<div class="mount-point" data-mount-point> <!-- created by `render` function -->
  <h5>Your name is:</h5>
  <div>Chris</div>
  <hr/>
</div>
```
The CSS-class `mount-point` consists only of a `display: contents;` directive so that the element will not appear
in the visual rendering of the page.

Whenever the mount-point is definitely the only sub-element of its parent element, you can omit the dedicated 
`<div>`-mount-point-tag by setting the `into` parameter to the parent element. In this case the rendering engine
uses the existing parent node as reference for the mount-point:
```kotlin
render {
    dl { // `this` is <dl>-tag within this scope
        flowOf("fritz2" to "Awesome web frontend framework").render(into = this) { (title, def) ->
            //                                                      ^^^^^^^^^^^
            //                        define parent node as anchor for mounting    
            dt { +title }
            dd { +def }
        }
    }
}
```
This will result in the following DOM structure:
```html
<dl data-mount-point> <!-- No more dedicated <div> needed! Data attribute gives hint that tag is a mount-point -->
  <dt>fritz2</dt>
  <dd>Awesome web frontend framework</dd>
</dl>
```