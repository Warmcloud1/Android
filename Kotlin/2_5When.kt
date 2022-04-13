//switch用法
when (x) {
    1 -> print("x == 1")
    2 -> print("x == 2")
    else -> {
        print("x is neither 1 nor 2")
    }
}


//兩個條件一次寫
when (x) {
    0, 1 -> print("x == 0 or x == 1")
    else -> print("otherwise")
}

//You can also check a value for being in or !in a range or a collection:
var x=30
var validNumbers=10..20
when (x) {
    in 1..10 -> print("x is in the range")
    in validNumbers -> print("x is valid")
    !in 10..20 -> print("x is outside the range")
    else -> print("none of the above")
}

// Another option is checking that a value is or !is of a particular type. Note that, due to smart casts,
// you can access the methods and properties of the type without any extra checks.
//這裡不懂
fun hasPrefix(x: Any) = when(x) {
    is String -> x.startsWith("prefix")
    else -> false
}


// when can also be used as a replacement for an if-else if chain.
// If no argument is supplied, the branch conditions are simply boolean expressions, and a branch is executed when its condition is true:
when {
    x.isOdd() -> print("x is odd")
    y.isEven() -> print("y is even")
    else -> print("x+y is odd")
}

// You can capture when subject in a variable using following syntax:
fun Request.getBody() =
    when (val response = executeRequest()) {
        is Success -> response.body
        is HttpError -> throw HttpException(response.status)
    }
