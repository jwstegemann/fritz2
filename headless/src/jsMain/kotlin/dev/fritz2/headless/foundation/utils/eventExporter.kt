package dev.fritz2.headless.foundation.utils

import dev.fritz2.headless.foundation.Initialize

class Exporter<T : Any>(initialize: Initialize<Exporter<T>>) {
    lateinit var payload: T

    fun export(payload: T): T {
        this.payload = payload
        return payload
    }

    init {
        initialize()
    }
}

fun <T : Any> export(scope: Exporter<T>.() -> Unit): T =
    Exporter(scope).payload
