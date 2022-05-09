package com.boreal.commonutils.globalmethod

import android.provider.Settings
import android.util.Log
import com.boreal.commonutils.application.CUAppInit
import java.security.SecureRandom
import java.util.*

fun getDeviceId(): String =
    Settings.Secure.getString(CUAppInit.getAppContext().contentResolver, Settings.Secure.ANDROID_ID)

fun randomID() = UUID.randomUUID().toString().replace('-', ' ')
    .replace("\\s".toRegex(), "")

fun randomNumberId() = (10000..99999).random()

fun randomANID(sizeId: Int = 20) = List(sizeId) {
    (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
}.joinToString("").substring(0, sizeId)

fun firestoreId(sizeId: Int = 20): String {
    val builder = StringBuilder()
    val dictionary = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    val rand = SecureRandom()
    for (i in 0 until sizeId) {
        builder.append(dictionary.toCharArray(rand.nextInt(dictionary.length)))
    }
    Log.d("FIRESTORE_ID", builder.toString())
    return builder.toString()
}