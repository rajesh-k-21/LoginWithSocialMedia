package com.madlab.logindemo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData(
    var name: String? = null,
    var email: String? = null,
    var profileUrl: String? = null
) : Parcelable