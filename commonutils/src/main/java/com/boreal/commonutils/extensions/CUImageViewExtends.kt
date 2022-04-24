package com.boreal.commonutils.extensions

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.boreal.commonutils.application.CUAppInit
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * @author Baudelio Andalon
 * @sample imgItem.changeDrawable(R.drawable.newImg)
 */
fun ImageView.changeDrawable(drawable: Int) {
    setImageDrawable(
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

fun Bitmap.getImageUri(levelQuality: Int = 100): Uri {
    val bytes = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, levelQuality, bytes)
    val path = MediaStore.Images.Media.insertImage(
        CUAppInit.getAppContext().contentResolver,
        this, UUID.randomUUID().toString() + ".png", "drawing"
    )
    return Uri.parse(path)
}

fun Uri.getImageBitmap(): Bitmap =
    MediaStore.Images.Media.getBitmap(CUAppInit.getAppContext().contentResolver, this)