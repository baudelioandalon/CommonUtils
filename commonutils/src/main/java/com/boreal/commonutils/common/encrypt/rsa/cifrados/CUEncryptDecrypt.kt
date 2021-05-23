package com.boreal.commonutils.common.encrypt.rsa.cifrados

import android.util.Log
import org.apache.commons.codec.binary.Base64
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

open class CUEncryptDecrypt {

    companion object : CUEncryptDecrypt() {

        val ALGORITHM = "RSA/ECB/PKCS1Padding"
        private val UTF8 = StandardCharsets.UTF_8.name()

        fun createKeyPrivate(encodePrivateKey: String?): PrivateKey? {
            val keyPriv = Base64.decodeBase64(encodePrivateKey)
            val spec = PKCS8EncodedKeySpec(keyPriv)
            var privKy: PrivateKey? = null
            try {
                val kf = KeyFactory.getInstance("RSA")
                privKy = kf.generatePrivate(spec)
            } catch (e: InvalidKeySpecException) {
            } catch (e: NoSuchAlgorithmException) {
            }
            return privKy
        }

        fun createKeyPublic(encodePublicKey: String?): PublicKey? {
            val keyPub = Base64.decodeBase64(encodePublicKey)
            var pubKey: PublicKey? = null
            try {
                val spec = X509EncodedKeySpec(keyPub)
                val kf: KeyFactory
                kf = KeyFactory.getInstance("RSA")
                pubKey = kf.generatePublic(spec)
            } catch (e: NoSuchAlgorithmException) {
            } catch (e: InvalidKeySpecException) {
            }
            return pubKey
        }

        @Throws(Exception::class)
        fun decryptMessage(encryptedText: String?, privKy: PrivateKey?): String {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, privKy)
            Log.e("DECRYPTED METHOD", encryptedText!!)

            return try {
                String(cipher.doFinal(Base64.decodeBase64(encryptedText)), charset(UTF8))
            } catch (e: Exception) {
                Log.e("DECRYPTED METHOD ERROR", "e.message: ${e.message}")
                encryptedText
            }
        }

        fun encryptMessage(plainText: String, pubKy: PublicKey?): String {
            val cipher: Cipher
            var encriptado = ""
            try {
                cipher = Cipher.getInstance(ALGORITHM)
                cipher.init(Cipher.ENCRYPT_MODE, pubKy)

                Log.e("ENCRYPTED METHOD", plainText)
                encriptado =
                    Base64.encodeBase64String(cipher.doFinal(plainText.toByteArray(charset(UTF8))))
            } catch (e: NoSuchAlgorithmException) {
            } catch (e: NoSuchPaddingException) {
            } catch (e: InvalidKeyException) {
            } catch (e: IllegalBlockSizeException) {
            } catch (e: BadPaddingException) {
            } catch (e: UnsupportedEncodingException) {
            }
            return encriptado
        }
    }

}