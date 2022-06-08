// 在 Kotlin 之中，可以用「?:」表達，如果左邊不是空值的話直接回傳左邊的內容，否則回傳右邊的內容

val number = token.toIntOrNull() ?: print("number is null")




//資料型態+?
String? Car?
資料型態可以跟null共存

// 資料型態!!
Int!!
絕對無null


XML 裡:
如果左边运算数不是 null，则 Null 合并运算符 (??) 选择左边运算数，如果左边运算数为 null，则选择右边运算数。
android:text="@{user.displayName ?? user.lastName}"

等同於

android:text="@{user.displayName != null ? user.displayName : user.lastName}"
