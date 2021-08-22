package com.boreal.commonutils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.boreal.commonutils.component.disableBackButton
import kotlin.reflect.KClass

abstract class CUBaseFragment<T : ViewDataBinding, V : ViewModel>(private val vkClass: KClass<V>) : Fragment() {

    lateinit var CUBackHandler: CUBackHandler
    private var listenerBackPress: BACUBackFragment? = null

    lateinit var mBinding: T

    val viewModel by lazy {
        ViewModelProvider(this).get(vkClass.javaObjectType)
    }

    /**
     * @author Baudelio Andalon
     * @since 21/09/2020
     * @see Obtener el layout
     */
    abstract fun getLayout(): Int
    abstract fun initDependency(savedInstanceState: Bundle?)
    abstract fun initObservers()
    abstract fun initView()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mBinding = DataBindingUtil.inflate(inflater,getLayout(), container, false)

        initDependency(savedInstanceState)
        initObservers()
        if (activity is CUBackHandler) {
            CUBackHandler = activity as CUBackHandler
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
    fun showProgressBarCustom(message: String? = null, isCancelable: Boolean = false){
        CUBackHandler.showProgressBarCustom(message, isCancelable)
    }

    fun disableBackButton(){
        requireActivity().disableBackButton()
    }

    /**
     * @author DanielGC
     * @see Oculta el progressbar
     */
    fun hideProgressBarCustom(){
        CUBackHandler.hideProgressBarCustom()
    }

    fun setBACUBackFragment(listenerBackPress: BACUBackFragment){
        this.listenerBackPress = listenerBackPress
    }

    /**
     * @author DanielGC
     */
    fun hideKeyboard() {
        CUBackHandler.hideKeyBoard()
    }

    fun showLottie(lottie: LottieAnimationView,
                   containerPersonality: View,
                   containerParent: View, show: Boolean){
        CUBackHandler.showLottie(lottie,
            containerPersonality,
            containerParent, show)
    }

    /**
     * Acción que se ejecuta cuando se detecta que se preciona el boton de back de android,
     * esta funcion es llamda desde base activity
     */
    fun onFragmentBackPressed(): Boolean{
        if(listenerBackPress != null)
            listenerBackPress?.onBackPress()

        return false
    }

    /**
     * Interface que se implementa en cada fragmento que requiera aplicar una
     * funcionalidad cuando se detecte que se preciono el boton de back
     */
    interface BACUBackFragment {
        fun onBackPress()
    }

}