import com.sun.org.apache.xerces.internal.dom.TextImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*
import kotlinx.html.*
import kotlinx.html.consumers.filter
import kotlinx.html.dom.*
import kotlinx.html.dom.createHTMLDocument
import org.w3c.dom.Element

@ExperimentalCoroutinesApi
class Var<T>(initValue: T) {
    private val channel = ConflatedBroadcastChannel<T>(initValue)

    fun value(): T = channel.value

    @FlowPreview
    fun flow(): Flow<T> = channel.asFlow()

    suspend fun set(value: T): Unit = channel.send(value)
}


abstract class SingleMountPoint<T>(upstream: Flow<T>) {
    init {
        GlobalScope.launch {
            upstream.collect {
                set(it, last)
                last = it
            }
        }
    }

    var last: T? = null

    abstract fun set(value: T, last: T?): Unit
}

class PrintingIntMountPoint(val title: String, val upstream: Flow<Int>): SingleMountPoint<Int>(upstream) {
    override fun set(value: Int, last: Int?) {
        println("$title --> new Value: $value")
    }
}


@FlowPreview
@ExperimentalCoroutinesApi
fun main() {

    runBlocking {

        val x = Var<Int>(10)

        val mountPointRaw = PrintingIntMountPoint("raw", x.flow())

        val mountPointTwice = PrintingIntMountPoint("twice", x.flow().map { it * 2 })

        while(true) {
            val inputString: String? = readLine()
            val inputInt : Int? = inputString?.toInt()
            if (inputInt != null) x.set(inputInt) else break
        }

        println("...done!")

    }

}