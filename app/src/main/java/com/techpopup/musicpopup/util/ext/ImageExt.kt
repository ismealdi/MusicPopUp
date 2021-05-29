package com.techpopup.musicpopup.util.ext

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.techpopup.musicpopup.R


fun progress(context: Context): CircularProgressDrawable {
    val progress = CircularProgressDrawable(context)
    progress.strokeWidth = 2f
    progress.centerRadius = 15f
    progress.setColorSchemeColors(
        ContextCompat.getColor(context, R.color.colorAmPrimaryDark),
        ContextCompat.getColor(context, R.color.colorAmPrimary)
    )
    progress.start()

    return progress
}

fun ImageView?.imageFlat(
    glide: RequestManager,
    url: String?
) {

    if (url != null) {
        this?.let {
            var requestOptions = RequestOptions()

            requestOptions = requestOptions.transform(CenterCrop())
            requestOptions = requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            requestOptions = requestOptions.placeholder(
                progress(
                    it.context
                )
            )

            requestOptions = requestOptions.timeout(3000)

            glide.load(url).apply(requestOptions).into(it)
        }
    }
}