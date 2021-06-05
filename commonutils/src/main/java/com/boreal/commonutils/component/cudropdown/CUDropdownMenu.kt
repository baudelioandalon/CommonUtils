package com.boreal.commonutils.component.cudropdown

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.TextView
import com.boreal.commonutils.R
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.END_ICON_NONE

@SuppressLint("Recycle")
class CUDropdownMenu(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    val tvLabel: TextView by lazy {
        findViewById(R.id.tv_label)
    }

    val textInputLayout: TextInputLayout by lazy {
        findViewById(R.id.text_input_layout)
    }

    val autocomplete: AutoCompleteTextView by lazy {
        findViewById(R.id.auto_complete)
    }


    init {
        inflate(context, R.layout.cu_exposed_dropdown, this)

        context?.obtainStyledAttributes(attrs, R.styleable.AKAutocomplete)?.let {
            processAttrs(it)
        }
    }

    private fun processAttrs(typedArray: TypedArray) {
        for (i in 0 until typedArray.indexCount) {
            when (val attr = typedArray.getIndex(i)) {
                //Set Label
                R.styleable.AKAutocomplete_label -> setLabel(typedArray.getString(attr))
                //SetInputType
                R.styleable.AKAutocomplete_android_inputType -> setInputType(
                    typedArray.getInt(
                        attr,
                        EditorInfo.TYPE_TEXT_VARIATION_NORMAL
                    )
                )
                //End Icon Mode
                R.styleable.AKAutocomplete_endIconMode -> {
                    setEndIconMode(typedArray.getInt(attr, END_ICON_NONE))
                }
                //Label color
                R.styleable.AKAutocomplete_android_textColor -> {
                    setColorLabel(typedArray.getInt(attr, R.color.blue))
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
        autocomplete.inputType = type
    }

    private fun setEndIconMode(iconMode: Int) {
        textInputLayout.endIconMode = iconMode
    }

}