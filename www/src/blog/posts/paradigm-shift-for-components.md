---
layout: layouts/post.njk
image: paradigm-shift-for-components.jpg
type: article
title: What we have learned about reuse and components in web front-end development.
description: "This article is about how we built and delivered components with fritz2 so far, the problems we noticed
more and more and why a new approach is the solution for us. At the same time we're saying goodbye to the components 
delivered with fritz2 up to version 0.14 and introduce their new and improved replacement."
date: 2022-03-01
author: chausknecht
readtime: 15
---

## History

Since early 2020, we have been developing a frontend library at and for [Öffentliche Versicherung](https://www.oeffentliche.de)
for Web UI development in Kotlin: **fritz2**.

fritz2 is a reactive UI framework. At its core, it provides functions for reactively linking a data model to a
corresponding user interface. This means that changes to the data model immediately lead to changes to the user
interface, and events from the browser in turn change the data model. fritz2 provides the necessary tools to manage the
data model, the UI, and the cyclic, reactive connection between both aspects using a declarative syntax.

fritz2 sees itself as a lightweight framework, which is why the core relies on a few Kotlin features basic for reactive
programming like [Coroutines](https://kotlinlang.org/docs/coroutines-basics.html)
and especially [Flows](https://kotlinlang.org/docs/flow.html). The central fritz2 concepts, like stores, handler, events, 
and tags rely on and integrate these Kotlin building blocks and do not hide them, but on the contrary expose them 
as part of the framework API. So a user does not primarily have to learn fritz2, but much more the universal Kotlin core
concepts of coroutines and flows.

By focusing on the reactive connection of model and UI, the few concepts necessary for this and the resulting
lightweight API, fritz2 allows to easily and elegantly develop modern browser-based UIs.

## Motivation for reuse

A web application is of course based on HTML and CSS at its core. However, for many applications you need the same or
very similar UI patterns and structures, commonly called *components*. Such components form self-contained units that
encapsulate function, presentation and, in some cases, data handling.

The use - and thus also the development of such components - is worthwhile in itself due to the following
aspects:

- Scarce resources: The focus of an application usually lies on the domain and thus on a higher level than the
  small-scale generation of a good and functional DOM structure. When developing multiple applications within an
  organization, it is even more worthwhile to reuse basic building blocks.
- Minimization of maintenance: In case of errors or new features, not all places in an application or in all
  applications that implement an affected UI feature need to be touched; it is sufficient to improve the components in a
  central location.
- Uniform UX: Many organizations require a uniform appearance, structures, and operation within their own
  applications. Centrally built and maintained components are therefore a simple and efficient way to implement such
  requirements.

## Naive approach

It was therefore obvious to us to integrate such a component library directly into fritz2, in order to use it as the
basis for the applications of the "Öffentliche Versicherung".

We started with version [0.8](https://github.com/jwstegemann/fritz2/releases/tag/v0.8), to integrate
a [component library](https://components.fritz2.dev/#Welcome) into fritz2, which then - including a custom declarative
language for styling - has been extended in the following versions.

This library consisted of 20 different components, which covered the range from the simple layout, over typical,
simple form elements, up to complex generic data tables and whole page frames.

A basic idea was that each component must still be individually styleable in order to adapt it to the specific context
of use within the application.

In addition, we tried to incorporate a theme structure as the basis for the look and feel. This made it possible to
adapt the components for all applications of an organization to a uniform corporate identity design on the basis of a
central styling document (style system). In addition, we took it one step further and derived from the
central organization theme, to create and use customized themes for different areas of the company. This actually made
it possible to implement two different design concepts with the same components.

## Problems of the naive approach

Unfortunately, we now consider this approach a failure.

We have identified the following problems with it:

- Our components allow their appearance to be customized via the theme. This includes styling
  information to a large extent, but corresponds to *certain* HTML tags or an entire DOM substructure. Because of this 
  strong relation between structure and presentation, it is difficult to make components really flexible:
  - If there are different variants of the structure, there must also be styling definitions for each variant. This
    increases the complexity of the components and the theme - even if only one variant is used within an application.
    An example is the position of the label in a FormControl: it is either always above the input element or always to 
    the left of it.
  - Often styling needs to be applied to several tags for a special state to achieve a desired effect. An example of
    this is the deactivation of form elements, where you can style the label and input element in different ways for
    such an effect. There is a great risk of the user undoing the special styling with his specifications when 
    customizing the theme.
  - It also gets complicated when the styling is dependent on a status. This must be made available accordingly in the
    sections in the theme. A component specific type can not be passed on directly because it is not available at the
    package level of the theme. So you must fall back to base types as a workaround, with the cost of loosing
    expressive semantic types and thus a loss of intention revealing.
  - The user will always face the problem of not being able to make really meaningful adjustments to the theme without
    in-depth knowledge of the final DOM structure of a component, because he has to understand to which tag a styling
    from the theme would be applied. This cannot be achieved by good naming alone and is anything but trivial.
  - If it comes to fixing bugs or adding new features which require a change of the styling in the theme, the worst case
    scenario is that a customized theme no longer works properly. Finally, the user must
    carefully analyze the changes made to the default theme in order to adapt the new, relevant styling definitions to
    his own theme.
- The components consist of code that is relatively difficult to understand. As already indicated, much is dependent on
  the theme and thus only comprehensible by reading the code there.
  - Despite the components having been planned and implemented from ground up to be flexible and customizable, the need
    to build "completely different" components for a similar use case soon arose - they turned out to be way less
    adaptable than anticipated, despite the complexity of the code. See previous item.
- Our focus during component creation was primarily on the design, which is in general no core competence of the developers,
  instead of focusing on usability (ARIA, keyboard support, standard functions, etc.).
- It takes immense effort to provide and maintain just 20 components alone in this flexibility with good defaults, not 
  factoring in the need for additional, not yet implemented components. In addition,
  urgently needed functions or features of existing components are still missing (keyboard support, ARIA attributes, and
  much more).

The most important conclusion we derived from these problems is that presentation and functionality of components
strongly depend on each other.

Trying to separate both aspects in a way that the component encapsulates the functionality and the structure,
but exposes the styling in a fully customizable way via a public API in the theme, is ultimately just a fallacy.
In truth, both parts are still strongly coupled and not independent of each other.

In our opinion, this makes it impossible to create components which guarantee perfect functionality on one hand, while 
being really flexible and adaptable on the other.

But what is the solution to this dilemma? To build innumerable, rigid special components, which, in addition to the
immense initial effort, would also result in a high level of code duplication and thus even greater maintenance effort?
Or go without central components at all, but build everything from scratch for every application?

We finally came across a concept that seems to provide a solution for combining both extreme alternatives in
an elegant way: **Headless** Components!

## Headless

In search of a replacement for the previous component library of fritz2, we found the
brilliant [headlessui](https://headlessui.com/) which served as inspiration
and template for us at the same time.

The idea behind it is as simple as the benefit is hard to grasp:

::: info 
A component is solely defined by its functionality and **not** by DOM structure and presentation.
:::

This requires explanation: If one considers only the basic function in the core, but the representation (DOM
structure and styling), the myriads of UI components out there can be reduced to a few like:

- a single selection from a list of options
- a multiple selection from a list of options
- showing and hiding panels like dialogs, popovers or action menus
- the fading in of associated content to a selected option
- the typing of text

The headless concept takes advantage of this fact and defines components solely by their functionality. Thus, the number
of such disjunctive components is reduced to relatively few: Currently, we have identified 15 of them that we have
already implemented or are about to implement.

These components are composed of smaller, well-defined building blocks (we call them *bricks*). Thus, the minimal
functional structure of a simple selection can be decomposed as follows:

```text
- single selection
    - two-way data binding (→ What is selected? → Preselection)
    // for each option
        - option (→ Is this option selected?)
            - toggle (→ changes selextion)
            - label
```

In practice, we supplement the functionality with a few often useful, but still optional additional functions:

```text
- single selection
    - two-way data binding
    - label (optional)
    - description (optional)
    - validation (optional) (→ Is this selection valid? → Messages)
    // for each option
        - option
            - toggle
            - label
            - description (optional)
```

Using these functions, you can build a classic RadioGroup based on native `<input type="radio">` tags,
but also any [other variants](https://tailwindui.com/components/application-ui/forms/radio-groups#component-45612766894822db447a2837d79355f5)
can be realized with it. They differ only in the selection method and structure of the HTML tags and styling.

fritz2's headless components consist of such context-sensitive component and module functions. These must be combined in
a way that suits the application and, if necessary, supplemented with standard HTML elements to create the desired
 structure. Special fields are used to manipulate statuses and functions in the context of the module.

The following (partly reduced) examples show two possible implementations of a RadioGroup.

The first example uses all tags created by default, is based upon the native `<input type="radio">` tag and is
generally minimal:

```kotlin
val frameworks = listOf("fritz2", "Spring", "flask")
val storedChoice = storeOf(frameworks.first(), job = Job())
// headless start
radioGroup<String>() { // <div>
    value(storedChoice) // data binding
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

As a second example, a selection is shown that replaces some tags created by default with other types - e.g. it
dispenses with `<input>` elements as a basis for the toggle - and in addition supports a special data type for the
selection:

```kotlin
data class Framework(val name: String, val info: String)
val frameworks = listOf(
  Framework("fritz2", "Kotlin based, reactive, frontend based web framework"),
  // ... and some more...
)
val storedChoice = storeOf(null, job = Job())
// headless start
radioGroup<Framework?>() { // <div>
    value(storedChoice) // data binding
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

The key insight here is the following: We can build two variants of a single selection aka RadioGroup with the same
building blocks. The effort is relatively low in relation to the functionality: Between ten and 25 lines of code are
necessary. But both components support keyboard operation, set appropriate ARIA attributes and, of course, deliver the
guaranteed functionality of the single selection by passing a 'store'. All the heavy work of the state handling is done
by the headless component itself, like reacting to user actions for selecting, setting a preselected option, indicating
the selected or active (mouse hovered) option and so on. This is a huge benefit compared to crafting this by yourself!

The individual components help the user with their expressive naming and thus also foster a better understanding
of the code as well.

In addition, one would still have to style each building block accordingly within the code in order to have a finished
and fully functional surface unit, which was omitted here for reasons of space and simplicity. In principle, one injects
the styling into the function block as the first parameter, analogous to the known tag factory functions. The source
code of
the [example for a RadioGroup](https://github.com/jwstegemann/fritz2/blob/master/headless-demo/src/jsMain/kotlin/dev/fritz2/headlessdemo/radioGroup.kt)
from the `headless-demo` project clearly shows that the styling specifications make the whole thing only slightly more
complex.

To emphasize it again: If you do not pass any styling to the brick functions, the resulting HTML tags will be rendered
without any styling!

The deliberate omission of internal styling for headless components has yet another advantage, which is immense in the
ecosystem of the web: Such components are open for any kind of styling and CSS framework, be
it [tailwindcss](https://tailwindcss.com/), [Bootstrap](https://getbootstrap.com/)
or native CSS styling. Each user can choose the technology he is used to and that suits him.

## And where are the components now?

As you can easily guess from the [problems described in the introduction](#problems-of-the-naive-approach), from now on
fritz2 will no longer ship styled components ready to use.

Instead, as described before, fritz2 provides the [headless components](/headless) in the new module `headless`, which
can be used as a basis for creating your own components.

To develop a ready styled and reusable component from the headless components and their bricks, fritz2 provides
additional tools in the package `dev.fritz2.headless.foundation`, which also form the foundation for the headless
components themselves. Particularly noteworthy are the concepts of
[*properties* and *hooks*](/headless/#basic-concepts-for-configuration). For the component construction, these aids
support the user to create an outwardly understandable and at the same time comfortable API, thereby preserving so much
flexibility and control in the component itself.

For the conception of an individual, styled and ready to use component, one should rely on these tools. They
can also be optimally combined with the headless components: Properties have a dedicated mechanism to pass on data to an
underlying property of a headless component.

## Outlook

In addition to the headless components implemented so far, we already have a few more in the pipeline that will further
complement our portfolio and cover missing functionalities, for example the *TypeAhead* (aka ComboBox). They serve as 
basis for (single) selections with large data sets that can be filtered by typing.

We hope that we have made you curious and that you will give our concept a chance! Of course, we would also be very happy
about your feedback.

To get started, please refer to the [documentation](/headless) of the headless components, which also explains some of
the most relevant basic concepts. Furthermore, have a look at
the [`headless-demo` module](https://github.com/jwstegemann/fritz2/tree/master/headless-demo/src/jsMain/kotlin/dev/fritz2/headlessdemo)
which contains all the demos from the docs.

For an easier introduction to the combination of fritz2 and tailwindcss, we have 
created a dedicated [template project](https://github.com/jwstegemann/fritz2-tailwind-template)
in addition to the [well known](https://github.com/jwstegemann/fritz2-template) one. We encourage you to use those
templates as basis for your own fritz2 based applications.

In the future, there will be more blog articles around the topic "component building with headless".

## Warm words about the retired components

As already clearly mentioned, the old components (and the styling API) are no longer part of fritz2. By the end of 2022,
we also stopped patches on the 0.14 branch. 

We would love to hear some feedback about the migration path you take!

If you have specific questions about the headless approach, its documentation so far, or probably you create your own
headless based components with fritz2, please let us know!

For now, we wave goodbye to the old components and welcome the exciting new headless components!