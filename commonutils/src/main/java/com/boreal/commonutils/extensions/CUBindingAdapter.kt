package com.boreal.commonutils.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("loadImage")
fun bindingImage(image: ImageView, imageUri: String?) {
    imageUri?.let {
        Picasso.get().load(it).into(image)
    }
}