import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.lifecycle.Observer
import ext.displayAccountingNumber
import ext.trimComma
import utils.KeyboardTriggerBehavior

class ProductActivity : AppCompatActivity() {


    private lateinit var binding:ActivityProductPacketBinding

    private var keyboardTriggerBehavior: KeyboardTriggerBehavior? = null

    private val queryProductViewModel: QueryProductViewModel by lazy {
        getViewModel {
            QueryProductViewModel(
                application, MainActivity.connector
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductPacketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        keyboardTriggerBehavior = KeyboardTriggerBehavior(this).apply {
            observe(this@ProductActivity, Observer {
                when (it) {
                    KeyboardTriggerBehavior.Status.OPEN -> {
                    }
                    KeyboardTriggerBehavior.Status.CLOSED -> {
                        onChangeEntrustPriceByEditText(binding.entrustPriceEditText.text.toString().trimComma().toDoubleOrNull())
                        onChangeOrderNumberByEditText(binding.orderNumberEditText.text.toString().trimComma().toLongOrNull())
                    }
                }
            })
        }
        initBind()
        initObserve()
        initSetListener()
    }


    //預設值:委託價為定價、數量為1張、預估金額為--
    private fun initBind() {
        binding.orderNumberEditText.setText("1")
        binding.entrustPriceEditText.setText("定價")
        binding.estimateTotalPriceTextView.text = "--"
        binding.canBuyOrderLotTextView.text = "--"
        binding.entrustPriceUpButton.isEnabled = false
        binding.entrustPriceDownButton.isEnabled = false
        binding.orderNumberUpButton.isEnabled = false
        binding.orderNumberDownButton.isEnabled = false
        binding.entrustPriceEditText.isEnabled = false
        binding.orderNumberEditText.isEnabled = false
        //下拉選單，選擇'數量'或'金額'下單方式
        with(binding.orderMethodSpinner) {
            ArrayList<String>().run {
                val entrustMethods = resources.getTextArray(R.array.stock_array)
                for (entrustMethod in entrustMethods.iterator()) {
                    this.add(entrustMethod.toString())
                }
                val spinnerAdapter =
                    ArrayAdapter<String>(
                        this@ProductActivity,
                        R.layout.order_method_spinner_text,
                        this
                    )
                //指定Spinner展開時，選項清單樣式，如果與未展開時樣式一樣可以不設定
                spinnerAdapter.setDropDownViewResource(R.layout.order_method_spinner_list_text)
//                R.drawable.red_radio_button?
                //設定好的ArrayAdapter，指定給要操作spinner
                adapter = spinnerAdapter
                onItemSelectedListener = spnOnItemSelected()
            }
        }
    }


    private fun initObserve() {
        //收尋按鈕按下後變化
        queryProductViewModel.queryProductData.observe(this) {
            binding.productNameTextView.visibility = View.VISIBLE
            binding.priceConstraintLayout.visibility = View.VISIBLE
            binding.stringConstraintLayout.visibility = View.VISIBLE
            binding.entrustPriceEditText.isEnabled = true
            binding.orderNumberEditText.isEnabled = true
            binding.entrustPriceUpButton.isEnabled = true
            binding.entrustPriceDownButton.isEnabled = true
            binding.orderNumberUpButton.isEnabled = true
            binding.orderNumberDownButton.isEnabled = true
            binding.rodButton.isChecked=true
            binding.current.isChecked=true
        }

        queryProductViewModel.liveTradeStatus.observe(this) {
            updateTradeStatus(it)
        }
        queryProductViewModel.liveProductName.observe(this) {
            updateProductName(it)
        }

        queryProductViewModel.liveProductPrice.observe(this) {
            updateProductPrice(it)
        }

        queryProductViewModel.liveDbper.observe(this) {
            updateDbper(it)
        }

        queryProductViewModel.liveCrper.observe(this) {
            updateCrper(it)
        }

        queryProductViewModel.liveStartPrice.observe(this) {
            updateRiseOrFallProductPrice()
        }

        queryProductViewModel.liveEntrustPrice.observe(this) {
            updateEntrustPrice(it)
        }

        queryProductViewModel.liveOrderNumber.observe(this) {
            updateOrderNumber(it)
        }

        queryProductViewModel.liveEstimateTotalPrice.observe(this) {
            updateEstimateTotalPrice(it)
        }

        queryProductViewModel.liveEstimateRestPrice.observe(this) {
            updateEstimateRestPrice(it)
        }


        queryProductViewModel.liveCanBuyLotNumber.observe(this) {
            updateCanBuyLotNumber(it)
        }

        queryProductViewModel.liveSpinnerSelected.observe(this) {
            when (it) {
                QueryProductViewModel.SpinnerSelected.LOT -> initBindByLot()
                QueryProductViewModel.SpinnerSelected.PRICE -> initBindByPrice()
            }
        }
    }

    private fun initSetListener() {
        val wholeButton = binding.wholeStockButton
        val zeroButton = binding.zeroStockButton
        val stockBackButton = binding.stockBackButton
        //收尋按鈕
        binding.searchImage.setOnClickListener {
            val sym = binding.stockSymbol.text.toString()
            queryProductViewModel.getProductInfo(sym)
        }

        //整股按鈕
        wholeButton.setOnClickListener {
            wholeButton.setTextColor(Color.BLACK)
            zeroButton.setTextColor(Color.WHITE)
            stockBackButton.setTextColor(Color.WHITE)
        }
        //零股按鈕
        zeroButton.setOnClickListener {
            wholeButton.setTextColor(Color.WHITE)
            zeroButton.setTextColor(Color.BLACK)
            stockBackButton.setTextColor(Color.WHITE)
        }
        //盤後按鈕
        stockBackButton.setOnClickListener {
            wholeButton.setTextColor(Color.WHITE)
            zeroButton.setTextColor(Color.WHITE)
            stockBackButton.setTextColor(Color.BLACK)
        }
        //買入按鈕
        binding.selectBuyButton.setOnClickListener {
            binding.noSecuritiesTrade.visibility = View.GONE
            binding.sellOrBuyRadioGroup.setBackgroundResource(R.drawable.red_stroke)
            binding.current.setButtonDrawable(R.drawable.red_radio_button)
            binding.noSecuritiesTrade.setButtonDrawable(R.drawable.red_radio_button)
            binding.rodButton.setButtonDrawable(R.drawable.red_radio_button)
            binding.iocButton.setButtonDrawable(R.drawable.red_radio_button)
            binding.fokButton.setButtonDrawable(R.drawable.red_radio_button)
        }
        //賣出按鈕
        binding.selectSellButton.setOnClickListener {
            binding.noSecuritiesTrade.visibility = View.VISIBLE
            binding.sellOrBuyRadioGroup.setBackgroundResource(R.drawable.green_stroke)
            binding.current.setButtonDrawable(R.drawable.green_radio_button)
            binding.noSecuritiesTrade.setButtonDrawable(R.drawable.green_radio_button)
            binding.rodButton.setButtonDrawable(R.drawable.green_radio_button)
            binding.iocButton.setButtonDrawable(R.drawable.green_radio_button)
            binding.fokButton.setButtonDrawable(R.drawable.green_radio_button)
        }

        //委託價
        binding.entrustPriceEditText.setOnEditorActionListener { view, _, _ ->
            onChangeEntrustPriceByEditText(view.text.toString().trimComma().toDoubleOrNull())
            //  輸入完成後選單隱藏
            false
        }

        binding.orderNumberEditText.setOnEditorActionListener{ view, _, _ ->
            onChangeOrderNumberByEditText(view.text.toString().trimComma().toLongOrNull())
            //  輸入完成後選單隱藏
            false
        }

        //下單數量 編輯數量Enter後執行
        binding.orderNumberEditText.setOnEditorActionListener { view, _, _ ->
            onChangeOrderNumberByEditText(view.text.toString().trimComma().toLongOrNull())
            false
        }

        //下單金額 上升按鈕
        binding.entrustPriceUpButton.setOnClickListener {
            onChangeEntrustPriceByButton(QueryProductViewModel.Signm.INC)
        }

        //下單金額 下降按鈕
        binding.entrustPriceDownButton.setOnClickListener {
            onChangeEntrustPriceByButton(QueryProductViewModel.Signm.DEC)
        }

        //下單數量 上升按鈕
        binding.orderNumberUpButton.setOnClickListener {
            onChangeOrderNumberByButton(QueryProductViewModel.Signm.INC)
        }

        //下單數量 下降按鈕
        binding.orderNumberDownButton.setOnClickListener {
            onChangeOrderNumberByButton(QueryProductViewModel.Signm.DEC)
        }
    }

    private fun onChangeEntrustPriceByEditText(entrustPrice: Double?) {
        if (entrustPrice != null) {
            this.queryProductViewModel.changeEntrustPriceByEditText(entrustPrice)
        }else{
            queryProductViewModel.setLiveEntrustPrice(queryProductViewModel.previousEntrustPrice)
        }
    }

    private fun onChangeOrderNumberByEditText(orderNumber: Long?) {
        if (orderNumber != null) {
            this.queryProductViewModel.changeOrderNumberByEditText(orderNumber)
        }else{
            queryProductViewModel.setLiveOrderNumber(queryProductViewModel.previousOrderNumber)
        }
    }

    //選擇增加或減少委託價
    private fun onChangeEntrustPriceByButton(changeSign: QueryProductViewModel.Signm) {
        this.queryProductViewModel.changeEntrustPriceByButton(changeSign)
    }

    private fun onChangeOrderNumberByButton(changeSign: QueryProductViewModel.Signm) {
        this.queryProductViewModel.changeOrderNumberByButton(changeSign)
    }

    private fun updateTradeStatus(string: String) {
        binding.tradeStatusTextView.text = when (string) {
            "Y" -> "可平空 可先買後賣"
            "N" -> "不可現充"
            "X" -> "可雙向"
            else -> ""
        }
    }

    private fun updateProductName(name: String) {
        binding.productNameTextView.text = name
    }

    private fun updateProductPrice(price: Double) {
        binding.productPriceTextView.text = price.toString()
    }


    @SuppressLint("StringFormatInvalid")
    private fun updateDbper(number: Int) {
        if (number == 0) {
            binding.dbperTextView.visibility = View.GONE
        } else {
            binding.dbperTextView.visibility = View.VISIBLE
        }
        binding.dbperTextView.text = getString(R.string.dbper, number)
    }

    @SuppressLint("StringFormatInvalid")
    private fun updateCrper(number: Int) {
        if (number == 0) {
            binding.crperTextView.visibility = View.GONE
        } else {
            binding.crperTextView.visibility = View.VISIBLE
        }
        binding.crperTextView.text = getString(R.string.crper, number)
    }

    @SuppressLint("StringFormatInvalid")
    private fun updateRiseOrFallProductPrice() {
        val riseFallPrice = queryProductViewModel.riseFallPrice()
        val riseFallPercentPrice = queryProductViewModel.riseFallPercentPrice()
        binding.productPriceRiseOrFallTextView.text =
            getString(R.string.rise_or_fall, riseFallPrice, riseFallPercentPrice)
    }

    private fun updateEntrustPrice(price: Double) {
        binding.entrustPriceEditText.setText(price.displayAccountingNumber())
    }

    private fun updateOrderNumber(price: Int) {
        binding.orderNumberEditText.setText(price.displayAccountingNumber())
    }

    private fun updateCanBuyLotNumber(price: Int) {
        binding.canBuyOrderLotTextView.text =
            price.displayAccountingNumber()
    }

    //計算總估計值 spinner為數量時
    private fun updateEstimateTotalPrice(price: Double) {
        binding.estimateTotalPriceTextView.text =
            price.displayAccountingNumber()
    }

    private fun updateEstimateRestPrice(price: Double) {
        binding.estimateRestPriceTextView.text =
            price.displayAccountingNumber()
    }

    //spinner 切換後
    private fun spnOnItemSelected() = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            val spnTextView = view as TextView
            when (spnTextView.text) {
                "數量" -> {
                    queryProductViewModel.setLiveSpinnerSelected(
                        QueryProductViewModel.SpinnerSelected.LOT
                    )
                }
                "金額" -> {
                    queryProductViewModel.setLiveSpinnerSelected(
                        QueryProductViewModel.SpinnerSelected.PRICE
                    )
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }


    //初始化 spinner為數量
    private fun initBindByLot() {
        with(binding) {
            entrustNumberUnit.text = "張"
            estimateRestPriceTextView.text = "--"
            estimateTotalPriceTextView.text = "--"
            orderNumberEditText.setText("")
            estimateRestPriceTextView.visibility = View.GONE
            canOrderTitleTextView.visibility = View.GONE
            canBuyOrderLotTextView.visibility = View.GONE
            canOrderLotUnit.visibility = View.GONE
            restPriceTitle.visibility = View.GONE
        }
    }

    //初始化 spinner為金額
    private fun initBindByPrice() {
        with(binding) {
            entrustNumberUnit.text = "元"
            estimateRestPriceTextView.text = "--"
            estimateTotalPriceTextView.text = "--"
            orderNumberEditText.setText("")
            canBuyOrderLotTextView.text = ""
            estimateRestPriceTextView.visibility = View.VISIBLE
            canOrderTitleTextView.visibility = View.VISIBLE
            canBuyOrderLotTextView.visibility = View.VISIBLE
            canOrderLotUnit.visibility = View.VISIBLE
            restPriceTitle.visibility = View.VISIBLE
        }
    }
}
