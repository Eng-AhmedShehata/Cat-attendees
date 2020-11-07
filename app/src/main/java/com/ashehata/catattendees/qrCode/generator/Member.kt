package com.ashehata.catattendees.qrCode.generator

import com.google.gson.annotations.SerializedName


data class Member(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("level")
    val level: String = "",
)