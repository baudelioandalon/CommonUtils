package com.boreal.commonutils.dagger.components

import android.app.Application
import android.content.Context
import com.boreal.commonutils.application.CUSecurity
import com.boreal.commonutils.dagger.modules.CUApplicationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CUApplicationModule::class])
interface CUApplicationComponent {

    fun application() : Application
    fun context() : Context
    fun security() : CUSecurity

}