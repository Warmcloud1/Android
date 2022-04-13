main(){
  val rectangle = Rectangle(5.0, 2.0)
  println("The perimeter is ${rectangle.perimeter}")
}



class Rectangle(var height: Double, var length: Double) {
    var perimeter = (height + length) * 2
}

//Inheritance between classes is declared by a colon (:). Classes are final by default; to make a class inheritable, mark it as open.
open class Shape

class Rectangle(var height: Double, var length: Double): Shape() {
    var perimeter = (height + length) * 2
}
