package com.boreal.commonutils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.boreal.commonutils.R
import com.boreal.commonutils.extensions.showToast

class DialogFragmentType: DialogFragment() {

    private lateinit var vBind: ZpErrorServerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialog)
       // setStyle(STYLE_NORMAL,android.R.style.Theme_DeviceDefault_Light_DialogWhenLarge_NoActionBar);
    }

   override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ZpErrorServerBinding.inflate(inflater, container, false).apply {
        vBind = this

        btnTryAgain.setOnClickListener {

            showToast("Reintentandoo")

        }
    }.root

}