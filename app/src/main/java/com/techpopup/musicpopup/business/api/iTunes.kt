package com.techpopup.musicpopup.business.api

import com.techpopup.musicpopup.util.network.AmNetworks
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunes {

    companion object {
        private const val search = "search"

        fun init(): iTunes = AmNetworks().bridge().create(iTunes::class.java)
    }

    @GET(search)
    fun search(
        @Query("term") query: String,
        @Query("limit") limit: Int? = 25,
        @Query("media") media: String? = "music"
    ) : Observable<String>

}