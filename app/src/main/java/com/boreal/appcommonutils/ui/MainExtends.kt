package com.boreal.appcommonutils.ui

import com.boreal.commonutils.ui.saveData

fun MainActivity.initElements(){

    mBinding.apply {
        btnOpen.setOnClickListener {
            txtElement.saveData("NUMBER")
            "CATORCE".saveData("NUMBER")
        }

        btnGet.setOnClickListener {

            109L.saveData("NUMBER")
        }
    }
}