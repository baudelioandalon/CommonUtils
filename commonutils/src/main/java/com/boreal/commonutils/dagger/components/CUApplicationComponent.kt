package com.boreal.commonutils.dagger.components

import android.app.Application
import android.content.Context
import com.boreal.commonutils.application.local.room.CURoomInstanceDatabase
import com.boreal.commonutils.application.local.shared.CUSecurity
import com.boreal.commonutils.dagger.modules.CUApplicationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CUApplicationModule::class])
interface CUApplicationComponent {

    fun application(): Application
    fun context(): Context
    fun security(): CUSecurity
    fun roomInstance(): CURoomInstanceDatabase
//    fun realmInstance() : Realm

}