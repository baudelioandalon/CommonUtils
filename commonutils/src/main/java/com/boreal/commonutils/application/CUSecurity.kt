package com.boreal.commonutils.application

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.boreal.commonutils.common.CUTypeObjectEncrypted
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject

class CUSecurity @Inject constructor(private val context: Context){

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

    fun remove(key: String){
        sharedPreferencesEditor.remove(key)
    }

    fun saveData(key: String, value: Any): Boolean{
        return when(value){
            is TextInputEditText -> {
                sharedPreferencesEditor.putString(key,  value.text.toString().trim().trimIndent()).apply()
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

    fun getData(key: String, type: CUTypeObjectEncrypted): Any {
        return when(type){
            CUTypeObjectEncrypted.STRING_OBJECT -> {
                sharedPreferences.getString(key, "").toString()
            }
            CUTypeObjectEncrypted.INT_OBJECT -> {
                sharedPreferences.getInt(key, 0)
            }
            CUTypeObjectEncrypted.BOOLEAN_OBJECT -> {
                sharedPreferences.getBoolean(key, false)
            }
            CUTypeObjectEncrypted.FLOAT_OBJECT -> {
                sharedPreferences.getFloat(key, 0f)
            }
            CUTypeObjectEncrypted.LONG_OBJECT -> {
                sharedPreferences.getLong(key, 0)
            }
        }
    }

    fun getDeviceId(): String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    companion object {
        const val ENCRIPTED_PREFERENCES = "encrypted_preferences"
    }

}