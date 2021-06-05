package com.boreal.commonutils.component.dialogs.blurdialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager

abstract class CUBlurDialogBinding<B : ViewDataBinding> constructor(
    private val callback: ((view: View) -> Unit)? = null,
    private val cancelable: Boolean = true,
) : CUSupportBlurDialogFragmentKotlin() {

    lateinit var mBinding: B

    abstract fun getLayout(): Int

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

        mBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        isCancelable = cancelable
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        callback?.invoke(mBinding.root)

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