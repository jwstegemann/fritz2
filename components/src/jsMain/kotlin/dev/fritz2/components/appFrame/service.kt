package dev.fritz2.components.appFrame

import dev.fritz2.components.appFrame
import kotlinx.browser.window

/**
 * Call this function at the beginning of your `main()` function to enable the
 * [PWA](https://developer.mozilla.org/en-US/docs/Web/Progressive_web_apps) functionality of the [appFrame]
 * component by registering your `serviceWorker.js` file.
 *
 * @param jsFileName path to your serviceWorker.js file to load
 */
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