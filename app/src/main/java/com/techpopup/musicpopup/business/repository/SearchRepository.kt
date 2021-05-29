package com.techpopup.musicpopup.business.repository

import com.techpopup.musicpopup.application.base.AmMutableLiveData
import com.techpopup.musicpopup.business.api.iTunes
import com.techpopup.musicpopup.business.schema.response.Search
import com.techpopup.musicpopup.util.ext.*

open class SearchRepository {

    private val api = iTunes.init()

    open fun search(query: String): AmMutableLiveData<Search> {
        val data: AmMutableLiveData<Search> = AmMutableLiveData()

        api.let {
            data.isLoading()
            api.search(query = query).observe()?.subscribe(
                { response ->
                    data.value = isResponse<Search>(response, genericType<Search>()).value()
                },
                { error ->
                    data.isError(error)
                }
            )
        }

        return data
    }


}