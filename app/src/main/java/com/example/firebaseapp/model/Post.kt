package com.example.firebaseapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post (
    val id: String = "",
    val content: String = "",
    val author: String = "",
    val timestamp: Long = 0L,
    var likesCount:Int = 0,
    var likes:MutableMap<String, Boolean> = HashMap()
) : Parcelable