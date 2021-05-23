package com.boreal.commonutils.ui.components

import android.content.Context
import com.github.barteksc.pdfviewer.PDFView
import android.util.Base64
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle

fun PDFView.createPdfWithBase64(pdfView: PDFView, base64: String, context: Context?) {

    val decodedString: ByteArray = Base64.decode(base64, Base64.DEFAULT)

    pdfView.fromBytes(decodedString).enableSwipe(true)
        .scrollHandle(DefaultScrollHandle(context))
        .spacing(20)
        .load()
}