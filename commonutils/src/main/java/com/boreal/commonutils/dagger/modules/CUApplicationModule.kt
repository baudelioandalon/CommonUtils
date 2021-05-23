package com.boreal.commonutils.dagger.modules

import android.app.Application
import android.content.Context
import com.boreal.commonutils.application.CUSecurity
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class CUApplicationModule @Inject constructor(private val application : Application,
                                              private val applicationContext: Context){

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideContext(): Context = applicationContext

    @Provides
    @Singleton
    fun provideBACUSecurity(): CUSecurity = CUSecurity(applicationContext)

}