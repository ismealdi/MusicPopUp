package com.techpopup.musicpopup.util.network.schema

import com.google.gson.annotations.SerializedName

data class Pagination(
    var limit: Int? = null,
    var page: Int? = null,
    @SerializedName("next_id") var nextId: Int? = null,
    @SerializedName("total_page", alternate = ["total_pages", "total"]) var total: Int = 0
)