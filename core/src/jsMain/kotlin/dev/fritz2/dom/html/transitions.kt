package dev.fritz2.dom.html

import dev.fritz2.dom.*
import dev.fritz2.utils.classes
import dev.fritz2.utils.nativeFunction
import kotlinx.coroutines.await
import kotlinx.coroutines.awaitAnimationFrame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import org.w3c.dom.HTMLElement
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
                kotlinx.browser.window.awaitAnimationFrame()
                kotlinx.browser.window.awaitAnimationFrame()
                target.domNode.setAttribute("class", "$classes ${transition.leave} ${transition.leaveEnd.orEmpty()}")
                target.waitForAnimation()
            }
        }

        internal val enterTransition: DomLifecycleHandler = { target, payload ->
            val transition = payload.unsafeCast<Transition?>()
            if (transition?.enter != null) {
                val classes = target.domNode.getAttribute("class").orEmpty()
                target.domNode.setAttribute("class", classes(classes, transition.enterStart))
                kotlinx.browser.window.awaitAnimationFrame()
                kotlinx.browser.window.awaitAnimationFrame()
                target.domNode.setAttribute(
                    "class",
                    classes(classes, transition.enter, transition.enterEnd)
                )
                target.waitForAnimation()
                target.domNode.setAttribute("class", classes)
            }
        }
    }
}

/**
 * a native function returning a [Promise] that is completed, when the currently running animations (if any) on a fiven [Node] have finished.
 */
val animationDone = nativeFunction<(Node) -> Promise<Unit>>(
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
 * @receiver the [Tag] the transition will be applied to
 */
fun Tag<HTMLElement>.transition(transition: Transition) {
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
 * @receiver the [Tag] the transition will be applied to
 */
fun Tag<HTMLElement>.transition(
    enter: String? = null,
    enterStart: String? = null,
    enterEnd: String? = null,
    leave: String? = null,
    leaveStart: String? = null,
    leaveEnd: String? = null
) = transition(Transition(enter, enterStart, enterEnd, leave, leaveStart, leaveEnd))


/**
 * Applies a transition (enter and/or leave) to a [Tag] whenever a new value appears on a [Flow]
 * The enter-transition will be executed when `true` appears on the [Flow]
 * The leave-transition will be executed when `false` appears on the [Flow]
 * Processing of further operations will not wait for the animation to finish.
 *
 * @param on [Flow] to trigger the transition
 * @param transition definition of the enter- and leave-transition
 * @receiver the [Tag] the transition will be applied to
 */
fun Tag<HTMLElement>.transition(on: Flow<Boolean>, transition: Transition) {
    className(on.transform {
        if (it) {
            emit(transition.enterStart.orEmpty())
            kotlinx.browser.window.awaitAnimationFrame()
            kotlinx.browser.window.awaitAnimationFrame()
            emit(classes(transition.enter, transition.enterEnd))
            waitForAnimation()
            emit("")
        } else {
            emit(classes(transition.leaveStart))
            kotlinx.browser.window.awaitAnimationFrame()
            kotlinx.browser.window.awaitAnimationFrame()
            emit(classes(transition.leave, transition.leaveEnd))
            waitForAnimation()
            emit("")
        }
    })
}

/**
 * Applies a transition (enter and/or leave) to a [Tag] whenever a new value appears on a [Flow]
 * The enter-transition will be executed when `true` appears on the [Flow]
 * The leave-transition will be executed when `false` appears on the [Flow]
 * Processing of further operations will not wait for the animation to finish.
 *
 * @param on [Flow] to trigger the transition
 * @param enter mandatory classes to control the enter-transition.
 * @param enterStart optional classes to define the starting point of the enter-transition
 * @param enterEnd optional classes to define the end point of the enter-transition
 * @param leave mandatory classes to control the leave-transition.
 * @param leaveStart optional classes to define the starting point of the leave-transition
 * @param leaveEnd optional classes to define the end point of the leave-transition
 * @receiver the [Tag] the transition will be applied to
 */
fun Tag<HTMLElement>.transition(
    on: Flow<Boolean>,
    enter: String? = null,
    enterStart: String? = null,
    enterEnd: String? = null,
    leave: String? = null,
    leaveStart: String? = null,
    leaveEnd: String? = null
) = transition(on, Transition(enter, enterStart, enterEnd, leave, leaveStart, leaveEnd))

/**
 * wait for a running animation on the DOM-Node to finish.
 */
suspend fun WithDomNode<*>.waitForAnimation() {
    kotlinx.browser.window.awaitAnimationFrame()
    kotlinx.browser.window.awaitAnimationFrame()
    animationDone(domNode).await()
}



