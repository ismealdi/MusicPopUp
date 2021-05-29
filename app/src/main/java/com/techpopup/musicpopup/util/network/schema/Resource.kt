package com.techpopup.musicpopup.util.network.schema

data class Resource<T>(var status: Status? = null, var meta: Meta? = null, var payload: T? = null, var errorData: Error? = null) {
    companion object {
        fun <T> success(data: T?, meta: Meta): Resource<T> {
            return Resource(Status.SUCCESS, meta, payload = data)
        }

        fun <T> error(data: T?, meta: Meta): Resource<T> {
            return Resource(Status.ERROR, meta, data)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }
    }
}