fun main() = runBlocking<Unit> {
    val mHandler = CoroutineExceptionHandler {
            context, exception -> println("CaughtAAA $exception")
    }

    //錯誤例子:
    val scope = CoroutineScope(Job())
    scope.launch(mHandler) {
        launch {
            throw Exception("Failed1 coroutine")
        }
    }

    //錯誤例子:
    scope.launch(mHandler) {
        launch {
            throw Exception("Failed2 coroutine")
        }
    }

    
    scope.launch (mHandler){
        throw Exception("I throw an Exception")
    }
  //print:CaughtAAA java.lang.Exception: I throw an Exception
    
    
    scope.launch (mHandler){
        try {
            throw Exception("I throw an Exception")
        } catch(e: Exception) {
            //優先
            println("try/catch got $e")
        }
    }
    //print:try/catch got java.lang.Exception: I throw an Exception
}
