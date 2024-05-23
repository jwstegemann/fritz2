@file:Suppress("unused")

package dev.fritz2.core

import kotlinx.coroutines.await
import kotlinx.coroutines.awaitAnimationFrame
import kotlinx.coroutines.flow.*
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
                target.domNode.setAttribute("class", joinClasses(classes, transition.enterStart))
                kotlinx.browser.window.awaitAnimationFrame()
                kotlinx.browser.window.awaitAnimationFrame()
                target.domNode.setAttribute(
                    "class",
                    joinClasses(classes, transition.enter, transition.enterEnd)
                )
                target.waitForAnimation()
                target.domNode.setAttribute("class", classes)
            }
        }
    }
}

/**
 * a native function returning a [Promise] that is completed,
 * when the currently running animations (if any) on a given [Node] have finished.
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
 * @param transition definition of enter- and leave-transition
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
 * Applies a transition (enter and/or leave) to a [Tag] whenever a new value appears on a [Flow].
 *
 * The enter-transition will be executed when `true` appears on the [Flow].
 * The leave-transition will be executed when `false` appears on the [Flow].
 *
 * Processing of further operations will not wait for the animation to finish.
 *
 *
 * #### Fine-grained control
 *
 * For special use cases, [afterLeaveClasses] and/or [initialClasses] can be applied. Setting them is useful in cases
 * where fine-grained control over the transition is needed and the required classes cannot be set via [Tag.className]
 * due to timing problems.
 *
 * One example would be a use-case where hiding an element via CSS is combined with a transition that should be executed
 * right before the element is hidden. Since the display property cannot be animated via CSS, the hidden class has to be
 * manually applied after the transition has finished. In this case, using two separate calls to [Tag.className] and
 * [transition] would lead to a race-condition.
 *
 * Example to hide/show an element with a transition using Tailwind CSS:
 * ```
 * val opened = storeOf(false)
 * div {
 *     // initially hidden, faded in and out via the transition
 *     transition(
 *         on = opened,
 *         transition = Transition(...),
 *         hasLeftClasses = "hidden",
 *         initialClasses = "hidden"
 *     )
 * }
 * ```
 *
 *
 * @param on [Flow] to trigger the transition
 * @param transition definition of enter- and leave-transition
 * @param afterLeaveClasses Classes that should be present whenever the leave animation has been executed.
 * Present until the next enter transition is executed.
 * @param initialClasses Classes that should initially be present, before any transition has been executed.
 *
 * @receiver the [Tag] the transition will be applied to
 */
fun Tag<HTMLElement>.transition(
    on: Flow<Boolean>,
    transition: Transition,
    afterLeaveClasses: String? = null,
    initialClasses: String? = null
) {
    className(
        on.drop(
            // If initial classes are provided: Drop first element (initial class is set via the `initial` parameter
            // of the `joinClasses` function instead):
            if (initialClasses == null) 0 else 1
        ).transform { isEntering ->
            val (start, trans, end, afterEnd) = with(transition) {
                if (isEntering) arrayOf(enterStart, enter, enterEnd, "")
                else arrayOf(leaveStart, leave, leaveEnd, afterLeaveClasses)
            }
            emit(joinClasses(start))
            waitForAnimation()
            emit(joinClasses(trans, end))
            waitForAnimation()
            emit(joinClasses(afterEnd))
        },
        initial = joinClasses(initialClasses)
    )
}

/**
 * Applies a transition (enter and/or leave) to a [Tag] whenever a new value appears on a [Flow].
 *
 * The enter-transition will be executed when `true` appears on the [Flow].
 * The leave-transition will be executed when `false` appears on the [Flow].
 *
 * Processing of further operations will not wait for the animation to finish.
 *
 *
 * #### Fine-grained control
 *
 * For special use cases, [afterLeaveClasses] and/or [initialClasses] can be applied. Setting them is useful in cases
 * where fine-grained control over the transition is needed and the required classes cannot be set via [Tag.className]
 * due to timing problems.
 *
 * One example would be a use-case where hiding an element via CSS is combined with a transition that should be executed
 * right before the element is hidden. Since the display property cannot be animated via CSS, the hidden class has to be
 * manually applied after the transition has finished. In this case, using two separate calls to [Tag.className] and
 * [transition] would lead to a race-condition.
 *
 * Example to hide/show an element with a transition using Tailwind CSS:
 * ```
 * val opened = storeOf(false)
 * div {
 *     // initially hidden, faded in and out via the transition
 *     transition(
 *         on = opened,
 *         enter = "transition duration-100 ease-out",
 *         enterStart = "opacity-0 scale-y-95",
 *         enterEnd = "opacity-100 scale-y-100",
 *         leave = "transition duration-100 ease-in",
 *         leaveStart = "opacity-100 scale-y-100",
 *         leaveEnd = "opacity-0 scale-y-95",
 *         hasLeftClasses = "hidden",
 *         initialClasses = "hidden"
 *     )
 * }
 * ```
 *
 *
 * @param on [Flow] to trigger the transition
 * @param enter mandatory classes to control the enter-transition.
 * @param enterStart optional classes to define the starting point of the enter-transition
 * @param enterEnd optional classes to define the end point of the enter-transition
 * @param leave mandatory classes to control the leave-transition.
 * @param leaveStart optional classes to define the starting point of the leave-transition
 * @param leaveEnd optional classes to define the end point of the leave-transition
 * ram afterLeaveClasses Classes that should be present whenever the leave animation has been executed.
 * Present until the next enter transition is executed.
 * @param initialClasses Classes that should initially be present, before any transition has been executed.
 *
 * @receiver the [Tag] the transition will be applied to
 */
fun Tag<HTMLElement>.transition(
    on: Flow<Boolean>,
    enter: String? = null,
    enterStart: String? = null,
    enterEnd: String? = null,
    leave: String? = null,
    leaveStart: String? = null,
    leaveEnd: String? = null,
    hasLeftClasses: String? = null,
    initialClasses: String? = null
) = transition(on, Transition(enter, enterStart, enterEnd, leave, leaveStart, leaveEnd), hasLeftClasses, initialClasses)


/**
 * wait for a running animation on the DOM-Node to finish.
 */
suspend fun WithDomNode<*>.waitForAnimation() {
    runCatching {
        kotlinx.browser.window.awaitAnimationFrame()
        kotlinx.browser.window.awaitAnimationFrame()
        animationDone(domNode).await()
    }
}