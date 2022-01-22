package com.boreal.commonutils.extensions

import java.text.Normalizer


private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()


fun CharSequence.removeTilde(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.replace(temp, "")
}