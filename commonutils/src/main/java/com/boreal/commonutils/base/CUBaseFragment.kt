package com.boreal.commonutils.base

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.boreal.commonutils.R
import com.boreal.commonutils.extensions.disableBackButton
import com.boreal.commonutils.utils.*

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
        if (activity is CUBackHandler) {
            cuBackHandler = activity as CUBackHandler
        }
        initObservers()
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

    private var onRequestFailure: () -> Unit = {}
    private var onRequestSuccess: () -> Unit = {}

    fun getPermissionsStorage(onFailure: () -> Unit = {}, onSuccess: () -> Unit) {
        ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            permissionStorage
        )
        onRequestFailure = onFailure
        onRequestSuccess = onSuccess
        requestAllPermissions(REQUEST_STORAGE)
    }

    fun getPermissionsCamera(onFailure: () -> Unit = {}, onSuccess: () -> Unit) {
        ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            permissionCamera
        )
        onRequestFailure = onFailure
        onRequestSuccess = onSuccess
        requestAllPermissions(REQUEST_IMAGE_CAPTURE)
    }

    fun getPermissionsReadStorage(onFailure: () -> Unit = {}, onSuccess: () -> Unit) {
        ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            permissionReadStorage
        )
        onRequestFailure = onFailure
        onRequestSuccess = onSuccess
        requestAllPermissions(REQUEST_GALLERY_CAPTURE)
    }

    //TODO Buscar el metodo no deprecado
    private fun requestAllPermissions(requestCode: Int) {
        requestPermissions(
            arrayOf(permissionCamera, permissionReadStorage, permissionStorage),
            requestCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_STORAGE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    onRequestSuccess()
                } else {
                    onRequestFailure()
                    Toast.makeText(
                        requireContext(),
                        "No cuenta con permisos de almacenamiento",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            REQUEST_IMAGE_CAPTURE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    onRequestSuccess()
                } else {
                    onRequestFailure()
                    Toast.makeText(
                        requireContext(),
                        "No cuenta con permisos de cámara",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            REQUEST_GALLERY_CAPTURE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    onRequestSuccess()
                } else {
                    onRequestFailure()
                    Toast.makeText(
                        requireContext(),
                        "No cuenta con permisos de lectura",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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

    fun showKeyboard(view: View) {
        cuBackHandler.showKeyBoard(view)
    }

    fun configToolbarDefault(color: Int = Color.WHITE) {
        if (activity != null) {
            requireActivity().window.statusBarColor = color
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
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