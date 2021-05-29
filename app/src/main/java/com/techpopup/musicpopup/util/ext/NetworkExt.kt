package com.techpopup.musicpopup.util.ext

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.MalformedJsonException
import com.techpopup.musicpopup.R
import com.techpopup.musicpopup.application.base.AmActivity
import com.techpopup.musicpopup.application.base.AmApplicationProvider
import com.techpopup.musicpopup.application.base.AmMutableLiveData
import com.techpopup.musicpopup.util.common.AmConstant.NETWORK.BAD_GATEWAY
import com.techpopup.musicpopup.util.common.AmConstant.NETWORK.GONE
import com.techpopup.musicpopup.util.common.AmConstant.NETWORK.INTERNAL_SERVER_ERROR
import com.techpopup.musicpopup.util.common.AmConstant.NETWORK.NOT_AUTHORIZED
import com.techpopup.musicpopup.util.common.AmConstant.NETWORK.NOT_FOUND
import com.techpopup.musicpopup.util.common.AmConstant.NETWORK.SERVICE_UNAVAILABLE
import com.techpopup.musicpopup.util.common.AmConstant.NETWORK.SUCCESS_RESPONSE
import com.techpopup.musicpopup.util.common.AmLogs
import com.techpopup.musicpopup.util.network.schema.Meta
import com.techpopup.musicpopup.util.network.schema.Payload
import com.techpopup.musicpopup.util.network.schema.Resource
import com.techpopup.musicpopup.util.network.schema.Status
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.net.UnknownServiceException


inline fun <reified T> genericType() = object: TypeToken<T>() {}.type

fun <T> AmMutableLiveData<T>.isLoading() {
    AmLogs.http("Loading fetching data")
    this.value = Resource(status = Status.LOADING)
}

open class isResponse<A>(private var resource: String, private val type: Type)  {

    fun value() : Resource<A>? {
        if(isJSONValid(resource)) {
            return try {
                val itemList: A? = Gson().fromJson(resource, type)
                Resource.success(data = itemList, success())
            } catch (e: JsonSyntaxException) {
                AmLogs.http("Error Info Network $resource")
                val meta = Meta(500, message = resource)
                Resource.error(null, meta)

            }
        }

        val meta = internalError()

        if(resource.isNotEmpty()) {
            meta.message = resource
        }

        return Resource.error(null, meta)
    }
}

fun <T> AmMutableLiveData<T>.isError(error: Throwable?) {
    when (error) {
        is HttpException -> {
            val response = error.response()?.errorBody()?.string()

            try {
                Gson().fromJson(response, Payload::class.java)?.let {
                    val meta = it.meta
                    meta?.code = error.code()
                    this.value = Resource(Status.ERROR, meta, errorData = it.error)

                    return
                }
            } catch (e: IllegalStateException) {
                val meta = responseInvalidError(error.code())
                this.value = Resource(Status.ERROR, meta)

                return
            } catch (e: JsonSyntaxException) {
                val meta = responseInvalidError(error.code())
                this.value = Resource(Status.ERROR, meta)

                return
            }

            val meta = Meta(error.code(), error.message())
            this.value = Resource(Status.ERROR, meta)

            return
        }

        is SocketTimeoutException -> {
            val meta = timeOutError()
            this.value = Resource(Status.ERROR, meta)

            return
        }

        is UnknownHostException -> {
            val meta = noInternetConnection()
            this.value = Resource(Status.ERROR, meta)
        }

        is UnknownServiceException -> {
            val meta = internalError()
            this.value = Resource(Status.ERROR, meta)
        }

        is MalformedJsonException -> {
            val meta = internalError()
            this.value = Resource(Status.ERROR, meta)
        }

        is IllegalStateException -> {

            val meta = internalError()
            this.value = Resource(Status.ERROR, meta)
        }

        else -> {
            val meta = internalError()
            this.value = Resource(Status.ERROR, meta)
        }
    }

    AmLogs.http("Error Info Network ${error?.message.toString()}")
}

fun <T> Observable<T>.observe(): Observable<T>? {
    return this.subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()
    )
}

fun Meta?.isError(
    activity: AmActivity,
    customAction: (() -> Unit)? = null,
    function: (String) -> Unit,
): Boolean {
    this?.let { meta ->
        if (meta.code != SUCCESS_RESPONSE && meta.code != NOT_AUTHORIZED && meta.code != GONE) {
            function.invoke(meta.message ?: emptyString())
            return true
        } else if (meta.code == NOT_AUTHORIZED || meta.code == GONE) {

            var action: (() -> Unit)? = null

            customAction?.let {
                action = { customAction.invoke() }
            }

            //activity.newToken(meta.message ?: emptyString(), customAction = action)

            function.invoke(emptyString())
            return true
        }
    }

    return false
}

fun Status?.isLoading() = (this == Status.LOADING)

fun internalError() = Meta(INTERNAL_SERVER_ERROR,
    AmApplicationProvider.get().getString(R.string.text_something_went_wrong)) // "Internal Error"
fun noInternetConnection() = Meta(SERVICE_UNAVAILABLE,
    AmApplicationProvider.get().getString(R.string.message_connect_to_internet)) // "No Internet Connection"
fun timeOutError() = Meta(BAD_GATEWAY,
    AmApplicationProvider.get().getString(R.string.message_connect_to_internet)) //"Timeout"
fun success() = Meta(SUCCESS_RESPONSE, emptyString())
fun responseInvalidError(code: Int?) = Meta(code,
    AmApplicationProvider.get().getString(R.string.text_something_went_wrong)) // "Invalid Response Format"
fun notFoundContent(): Int = NOT_FOUND