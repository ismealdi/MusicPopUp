package com.techpopup.musicpopup.util.network.schema

import com.google.gson.annotations.SerializedName

data class Meta(
    var code: Int? = 400,
    @SerializedName("message", alternate = ["msg"]) var message: String? = "Bad Request",
    var error: String? = null
)