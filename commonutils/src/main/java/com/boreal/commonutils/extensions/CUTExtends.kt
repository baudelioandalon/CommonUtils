package com.boreal.commonutils.extensions

import com.boreal.commonutils.application.CUAppInit
import com.boreal.commonutils.common.encrypt.CUKeysSecurity
import com.boreal.commonutils.common.encrypt.rsa.cifrados.CUEncryptDecrypt
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T> doSync(
    method: () -> T,
    methodAfter: ((param: T) -> Unit)? = null,
    thread: CoroutineContext = Dispatchers.IO
) {
    var result: T?
    GlobalScope.launch(thread) {
        result = withContext(thread) {
            method()
        }
        withContext(thread) {
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

fun <T : Any> T.saveData(keyValue: String) = CUAppInit.getCUSecurity().saveData(keyValue, this)

fun <T> T.getData(keyValue: String) = CUAppInit.getCUSecurity().getData(keyValue, this as Any) as T