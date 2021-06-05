package com.boreal.commonutils.application.local.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmUserData(
    @PrimaryKey
    var id: Int? = null,
    var userName: String? = null,
    var userEmail: String? = null,
    var userPhone: String? = null
) : RealmObject()