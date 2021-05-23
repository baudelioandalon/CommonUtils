package com.boreal.commonutils.common.encrypt.rsa.utilidades

import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.Throws
import android.util.Base64

object CUtilidades {

    @Throws(NullPointerException::class, UnsupportedEncodingException::class)
    fun convierteEnBytesABase64(claveEnBytes: ByteArray?): String {
        return URLEncoder.encode(String(Base64.encode(claveEnBytes, Base64.NO_WRAP)), "UTF-8")
    }

    @JvmStatic
    @Throws(UnsupportedEncodingException::class)
    fun convierteStringABytes(clave: String?): ByteArray {
        return Base64.decode(URLDecoder.decode(clave, "UTF-8"),Base64.NO_WRAP)
    }

}