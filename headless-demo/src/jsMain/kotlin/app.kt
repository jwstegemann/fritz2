import utils.renderTailwind
import utils.require

fun main() {
    require("./styles.css")
    renderTailwind {
        div("bg-indigo-200 w-full h-16") {
            +"Hallo Welt!"
        }
    }
}