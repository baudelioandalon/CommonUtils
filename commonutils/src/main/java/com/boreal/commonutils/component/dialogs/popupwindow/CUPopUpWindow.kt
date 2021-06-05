package com.boreal.commonutils.component.dialogs.popupwindow

import android.content.Context
import android.graphics.Rect
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.LayoutRes
import com.boreal.commonutils.R
import com.boreal.commonutils.application.CUAppInit

class CUPopUpWindow(
    @LayoutRes resource: Int,
    view: View,
    callbackCU: ((view: View) -> Unit)? = null,
    clickEveryWhereToDismiss: Boolean = true,
    animation: Int = R.style.AnimationPopUpWindowScaleInOut,
    gravity: Int = Gravity.NO_GRAVITY
) {
    init {
        // inflate the layout of the popup window
        val popupView: View = (CUAppInit.getAppContext()
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(resource, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.animationStyle = animation

        // which view you pass in doesn't matter, it is only used for the window tolken
        val rectf = Rect()
        view.getLocalVisibleRect(rectf)
        view.getGlobalVisibleRect(rectf)
        popupWindow.showAtLocation(view, gravity, rectf.right, rectf.bottom + 5)

        callbackCU?.invoke(popupView)

        // dismiss the popup window when touched
        if (clickEveryWhereToDismiss) {
            popupView.setOnTouchListener { _, _ ->
                popupWindow.dismiss()
                true
            }
        }

    }

}