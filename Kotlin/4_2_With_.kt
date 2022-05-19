//目的:with 一般常常作為初始化時使用，透過 with()很明確知道是為了括弧中的變數進行設定。
// with(T) 之中的傳入值可以以 this (稱作 identifier) 在 scope 中取用，不用打出 this也沒關係。
//雖然， with 也會將最後一行回傳，但目前看起來大部分還是只用它來做初始化。
val greatSmartphone = GoodSmartPhone()
with(greatSmartphone) {
    this.setCleanSystemInterface(true) //等同於 setGreatBatteryLife(true)
    setGreatBuildQuality(true)
    setNouch(ture)
}
//但很多使用狀況變數可能是可為空的變數，如此一來 with的 scope 中就必須要宣告 「?」或「!!」來取用該物件的方法 (Method)。
private fun buildGreatSmartphone(goodSmartPhone: GoodSmartPhone?) {
    with(goodSmartPhone) {
        this?.setCleanSystemInterface(true)
        this?.setGreatBatteryLife(true)
        this?.setGreatBuildQuality(true)
        this?.setNouch(ture)
    }
}
//參考資料:https://louis383.medium.com/%E7%B0%A1%E4%BB%8B-kotlin-run-let-with-also-%E5%92%8C-apply-f83860207a0c
