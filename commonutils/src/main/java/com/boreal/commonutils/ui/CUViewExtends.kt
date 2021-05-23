package com.boreal.commonutils.ui

import android.util.Log
import android.view.View
import com.boreal.commonutils.application.CUAppInit
import com.boreal.commonutils.common.CUTypeObjectEncrypted
import com.boreal.commonutils.common.encrypt.CUKeysSecurity
import com.boreal.commonutils.common.encrypt.rsa.cifrados.CUEncryptDecrypt

fun View.hideView() {
    this.visibility = View.GONE
}


fun View.showView() {
    this.visibility = View.VISIBLE
}

fun String.encrypt(): String {
    val encryptDecrypt = CUEncryptDecrypt
    val publicKey = CUAppInit.getCUSecurity().getData(
        CUKeysSecurity.PUBLIC_KEY_RSA.name,
        CUTypeObjectEncrypted.STRING_OBJECT
    ) as String
    val encrypted = encryptDecrypt.encryptMessage(
        this,
        encryptDecrypt.createKeyPublic(publicKey)
    )
    Log.e("ENCRYPT KEY", publicKey)
    Log.e("ENCRYPTED", encrypted)

    return encrypted
}

fun String.decrypt(): String {
    val encryptDecrypt = CUEncryptDecrypt
    val privateKey = CUAppInit.getCUSecurity().getData(
        CUKeysSecurity.PRIVATE_KEY_RSA.name,
        CUTypeObjectEncrypted.STRING_OBJECT
    ) as String

    val decrypt = encryptDecrypt.decryptMessage(
        this,
        encryptDecrypt.createKeyPrivate(privateKey)
    )
    Log.e("DECRYPT KEY", privateKey)
    Log.e("DECRYPTED", decrypt)
    return decrypt
}