// mutable 非同步協程，共享變數 (actor):https://kotlinlang.org/docs/shared-mutable-state-and-concurrency.html#actors


//似乎Dispatchers.Default本身還會產生許多協程，與原本理解不同

//失敗:
var counter = 0

fun main() = runBlocking {
    // confine everything to a single-threaded context
    withContext(Dispatchers.Default) {
        massiveRun {
            counter++
        }
    }
    println("Counter = $counter")
}
/*
我在的協程DefaultDispatcher-worker-4
我在的協程DefaultDispatcher-worker-9
我在的協程DefaultDispatcher-worker-7
我在的協程DefaultDispatcher-worker-10
我在的協程DefaultDispatcher-worker-8
我在的協程DefaultDispatcher-worker-5
我在的協程DefaultDispatcher-worker-6
我在的協程DefaultDispatcher-worker-3
我在的協程DefaultDispatcher-worker-11
我在的協程DefaultDispatcher-worker-1
Completed 1000 actions in 6 ms
Counter = 970
*/



//成功:
// 原因:沒有共同變數，皆在同一個協程完成
val counterContext6 = newSingleThreadContext("CounterContext")
var counter6 = 0

fun main6() = runBlocking {
    // confine everything to a single-threaded context
    withContext(counterContext6) {
        massiveRun {
            counter6++
        }
    }
    println("Counter = $counter6")
}
/*
我在的協程CounterContext
我在的協程CounterContext
我在的協程CounterContext
我在的協程CounterContext
我在的協程CounterContext
我在的協程CounterContext
我在的協程CounterContext
我在的協程CounterContext
我在的協程CounterContext
我在的協程CounterContext
Completed 1000 actions in 5 ms
Counter = 1000
*/

suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 10  // number of coroutines to launch
    val k = 100 // times an action is repeated by each coroutine
    val time = measureTimeMillis {
        coroutineScope { // scope for coroutines
            repeat(n) {
                launch {
                    repeat(k) { action() }
                    println("我在的協程${Thread.currentThread().name}")
                }
            }
        }
    }
    newSingleThreadContext("A")

    println("Completed ${n * k} actions in $time ms")
}
