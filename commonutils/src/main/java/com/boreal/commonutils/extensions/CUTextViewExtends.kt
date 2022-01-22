package com.boreal.commonutils.extensions

import android.content.Context
import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.boreal.commonutils.application.CUAppInit

/**
 * @author Baudelio Andalon
 * @sample tvName.changeTextColor(R.color.redColor)
 * Cambiar el color de un textView
 */
fun TextView.changeTextColor(color: Int, context: Context = CUAppInit.getAppContext()) {
    setTextColor(
        ContextCompat.getColor(
            context,
            color
        )
    )
}

fun TextView.changeTypeFace(fontFamily: Int) {
    typeface = ResourcesCompat.getFont(
        CUAppInit.getAppContext(),
        fontFamily
    )
}

/**
 *
 * */
fun TextView.changeFontSizeColorText(
    fontFamily: Int? = null,
    sizeInDP: Int? = null,
    color: Int? = null, textString: String? = null
) {
    fontFamily?.let {
        changeTypeFace(it)
    }
    sizeInDP?.let {
        changeTextSize(it)
    }
    color?.let {
        changeTextColor(it)
    }
    textString?.let {
        text = it
    }
}

fun TextView.changeTextSize(sizeInDP: Int) {
    this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeInDP.toFloat())
}