package com.ashehata.catattendees.qrCode.generator

import com.google.gson.annotations.SerializedName


data class Member(
    @SerializedName("memberName")
    val name: String = "",
    @SerializedName("memberLevel")
    val level: String = "",
)