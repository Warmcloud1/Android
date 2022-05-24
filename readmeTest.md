## 1. 若未使用CoroutinScope.async擴充方法請試著將習題二之須同步執行的API改為使用之並比較兩者之差異
## 2. 若API發生例外則Coroutines該如何處理之，在launch、async、CoroutineScope又可能有差異。

#TEST

例外發生時CoroutineScope管理的child job因為Job會有不同的行為，
請紀錄於root/REAMDE.md中。
* launch:
    1. 不會回傳值:
launch會啟動一個新的協程，並且不會將結果返回給調用者。 任何被認為是“一勞永逸”的工作都可以使用啟動來開始。
    2. 參考HW4_1
* async:
    1. 會回傳值async
啟動一個新的協程，並允許您使用名為 await 的掛起函數返回結果。
    2. 參考HW4_2
    3. 處理異常
由於 async 期望最終調用 await，它會保存異常並將它們作為 await 調用的一部分重新拋出。 這意味著如果您使用 async 從常規函數啟動新的協程，您可能會默默地丟棄異常。 這些丟棄的異常不會出現在您的崩潰指標中，也不會在 logcat 中記錄下來。 
* 參考資料
<https://developer.android.com/kotlin/coroutines/coroutines-adv>

<https://medium.com/jastzeonic/kotlin-coroutine-%E9%82%A3%E4%B8%80%E5%85%A9%E4%BB%B6%E4%BA%8B%E6%83%85-685e02761ae0>
