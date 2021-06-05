package com.boreal.commonutils.application.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CUUserModel::class], version = 1, exportSchema = false)
abstract class CURoomInstanceDatabase: RoomDatabase() {
    abstract fun userDao(): CUUserDaoCU
}