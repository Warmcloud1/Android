val age = 5
val name = "Rover"
//val is a special word used by Kotlin, called a keyword, indicating that what follows is the name of a variable.
  
//age is the name of the variable.
  
//= makes the value of age (on its left) be the same as the value on its right. In math, a single equal sign is used to assert that the values on each side are the same.
//   In Kotlin, unlike in math, a single equal sign is used to assign the value on the right to the named variable on the left.
  
  println("You are already ${age}! ${name}")
  
  printBorder()
  printBorder2()
  printBorder3("=",18)   
  
  
//learn how to simplify that code using techniques for repeating and reusing code,
//一般
fun printBorder(){
    println("==================")
}

//使用repeat
fun printBorder2(){
    repeat(18){
        print("=")
    }
    println()
}

//增加參數
fun printBorder3(border:String,timeToRepeat:Int){  //parameter
    repeat(timeToRepeat){
        print(border)
    }
    println()
}

//回傳數字
fun sum(a: Int, b: Int): Int {
    return a + b
}

//   Case5:returns no meaningful value.
fun printSum(a: Int, b: Int): Unit { //:Unit 可以省略
    println("sum of $a and $b is ${a + b}")
}

// A function body can be an expression. Its return type is inferred.
  fun sum2(a:Int,b:Int)=a+b

