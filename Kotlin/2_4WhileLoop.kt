//while
val items = listOf("apple", "banana", "kiwifruit")
var index = 0
while (index < items.size) {
    println("item at $index is ${items[index]}")
    index++
}
/*
item at 0 is apple
item at 1 is banana
item at 2 is kiwifruit
*/

//do-while executes the body and then checks the condition. If it's satisfied, the loop repeats.
// So, the body of do-while executes at least once regardless of the condition.
do {
    val y = retrieveData()
} while (y != null) // y is visible here!
