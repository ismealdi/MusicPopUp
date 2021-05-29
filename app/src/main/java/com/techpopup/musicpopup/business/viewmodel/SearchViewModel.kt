package com.techpopup.musicpopup.business.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.techpopup.musicpopup.business.repository.SearchRepository
import com.techpopup.musicpopup.business.schema.response.Search
import com.techpopup.musicpopup.util.network.schema.Resource

class SearchViewModel : ViewModel() {

    private var searchRepository: SearchRepository = SearchRepository()

    private val _search = MutableLiveData<String>()

    val songs: LiveData<Resource<Search>> = Transformations.switchMap(_search) {
        searchRepository.search(it)
    }

    fun search(query: String) {
        _search.value = query
    }

    fun retrySearch() {
        _search.value?.let {
            _search.value = it
        }
    }

}