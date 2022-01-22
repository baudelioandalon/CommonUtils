package com.boreal.commonutils.common.encrypt.rsa.cifrados

import android.util.Base64
import android.util.Log
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Arrays
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

open class CUEncryptDecrypt {

    companion object : CUEncryptDecrypt() {

        val ALGORITHM = "RSA/ECB/PKCS1Padding"
        private const val RSA_ALGORITHM = "RSA"

        fun createKeyPrivate(encodePrivateKey: String): PrivateKey? {
            val keyPriv: ByteArray = Base64.decode(encodePrivateKey.toByteArray(), 0)
            val spec = PKCS8EncodedKeySpec(keyPriv)
            var privKy: PrivateKey? = null
            try {
                val kf = KeyFactory.getInstance(RSA_ALGORITHM)
                privKy = kf.generatePrivate(spec)
            } catch (e: InvalidKeySpecException) {
            } catch (e: NoSuchAlgorithmException) {
            }
            return privKy
        }

        fun createKeyPublic(encodePublicKey: String): PublicKey? {
            val keyPub: ByteArray = Base64.decode(encodePublicKey.toByteArray(), 0)
            var pubKey: PublicKey? = null
            try {
                val spec = X509EncodedKeySpec(keyPub)
                val kf = KeyFactory.getInstance(RSA_ALGORITHM)
                pubKey = kf.generatePublic(spec)
            } catch (e: NoSuchAlgorithmException) {
            } catch (e: InvalidKeySpecException) {
            }
            return pubKey
        }

        @Throws(Exception::class)
        fun decryptMessage(encryptedText: String?, privateKey: PrivateKey?): String {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            Log.e("DECRYPTED METHOD", encryptedText!!)

            return try {
                val dataBytes = Base64.decode(encryptedText.toByteArray(), 0)
                val data = Arrays.copyOfRange(dataBytes, 0, dataBytes.size)
                val pwdcipher = Cipher.getInstance(ALGORITHM)
                pwdcipher.init(Cipher.DECRYPT_MODE, privateKey)
                String(pwdcipher.doFinal(data), StandardCharsets.UTF_8)
            } catch (e: Exception) {
                Log.e("DECRYPTED METHOD ERROR", "e.message: ${e.message}")
                encryptedText
            }
        }

        fun encryptMessage(plainText: String, pubKy: PublicKey?): String {
            try {
                val cipher: Cipher = Cipher.getInstance(ALGORITHM)
                cipher.init(Cipher.ENCRYPT_MODE, pubKy)
                val dataBytes = cipher.doFinal(plainText.toByteArray(charset("UTF-8")))
                return String(Base64.encode(dataBytes, Base64.NO_WRAP))
            } catch (e: NoSuchAlgorithmException) {
            } catch (e: NoSuchPaddingException) {
            } catch (e: InvalidKeyException) {
            } catch (e: IllegalBlockSizeException) {
            } catch (e: BadPaddingException) {
            } catch (e: UnsupportedEncodingException) {
            }
            return plainText
        }
    }

}