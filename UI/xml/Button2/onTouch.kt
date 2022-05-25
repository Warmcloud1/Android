        
MainActity(){
        ...
        val button1 = findViewById<Button>(R.id.button1)
        val button2=findViewById<Button>(R.id.button2)

        button1.setOnTouchListener { v, event ->
            button1.isSelected = true
            button2.isSelected = false
            println("1 OnTouch")
            true
        }

        //滑鼠不動下，點下觸發一次 放開觸發一次 在View內按住移動會不斷觸發
        button2.setOnTouchListener(object:View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                println("2 OnTouch")
                //會執行其他監聽器
                return false
            }

        })

        //若onTouch為true 不會執行
        button2.setOnClickListener {
            button2.isSelected = true
            button1.isSelected = false
            println("2 Click")
        }
}

