package com.boreal.commonutils.application

import android.app.Application
import android.content.Context
import com.boreal.commonutils.dagger.components.CUApplicationComponent
import com.boreal.commonutils.dagger.components.DaggerCUApplicationComponent
import com.boreal.commonutils.dagger.modules.CUApplicationModule

open class CUAppInit : Application() {

    fun init(application: Application, applicationContext: Context) {
        appGlobal = DaggerCUApplicationComponent
            .builder()
            .cUApplicationModule(CUApplicationModule(application, applicationContext))
            .build()
    }

    /**
     *@see -> Se declaran constantes para acceder al valor sin crear una instancia de la clase
     */
    companion object : CUAppInit() {

        lateinit var appGlobal: CUApplicationComponent

        fun getAppContext() = appGlobal.context()
        fun getCUSecurity() = appGlobal.security()
        fun getRoomInstance() = appGlobal.roomInstance()
//        fun getRealmInstance() = appGlobal.realmInstance()
    }

}