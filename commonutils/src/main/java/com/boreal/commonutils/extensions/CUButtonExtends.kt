package com.boreal.commonutils.extensions

import android.util.TypedValue
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import com.boreal.commonutils.application.CUAppInit


fun Button.changeTypeFace(fontFamily: Int) {
    typeface = ResourcesCompat.getFont(
        CUAppInit.getAppContext(),
        fontFamily
    )
}



fun Button.changeTextSize(sizeInDP: Int) {
    this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeInDP.toFloat())
}