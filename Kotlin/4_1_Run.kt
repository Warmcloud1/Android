//1.可以將 run 想像成一個獨立出來的 Scope ， run 會把最後一行的東西回傳或是帶到下一個 chain。
val whatsMyName = "Francis"
run {
    val whatsMyName = "Ajax"
    println("Call my name! $whatsMyName")
}
println("What's my name? $whatsMyName")
//Result
//Call my name! Ajax
//What's my name? Francis


//2.
run {
    val telephone = Telephone()
    telephone.whoCallMe = "English"
    telephone    // <--  telephone 被帶到下一個 Chain
}.callMe("Softest part of heart")    // <-- 這裡可以執行 `Telephone` Class 的方法

// callMe function in Telephone class
fun callMe(myName: String) {
    println("$whoCallMe ! Call me $myName !!");
}


//參考資料: https://louis383.medium.com/%E7%B0%A1%E4%BB%8B-kotlin-run-let-with-also-%E5%92%8C-apply-f83860207a0c
