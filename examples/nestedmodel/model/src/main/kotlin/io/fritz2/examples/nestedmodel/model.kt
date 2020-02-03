package io.fritz2.examples.nestedmodel

import io.fritz2.optics.Lenses
import io.fritz2.optics.withId

@Lenses
data class Outer(val inner: Inner, val value: String, val seq: List<Element>)

@Lenses
data class Inner(val value: String)

@Lenses
data class Element(val value: String, override val id: String) : withId
