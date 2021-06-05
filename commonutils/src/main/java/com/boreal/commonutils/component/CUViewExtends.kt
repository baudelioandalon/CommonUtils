package com.boreal.commonutils.ui

import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.FragmentActivity
import com.boreal.commonutils.application.local.room.CUUserModel
import com.boreal.commonutils.application.CUAppInit
import com.boreal.commonutils.common.encrypt.CUKeysSecurity
import com.boreal.commonutils.common.encrypt.rsa.cifrados.CUEncryptDecrypt
import io.realm.RealmObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.Normalizer

fun <T : View> T.hideView() {
    this.visibility = View.GONE
}

fun <T : View> T.showView() {
    this.visibility = View.VISIBLE
}

fun <T> doSync(method: () -> T, methodAfter: ((param: T) -> Unit)? =  null) {
    var result: T?
    GlobalScope.launch(Dispatchers.IO) {
        result = withContext(Dispatchers.IO) {
            method()
        }
        withContext(Dispatchers.IO) {
            methodAfter?.invoke(result as T)
        }

    }
}

fun <T> T.encrypt(): String {
    val encrypted: String
    CUEncryptDecrypt.apply {
        encrypted = encryptMessage(
            this@encrypt.toString(),
            createKeyPublic("".getData(CUKeysSecurity.PUBLIC_KEY_RSA.name))
        )
    }
    return encrypted
}

fun <T> T.decrypt(): String {
    val decrypt: String
    CUEncryptDecrypt.apply {
        decrypt = decryptMessage(
            this@decrypt.toString(),
            createKeyPrivate("".getData(CUKeysSecurity.PUBLIC_KEY_RSA.name))
        )
    }
    return decrypt
}

fun String.maskCardNumber() = if (this.length == 16) {
    this.substring(0, 4) + "********" + this.substring(12)
} else {
    this
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

fun <T> T.getData(keyValue: String) = CUAppInit.getCUSecurity().getData(keyValue, this as Any) as T

fun <T> T.insertData() {
    GlobalScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.IO) {
            when(this@insertData){
                is CUUserModel -> {
                    CUAppInit.getRoomInstance().userDao().insert(this@insertData)
                }
            }
        }
    }
}

fun RealmObject.saveLocalData() {
    doSync({
        try {
            CUAppInit.getRealmInstance().apply {
                beginTransaction()
                copyToRealmOrUpdate(this@saveLocalData)
                commitTransaction()
            }
            true
        } catch (e: Exception) {
            false
        }
    })
}

fun RealmObject.autoGenerateId(primaryKey: String = "id"): Int {
    val currentIdNumber = CUAppInit.getRealmInstance().where(this::class.java).max(primaryKey)
    return if (currentIdNumber == null) {
        1
    } else {
        currentIdNumber.toInt() + 1
    }
}