package com.boreal.commonutils.component.roundablelayout

import androidx.core.content.ContextCompat
import com.boreal.commonutils.application.CUAppInit

fun CURoundableLayout.changeBackgroundColor(color: Int) {
    backgroundColor = ContextCompat.getColor(CUAppInit.getAppContext(), color)
}