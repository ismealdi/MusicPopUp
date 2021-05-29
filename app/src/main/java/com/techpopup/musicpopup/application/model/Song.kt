package com.techpopup.musicpopup.application.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(
    @SerializedName("artworkUrl100") var artwork: String? = null,
    @SerializedName("previewUrl") var preview: String? = null,
    var trackName: String? = null,
    var artistName: String? = null,
    var collectionName: String? = null
): Parcelable {
    var isPlay: Boolean = false
}



