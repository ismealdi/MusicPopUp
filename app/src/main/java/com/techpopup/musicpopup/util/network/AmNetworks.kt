package com.techpopup.musicpopup.util.network

import com.google.gson.GsonBuilder
import com.techpopup.musicpopup.BuildConfig
import com.techpopup.musicpopup.util.common.AmLogs
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class AmNetworks {

    fun bridge(baseUrl: String = BuildConfig.baseUrl): Retrofit {

        val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                AmLogs.http(message)
            }
        })

        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }

        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor(object : Interceptor {

            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                    .header("Cache-Control", "no-cache")
                    .header("Cache-Control", "no-store")

                /*val token = AmApplication.amContext?.let {
                    AmPreferences(it, AmConstant.Preference.UserToken).getString()
                } ?: run { emptyString() }

                if(token.isNotEmpty()) {
                    requestBuilder.header("Authorization", token)
                } else {
                    requestBuilder.header("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IllvdXIgVXNlcidzIElEIiwib3RoZXIiOiJTb21lIG90aGVyIGRhdGEiLCJBUElfVElNRSI6MTYxMjI3MzkxNH0.gd05N1TBXNjGBtIimO7HY_cEvvo_9AA0BADaqiRGnJc")
                }*/

                return chain.proceed(requestBuilder.build())
            }
        })

        httpClient.connectTimeout(120, TimeUnit.SECONDS)
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.callTimeout(120, TimeUnit.SECONDS)
        httpClient.writeTimeout(50, TimeUnit.SECONDS)
        httpClient.addInterceptor(logging)

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setLenient().create()

        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .client(httpClient.build())
            .build()
    }
}