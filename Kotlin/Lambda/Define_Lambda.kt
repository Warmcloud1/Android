//定義 Lambdas 表達式
fun main(args: Array<String>) {

    val greeting:() -> Unit = { println("Hello!")}

    // invoking function
    greeting()
    
    // 也可以寫成下面這
    greeting.invoke()
    
    val greeting2:(String) -> Unit = { it -> println(it)}

    // invoking function
    greeting2("Hello!")
    // 也可以寫成下面這樣
    greeting2.invoke("Hello!")
}
/*
1.
(Int, Int) 代表兩個傳入的參數 , Unit 代表並沒有要回傳任何 value
(Int, Int) -> Unit
2.
括號內 可以為空,代表沒有傳入任何參數
() -> Unit
*/
