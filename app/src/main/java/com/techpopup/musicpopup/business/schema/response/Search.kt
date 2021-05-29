package com.techpopup.musicpopup.business.schema.response
import android.os.Parcelable
import com.techpopup.musicpopup.application.model.Song
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Search(
    var resultCount: Int? = null,
    var results: List<Song>? = null
): Parcelable



