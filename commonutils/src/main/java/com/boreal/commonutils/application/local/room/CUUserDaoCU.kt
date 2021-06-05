package com.boreal.commonutils.application.local.room

import androidx.room.*

@Dao
interface CUUserDaoCU {

    @Query("SELECT * FROM data_user")
    fun getUsers(): List<CUUserModel>

    @Query("DELETE FROM data_user")
    fun deleteAllUsers()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: CUUserModel)

    @Update
    fun update(model: CUUserModel)

    @Delete
    fun delete(model: CUUserModel)


}