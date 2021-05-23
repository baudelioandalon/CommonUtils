package com.boreal.commonutils.ui.components

import com.boreal.commonutils.R


fun CUTextField.validateRequired(message: String = context.getString(R.string.validation_required)): Boolean {
    textInputLayout.error = null
    if (editText.text.toString().trim().isEmpty()) {
        textInputLayout.error = message
        return false
    }
    return true
}

fun CUTextField.validateCardNumber(message: String = context.getString(R.string.validation_cardNumber)): Boolean {
    textInputLayout.error = null
    if (editText.text.toString().trim().length < 19) {
        textInputLayout.error = message
        return false
    }
    return true
}

fun CUTextField.validateCardExpiryDate(message: String = context.getString(R.string.validation_cardExpiryDate)): Boolean {
    textInputLayout.error = null
    if (editText.text.toString().trim().length < 5) {
        textInputLayout.error = message
        return false
    }
    return true
}

fun CUTextField.validateCardNIP(message: String = context.getString(R.string.validation_card_nip)): Boolean {
    textInputLayout.error = null
    if (editText.text.toString().trim().length < 4) {
        textInputLayout.error = message
        return false
    }
    return true
}

fun CUTextField.validateEmail(message: String = context.getString(R.string.validation_email)): Boolean {
    textInputLayout.error = null
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editText.text.toString().trim()).matches()) {
        textInputLayout.error = message
        return false
    }
    return true
}

fun CUTextField.validateMinNumber(
    minNumber: Int,
): Boolean {
    textInputLayout.error = null
    if (editText.text.toString().trim().length < minNumber) {
        textInputLayout.error =
            context.getString(R.string.validation_min_number) + " ${if (minNumber > 1) "$minNumber dígitos" else "un dígito"}"
        return false
    }
    return true
}
