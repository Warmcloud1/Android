// 在 Kotlin 之中，可以用「?:」表達，如果左邊不是空值的話直接回傳左邊的內容，否則回傳右邊的內容

val number = token.toIntOrNull() ?: print("number is null")




//資料型態+?
String? Car?
資料型態可以跟null共存

// 資料型態!!
Int!!
絕對無null
