package com.boreal.commonutils.base

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.boreal.commonutils.R
import com.boreal.commonutils.databinding.CuLoadingViewBinding
import com.kaopiz.kprogresshud.KProgressHUD

abstract class CUBaseActivity<B> : AppCompatActivity(), CUBackHandler {

    val binding: B by lazy {
        DataBindingUtil.setContentView(this, getLayout()) as B
    }

    private var hud: KProgressHUD? = null

    //    private var AKCUBaseFragment: AKCUBaseFragment? = null
    var isEnableActionButtonBackPress = true

    abstract fun getLayout(): Int
    open fun initDependency() {}
    open fun initObservers() {}
    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDependency()
        initObservers()
        initView()
        adjustFontScale(resources.configuration, 1.0f)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    override fun showProgressBarCustom(message: String?, isCancelable: Boolean) {
        val view = layoutInflater.inflate(R.layout.cu_loading_view, null)
        val mBindingMessage = CuLoadingViewBinding.bind(view)
        message?.let {
            mBindingMessage.txtLoading.text = message
        }

        if (hud == null) {
            mBindingMessage.lottieView.setAnimation(R.raw.a_loading_lottie)
            mBindingMessage.lottieView.repeatCount = LottieDrawable.INFINITE
            mBindingMessage.lottieView.playAnimation()

            hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCustomView(view)
                .setAnimationSpeed(2)
                .setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                .setDimAmount(0.6f)
        }
        if (hud != null) {
            hud?.setCancellable(isCancelable)
            if (isCancelable) {
                hud?.setCancellable {
                    hideProgressBarCustom()
                }
            }
            hud?.show()
        }

    }


    private fun configToolbarDefault() {
        this.apply {
            window.statusBarColor = Color.WHITE
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }


    override fun hideProgressBarCustom() {
        if (hud != null && hud!!.isShowing)
            hud?.dismiss()
    }

    override fun hideKeyBoard() {
        if (currentFocus != null) {
            val inputMethodManager: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun showKeyBoard(viewEditable: View) {
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(viewEditable, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    /**
     * MUESTRA U OCULTA EL LOTTIE
     */
    override fun showLottie(
        lottie: LottieAnimationView,
        containerPersonality: View,
        containerParent: View, show: Boolean
    ) {
        if (show) {
            isEnableActionButtonBackPress = false
            hideKeyBoard()
            lottie.visibility = View.VISIBLE
            containerPersonality.visibility = View.GONE
            containerParent.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.white
                )
            )
        } else {
            isEnableActionButtonBackPress = true
            lottie.visibility = View.GONE
            containerPersonality.visibility = View.VISIBLE
            containerParent.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.grayLight
                )
            )
        }
    }


    /**
     * Funcion que anula la configuracion del sistema para cambiar el tamaño de las fuente
     */
    private fun adjustFontScale(configuration: Configuration, scale: Float) {
        configuration.fontScale = scale
        val metrics = resources.displayMetrics
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)
    }

}