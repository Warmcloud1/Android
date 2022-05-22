val pref = getSharedPreferences("testA", MODE_PRIVATE)
pref.edit()
    .putString("USER", text.toString())
    .commit()
    
    
val getPref = getSharedPreferences("testA", MODE_PRIVATE)
val userid = getPref.getString("USER", "預設值")
binding.textViewMain.text = userid
