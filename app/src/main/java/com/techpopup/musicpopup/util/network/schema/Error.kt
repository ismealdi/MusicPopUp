package com.techpopup.musicpopup.util.network.schema


import com.google.gson.annotations.SerializedName

/**
 * This is sample error from api
 * {
 * "field":"auth_name",
 * info":"Email not registered",
 * "message":"Create a new account with this email?"
 * }
 */

data class Error(
    @SerializedName("field")
    val field: String?,
    @SerializedName("info")
    val info: String?,
    @SerializedName("message")
    val message: String?
)