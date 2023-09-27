import kotlinx.coroutines.*
import kotlin.test.Test

class TestTest {

    suspend fun test2(): Job {
        val job = GlobalScope.launch {
            System.err.println(coroutineContext.job)
            System.err.println("Start")
            delay(1000)
            System.err.println("stop")
        }

        val scope = (GlobalScope + job)
        return scope.launch {
            System.err.println(coroutineContext.job)
            System.err.println("Start2")
            delay(5000)
            System.err.println("stop2")
        }

    }

    @Test
    fun test(): Unit = runBlocking {
        val job = test2();
        System.gc()
        job.join()
    }

}