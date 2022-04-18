package com.boreal.commonutils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.stfalcon.imageviewer.StfalconImageViewer
import java.util.ArrayList


fun Activity.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}
fun Activity.showImageViewer(listImages: List<String>) {
    listImages.filter { it != "NONE" && it != "" }.apply {
        if (isEmpty()) return@apply showToast("No hay imagen para mostrar")
        StfalconImageViewer.Builder(
            this@showImageViewer,
            this
        ) { view, image ->
            Picasso.get().load(image).into(view)
        }.show()
    }
}
//Abrir otra actividad mandandole parametros o no
inline fun <reified T : Activity> Activity.goToActivity(noinline init: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.init() //pasar parámetros
    startActivity(intent)
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