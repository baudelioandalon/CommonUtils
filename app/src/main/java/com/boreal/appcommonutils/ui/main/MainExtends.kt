package com.boreal.appcommonutils.ui.main

import android.util.Log
import com.boreal.commonutils.extensions.getData
import com.boreal.commonutils.extensions.saveData
import com.boreal.commonutils.extensions.setOnSingleClickListener
import com.boreal.commonutils.extensions.showToast

fun MainActivity.initElements(){

    binding.apply {
        btnOpen.setOnClickListener {
//            txtElement.saveData("NUMBER")
//            "CATORCE".saveData("NUMBER")
        }

        btnGet.setOnClickListener {
            109.saveData("NUMBER")
            val data = 0.getData("NUMBER")
            Log.e("string", data.toString())
        }


        btnInts.setOnSingleClickListener {
            showToast(txtInputElement.getIntegers().toString())
        }
        btnIntegerWithFormat.setOnSingleClickListener {
            showToast(txtInputElement.getIntegerString())
        }
        btnString.setOnSingleClickListener {
            showToast(txtInputElement.getAmountWithFormat())
        }
        btnFloat.setOnSingleClickListener {
            showToast(txtInputElement.getAmount().toString())
        }
    }
}