package com.boreal.appcommonutils.ui

import com.boreal.appcommonutils.R
import com.boreal.appcommonutils.databinding.ActivityMainBinding
import com.boreal.commonutils.application.CUAppInit
import com.boreal.commonutils.base.CUBaseActivity

class MainActivity : CUBaseActivity<ActivityMainBinding, MainViewModel>(MainViewModel::class) {


    override fun getLayout() = R.layout.activity_main

    override fun initDependency() {

    }

    override fun initObservers() {
    }

    override fun initView() {
        CUAppInit().init(application, applicationContext)
        initElements()
    }

}