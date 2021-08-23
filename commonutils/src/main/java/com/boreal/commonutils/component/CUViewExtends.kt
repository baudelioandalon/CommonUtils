package com.boreal.commonutils.component

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.InputFilter
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.boreal.commonutils.application.CUAppInit
import com.boreal.commonutils.application.local.room.CUUserModel
import com.boreal.commonutils.common.encrypt.CUKeysSecurity
import com.boreal.commonutils.common.encrypt.rsa.cifrados.CUEncryptDecrypt
import io.realm.RealmObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.Normalizer
import java.text.NumberFormat
import java.util.*

fun <T : View> T.hideView() {
    this.visibility = View.GONE
}

fun <T : View> T.showView() {
    this.visibility = View.VISIBLE
}

fun View.animateFadeIn(duration: Long = 350) {
    this.alpha = 0.0f
    this.animate()
        .setDuration(duration)
        .alpha(1.0f)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                this@animateFadeIn.animate().setListener(null)
            }
        })
}

fun <T> doSync(method: () -> T, methodAfter: ((param: T) -> Unit)? = null) {
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


fun View.setOnSingleClickListener(doOnClick: ((View) -> Unit)) =
    setOnClickListener(OnSingleClickListener(doOnClick))

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

/**
 * @author Baudelio Andalon
 * @sample imgItem.changeDrawable(R.drawable.newImg)
 */
fun ImageView.changeDrawable(drawable: Int) {
    this.setImageDrawable(
        ContextCompat.getDrawable(
            CUAppInit.getAppContext(),
            drawable
        )
    )
}


/**
 * @author Baudelio Andalon
 * @sample imgItem.changeImgColor(R.color.redColor)
 */
fun ImageView.changeImgColor(color: Int) {
    this.drawable?.let {
        DrawableCompat.setTint(
            DrawableCompat.wrap(it),
            ContextCompat.getColor(CUAppInit.getAppContext(), color)
        )
    }
}

/**
 * @author Baudelio Andalon
 * @sample edtName.maxLength(20)
 * Definir el tamaño maximo de un editText
 */
fun EditText.maxLength(maxLength: Int) {
    filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
}


/**
 * @author Baudelio Andalon
 * @sample edit.changeHintTextColor(R.color.redColor)
 * Definir el color del hint de un EditText
 */
fun EditText.changeHintTextColor(color: Int, context: Context = CUAppInit.getAppContext()) {
    setHintTextColor(ContextCompat.getColor(context, color))
}

/**
 * @author Baudelio Andalon
 * @sample tvName.changeTextColor(R.color.redColor)
 * Cambiar el color de un textView
 */
fun TextView.changeTextColor(color: Int, context: Context = CUAppInit.getAppContext()) {
    setTextColor(
        ContextCompat.getColor(
            context,
            color
        )
    )
}

fun TextView.changeTypeFace(fontFamily: Int) {
    typeface = ResourcesCompat.getFont(
        CUAppInit.getAppContext(),
        fontFamily
    )
}

fun Button.changeTypeFace(fontFamily: Int) {
    typeface = ResourcesCompat.getFont(
        CUAppInit.getAppContext(),
        fontFamily
    )
}

/**
 *
 * */
fun TextView.changeFontSizeColorText(
    fontFamily: Int? = null,
    sizeInDP: Int? = null,
    color: Int? = null, textString: String? = null
) {
    fontFamily?.let {
        changeTypeFace(it)
    }
    sizeInDP?.let {
        changeTextSize(it)
    }
    color?.let {
        changeTextColor(it)
    }
    textString?.let {
        text = it
    }
}

fun TextView.changeTextSize(sizeInDP: Int) {
    this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeInDP.toFloat())
}

fun Button.changeTextSize(sizeInDP: Int) {
    this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeInDP.toFloat())
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
            when (this@insertData) {
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

//Abrir otra actividad mandandole parametros o no
inline fun <reified T : Activity> Activity.goToActivity(noinline init: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.init() //pasar parámetros
    startActivity(intent)
}

//Abrir otra actividad mandandole parametros o no
inline fun <reified T : Activity> Fragment.goToActivity(noinline init: Intent.() -> Unit = {}) {
    val intent = Intent(requireContext(), T::class.java)
    intent.init() //pasar parámetros
    startActivity(intent)
}


fun getDeviceId(): String =
    Settings.Secure.getString(CUAppInit.getAppContext().contentResolver, Settings.Secure.ANDROID_ID)

fun RealmObject.autoGenerateId(primaryKey: String = "id"): Int {
    val currentIdNumber = CUAppInit.getRealmInstance().where(this::class.java).max(primaryKey)
    return if (currentIdNumber == null) {
        1
    } else {
        currentIdNumber.toInt() + 1
    }
}

/**
 * Ocultar teclado
 * */
fun Activity.hideKeyBoardFragment(vieww: View?) = run {
    if (vieww != null) {
        val input = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(vieww.windowToken, 0)
    }
}

fun randomID() = UUID.randomUUID().toString().replace('-', ' ')
    .replace("\\s".toRegex(), "")