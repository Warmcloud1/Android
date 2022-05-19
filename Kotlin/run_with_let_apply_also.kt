// T.run
// 什⋯ 什麼嘛，多了 T 是怎麼一回事！
// 這些 function 的使用方式，需要接在一個變數後面才行。像是 someVariable.run { /* do something */ }，
// 包含 T.run 下面四個 let also apply 都屬於這種 extension function。因為 run有兩種用法，這裡為了避免混淆而將 T 寫出來。

// T.run 也能像 with 一樣來做初始化，而且 extension function 有個好處是可以在使用時就進行 「?」 或 「!!」 的宣告。
// 另外，T 能夠以 this 的形式在 scope 內取用。像是上面的範例，如果用 T.run來 做初始化，就會是：

private fun buildGreatSmartphone(goodSmartPhone: GoodSmartPhone?) {
    goodSmartPhone?.run {
        this.setCleanSystemInterface(true)
        // `this` is not necessary
        setGreatBatteryLife(true)
        setGreatBuildQuality(true)
        setNouch(ture)
    }   
}

//當然如果傳進來的變數是空值， T.run{} 內的程式碼就根本不會執行了。
// 除此之外， T.run 和 run 的特性完全一樣。可以將最後一行的東西回傳，或是傳給下一個 chain。參考以下範例，要根據筆電系統版本印出 Windows 的開發代號：

// data class Laptop(maker, model, system)
val laptopA = Laptop("Dell", "XPS 13 9343c", "Windows 8.1")
val laptopB = Laptop("Lenovo", "T420s", "Windows 7")
val laptopC = Laptop("MSI", "GS65 Stealth", "Windows 10")
printWindowsCodeName(laptopA)
printWindowsCodeName(laptopB)
printWindowsCodeName(laptopC)

fun printWindowsCodeName(laptop: Laptop?) {
    val codename = laptop?.run {
            // `this` is Laptop.
            // `this` can ignore when use fields and methods
            system.split(" ")    // <-- pass to next chain
        }?.run {
            // `this` is the split strings. a List<String>
            val result = when (this.last()) {
                "7" -> "Blackcomb"
                "8" -> "Milestone"
                "8.1" -> "Blue"
                "10" -> "Threshold"
                else -> "Windows 9"
            }
            result    //  <-- pass value back
        }
    
    println("${laptop?.system} codename is $codename")
}

//reult:
// Windows 8.1 codename is Blue
// Windows 7 codename is Blackcomb
// Windows 10 codename is Threshold


//let
// 又或者可以寫成 T.let，也是一個 extension function。T 在 scope 內則是用 it 來存取而不是 this。也可以依照需求改成其他的名字，增加可讀性。
// 與 run 相同，會將最後一行帶到下一個 chain 或是回傳。

class TreasureBox {
    private val password = "password"
    private val treasure = "You've got a Windows install USB"
    fun open(key: String?): String {
        val result = key?.let {
            // `it` is the key String.
            // `this` is TreasureBox.
            
            var treasure = "error"
            if (it == password) {
                treasure = this.treasure
            }
            treasure     // <-- pass value back
        } ?: "error"
      
        return result
    }
}

val treasureBox = TreasureBox()
println("Open the box , and ${treasureBox.open(null)}")
println("Open the box , and ${treasureBox.open("admin")}")
println("Open the box , and ${treasureBox.open("password")}")

// result
// Open the box , and error
// Open the box , and error
// Open the box , and You've got a Windows install USB



/*also
也可以寫作 T.also
剩下的 also和 apply 決大部分也是使用於初始化物件。前文提到：這幾種 Standard Library Function 其實可以互相替換，選擇合適的場景使用即可。

而它們與上面的 run 與 let的不同之處在於： run與 let 會將最後一行傳給下個 Chain 或是回傳，物件類型依最後一行而定； also和 apply 則是將「自己 (this)」回傳或傳入下個 chain。

有點像是 builder pattern ，做完一次設定後又將自己回傳回去。另外， also在 scope 內可以透過 it 來存取 T本身。*/
val drink = FiftyLan().also {
    it.setSugarLevel(FiftyLan.SugarLevel.Half)
}.also {
    it.setIceLevel(FiftyLan.IceLevel.Few)
}.also {
    it.要多帶我們一杯紅茶拿鐵嗎好喝喔 = false
}.also {
    it.plasticBag = true
}

drink.printResult()

//result:
// Your drink details:
// sugar level is 50
// ice level is 70
// Customer needs plastic bag = true


// apply
// 也可以寫作 T.apply 。
// apply與 also 有 87 分像，不同的地方是 apply 在 scope 內 T的存取方式是 this ，其他都與 also 一樣。

// 這裡的範例以 Fragment 生成時，需要時做的 newInstance()方法。利用 apply 和 also 在 Kotlin 之中如何改寫：

companion object {
    private const val COFFEE_SHOP_LIST_KEY = "coffee-list-key"
    fun newInstance(coffeeShops: List<CoffeeShop>): ListFragment {
        return ListFragment().apply {
            // `this` is `ListFragment` in apply scope  
            arguments = Bundle().also {
                // `it` is `Bundle` in also scope
                // `this` is `ListFragment`        
                it.putParcelableArrayList(COFFEE_SHOP_LIST_KEY, coffeeShops as ArrayList<out Parcelable>)
            }
        }
    }
}

//參考
