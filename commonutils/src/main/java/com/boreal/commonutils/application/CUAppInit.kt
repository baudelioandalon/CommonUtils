package com.boreal.commonutils.application

import android.app.Application
import android.content.Context
import com.boreal.commonutils.dagger.components.CUApplicationComponent
import com.boreal.commonutils.dagger.components.DaggerCUApplicationComponent
import com.boreal.commonutils.dagger.modules.CUApplicationModule
import java.io.File

open class CUAppInit {

    fun init(application: Application, applicationContext: Context) {
//        deleteCache(applicationContext)
//        applicationContext.cacheDir.deleteRecursively()
        appGlobal = DaggerCUApplicationComponent
        .builder()
        .cUApplicationModule(CUApplicationModule(application, applicationContext))
        .build()
    }

    open fun deleteCache(context: Context) {
        try {
            val dir: File = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children: Array<String> = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile()) {
            dir.delete()
        } else {
            false
        }
    }


    /**
     *@see -> Se declaran constantes para acceder al valor sin crear una instancia de la clase
     */
    companion object: CUAppInit(){

        lateinit var appGlobal : CUApplicationComponent

        fun getAppContext() = appGlobal.context()

        fun getCUSecurity() = appGlobal.security()

        fun getRoomInstance() = appGlobal.roomInstance()

        fun getRealmInstance() = appGlobal.realmInstance()

    }

}