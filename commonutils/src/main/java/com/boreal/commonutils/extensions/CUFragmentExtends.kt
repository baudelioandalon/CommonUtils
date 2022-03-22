package com.boreal.commonutils.extensions

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.stfalcon.imageviewer.StfalconImageViewer
import java.util.ArrayList


fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}


fun Fragment.showImageViewer(listImages: ArrayList<String>) {
    listImages.filter { it != "NONE" && it != "" }.apply {
        if (isEmpty()) return@apply showToast("No hay imagen para mostrar")
        StfalconImageViewer.Builder(
            context,
            this
        ) { view, image ->
            Picasso.get().load(image).into(view)
        }.show()
    }
}

fun Fragment.getSupportFragmentManager() = requireActivity().supportFragmentManager

//Abrir otra actividad mandandole parametros o no
inline fun <reified T : Activity> Fragment.goToActivity(noinline init: Intent.() -> Unit = {}) {
    val intent = Intent(requireContext(), T::class.java)
    intent.init() //pasar parámetros
    startActivity(intent)
}