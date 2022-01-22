package com.boreal.commonutils.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.boreal.commonutils.component.OnSingleClickListener

fun <T : View> T.hideView() {
    visibility = View.GONE
}

fun <T : View> T.showView() {
    visibility = View.VISIBLE
}

fun <T : View> T.showIf(sentence: Boolean, result: (Boolean) -> Unit = {}) {
    result(sentence)
    visibility = if (sentence) View.VISIBLE else View.GONE
}

fun <T : View> T.hideIf(sentence: Boolean, result: (Boolean) -> Unit = {}) {
    result(sentence)
    visibility = if (sentence) View.GONE else View.VISIBLE
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

fun View.setOnSingleClickListener(doOnClick: ((View) -> Unit)) =
    setOnClickListener(OnSingleClickListener(doOnClick))

