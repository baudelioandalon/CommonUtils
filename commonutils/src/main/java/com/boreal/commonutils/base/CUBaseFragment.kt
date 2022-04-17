package com.boreal.commonutils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.boreal.commonutils.R
import com.boreal.commonutils.extensions.disableBackButton

abstract class CUBaseFragment<T : ViewDataBinding> :
    Fragment() {

    lateinit var cuBackHandler: CUBackHandler
    private var listenerBackPress: CUBackFragment? = null
    private var callbackDispatcher: OnBackPressedCallback? = null

    lateinit var binding: T

    /**
     * @author Baudelio Andalon
     * @since 21/09/2020
     * @see Obtener el layout
     */
    abstract fun getLayout(): Int
    open fun initDependency(savedInstanceState: Bundle? = null) {}
    open fun initObservers() {}
    abstract fun initView()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)

        initDependency(savedInstanceState)
        initObservers()
        if (activity is CUBackHandler) {
            cuBackHandler = activity as CUBackHandler
        }
        initView()
        return binding.root
    }

    /**
     * @author DanielGC
     * @see Muestra el progressabar
     * @param message: String?
     * @param isCancelable: Boolean
     */
    fun showProgress(
        message: String? = null,
        isCancelable: Boolean = false,
        lottieResourceCustom: Int? = R.raw.a_loading_lottie
    ) {
        cuBackHandler.showProgress(message, isCancelable, lottieResourceCustom)
    }

    fun disableBackButton() {
        requireActivity().disableBackButton()
    }

    fun onBackPressedDispatcher(onBackPressed: (back: OnBackPressedCallback) -> Unit) {
        callbackDispatcher = requireActivity().onBackPressedDispatcher.addCallback {
            onBackPressed.invoke(this)
        }
    }

    fun onFragmentBackPressed(forceBackPressed: Boolean = false) {
        if (!forceBackPressed) {
            requireActivity().onBackPressed()
        } else {
            callbackDispatcher?.isEnabled = false
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        callbackDispatcher = null
    }

    fun disableOnBackDispatcher() {
        callbackDispatcher?.isEnabled = false
    }

    fun enableOnBackDispatcher() {
        callbackDispatcher?.isEnabled = true
    }

    /**
     * @author DanielGC
     * @see Oculta el progressbar
     */
    fun hideProgressBarCustom() {
        cuBackHandler.hideProgress()
    }

    fun setBackFragment(listenerBackPress: CUBackFragment) {
        this.listenerBackPress = listenerBackPress
    }

    /**
     * @author DanielGC
     */
    fun hideKeyboard() {
        cuBackHandler.hideKeyBoard()
    }

    fun showLottie(
        lottie: LottieAnimationView,
        containerPersonality: View,
        containerParent: View, show: Boolean
    ) {
        cuBackHandler.showLottie(
            lottie,
            containerPersonality,
            containerParent, show
        )
    }

//    /**
//     * Acción que se ejecuta cuando se detecta que se preciona el boton de back de android,
//     * esta funcion es llamada desde base activity
//     */
//    fun onFragmentBackPressed(): Boolean {
//        listenerBackPress?.onBackPress()
//        return false
//    }


    /**
     * Interface que se implementa en cada fragmento que requiera aplicar una
     * funcionalidad cuando se detecte que se preciono el boton de back
     */
    interface CUBackFragment {
        fun onBackPress()
    }

}