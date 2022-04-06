package com.boreal.commonutils.base

import android.view.View
import com.airbnb.lottie.LottieAnimationView

interface CUBackHandler {
    fun showProgress(message: String? = "Cargando...", isCancelable: Boolean = false)
    fun hideProgress()
    fun hideKeyBoard()
    fun showKeyBoard(viewEditable: View)
    fun showLottie(
        lottie: LottieAnimationView,
        containerPersonality: View,
        containerParent: View, show: Boolean
    )
}