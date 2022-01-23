package com.boreal.commonutils.dialogs.blurdialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.boreal.commonutils.component.dialogs.blurdialog.CUSupportBlurDialogFragmentKotlin

class CUBlurDialogBinding<B : ViewDataBinding> constructor(
    @LayoutRes private val layout: Int,
    private val cancelable: Boolean = true,
    private val callback: ((binding: B, dialog: DialogFragment) -> Unit)? = null,
) : CUSupportBlurDialogFragmentKotlin() {

    lateinit var mBinding: B

    override fun isDimmingEnabled(isDimmingEnabled: Boolean): Boolean = isDimmingEnabled
    override fun isActionBarBlurred(isActionBarBlurred: Boolean): Boolean = isActionBarBlurred
    override fun isRenderScriptEnabled(isRenderScriptEnabled: Boolean): Boolean =
        isRenderScriptEnabled

    override fun blurLevel(blurlevel: Int) = 8
    override fun isDebugEnable(debugEnabled: Boolean): Boolean = debugEnabled
    override fun downScaleFactor(downScaleFactor: Float): Float = downScaleFactor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = DataBindingUtil.inflate(inflater, layout, container, false)
        isCancelable = cancelable
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        callback?.invoke(mBinding, this)

        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            dialog?.window!!.setLayout(
                (resources.displayMetrics.widthPixels * 0.9).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    fun showCustom(manager: FragmentManager, tag: String?) {
        if (!this.isAdded && (this.dialog == null || !this.dialog!!.isShowing)) {
            this.show(manager, tag)
        }

    }

    fun hide() {
        if (this.dialog != null && this.dialog!!.isShowing)
            this.dismiss()
    }

}