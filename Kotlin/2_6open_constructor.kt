fun main() {
    Baby("Amy")
    Baby("Amy", Baby("Bob"))
}
//
//class Person constructor(){
//    val firstProperty ="First property : $name".also(::println)
//    init{
//        println("First initializer block that prints ${name}")
//    }
//}


class Person2 {
    var child: MutableList<Person2> = mutableListOf()

    constructor(parent: Person2) {
        parent.child.add(this)
    }
}

interface Create {
    val life: Int
    fun move() //默認open
}



abstract class Person(val name: String, override val life: Int = 0):Create {
    var children: MutableList<Person> = mutableListOf()

    open val size: Int =
        name.length.also { println("Initializing size in Person name: $it") }

    open val get = "A"
    open var getSet = "B"

    constructor(name: String, parent: Person) : this(name) {
        parent.children.add(this)
    }

    abstract fun isTall():Boolean

    override fun move() {
        println("行走")
    }
}

class Baby : Person {
    //你也可以用一个 var 属性覆盖一个 val 属性，但反之则不行。 这是允许的，因为一个 val 属性本质上声明了一个 get 方法， 而将其覆盖为 var 只是在子类中额外声明一个 set 方法。
//    override val get = "C"  //OK
    override var get = "D"  //OK

    //    override val getSet = "A" //不OK
    override var getSet = "c" //OK

    override fun isTall() :Boolean{
        return false
    }


    constructor(name: String) : super(name)

    constructor(name: String, parent: Person) : super(name, parent)


    override fun move() {
        println("爬行")
    }
}
//https://www.kotlincn.net/docs/reference/classes.html#%E8%A6%86%E7%9B%96%E5%B1%9E%E6%80%A7
