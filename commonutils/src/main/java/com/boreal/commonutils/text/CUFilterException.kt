package com.boreal.commonutils.text

enum class CUFilterException(val exceptionsCharacters: ArrayList<Char>) {
    EMAIL(arrayListOf('@','.','-','_'))
}