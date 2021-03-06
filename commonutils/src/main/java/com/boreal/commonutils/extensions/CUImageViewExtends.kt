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