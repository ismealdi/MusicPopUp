package com.techpopup.musicpopup.util.network.schema

data class Payload<T>(
    var meta: Meta? = null,
    var data: T? = null,
    var pagination: Pagination? = null,
    var error: Error? = null
)