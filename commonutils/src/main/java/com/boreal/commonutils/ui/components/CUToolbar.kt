package com.boreal.commonutils.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.boreal.commonutils.R
import com.google.android.material.appbar.MaterialToolbar

@SuppressLint("Recycle")
class CUToolbar(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {


    val toolbar: MaterialToolbar by lazy {
        findViewById(R.id.material_toolbar)
    }

    init {
        inflate(context, R.layout.cu_toolbar_white, this)
        context?.obtainStyledAttributes(attrs, R.styleable.AKToolbar)?.let {
            processAttrs(it)
        }
    }


    private fun processAttrs(typedArray: TypedArray) {
        for (i in 0 until typedArray.indexCount) {
            when (val attr = typedArray.getIndex(i)) {
                //Set Label
                R.styleable.AKToolbar_android_title -> setTitle(typedArray.getString(attr))

            }

        }
        typedArray.recycle()
    }

    private fun setTitle(title: String?) {
        toolbar.title = title
    }

    fun enableBackButton(activity: AppCompatActivity) {
        activity.setSupportActionBar(toolbar)
        activity.actionBar?.setDisplayShowTitleEnabled(false)
        val drawable = ContextCompat.getDrawable(activity, R.drawable.ic_baseline_arrow_back_24)
        val actionBar = activity.supportActionBar
        actionBar?.setHomeAsUpIndicator(drawable)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
    }
}