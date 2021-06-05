package com.boreal.commonutils.application.local.shared

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject

class CUSecurity @Inject constructor(private val context: Context) {

    private var masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private var sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        ENCRIPTED_PREFERENCES,
        masterKey, // masterKey created above
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    private var sharedPreferencesEditor: SharedPreferences.Editor = sharedPreferences.edit()

    init {

        sharedPreferencesEditor.apply()
    }

    fun remove(key: String) {
        sharedPreferencesEditor.remove(key)
    }

    fun saveData(key: String, value: Any): Boolean {
        return when (value) {
            is TextInputEditText -> {
                sharedPreferencesEditor.putString(key, value.text.toString().trim().trimIndent())
                    .apply()
                true
            }
            is String -> {
                sharedPreferencesEditor.putString(key, value).apply()
                true
            }
            is Int -> {
                sharedPreferencesEditor.putInt(key, value).apply()
                true
            }
            is Boolean -> {
                sharedPreferencesEditor.putBoolean(key, value).apply()
                true
            }
            is Float -> {
                sharedPreferencesEditor.putFloat(key, value).apply()
                true
            }
            is Long -> {
                sharedPreferencesEditor.putLong(key, value).apply()
                true
            }
            else -> false
        }
    }

    fun getData(key: String, type: Any):  Any{
        return when (type) {
            is String -> {
                sharedPreferences.getString(key, "") as String
            }
            is Int -> {
                sharedPreferences.getInt(key, 0)
            }
            is Boolean -> {
                sharedPreferences.getBoolean(key, false)
            }
            is Float -> {
                sharedPreferences.getFloat(key, 0f)
            }
            is Long -> {
                sharedPreferences.getLong(key, 0)
            }
            else -> {

            }
        }
    }

    fun getDeviceId(): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    companion object {
        const val ENCRIPTED_PREFERENCES = "encrypted_preferences"
    }

}