package com.boreal.commonutils.dagger.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.boreal.commonutils.application.local.room.CURoomInstanceDatabase
import com.boreal.commonutils.application.local.shared.CUSecurity
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
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

    @Provides
    @Singleton
    fun provideRoomInstance() = Room.databaseBuilder(
        applicationContext,
        CURoomInstanceDatabase::class.java,
        "room-database"
    ).build()

    @Provides
    @Singleton
    fun provideRealmInstance(): Realm {
        Realm.init(applicationContext)
        val config = RealmConfiguration.Builder()
            .name("realm-database.db")
            .deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .schemaVersion(0)
            .build()
        Realm.setDefaultConfiguration(config)

        return Realm.getDefaultInstance()
    }


}