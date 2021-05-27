package com.boreal.commonutils.ui

import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.FragmentActivity
import com.boreal.commonutils.application.CUAppInit
import com.boreal.commonutils.common.CUTypeObjectEncrypted
import com.boreal.commonutils.common.encrypt.CUKeysSecurity
import com.boreal.commonutils.common.encrypt.rsa.cifrados.CUEncryptDecrypt
import java.text.Normalizer
import kotlin.reflect.KClass

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

fun FragmentActivity.disableBackButton() {
    onBackPressedDispatcher.addCallback(this) {}
}

private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()

fun CharSequence.removeTilde(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.replace(temp, "")
}

fun <T : Any> T.saveData(keyValue: String) = CUAppInit.getCUSecurity().saveData(keyValue, this)

fun <T> T.getData(keyValue: String) = CUAppInit.getCUSecurity().getData(keyValue,this as Any) as T