package com.boreal.commonutils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.boreal.commonutils.R
import com.boreal.commonutils.databinding.CuLoadingViewBinding

class DialogFragmentType(
    @LayoutRes val layoutRes: Int = R.layout.cu_loading_view,
    private val cancelableDialog: Boolean = false
) : DialogFragment(layoutRes) {

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
    }.root

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            isCancelable = cancelableDialog
            dialog!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }
    }

}