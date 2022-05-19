//將 Lambdas 表達式 當作參數傳遞

fun main(args: Array<String>){    
    funA{
        println("funA_out")
    }
} 
private fun funA(callback:()->Unit){
    println("funA_Inline")
    callback.invoke() //==callback()
}

//參考:https://ithelp.ithome.com.tw/articles/10247339

// I/System.out: funA_Inline
// I/System.out: funA_out
