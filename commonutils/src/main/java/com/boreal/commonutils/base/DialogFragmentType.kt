package com.boreal.commonutils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieDrawable
import com.boreal.commonutils.R
import com.boreal.commonutils.databinding.CuLoadingViewBinding
import com.boreal.commonutils.extensions.setOnSingleClickListener

class DialogFragmentType(
    private val textLoading: String? = null,
    private val cancelableDialog: Boolean = false,
    private val lottieResource: Int? = R.raw.a_loading_lottie,
) : DialogFragment(R.layout.cu_loading_view) {

    private lateinit var bindingDialog: CuLoadingViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialog)
        isCancelable = cancelableDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = CuLoadingViewBinding.inflate(inflater, container, false).apply {
        bindingDialog = this
        txtLoading.text = textLoading ?: ""
        if (cancelableDialog) {
            containerLoading.setOnSingleClickListener {
                dialog?.dismiss()
            }
        }

        lottieResource?.let {
            lottieView.setAnimation(lottieResource)
            lottieView.repeatCount = LottieDrawable.INFINITE
            lottieView.playAnimation()
        }
    }.root

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            isCancelable = cancelableDialog
            val window: Window? = dialog!!.window
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }
    }

}