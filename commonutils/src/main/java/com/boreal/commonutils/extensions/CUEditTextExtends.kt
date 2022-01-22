package com.boreal.commonutils.extensions

import android.content.Context
import android.text.InputFilter
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.boreal.commonutils.application.CUAppInit

/**
 * @author Baudelio Andalon
 * @sample edtName.maxLength(20)
 * Definir el tamaño maximo de un editText
 */
fun EditText.maxLength(maxLength: Int) {
    filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
}


/**
 * @author Baudelio Andalon
 * @sample edit.changeHintTextColor(R.color.redColor)
 * Definir el color del hint de un EditText
 */
fun EditText.changeHintTextColor(color: Int, context: Context = CUAppInit.getAppContext()) {
    setHintTextColor(ContextCompat.getColor(context, color))
}