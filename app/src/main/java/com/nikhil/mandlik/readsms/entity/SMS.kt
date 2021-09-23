package com.nikhil.mandlik.readsms.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SMS(
    val id : String,
    val address : String,
    val body : String,
    val date: String
) : Parcelable
