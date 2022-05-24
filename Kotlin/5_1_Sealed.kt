//在表達一個元件的狀態時，可能第一個想到的會是 enum class 。而現在有了新的選擇—— sealed class 。
// 它們倆的差別是： enum class 可以拿來簡單的判斷狀態，但不能傳遞變數；反之，sealed class 可以攜帶變數。

/*sealed class 是一個 abstract class ，本身並不能被 instantiate（實體化）。
但可以透過繼承某個 sealed class ，限縮可能的類型。可以傳遞變數這種特性讓它可以作為進階版的 enum 使用，與 when語法配合後效果更佳。
*/

//參考 https://louis383.medium.com/kotlin-sealed-classes-%E7%9A%84%E5%9F%BA%E7%A4%8E%E4%BD%BF%E7%94%A8-de660dbb63d2

fun main(args: Array<String>) {
    val machineState = checkStateByMachineId(1)
    machineStatePrinting(machineState)
    println()

    val secondMachineState = checkStateByMachineId(2)
    machineStatePrinting(secondMachineState)
    println()

    val thirdMachineState = checkStateByMachineId(3)
    machineStatePrinting(thirdMachineState)
    println()

    val myMachine = WorkingState.Finished(listOf("AA","BB","CC"))
    machineStatePrinting(myMachine)

}


sealed class WorkingState {
    data class Finished(val result: List<String>) : WorkingState()
    data class ErrorHappened(val whatHappened: String) : WorkingState()
    object Working : WorkingState()
    object EmptyResult : WorkingState()
}



fun checkStateByMachineId(id: Int): WorkingState {
    return when (id) {
        1 -> {
            val result = listOf<String>("Warthog", "Hedgehog", "Badger", "Drake")
            return WorkingState.Finished(result)
        }
        2 -> {
            WorkingState.ErrorHappened("too big to eat")
        }
        3 -> {
            WorkingState.EmptyResult
        }
        else -> WorkingState.Working
    }
}

fun machineStatePrinting(workingState: WorkingState) {
    when(workingState){
        is WorkingState.Finished->{
            if(workingState.result.isNotEmpty()){  //可直接拿result當屬性
                for((index,text) in workingState.result.withIndex()){
                    println("the ${index + 1} result $text")
                }
            }
        }
        is WorkingState.ErrorHappened ->{
            println("""
                Error Occurred:reason is
                ${workingState.whatHappened}
            """.trimIndent())
        }
        WorkingState.Working->{
            println("machine is working")
        }
        WorkingState.EmptyResult -> {
            println("It's empty result")
        }

    }
}
