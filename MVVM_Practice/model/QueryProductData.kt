package com.example.mvvm-practice.Model

data class QueryProductData (
    val name : String, //商品名稱
    val price : Double, //商品價格
    val startPrice : Double, //開盤價
    val tradeStatus : String, //現股當沖碼 (Y:可先買後賣 N:不可現充 X:可雙向)
    val crper : Int, //融資成數
    val dbper : Int, //融卷成數
    )
