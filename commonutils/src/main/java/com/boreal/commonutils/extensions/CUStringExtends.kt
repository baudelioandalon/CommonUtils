package com.boreal.commonutils.extensions

fun String.maskCardNumber() = if (this.length == 16) {
    this.substring(0, 4) + "********" + this.substring(12)
} else {
    this
}