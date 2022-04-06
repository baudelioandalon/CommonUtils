package com.boreal.commonutils.base

import android.view.View
import androidx.annotation.RawRes
import com.airbnb.lottie.LottieAnimationView
import com.boreal.commonutils.R

interface CUBackHandler {
    fun showProgress(
        message: String? = null,
        isCancelable: Boolean = false,
        @RawRes lottieResource: Int? = R.raw.a_loading_lottie
    )

    fun hideProgress()
    fun hideKeyBoard()
    fun showKeyBoard(viewEditable: View)
    fun showLottie(
        lottie: LottieAnimationView,
        containerPersonality: View,
        containerParent: View, show: Boolean
    )
}