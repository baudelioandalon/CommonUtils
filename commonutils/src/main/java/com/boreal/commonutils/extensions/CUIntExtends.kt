package com.boreal.commonutils.extensions

import android.content.Context

fun Int.resDrawableArray(
    context: Context,
    block: (position: Int, drawableResId: Int) -> Unit
) {
    val array = context.resources.obtainTypedArray(this)
    repeat(array.length()) {
        block(it, array.getResourceId(it, -1))
    }
    array.recycle()
}