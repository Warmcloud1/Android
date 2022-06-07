
//改成funA
binding.editTextId.setOnEditorActionListener(funA())

private fun funA()=object: TextView.OnEditorActionListener{
    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        TODO("Not yet implemented")
        return false
    }
}

//匿名類
binding.editTextId.setOnEditorActionListener(object: TextView.OnEditorActionListener{
    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        return false
    }
})


//lambda
binding.orderNumberEditText.setOnEditorActionListener { v, actionId, event -> false }



fun funA2()= TextView.OnEditorActionListener { v, actionId, event -> TODO("Not yet implemented") false }
