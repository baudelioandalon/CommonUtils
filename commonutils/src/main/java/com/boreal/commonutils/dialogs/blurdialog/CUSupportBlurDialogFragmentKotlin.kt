package com.boreal.commonutils.dialogs.blurdialog

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.boreal.commonutils.R
import fr.tvbarthel.lib.blurdialogfragment.BlurDialogEngine

abstract class CUSupportBlurDialogFragmentKotlin : DialogFragment() {


    private var mBlurEngine: BlurDialogEngine? = null
    private var mToolbar: Toolbar? = null
    private var mDimmingEffect = false

    abstract fun isDimmingEnabled (isDimmingEnabled: Boolean = false) : Boolean
    abstract fun isActionBarBlurred(isActionBarBlurred: Boolean = false) : Boolean
    abstract fun isRenderScriptEnabled(isRenderScriptEnabled: Boolean = false) : Boolean
    abstract fun blurLevel(blurlevel: Int = 8): Int
    abstract fun isDebugEnable(debugEnabled: Boolean = false) : Boolean
    abstract fun downScaleFactor(downScaleFactor: Float = 4.0f) : Float

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (mBlurEngine != null) {
            mBlurEngine!!.onAttach(activity) // re attached
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBlurEngine = BlurDialogEngine(requireActivity())
        if (mToolbar != null) {
            mBlurEngine!!.setToolbar(mToolbar)
        }
        val radius = blurLevel()
        require(radius > 0) { "Blur radius must be strictly positive. Found : $radius" }
        mBlurEngine!!.setBlurRadius(radius)
        val factor = downScaleFactor()
        require(factor > 1.0) { "Down scale must be strictly greater than 1.0. Found : $factor" }
        mBlurEngine!!.setDownScaleFactor(factor)
        mBlurEngine!!.setUseRenderScript(isRenderScriptEnabled())
        mBlurEngine!!.debug(isDebugEnable())
        if(isActionBarBlurred()){
            mBlurEngine!!.setBlurActionBar(isActionBarBlurred())
        }
        mDimmingEffect = isDimmingEnabled()
    }

    override fun onStart() {
        val dialog = dialog
        if (dialog != null) {

            // enable or disable dimming effect.
            if (!mDimmingEffect) {
                dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }

            // add default fade to the dialog if no window animation has been set.
            val currentAnimation = dialog.window!!.attributes.windowAnimations
            if (currentAnimation == 0) {
                dialog.window!!.attributes.windowAnimations =
                    R.style.BlurDialogFragment_Default_Animation
            }
        }
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        mBlurEngine!!.onResume(retainInstance)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mBlurEngine!!.onDismiss()
    }

    override fun onDetach() {
        super.onDetach()
        mBlurEngine!!.onDetach()
    }

    override fun onDestroyView() {
        if (dialog != null) {
            dialog!!.setDismissMessage(null)
        }
        super.onDestroyView()
    }

    fun setToolbar(toolBar: Toolbar?) {
        mToolbar = toolBar
        if (mBlurEngine != null) {
            mBlurEngine!!.setToolbar(toolBar)
        }
    }

}