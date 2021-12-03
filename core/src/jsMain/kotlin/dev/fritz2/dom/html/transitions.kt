package dev.fritz2.dom.html

import dev.fritz2.dom.DomLifecycleHandler
import dev.fritz2.dom.Tag
import dev.fritz2.dom.afterMount
import dev.fritz2.dom.beforeUnmount
import dev.fritz2.utils.nativeFunction
import kotlinx.coroutines.await
import org.w3c.dom.Node
import kotlin.js.Promise


/**
 * Definition of a css-transition (enter and/or leave) to be executed when a [Tag] is
 * mounted to or removed from the DOM.
 */
class Transition(
    /**
     * Classes to control the enter-transition.
     * Needs to be defined for the enter-transition to be executed.
     */
    val enter: String? = null,
    /**
     * Optional classes to define the starting point of the enter-transition
     */
    val enterStart: String? = null,
    /**
     * Optional classes to define the end point of the enter-transition
     */
    val enterEnd: String? = null,
    /**
     * Classes to control the leave-transition.
     * Needs to be defined for the leave-transition to be executed.
     */
    val leave: String? = null,
    /**
     * Optional classes to define the starting point of the leave-transition
     */
    val leaveStart: String? = null,
    /**
     * Optional classes to define the end point of the leave-transition
     */
    val leaveEnd: String? = null
) {
    companion object {
        internal val leaveTransition: DomLifecycleHandler = { target, payload ->
            val transition = payload.unsafeCast<Transition?>()
            if (transition?.leave != null) {
                val classes = target.domNode.getAttribute("class").orEmpty()
                target.domNode.setAttribute("class", "$classes ${transition.leaveStart.orEmpty()}")
                target.domNode.setAttribute("class", "$classes ${transition.leave} ${transition.leaveEnd.orEmpty()}")
                animationDone(target.domNode).await()
            }
        }

        internal val enterTransition: DomLifecycleHandler = { target, payload ->
            val transition = payload.unsafeCast<Transition?>()
            if (transition?.enter != null) {
                val classes = target.domNode.getAttribute("class").orEmpty()
                target.domNode.setAttribute("class", "$classes ${transition.enterStart.orEmpty()}")
                //this has to be called two times to ensure, that the enterStart classes are really rendered by the browser
                kotlinx.browser.window.requestAnimationFrame {
                    kotlinx.browser.window.requestAnimationFrame {
                        target.domNode.setAttribute(
                            "class",
                            "$classes ${transition.enter} ${transition.enterEnd.orEmpty()}"
                        )
                        animationDone(target.domNode).then {
                            target.domNode.setAttribute("class", classes)
                        }
                    }
                }
            }
        }
    }
}

internal val animationDone = nativeFunction<(Node) -> Promise<Unit>>(
    "_node", block = """
         return Promise.all(
           _node.getAnimations().map(
             function(animation) {
               return animation.finished
             }
           )
         )
    """.trimIndent()
)

/**
 * Applies a transition (enter and/or leave) to a [Tag].
 * The enter-transition will be executed right after the [Tag] is mounted to the DOM.
 * The leave-transition will be executed right before the [Tag] is removed from the DOM.
 * Further operation of the MountPoint rendering the [Tag] is suspended until the leave-animation is done.
 *
 * @param transition definition of the enter- and leave-transition
 * @param content lambda returning the [Tag] the transition will be applied to
 */
fun RenderContext.transition(transition: Transition, content: RenderContext.() -> Tag<*>) =
    content().apply {
        if (transition.leave != null) beforeUnmount(transition, Transition.leaveTransition)
        if (transition.enter != null) afterMount(transition, Transition.enterTransition)
    }

/**
 * Applies a transition (enter and/or leave) to a [Tag].
 * The enter-transition will be executed right after the [Tag] is mounted to the DOM.
 * The leave-transition will be executed right before the [Tag] is removed from the DOM.
 * Further operation of the MountPoint rendering the [Tag] is suspended until the leave-animation is done.
 *
 * @param enter mandatory classes to control the enter-transition.
 * @param enterStart optional classes to define the starting point of the enter-transition
 * @param enterEnd optional classes to define the end point of the enter-transition
 * @param leave mandatory classes to control the leave-transition.
 * @param leaveStart optional classes to define the starting point of the leave-transition
 * @param leaveEnd optional classes to define the end point of the leave-transition
 * @param content Lambda returning the [Tag] the transition will be applied to
 */
fun RenderContext.transition(
    enter: String? = null,
    enterStart: String? = null,
    enterEnd: String? = null,
    leave: String? = null,
    leaveStart: String? = null,
    leaveEnd: String? = null, content: RenderContext.() -> Tag<*>
) = transition(Transition(enter, enterStart, enterEnd, leave, leaveStart, leaveEnd), content)



