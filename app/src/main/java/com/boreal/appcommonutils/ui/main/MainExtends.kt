package com.boreal.appcommonutils.ui.main

import android.util.Log
import com.boreal.commonutils.component.getData
import com.boreal.commonutils.component.saveData

fun MainActivity.initElements(){

    mBinding.apply {
        btnOpen.setOnClickListener {
//            txtElement.saveData("NUMBER")
//            "CATORCE".saveData("NUMBER")
        }

        btnGet.setOnClickListener {
            109.saveData("NUMBER")
            val data = 0.getData("NUMBER")

            Log.e("string", data.toString())
        }
    }
}