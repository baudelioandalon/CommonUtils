package com.boreal.commonutils.component.dialogs.blurdialog

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.boreal.commonutils.R
import com.boreal.commonutils.application.CUAppInit

fun CUBlurDialog.initElement(view: View) {
    if (resource == null) {
        view.let { vista ->
            CUTitleModel?.let { title ->
                vista.findViewById<ImageView>(R.id.imgGeneric).apply {
                    if (title.CUTitleType == CUTitleType.SUCCESS) {
                        setImageDrawable(
                            ContextCompat.getDrawable(
                                CUAppInit.getAppContext(),
                                R.drawable.ic_success
                            )
                        )
                    } else {
                        setImageDrawable(
                            ContextCompat.getDrawable(
                                CUAppInit.getAppContext(),
                                R.drawable.ic_warning
                            )
                        )
                    }
                }

                title.titleString?.let {
                    vista.findViewById<TextView>(R.id.tvTitle).apply {
                        visibility = View.VISIBLE
                        text = it
                    }
                }

            }

            messageGenericResource?.let {
                vista.findViewById<TextView>(R.id.tvMessageGeneric).apply {
                    visibility = View.VISIBLE
                    text = it
                }
            }

            messageGeneric?.let {
                vista.findViewById<TextView>(R.id.tvMessageGeneric).apply {
                    visibility = View.VISIBLE
                    text = it
                }
            }
            vista.findViewById<Button>(R.id.btnGeneric).apply {
                background = ContextCompat.getDrawable(
                    CUAppInit.getAppContext(),
                    R.drawable.shape_filled_normal
                )
                setOnClickListener {
                    buttonClicked?.invoke(dialog!!)
                }
                buttonTopText?.let {
                    text = it
                }
            }

            vista.findViewById<TextView>(R.id.btnCancel).apply {
                setOnClickListener {
                    buttonCancelClicked?.invoke(dialog!!)
                }
                buttonCancelText?.let {
                    text = it
                    visibility = View.VISIBLE
                }
            }

            buttonType?.let { button ->
                vista.findViewById<Button>(R.id.btnGeneric).apply {
                    background = if (button.CUButtonStyle == CUButtonStyle.NORMAL) {
                        ContextCompat.getDrawable(
                            CUAppInit.getAppContext(),
                            R.drawable.shape_filled_normal
                        )
                    } else {
                        ContextCompat.getDrawable(
                            CUAppInit.getAppContext(),
                            R.drawable.shape_filled_disabled
                        )
                    }
                    text = button.messageButton
                    setOnClickListener {
                        buttonClicked?.invoke(dialog!!)
                    }
                }
            }
        }

    }

}