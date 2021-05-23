package com.boreal.commonutils.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.boreal.commonutils.R
import com.boreal.commonutils.application.CUAppInit
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.END_ICON_NONE


@SuppressLint("Recycle")
class CUTextField(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    val tvLabel: TextView by lazy {
        findViewById(R.id.tv_label)
    }

    val textInputLayout: TextInputLayout by lazy {
        findViewById(R.id.text_input_layout)
    }

    val editText: TextInputEditText by lazy {
        findViewById(R.id.edit_text)
    }


    init {
        inflate(context, R.layout.cu_text_field, this)
        context?.obtainStyledAttributes(attrs, R.styleable.AKTextField)?.let {
            processAttrs(it)
        }
    }

    private fun processAttrs(typedArray: TypedArray) {
        for (i in 0 until typedArray.indexCount) {
            when (val attr = typedArray.getIndex(i)) {
                //Set Label
                R.styleable.AKTextField_label -> setLabel(typedArray.getString(attr))
                //SetInputType
                R.styleable.AKTextField_android_inputType -> setInputType(
                    typedArray.getInt(
                        attr,
                        EditorInfo.TYPE_TEXT_VARIATION_NORMAL
                    )
                )
                //End Icon Mode
                R.styleable.AKTextField_endIconMode -> {
                    setEndIconMode(typedArray.getInt(attr, END_ICON_NONE))
                }
                //Label color
                R.styleable.AKTextField_android_textColor -> {
                    setColorLabel(typedArray.getInt(attr, R.color.blue))
                }
                //Set end icon
                R.styleable.AKTextField_endIconDrawable -> {
                    setEndIconDrawable(typedArray.getResourceId(attr, -1))
                }

                // Max Length
                R.styleable.AKTextField_android_maxLength -> {
                    setMaxLength(typedArray.getInt(attr, -1))
                }

            }

        }
        typedArray.recycle()
    }

    private fun setLabel(charSequence: CharSequence?) {
        tvLabel.text = charSequence
    }

    private fun setColorLabel(color: Int) {
        tvLabel.setTextColor(color)
    }

    private fun setInputType(type: Int) {
        editText.inputType = type
    }

    private fun setEndIconMode(iconMode: Int) {
        textInputLayout.endIconMode = iconMode
    }

    private fun setMaxLength(maxNumber: Int) {
        if (maxNumber == -1) return
        editText.filters = editText.filters + arrayOf<InputFilter>(LengthFilter(maxNumber))
    }

    private fun setEndIconDrawable(drawable: Int) {
        if (drawable == -1) return
        textInputLayout.endIconDrawable = ContextCompat.getDrawable(
            CUAppInit.getAppContext(),
            drawable
        )
    }
}