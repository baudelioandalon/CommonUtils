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
import com.boreal.commonutils.R

abstract class CUBaseActivity<B> : AppCompatActivity(), CUBackHandler {

    val binding: B by lazy {
        DataBindingUtil.setContentView(this, getLayout()) as B
    }

    private lateinit var dialog: DialogFragmentType

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

    override fun showProgress(
        message: String?,
        isCancelable: Boolean,
        lottieResource: Int?,
    ) {
        if (!this::dialog.isInitialized) {
            dialog =
                DialogFragmentType(
                    textLoading = message ?: "",
                    cancelableDialog = isCancelable,
                    lottieResource = lottieResource
                )
        }
        if (!dialog.isVisible && !dialog.isAdded) {
            dialog.show(supportFragmentManager, "dialog")
        }

    }

    fun configToolbarDefault(color: Int = Color.WHITE) {
        window.statusBarColor = color
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }


    override fun hideProgress() {
        if (this::dialog.isInitialized && dialog.isVisible) {
            dialog.dismiss()
        }
    }

    override fun hideKeyBoard() {
        if (currentFocus != null) {
            val inputMethodManager: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun showKeyBoard(viewEditable: View, requestFocused: Boolean) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInputFromWindow(
            viewEditable.applicationWindowToken,
            InputMethodManager.SHOW_FORCED, 0
        )
        if (requestFocused) {
            viewEditable.requestFocus()
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