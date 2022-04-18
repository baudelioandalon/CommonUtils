package com.boreal.commonutils.extensions

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.stfalcon.imageviewer.StfalconImageViewer

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}

fun Fragment.showImageViewer(listImages: List<String>) {
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

inline fun <reified T : Activity> Fragment.goToActivity(noinline init: Intent.() -> Unit = {}) {
    val intent = Intent(requireContext(), T::class.java)
    intent.init() //pasar parámetros
    startActivity(intent)
}

fun Fragment.openFacebookActivity(pageId: String) {
    lateinit var uri: Uri
    try {
        val applicationInfo =
            requireActivity().packageManager.getApplicationInfo("com.facebook.katana", 0)
        if (applicationInfo.enabled) {
            uri = Uri.parse("fb://page/$pageId")
        }
    } catch (ignored: PackageManager.NameNotFoundException) {
        uri = Uri.parse(pageId)
    }
    startActivity(Intent(Intent.ACTION_VIEW, uri))
}