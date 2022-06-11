import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvm-practice.Model.QueryProductData
import com.pocket.gatewayconnector.GatewayConnector
import com.pocket.gatewayconnector.ext.sendQueryProductPacket
import pkts.QueryProductPacket
import timber.log.Timber
import com.example.mvvm-practice.Model.BaseAndroidViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat

class QueryProductViewModel(
    application: Application,
    private val connector: GatewayConnector
) : BaseAndroidViewModel(application) {
    //判定顯示幾位數
    enum class Digital {
        //顯示至整數
        UNIT_DIGITAL,

        //顯示至小數點第一位
        ONE_POINT_DIGITAL,

        //顯示至小數點第二位
        TWO_POINT_DIGITAL,
    }

    //下拉選單
    enum class SpinnerSelected {
        LOT,  //選擇張數'數量'
        PRICE //選擇'金額'數量
    }

    //按鈕決定增加或減少
    enum class Signm {
        INC, //增加
        DEC  //減少
    }

    companion object {
        //委託價最小值
        private const val MIN_ENTRUST_PRICE = 0.0

        //最小值'張數'數量
        private const val MIN_ORDER_LOT_NUMBER: Int = 1

        //最大值下單'張數'數量
        private const val MAX_ORDER_LOT_NUMBER: Int = 499

        //最小值下單'金額'數量
        private const val MIN_ORDER_PRICE_NUMBER: Int = 1

        //最大值下單'金額'數量
        private const val MAX_ORDER_PRICE_NUMBER: Int = 999999999
    }

    //商品資訊
    private val _queryProductData = MutableLiveData<QueryProductData>()
    val queryProductData: LiveData<QueryProductData> = _queryProductData

    //商品名稱
    private val _liveProductName = MutableLiveData<String>()
    val liveProductName: LiveData<String> = _liveProductName

    //商品價格
    private val _liveProductPrice = MutableLiveData<Double>()
    val liveProductPrice: LiveData<Double> = _liveProductPrice

    //融卷
    private val _liveDbper = MutableLiveData<Int>()
    val liveDbper: LiveData<Int> = _liveDbper

    //融資
    private val _liveCrper = MutableLiveData<Int>()
    val liveCrper: LiveData<Int> = _liveCrper

    //開盤價
    private val _liveStartPrice = MutableLiveData<Double>()
    val liveStartPrice: LiveData<Double> = _liveStartPrice

    //現股當沖碼 (Y:可先買後賣 N:不可現充 X:可雙向)
    private val _liveTradeStatus = MutableLiveData<String>()
    val liveTradeStatus: LiveData<String> = _liveTradeStatus

    //委託價
    private val _liveEntrustPrice = MutableLiveData<Double>()
    val liveEntrustPrice: LiveData<Double> = _liveEntrustPrice

    //下單數量
    private val _liveOrderNumber = MutableLiveData<Int>()
    val liveOrderNumber: LiveData<Int> = _liveOrderNumber

    //總估價
    private val _liveEstimateTotalPrice = MutableLiveData<Double>()
    val liveEstimateTotalPrice: LiveData<Double> = _liveEstimateTotalPrice

    //估計剩餘價格
    private val _liveEstimateRestPrice = MutableLiveData<Double>()
    val liveEstimateRestPrice: LiveData<Double> = _liveEstimateRestPrice

    //可以下單數量
    private val _liveCanBuyLotNumber = MutableLiveData<Int>()
    val liveCanBuyLotNumber: LiveData<Int> = _liveCanBuyLotNumber

    //下拉選單 選擇方式'數量/金額'，預設'數量'
    private val _liveSpinnerSelected = MutableLiveData<SpinnerSelected>()
    val liveSpinnerSelected : LiveData<SpinnerSelected> = _liveSpinnerSelected

    //先前委託價格
    var previousEntrustPrice = 0.0

    //先前下單數量
    var previousOrderNumber = 0

    private var isFetching = false

    //商品漲跌百分比
    fun riseFallPercentPrice(): String {
        val startPrice = _liveStartPrice.value?.toDouble() ?: return "--"
        val productPrice = _liveProductPrice.value?.toDouble() ?: return "--"
        return digitalType(Digital.TWO_POINT_DIGITAL).format((productPrice - startPrice) / startPrice * 100)
    }

    //商品漲跌價
    fun riseFallPrice(): String {
        val startPrice = _liveStartPrice.value?.toDouble() ?: return "--"
        val productPrice = _liveProductPrice.value?.toDouble() ?: return "--"
        return digitalType(Digital.TWO_POINT_DIGITAL).format(productPrice - startPrice)
    }

    fun getProductInfo(
        sym: String,
    ) {
        if (isFetching) return

        isFetching = true

        val queryPacket = QueryProductPacket().apply {
            m_seq = connector.nextSeq
            m_sym = sym//商品id
        }
        if (sym.isNotBlank()) queryPacket.m_sym = sym

        connector.twse.sendQueryProductPacket(
            queryPacket,
            {
                Timber.e("${it.javaClass.simpleName}=$it")
                showToast("${it.m_code}: ${it.m_msg}") //回應代碼、回應訊息
                if (it.m_code == "000000") {
                    if(it.m_products.size()>=1){
                    val queryProductData = QueryProductData(
                            it.m_products.get(0).m_stock_name,
                            it.m_products.get(0).m_pricemkt,
                            it.m_products.get(0).m_ldc_price,
                            it.m_products.get(0).m_trade_code,
                            it.m_products.get(0).m_crper,
                            it.m_products.get(0).m_dbper
                    )
                    postLiveQueryProductData(queryProductData)
                    }
                }
                //todo可刪 測試取得資料
                for(i in 0 until it.m_products.size()){
                    for(j in 0..35){
                        println("$i ${j}:${ it.m_products.get(i).GetValueByKeyCode(j)}")
                    }
                }

                isFetching = false
            })
    }

    fun setLiveSpinnerSelected(spinner:SpinnerSelected){
        _liveSpinnerSelected.value = spinner
    }

    private fun postLiveQueryProductData(queryProductData: QueryProductData){

        _queryProductData.postValue(queryProductData)
        viewModelScope.launch {
            _liveProductName.value=(queryProductData.name)
            _liveProductPrice.value=(queryProductData.price)
            _liveCrper.value=(queryProductData.crper)
            _liveDbper.value=(queryProductData.dbper)
            _liveStartPrice.value=(queryProductData.startPrice)
            _liveTradeStatus.value=(queryProductData.tradeStatus)
            //初始化
            _liveSpinnerSelected.value=(SpinnerSelected.LOT)
            _liveEntrustPrice.value=(queryProductData.price)
            _liveOrderNumber.value=(1)
        }
    }

    fun setLiveEntrustPrice(price: Double) {
        _liveEntrustPrice.value = price
        calEstimate()
        recordPrevious()
    }

    fun setLiveOrderNumber(price: Int) {
        _liveOrderNumber.value = price
        calEstimate()
        recordPrevious()
    }

    private fun setLiveCanBuyOrderNumber(canBuyLotNumber:Int) {
        _liveCanBuyLotNumber.value = canBuyLotNumber
    }

    private fun setLiveEstimateTotalPrice(price:Double) {
        _liveEstimateTotalPrice.value = price
    }

    private fun setLiveEstimateRestPrice(price:Double) {
        _liveEstimateRestPrice.value = price
    }

    //選單為Price 用編輯文字改變下單值
    fun changeOrderNumberByEditText(orderNumber: Long) {
        when (_liveSpinnerSelected.value) {
            SpinnerSelected.LOT -> {
                setLiveOrderNumber(checkIntNumberRange(MIN_ORDER_LOT_NUMBER, MAX_ORDER_LOT_NUMBER, orderNumber))
            }
            SpinnerSelected.PRICE -> {
                setLiveOrderNumber( checkIntNumberRange(MIN_ORDER_PRICE_NUMBER, MAX_ORDER_PRICE_NUMBER, orderNumber))
            }
        }
    }

    fun changeEntrustPriceByEditText(entrustPrice: Double) {
        when (_liveSpinnerSelected.value) {
            SpinnerSelected.LOT -> {
                fixEntrustPrice(entrustPrice)
            }
            SpinnerSelected.PRICE -> {
                fixEntrustPrice(entrustPrice)
            }
        }
    }

    fun changeEntrustPriceByButton(changeSign: Signm) {
        val entrustPrice=_liveEntrustPrice.value
        val sign = currentSign(changeSign)
        //升降單位
        if (entrustPrice == null) {
            addEntrustPrice(sign * 1 + 0.0)
        } else if (entrustPrice >= 1000) {
            addEntrustPrice(sign * 5.0)
        } else if (entrustPrice >= 500 && entrustPrice < 1000) {
            addEntrustPrice(sign * 1.0)
        } else if (entrustPrice >= 100 && entrustPrice < 500) {
            addEntrustPrice(sign * 0.5)
        } else if (entrustPrice >= 50 && entrustPrice < 100) {
            addEntrustPrice(sign * 0.1)
        } else if (entrustPrice >= 10 && entrustPrice < 50) {
            addEntrustPrice(sign * 0.05)
        } else if (entrustPrice >= 0 && entrustPrice < 10) {
            addEntrustPrice(sign * 0.01)
        } else {
            //升降單位不變
        }
    }

    //選單為Price 用按鈕改變下單值
    fun changeOrderNumberByButton(changeSign: Signm) {
        var orderNumber = _liveOrderNumber.value
        //決定正負號
        val sign = currentSign(changeSign)
        if (orderNumber != null) {
            //計算數值
            orderNumber += sign * 1
            //確認range
            orderNumber = when (_liveSpinnerSelected.value) {
                SpinnerSelected.LOT -> {
                    checkIntNumberRange(MIN_ORDER_LOT_NUMBER, MAX_ORDER_LOT_NUMBER, orderNumber.toLong())
                }
                SpinnerSelected.PRICE -> {
                    checkIntNumberRange(MIN_ORDER_PRICE_NUMBER, MAX_ORDER_PRICE_NUMBER, orderNumber.toLong())
                }
                else -> {
                    0
                    throw IllegalArgumentException("沒有選擇選單")
                }
            }
            setLiveOrderNumber(orderNumber)
        }
    }

    //計算總估計值 spinner為金額時
    private fun calEstimate() {
        val entrustPrice = _liveEntrustPrice.value?:return
        val orderNumber = _liveOrderNumber.value?:return
        when (_liveSpinnerSelected.value) {
            SpinnerSelected.LOT -> {
                //預估金額 = 委託價 * 數量(張) * 1000股
                setLiveEstimateTotalPrice(entrustPrice * orderNumber * 1000)
                setLiveEstimateRestPrice(0.0)

            }
            SpinnerSelected.PRICE -> {
                //需求:剩餘金額 + 預估金額 == 下單金額
                //需求:(委託額 * 張數)*1000 == 預估金額
                //計算剩餘金額 = 下單金額 / (委託金額 * 1000) 的餘數

                setLiveEstimateRestPrice(orderNumber % (entrustPrice * 1000))
                val estimateRestPrice =_liveEstimateRestPrice.value!!
                setLiveEstimateTotalPrice(orderNumber - estimateRestPrice)
                val estimateTotalPrice = _liveEstimateTotalPrice.value!!
                //預估金額 = 下單金額 - 剩餘金額

                if (orderNumber >= (entrustPrice * 1000)) {
                    //需求:最大可買張數=[輸入金額/(委託價格*購買股數),取整數,無條件捨小數]/1000
                    //張數 = 金額 / 委託金額 /1000 要整數
                    setLiveCanBuyOrderNumber((estimateTotalPrice / (entrustPrice * 1000)).toInt())
                } else {
                    setLiveCanBuyOrderNumber(0)
                }
            }
        }
    }

    private fun addEntrustPrice(addNumber: Double) {
        var entrustPrice = _liveEntrustPrice.value!!
        entrustPrice += addNumber
        entrustPrice = checkDoubleRange(MIN_ENTRUST_PRICE, entrustPrice, entrustPrice)
        fixEntrustPrice(entrustPrice)
    }

    //修正 委託價為正確位數
    private fun fixEntrustPrice(price: Double) {
        setLiveEntrustPrice(useDigitalRule(price).format(price).toDouble())
    }

    //當前正負號
    private fun currentSign(change: Signm): Int {
        return when (change) {
            Signm.INC -> 1
            Signm.DEC -> -1
            else -> throw IllegalArgumentException("Unknown currentSign")
        }
    }

    //確認浮點數range
    private fun checkDoubleRange(min: Double, max: Double, currentNumber: Double): Double {
        return if (currentNumber < min) {
            min
        } else if (currentNumber > max) {
            max
        } else {
            currentNumber
        }
    }

    //確認整數range
    private fun checkIntNumberRange(min: Int, max: Int, currentNumber: Long): Int {
        return if (currentNumber < min) {
            min
        } else if (currentNumber > max) {
            max
        } else {
            currentNumber.toInt()
        }
    }

    fun recordPrevious() {
        previousEntrustPrice = _liveEntrustPrice.value?:liveProductPrice.value!!
        previousOrderNumber = _liveOrderNumber.value?:1
    }

    //顯示小數位的規則 "上市、上櫃、興櫃個股" 與 "(認購(售)權證、認股權憑證"規則相同
    private fun useDigitalRule(price: Double): NumberFormat {
        return if (price == null) {
            digitalType(Digital.UNIT_DIGITAL)
        } else if (price >= 500) {
            digitalType(Digital.UNIT_DIGITAL)
        } else if (price >= 50 && price < 500) {
            digitalType(Digital.ONE_POINT_DIGITAL)
        } else if (price >= 0 && price < 50) {
            digitalType(Digital.TWO_POINT_DIGITAL)
        } else {
            throw IllegalArgumentException("股票價格顯示不正確")
        }
    }


    //選擇顯示幾位數
    private fun digitalType(digital: Digital): NumberFormat {
        return when (digital) {
            (Digital.UNIT_DIGITAL) -> DecimalFormat("#")
            (Digital.ONE_POINT_DIGITAL) -> DecimalFormat("#.#")
            (Digital.TWO_POINT_DIGITAL) -> DecimalFormat("#.##")
            else -> throw IllegalArgumentException("Unknown DigitalFormat")
        }
    }
}
