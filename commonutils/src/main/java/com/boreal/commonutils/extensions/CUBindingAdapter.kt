package com.boreal.commonutils.extensions

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.boreal.commonutils.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

@SuppressLint("CheckResult")
@BindingAdapter("loadImage")
fun bindingImage(image: ImageView, imageUri: String?) {
    if (imageUri == null || imageUri == "NONE" || imageUri.isEmpty()) {
        image.changeDrawable(R.drawable.ic_warning_square)
        return
    }

    Glide.with(image.context).load(imageUri).placeholder(R.drawable.ic_wait)
        .addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                image.changeDrawable(R.drawable.ic_warning_square)
                return true
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                if (resource == null) {
                    image.changeDrawable(R.drawable.ic_warning_square)
                    return false
                }
                image.setImageDrawable(resource)
                return true
            }
        }).into(image)
}

@SuppressLint("CheckResult")
@BindingAdapter("loadUserImage")
fun bindingUserImage(image: ImageView, imageUri: String?) {
    if (imageUri == null || imageUri == "NONE" || imageUri.isEmpty()) {
        image.changeDrawable(R.drawable.ic_user_register_blue)
        return
    }

    Glide.with(image.context).load(imageUri).placeholder(R.drawable.ic_wait)
        .addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                image.changeDrawable(R.drawable.ic_warning_square)
                return true
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                if (resource == null) {
                    image.changeDrawable(R.drawable.ic_user_register_blue)
                    return false
                }
                image.setImageDrawable(resource)
                return true
            }
        }).into(image)
}