package com.techpopup.musicpopup.application.base

import androidx.lifecycle.MutableLiveData
import com.techpopup.musicpopup.util.network.schema.Resource

open class AmMutableLiveData<T> : MutableLiveData<Resource<T>>()