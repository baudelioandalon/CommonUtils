package com.boreal.commonutils.component.cunumberamount

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.text.InputFilter
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.boreal.commonutils.R
import com.boreal.commonutils.component.*
import com.google.android.material.textview.MaterialTextView
import java.text.NumberFormat
import java.util.*


/**
 * @author Baudelio Andalon
 * @sample
 * <com.boreal.commonutils.component.cunumberamount.CUNumberAmount
 * android:id="@+id/txtInputElement"
 * android:layout_width="wrap_content"
 * android:layout_height="wrap_content"/>
 *
 * @property txtInputElement.getIntegers()
 * @property txtInputElement.getDecimals()
 * @property txtInputElement.getAmount()
 * @property txtInputElement.getAmountString()
 */
@SuppressLint("Recycle")
class CUNumberAmount(context: Context, val attrs: AttributeSet?) :
    LinearLayout(context, attrs) {

    private var current = ""
    private var enteros = ""
    private var amount = 0F
    private var amountAux = 0F
    private var sumAmountAux = 0F
    private var amountStr = ""
    private var limitIntegers = 5
    private var defaultEnabled = true
    private var defaultBehavior = true
    private var sumEnable = false
    private var sumReset = false
    private var defaultColorComponent = R.color.black
    private var defaultColorCashSimbol = R.color.black
    private var defaultGravityCashSimbol = 2
    private var defaultGravityDecimal = 3
    private var defaultColorInteger = R.color.black
    private var defaultColorDotSimbol = R.color.black
    private var defaultColorDecimal = R.color.black
    private var defaultSizeComponent = 30
    private var defaultSizeCashSimbol = 24
    private var defaultSizeInteger = 1f
    private var defaultSizeDot = 1f
    private var defaultSizeDecimal = 0.6f

    /**
     * Detecta cambios en el componenete, regresa 4 parametros
     * valueInString: String Ejemplo = $1,000.32
     * integers: Striing Ejemplo = 1000
     * decimals: String Ejemplo = 32
     * amountElement: Float Ejemplo 1000.32
     */
    lateinit var onValueChanged: (valueInString: String, integers: String, amountElement: Float) -> Unit

    private val txtCashSimbol: MaterialTextView by lazy {
        findViewById(R.id.cashSimbol)
    }

    private val edtIntegers: EditText by lazy {
        findViewById(R.id.etNumbers)
    }

    init {
        inflate(context, R.layout.cu_number_amount, this)
        processAttrs(context.obtainStyledAttributes(attrs, R.styleable.CUNumberAmount))

        //DefaultMethods
        setBehavior(defaultBehavior)
        isEnabledEdt(defaultEnabled)
        setColorComponent(defaultColorComponent)
        setColorCashSimbol(defaultColorCashSimbol)
        setGravityCashSimbol(defaultGravityCashSimbol)
        setColorInteger(defaultColorInteger)
        setGravityDecimal(defaultGravityDecimal)
        setSizeComponent(defaultSizeComponent)
        setSizeCashSimbol(defaultSizeCashSimbol)
        setSizeInteger(defaultSizeInteger)
        setLimitInteger(limitIntegers)
    }

    fun setValue(valueInString: String) {
        val values = valueInString.split(".")
        edtIntegers.setText(values.getOrNull(0))
    }


    fun setValueInt(valueInString: String) {
        if (valueInString.length > 7) {
            txtCashSimbol.textSize = 20F
            edtIntegers.textSize = 40F
            edtIntegers.setText(valueInString)
        } else {
            edtIntegers.setText(valueInString)
        }
    }

    fun setAmount(valueInString: String) {
        edtIntegers.apply {
            amountStr += valueInString
                .replace(",", "")
                .replace(".", "")
            setText(amountStr)
        }
    }

    fun sumAmount() {
        edtIntegers.apply {
            if (!sumReset) {
                val getAmount = text.toString()
                amountAux += if (getAmount.contains(",")) {
                    getAmount.replace(",", "").toFloat()
                } else {
                    getAmount.toFloat()
                }
                if (amountAux > 0) {
                    sumAmountAux = amountAux
                    if (sumEnable) {
                        amountStr = if (amountAux.round().toString().contains(".")) {
                            when {
                                amountAux.round().toString().split(".")[1] == "0" -> {
                                    amountAux.round().toString().split(".")[0] + ".00"
                                }
                                amountAux.round().toString().split(".")[1].length == 1 -> {
                                    amountAux.round().toString()
                                        .split(".")[0] +
                                            "." + amountAux.round().toString().split(".")[1] + "0"
                                }
                                else -> {
                                    amountAux.round().toString()
                                }
                            }
                        } else {
                            amountAux.round().toString()
                        }
                        setText("0")
                        sumEnable = false
                        sumReset = true
                    } else {
                        amountStr = "0"
                        setText(amountStr)
                        sumEnable = true
                        sumReset = false
                    }
                }
            }
        }
    }

    fun setSum(sum: String) {
        edtIntegers.apply {
            if (!sumReset) {
                setText("0")
                setText(sum)
            }
        }
    }

    fun Float.round(decimals: Int = 2): Float = "%.${decimals}f".format(this).toFloat()

    /**
     * Tiene que ir siempre arriba antes se setear la cantidad
     * para efecto de reiniciar la cantidad al momento de sumar
     */
    fun setResetAmount() {
        edtIntegers.apply {
            if (sumReset) {
                amountStr = "0"
                setText(amountStr)
                sumReset = false
                sumEnable = true
            }
        }
    }

    fun deleteAmount() {
        edtIntegers.apply {
            if (!sumReset) {
                val array: Array<String> =
                    amountStr.toCharArray().map { it.toString() }.toTypedArray()
                val arr = remove(array.toMutableList(), array.lastIndex)
                if (arr.size == 0 || amountStr == "0.00" || amountStr == "0.0" || amountStr == "0") {
                    amountStr = "0"
                    setText(amountStr)
                    amountAux = amountStr.toFloat()
                } else {
                    amountStr = arr.joinToString()
                        .filterNot { it.isWhitespace() }
                        .replace(",", "")
                    setText(amountStr)
                    amountAux = amountStr.toFloat()
                }
                sumEnable = false
                sumReset = false
            }
        }
    }

    fun longDeleteAmount() {
        edtIntegers.apply {
            amountStr = "0"
            setText(amountStr)
            amountAux = amountStr.toFloat()
            sumEnable = false
            sumReset = false
        }
    }

    private fun remove(arr: MutableList<String>, index: Int): MutableList<String> {
        if (index < 0 || index >= arr.size) {
            return arr
        }
        val aux: MutableList<String> = arr.toMutableList()
        position = 0
        val result = recursiveDelete(aux)
        result.removeAt(index)
        return result
    }

    var position = 0
    private fun recursiveDelete(aux: MutableList<String>): MutableList<String> {
        if (position == aux.size) {
            return aux
        } else {
            if (aux[position] == "," || aux[position] == ".") {
                aux.removeAt(position)
                recursiveDelete(aux)
            }
            position++
        }
        return aux
    }

    fun setEnabledInput(enabled: Boolean) {
        edtIntegers.isEnabled = enabled
    }

    fun setLimitInteger(maxValue: Int) {
        limitIntegers = maxValue
        edtIntegers.maxLength(limitIntegers + 4)
    }

    fun getIntegers() = if (edtIntegers.text.toString().isNotEmpty()) {
        edtIntegers.text.toString().replace(",", "").toInt()
    } else {
        0
    }


    fun getIntegerString() =
        if (edtIntegers.text.toString().isNotEmpty()) {
            "${edtIntegers.text}"
        } else {
            "0"
        }


    fun getAmount(): Float {
        return amount
    }

    fun getAmountSum(): String {
        return if (sumAmountAux.round().toString().contains(".")) {
            when {
                sumAmountAux.round().toString().split(".")[1] == "0" -> {
                    sumAmountAux.round().toString().split(".")[0] + ".00"
                }
                sumAmountAux.round().toString().split(".")[1].length == 1 -> {
                    sumAmountAux.round().toString()
                        .split(".")[0] +
                            "." + sumAmountAux.round().toString().split(".")[1] + "0"
                }
                else -> {
                    sumAmountAux.round().toString()
                }
            }
        } else {
            sumAmountAux.round().toString()
        }
    }


    fun getAmountString() =
        if (edtIntegers.text.toString().isNotEmpty()) {
            "$${edtIntegers.text}"
        } else {
            "$0"
        }

    private fun processAttrs(typedArray: TypedArray) {
        for (i in 0 until typedArray.indexCount) {
            when (val attr = typedArray.getIndex(i)) {
                //Effect on complete component

                //Enable Behavior
                R.styleable.CUNumberAmount_na_behavior -> {
                    setBehavior(typedArray.getBoolean(attr, defaultBehavior))
                }

                //Enabled component
                R.styleable.CUNumberAmount_na_isEnabled -> {
                    isEnabledEdt(typedArray.getBoolean(attr, defaultEnabled))
                }

                //Color component
                R.styleable.CUNumberAmount_na_complete_color -> {
                    setColorComponent(
                        typedArray.getResourceId(attr, defaultColorComponent)
                    )
                }

                //Size component
                R.styleable.CUNumberAmount_na_complete_size -> {
                    setSizeComponent(
                        typedArray.getInt(attr, defaultSizeComponent)
                    )
                }

                //Size component
//                R.styleable.CUNumberAmount_na_no_decimals -> {
//                    setNoDecimals(
//                        typedArray.getBoolean(attr, defaultNoDecimals)
//                    )
//                }

                //_____________________________

                //Color cash simbol
                R.styleable.CUNumberAmount_na_cash_simbol_color -> {
                    setColorCashSimbol(
                        typedArray.getResourceId(attr, defaultColorCashSimbol)
                    )
                }

                //Gravity cash simbol
                R.styleable.CUNumberAmount_na_cash_simbol_gravity -> {
                    setGravityCashSimbol(
                        typedArray.getInt(attr, defaultGravityCashSimbol)
                    )
                }

                //Color integer
                R.styleable.CUNumberAmount_na_integer_color -> {
                    setColorInteger(
                        typedArray.getResourceId(attr, defaultColorInteger)
                    )
                }

                //Size cash simbol
                R.styleable.CUNumberAmount_na_cash_simbol_size -> {
                    setSizeCashSimbol(
                        typedArray.getInt(attr, defaultSizeCashSimbol)
                    )
                }

                //Size integer
                R.styleable.CUNumberAmount_na_integer_size -> {
                    setSizeInteger(
                        typedArray.getFloat(attr, defaultSizeInteger)
                    )
                }

                //MaxLength
                R.styleable.CUNumberAmount_na_max_length -> {
                    setLimitInteger(typedArray.getInt(attr, limitIntegers))
                }
            }
        }
        typedArray.recycle()
    }

    /**
     * Actualiza un valor especifico onbtenido del diseño xml
     */
//    fun updateParameterNoDecimals() {
//
//        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CUNumberAmount)
//
//        defaultNoDecimals = a.getBoolean(R.styleable.CUNumberAmount_na_no_decimals, false)
//
//    }

    fun setGravityCashSimbol(gravityEnum: Int) {
        defaultGravityCashSimbol = gravityEnum
        txtCashSimbol.gravity = when (defaultGravityCashSimbol) {
            1 -> {
                Gravity.TOP
            }
            2 -> {
                Gravity.CENTER
            }
            3 -> {
                Gravity.BOTTOM
            }
            else -> {
                Gravity.CENTER
            }

        }
    }

    fun setGravityDecimal(gravityEnum: Int) {
        defaultGravityDecimal = gravityEnum
//        edtDecimals.gravity = when (defaultGravityDecimal) {
//            1 -> {
//                Gravity.TOP
//            }
//            2 -> {
//                Gravity.CENTER
//            }
//            3 -> {
//                Gravity.BOTTOM
//            }
//            else -> {
//                Gravity.CENTER
//            }
//
//        }
    }

    fun EditText.setMaxLength(max: Int) {
        filters = arrayOf<InputFilter>(InputFilter.LengthFilter(max))
    }

    private fun setBehavior(startBehavior: Boolean, maxValue: Int = 15) {
        defaultBehavior = startBehavior
        var edited = false
        if (defaultBehavior) {
            edtIntegers.setMaxLength(maxValue)
            edtIntegers.doOnTextChanged { s, start, before, count ->
                if (!edited) {
                    if (!s.isNullOrEmpty()) {
                        if (s.toString() != current) {
                            val cleanString: String = s.replace("""[$,.€]""".toRegex(), "")
                            if (cleanString.isNotEmpty()) {

//                                if (!defaultNoDecimals) {
//                                    edtDecimals.hideView()
//                                }

                                val parsed = cleanString.toDouble()

                                val formatted = NumberFormat.getCurrencyInstance(Locale.US)
                                    .format((parsed / 100))
                                    .replace("€", "").trim().trimIndent().replace("$", "")


                                current = formatted
                                enteros = cleanString
                                amount = parsed.toFloat()
                                edited = true

                                val cleanText = NumberFormat.getCurrencyInstance(Locale.US)
                                    .format(
                                        enteros.replace(",", "").replace("€", "")
                                            .replace("$", "").toDouble()
                                    ).replace("€", "").trim().trimIndent().replace("$", "")

                                val noPoint = cleanText.substring(0, cleanText.lastIndexOf('.'))

                                val decimalText = ".00"
                                val decimalString = SpannableString(decimalText)
                                /**
                                 * INTEGER
                                 */
                                decimalString.setSpan(
                                    ForegroundColorSpan(
                                        ContextCompat.getColor(
                                            context,
                                            defaultColorDecimal
                                        )
                                    ),
                                    0,
                                    decimalText.length,
                                    0
                                ) // set color

                                edtIntegers.setText(noPoint)
                                edtIntegers.setSelection(noPoint.length)
//                                    edtDecimals.text = decimalText

                                if (this::onValueChanged.isInitialized) {
                                    onValueChanged.invoke(
                                        getAmountString(),
                                        getIntegerString(),
                                        getAmount()
                                    )
                                }
                            }

                        }
                    } else {
                        amount = 0F
                        if (this::onValueChanged.isInitialized) {
                            onValueChanged.invoke(
                                getAmountString(),
                                getIntegerString(),
                                getAmount()
                            )
                        }
                    }

                } else {
                    edited = false
                }
            }

        } else {
            edtIntegers.hint = "0"
        }
    }

    fun setColorCashSimbol(color: Int) {
        defaultColorCashSimbol = color
        txtCashSimbol.changeTextColor(defaultColorCashSimbol, context)
    }

    fun isEnabledEdt(enabled: Boolean) {
        defaultEnabled = enabled
        edtIntegers.isEnabled = defaultEnabled
    }

    fun setColorInteger(color: Int) {
        defaultColorInteger = color
    }

    fun setSizeComponent(sizeLetter: Int) {
        defaultSizeComponent = sizeLetter
        edtIntegers.changeTextSize(defaultSizeComponent)
    }

    fun setSizeCashSimbol(sizeLetter: Int) {
        defaultSizeCashSimbol = sizeLetter
        txtCashSimbol.changeTextSize(defaultSizeCashSimbol)
    }

    fun setSizeInteger(sizeLetter: Float) {
        defaultSizeInteger = sizeLetter
        edtIntegers.changeTextSize(defaultSizeInteger.toDouble().toInt())
        edtIntegers.setText("0")
    }

    fun setColorComponent(color: Int) {
        defaultColorComponent = color
        edtIntegers.changeHintTextColor(defaultColorComponent, context)
        edtIntegers.changeTextColor(defaultColorComponent, context)
    }

}