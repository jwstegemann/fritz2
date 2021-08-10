package dev.fritz2.components.appFrame

import kotlinx.browser.window

fun registerServiceWorker(jsFileName: String = "serviceWorker.js") {
    try {
        window.addEventListener("load", {
            window.navigator.serviceWorker.register(jsFileName)
        })
        console.log("ServiceWorker registered")
    } catch (t: Throwable) {
        console.log("Error registering ServiceWorker:", t)
    }
}