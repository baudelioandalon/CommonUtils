package com.boreal.commonutils.dialogs.blurdialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.boreal.commonutils.R

open class CUBlurDialog constructor(
    @LayoutRes val resource: Int? = null,
    val CUTitleModel: CUTitleModel? = null,
    val messageGeneric: String? = null,
    val messageGenericResource: Spanned? = null,
    val buttonType: ButtonType? = null,
    private val callback: ((view: View) -> Unit)? = null,
    val buttonClicked: ((dialog: Dialog) -> Unit)? = null,
    val buttonTopText : String ? = null,
    val buttonCancelText : String ? = null,
    val buttonCancelClicked: ((dialog: Dialog) -> Unit)? = null,
    private val cancelable: Boolean = true,
    private val blurLevel: Int = 8
) : CUSupportBlurDialogFragmentKotlin() {

    override fun isDimmingEnabled(isDimmingEnabled: Boolean): Boolean = isDimmingEnabled
    override fun isActionBarBlurred(isActionBarBlurred: Boolean): Boolean = isActionBarBlurred
    override fun isRenderScriptEnabled(isRenderScriptEnabled: Boolean): Boolean =
        isRenderScriptEnabled

    override fun blurLevel(blurlevel: Int) = blurLevel
    override fun isDebugEnable(debugEnabled: Boolean): Boolean = debugEnabled
    override fun downScaleFactor(downScaleFactor: Float): Float = downScaleFactor

    lateinit var viewRoot: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (resource == null) {
            viewRoot = inflater.inflate(R.layout.cu_dialog_generic, container, false)
            initElement(viewRoot)
        } else {
            viewRoot = inflater.inflate(resource, container, false)
        }

        // Set transparent background and no title
        isCancelable = cancelable
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        callback?.invoke(viewRoot)

        return viewRoot
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