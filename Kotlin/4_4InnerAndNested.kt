//Inner 為非靜態內部類
//Nested 為 靜態內部類 

fun main(args : Array<String>){
    val testClass =Test()
    testClass.InnerClass().print()
    Test.NestedClass().print()
}

//__________________________________________

class Test {
    class NestedClass{
        fun print(){
            print("NestClass")
        }

    }

    inner class InnerClass{
        fun print(){
            print("InnerClass")
        }
    }
}
