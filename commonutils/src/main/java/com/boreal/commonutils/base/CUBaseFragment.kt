package com.boreal.commonutils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.boreal.commonutils.extensions.disableBackButton
import kotlin.reflect.KClass

abstract class CUBaseFragment<T : ViewDataBinding> :
    Fragment() {

    lateinit var cuBackHandler: CUBackHandler
    private var listenerBackPress: CUBackFragment? = null

    lateinit var mBinding: T

    /**
     * @author Baudelio Andalon
     * @since 21/09/2020
     * @see Obtener el layout
     */
    abstract fun getLayout(): Int
    open fun initDependency(savedInstanceState: Bundle? = null) {}
    open fun initObservers(){}
    abstract fun initView()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false)

        initDependency(savedInstanceState)
        initObservers()
        if (activity is CUBackHandler) {
            cuBackHandler = activity as CUBackHandler
        }
        initView()
        return mBinding.root
    }

    /**
     * @author DanielGC
     * @see Muestra el progressabar
     * @param message: String?
     * @param isCancelable: Boolean
     */
    fun showProgressBarCustom(message: String? = null, isCancelable: Boolean = false) {
        cuBackHandler.showProgressBarCustom(message, isCancelable)
    }

    fun disableBackButton() {
        requireActivity().disableBackButton()
    }

    /**
     * @author DanielGC
     * @see Oculta el progressbar
     */
    fun hideProgressBarCustom() {
        cuBackHandler.hideProgressBarCustom()
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

    /**
     * Acción que se ejecuta cuando se detecta que se preciona el boton de back de android,
     * esta funcion es llamada desde base activity
     */
    fun onFragmentBackPressed(): Boolean {
        listenerBackPress?.onBackPress()
        return false
    }

    /**
     * Interface que se implementa en cada fragmento que requiera aplicar una
     * funcionalidad cuando se detecte que se preciono el boton de back
     */
    interface CUBackFragment {
        fun onBackPress()
    }

}