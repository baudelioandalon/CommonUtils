package com.boreal.commonutils.application.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_user")
data class CUUserModel(
    val usuario: String,
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)

