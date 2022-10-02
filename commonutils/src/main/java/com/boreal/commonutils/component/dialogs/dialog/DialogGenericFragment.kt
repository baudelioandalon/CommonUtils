package com.boreal.commonutils.component.dialogs.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.boreal.commonutils.R

class DialogGenericFragment<T : ViewDataBinding> constructor(
    @LayoutRes val layout: Int,
    val cancelable: Boolean = false,
    val transparent: Boolean = false,
    val fullScreen: Boolean = true,
    val marginWidth: Float = 0.8f,
    val marginHeight: Float = 0.8f,
    val bindingDialog: (T, DialogFragment) -> Unit
) : DialogFragment() {

    lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (fullScreen) {
            setStyle(
                STYLE_NO_TITLE,
                R.style.Widget_MaterialComponents_MaterialCalendar_Fullscreen
            )
        }
    }

    fun getDeviceMetrics(): DisplayMetrics? {
        val metrics = DisplayMetrics()
        val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        display.getMetrics(metrics)
        return metrics
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, layout, container, false) as T

        isCancelable = cancelable
        if (dialog != null && dialog!!.window != null) {
            if (transparent) {
                dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            if (fullScreen) {
                dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            }
        }
        initView()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setLayout(
                if (fullScreen) WindowManager.LayoutParams.MATCH_PARENT else ((getDeviceMetrics()?.widthPixels
                    ?: 1) * marginWidth).toInt(),
                if (fullScreen) WindowManager.LayoutParams.MATCH_PARENT else ((getDeviceMetrics()?.heightPixels
                    ?: 1) * marginHeight).toInt()
            )
        }
    }

    private fun initView() {
        bindingDialog(binding, this)
    }

}