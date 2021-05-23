package com.boreal.commonutils.common.encrypt.rsa.excepciones

import java.lang.Exception

class CUCifradosExcepcion(message: String, tipoCifrado: Int) : Exception() {

    var mensaje = ""
    var tipoCifrado = 0 // 1 --> cifrado, 2 --> descifrado
    override val message: String
        get() {
            when (tipoCifrado) {
                1 -> {
                    mensaje = "Ocurrio un error al encriptar el texto. $mensaje"
                }
                2 -> {
                    mensaje = "Ocurrio un error al desencriptar el texto. $mensaje"
                }
                3 -> {
                    mensaje = "Ocurrio un error al obtener llave de cifrado. $mensaje"
                }
                4 -> {
                    mensaje = "Ocurrio un error al obtener vector de cifrado. $mensaje"
                }
                5 -> {
                    mensaje = "Ocurrio un error al obtener trama de cifrado. $mensaje"
                }
            }
            return mensaje
        }

    companion object {
        private const val serialVersionUID = 9014862877505769727L
    }

    init {
        mensaje = message
        this.tipoCifrado = tipoCifrado
    }

}