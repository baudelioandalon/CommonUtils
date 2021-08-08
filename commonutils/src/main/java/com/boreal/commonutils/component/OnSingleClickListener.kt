package com.boreal.commonutils.component

import android.view.View

class OnSingleClickListener(
    private val doOnClick: ((View) -> Unit)
) : View.OnClickListener {
    private val intervalMillis = 500L

    override fun onClick(v: View) {
        if (enabled) {
            enabled = false
            v.postDelayed(ENABLE_AGAIN, intervalMillis)
            doOnClick(v)
        }
    }

    companion object {
        var enabled = true
        private val ENABLE_AGAIN =
            Runnable { enabled = true }

    }

}