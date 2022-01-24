package com.boreal.commonutils.globalmethod

import android.provider.Settings
import com.boreal.commonutils.application.CUAppInit
import java.util.UUID

fun getDeviceId(): String =
    Settings.Secure.getString(CUAppInit.getAppContext().contentResolver, Settings.Secure.ANDROID_ID)

fun randomID() = UUID.randomUUID().toString().replace('-', ' ')
    .replace("\\s".toRegex(), "")

fun randomNumberId() = (10000..99999).random()

private fun randomANID(sizeId: Int = 20) = List(sizeId) {
    (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
}.joinToString("")