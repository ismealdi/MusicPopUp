package com.techpopup.musicpopup.util.common

object AmConstant {

    object INTENT {
        object REQUEST {
        }
    }

    object Preference {
        const val UserToken = "constPrefUserToken"
    }

    object NETWORK {
        const val INTERNAL_SERVER_ERROR = 500
        const val BAD_REQUEST = 400
        const val NOT_AUTHORIZED = 401
        const val FORBIDDEN = 403
        const val NOT_FOUND = 404
        const val BAD_GATEWAY = 502
        const val SERVICE_UNAVAILABLE = 503
        const val SUCCESS_RESPONSE = 200
        const val GONE = 410

    }
}