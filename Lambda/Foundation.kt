fun main(args:Array<String>){
  val lam: (Int, Int) -> (Int) = { param1: Int, param2: Int -> param1 + param2 }
  lam(2,3)  //print(5)

  lamFun()(2,3)

  lamFunP  (1,{p:Int,param1: DoSomething, param2: Int -> param2 +p},2)
}

private fun lamFun():((Int, Int) -> Int){
    return {a:Int,b:Int->a+b}
}

private fun lamFunP(cc:Int,a:((Int,DoSomething, Int) -> Int),dd:Int){

}
