package com.boreal.commonutils.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

fun RecyclerView.itemPercent(
    percentWidth: Double = 0.0,
    percentHeight: Double = 0.0,
    changeOrientation: Int = RecyclerView.HORIZONTAL,
    reverseLayout: Boolean = false
) = with(layoutManager) {
    layoutManager = object : LinearLayoutManager(
        context, if (changeOrientation != HORIZONTAL) {
            changeOrientation
        } else {
            HORIZONTAL
        }, reverseLayout
    ) {
        override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
            if (percentWidth != 0.0) {
                lp.width = (width * percentWidth).roundToInt()
            }
            if (percentHeight != 0.0) {
                lp.height = (height * percentHeight).roundToInt()
            }
            return true
        }
    }
    layoutManager
}