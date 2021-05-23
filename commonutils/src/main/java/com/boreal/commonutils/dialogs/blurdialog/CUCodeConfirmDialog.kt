package com.boreal.commonutils.dialogs.blurdialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.boreal.commonutils.R
import com.boreal.commonutils.application.CUAppInit
import com.boreal.commonutils.databinding.CuDialogCodeConfirmBinding
import com.google.android.material.card.MaterialCardView
import java.util.concurrent.TimeUnit

class CUCodeConfirmDialog constructor(
    private val userEmail: String? = null,
    private val userPhone: String? = null, cancelable: Boolean = true,
    private val onContinueClicked: ((code: String, dialog: Dialog) -> Unit)? = null,
    private val onCancelClicked: ((dialog: Dialog) -> Unit)? = null,
    private val onResendClicked: ((dialog: Dialog) -> Unit)? = null,
    private val onTimerFinish: ((dialog: Dialog) -> Unit)? = null,
    private val titleContinueButton: String = "Enviar código",
    private val titleCancelButton: String = "Cancelar"
) :
    CUBlurDialogBinding<CuDialogCodeConfirmBinding>(cancelable = cancelable) {

    private lateinit var countDownTimer: CountDownTimer
    private var countCode = 0
    lateinit var list: List<MaterialCardView>
    private var flag = 0
    private val GREY = R.color.kprogresshud_grey_color
    private val RED = R.color.redError

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (userPhone != null) {
            mBinding.tvSubtitle.text = getString(R.string.message_codephone)
            flag = 1
        }
        setupCountdown()
        setupEmailAndCardViews()
        colorCodeFields(GREY)
        setupCodeFields()
        mBinding.apply {
            btnCancelCodeConfirm.setOnClickListener {
                onCancelClicked?.invoke(dialog!!)
            }
            btnContinueCodeConfirm.text = titleContinueButton
            btnCancelCodeConfirm.text = titleCancelButton
            btnResendCode.setOnClickListener {
                onResendClicked?.invoke(dialog!!)
            }
        }
    }

    private fun setupEmailAndCardViews() {
        mBinding.tvMail.text = userEmail

        flag = 0

        list = listOf(
            mBinding.cvPos1,
            mBinding.cvPos2,
            mBinding.cvPos3,
            mBinding.cvPos4,
            mBinding.cvPos5,
            mBinding.cvPos6
        )
    }

    override fun getLayout() = R.layout.cu_dialog_code_confirm

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        countDownTimer.cancel()
    }

    private fun setupCountdown() {
        mBinding.tvCountdown.text = ""
        countDownTimer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val min = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                var secText = (millisUntilFinished / 1000 % 60).toString()
                val sec = millisUntilFinished / 1000
                if (secText.length == 1) {
                    secText = "0${secText}"
                }
                if (sec.toString() == "0") {
                    onFinish()
                }
                "${min}:${secText}".also { mBinding.tvCountdown.text = it }
            }

            override fun onFinish() {
                blockCards()
                this.cancel()
            }
        }.start()
    }

    private fun blockCards() {
        countCode = 0
        mBinding.apply {
            tvError.apply {
                text = context.getString(R.string.coinid)
                visibility = View.VISIBLE
                setEnableCodeFields(false)
                colorCodeFields(RED)
                onTimerFinish?.invoke(dialog!!)
            }
            tvCountdown.text = getString(R.string.countdown_in_zero)
        }


    }

    private fun colorCodeFields(color: Int) {
        for (cardView in list) {
            cardView.strokeColor = ContextCompat.getColor(
                CUAppInit.getAppContext(),
                color
            )
            cardView.strokeWidth = 5
        }
    }

    private fun setupAutoNextField(
        et: EditText,
        sizeRequired: Int,
        etPrevious: EditText?,
        etDestination: EditText?
    ) {
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mBinding.apply {
                    if (et.text.toString().length == sizeRequired) {
                        etDestination?.requestFocus()
                        countCode++
                        if (countCode == 6) {
                            btnContinueCodeConfirm.isEnabled = true
                            btnContinueCodeConfirm.setOnClickListener {
                                onContinueClicked?.invoke(fullCode(), dialog!!)
                            }
                        }
                    }
                    if (et.text.toString().isEmpty()) {
                        countCode--
                        btnContinueCodeConfirm.isEnabled = false
                    }
                }
            }

            private fun fullCode(): String {

                return mBinding.codePos1.text.toString() +
                        mBinding.codePos2.text.toString() +
                        mBinding.codePos3.text.toString() +
                        mBinding.codePos4.text.toString() +
                        mBinding.codePos5.text.toString() +
                        mBinding.codePos6.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        et.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && et.text.toString().isEmpty()) {
                etPrevious?.requestFocus()
            }
            false
        }
    }

    private fun setupCodeFields() {

        setupAutoNextField(mBinding.codePos1, 1, null, mBinding.codePos2)
        setupAutoNextField(mBinding.codePos2, 1, mBinding.codePos1, mBinding.codePos3)
        setupAutoNextField(mBinding.codePos3, 1, mBinding.codePos2, mBinding.codePos4)
        setupAutoNextField(mBinding.codePos4, 1, mBinding.codePos3, mBinding.codePos5)
        setupAutoNextField(mBinding.codePos5, 1, mBinding.codePos4, mBinding.codePos6)
        setupAutoNextField(mBinding.codePos6, 1, mBinding.codePos5, null)

    }

    private fun setEnableCodeFields(enable: Boolean) {
        val list2 = listOf(
            mBinding.codePos1,
            mBinding.codePos2,
            mBinding.codePos3,
            mBinding.codePos4,
            mBinding.codePos5,
            mBinding.codePos6
        )
        for (codePos in list2) {
            codePos.isEnabled = enable
            codePos.setText("")
        }
    }
}