package com.boreal.commonutils.extensions

import com.boreal.commonutils.application.CUAppInit
import io.realm.RealmObject

fun RealmObject.saveLocal() {
    CUAppInit.getRealmInstance().executeTransaction { realm ->
        realm.insertOrUpdate(this)
    }
}
fun RealmObject.autoGenerateId(primaryKey: String = "id"): Int {
    val currentIdNumber = CUAppInit.getRealmInstance().where(this::class.java).max(primaryKey)
    return if (currentIdNumber == null) {
        1
    } else {
        currentIdNumber.toInt() + 1
    }
}

inline fun <reified T : RealmObject> getLocal() =
    CUAppInit.getRealmInstance().where(T::class.java).findAll()

