//第一個方法 spinner連動
//第一的方式 放入MainActivity...{

 val spinner: Spinner = findViewById<Spinner>(R.id.planets_spinner)

//    Create an ArrayAdapter using the string array and a default spinner layout
       ArrayAdapter.createFromResource(
           this,
           R.array.planets_array,
           android.R.layout.simple_spinner_item
       ).also { adapter ->
           // Specify the layout to use when the list of choices appears
           adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
           // Apply the adapter to the spinner
           spinner.adapter = adapter
       }
       spinner.setOnItemSelectedListener(spnOnItemSelected())


        }
        //與第一個方式動改格式
            private fun spnOnItemSelected()=object:AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

           val spnTextView=view as TextView
           spnTextView.setTextSize(50.0f)
           spnTextView.setTextColor(Color.BLUE)

        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
    }
}     

//第一個方法 放入res.layout.activity_main
    <Spinner
        android:id="@+id/planets_spinner"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:dropDownVerticalOffset="24sp"
        android:entries="@array/planets_array"
        android:spinnerMode="dropdown"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
  
  //第一個方法 放入res.values.strings 
  <resources>
    <string-array name="planets_array">
        <item>Mercury</item>
        <item>Venus</item>
        <item>Earth</item>
        <item>Mars</item>

    </string-array>
</resources>
            
//第二個方法 匯入list可以後續新增
  //MainActivity{
        val list = ArrayList<String>();
        val dataAdapter:ArrayAdapter<String> = ArrayAdapter<String>  (this,R.layout.spinner_text,list)
        list.add("All Lists");
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        list.add("112233")
        val dataArray=resources.getTextArray(R.array.planets_array)
        for(data in dataArray.iterator()){
            list.add(data.toString())
}
        
//放入 res.layout.spinner_text
<?xml version="1.0" encoding="utf-8"?>
<TextView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:textSize="20sp"
    android:gravity="left"
    android:textColor="#FF0000"
    android:padding="5dip"
    />
  
// 放入res.layout.activity_main
  <Spinner
      android:id="@+id/planets_spinner"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:dropDownVerticalOffset="24sp"
      android:spinnerMode="dropdown"
      app:layout_constraintBottom_toBottomOf="parent"
      app:layout_constraintEnd_toEndOf="parent"
      app:layout_constraintStart_toStartOf="parent"
      app:layout_constraintTop_toTopOf="parent" />

//放入res.values.strings 
  <resources>
    <string-array name="planets_array">
        <item>Mercury</item>
        <item>Venus</item>
        <item>Earth</item>
        <item>Mars</item>

    </string-array>
</resources>
