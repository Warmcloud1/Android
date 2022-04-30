**比較DiffUtil與Adapter.notifyItemChanged…差異**
==============
> DiffUtil
>>效率較高
>> RecyclerView有一個名為 DiffUtil 的類，用於計算兩個列表之間的差異。DiffUtil會接受一個舊列表和一個新列表，並確定二者有何不同。它會查找已添加、移除或更改的項。
>> 然後，它會使用一種算法（名為Eugene W. Myers 差分算法），來確定要生成新列表，需要對舊列表做出的最小更改量。
-------------
> Adapter.notifyItemChanged
>> 效率較差
>> notifyDataSetChanged()會告知 RecyclerView 整個列表可能無效。因此，RecyclerView會重新綁定並重新繪製列表中的每個項，包括屏幕上看不到的項。
>> 這是一項既繁重又不必要的工作。對於較大或複雜的列表，這個過程可能需要較長時間，以至於在用戶滾動瀏覽列表時，屏幕會閃爍或卡頓。
-------------
>參考資料:https://developer.android.com/codelabs/kotlin-android-training-diffutil-databinding?hl=zh-cn#2
