package utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// TODO: Move out of fritz2 imho! -> Tailwind styling!
object Visibility {
    val dropOut =
        "transition-[visibility,all] duration-[0ms,100ms] delay-[100ms,0ms] ease-[linear,ease-in] opacity-0 scale-95"
    val dropIn = "duration-100 ease-out opacity-100 scale-100"
    fun dropOn(visible: Flow<Boolean>) = visible.map { if (it) dropIn else dropOut }

    val fadeOut =
        "transition-[visibility,all] duration-[0ms,100ms] delay-[100ms,0ms] ease-[linear,ease-in] opacity-0"
    val fadeIn = "duration-100 ease-out opacity-100"
    fun fadeOn(visible: Flow<Boolean>) = visible.map { if (it) fadeIn else fadeOut }

    val popOut =
        "transition-[visibility,all] duration-[0ms,150ms] delay-[150ms,0ms] ease-[linear,ease-in] opacity-0 translate-y-1"
    val popIn = "duration-200 ease-out opacity-100 translate-y-0"
    fun popOn(visible: Flow<Boolean>) = visible.map { if (it) popIn else popOut }

}

