package com.boreal.appcommonutils.ui.test.ui.testmvp.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.boreal.appcommonutils.R
import com.boreal.appcommonutils.databinding.ActivityTestBinding
import com.boreal.appcommonutils.ui.test.viewmodel.TestViewModel
import com.boreal.commonutils.base.CUBaseActivity

class TestActivity : AppCompatActivity() {
    lateinit var bind: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityTestBinding.inflate(layoutInflater)
        setContentView(bind.root)


    }
}