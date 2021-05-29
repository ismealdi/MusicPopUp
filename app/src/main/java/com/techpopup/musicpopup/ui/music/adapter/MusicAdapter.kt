package com.techpopup.musicpopup.ui.music.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.RequestManager
import com.techpopup.musicpopup.R
import com.techpopup.musicpopup.application.base.AmItemViewHolder
import com.techpopup.musicpopup.application.base.AmRecyclerAdapter
import com.techpopup.musicpopup.application.model.Song
import com.techpopup.musicpopup.util.ext.imageFlat
import com.techpopup.musicpopup.util.ext.setTextOrDash
import kotlinx.android.synthetic.main.item_song.view.*


class MusicAdapter(
    private val glide: RequestManager,
    private val context: Context,
    private val data: MutableList<Song>,
    private val itemClick: ((Int?, Song?) -> Unit)? = null,
    private val isSearchEmpty: ((Boolean) -> Unit)? = null
) : AmRecyclerAdapter<Song, AmItemViewHolder<Song>>(context, data) {

    private var activeSong: Int? = null

    override fun getItemResourceLayout(viewType: Int): Int {
        return R.layout.item_song
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AmItemViewHolder<Song> {

        return SongViewHolder(
            mContext,
            getView(parent, viewType),
            mItemClickListener,
            mLongItemClickListener
        )
    }

    inner class SongViewHolder(
        context: Context?,
        itemView: View,
        itemClickListener: OnItemClickListener?,
        longItemClickListener: OnLongItemClickListener?,
    ) : AmItemViewHolder<Song>(
        context,
        itemView,
        itemClickListener,
        longItemClickListener
    ) {

        override fun bind(data: Song) {
            with(itemView) {
                val radius = resources.getDimension(R.dimen.am_radius)

                imageArtWork?.shapeAppearanceModel?.toBuilder()?.setAllCornerSizes(radius)?.build()?.let { imageArtWork?.setShapeAppearanceModel(it) }

                playBadge?.isVisible = data.isPlay
                imageArtWork?.imageFlat(glide, data.artwork)
                songArtist?.setTextOrDash(data.artistName)
                songTitle?.setTextOrDash(data.trackName)
                songAlbum?.setTextOrDash(data.collectionName)

                itemParent?.setOnClickListener {
                    itemClick?.invoke(absoluteAdapterPosition, data)
                }
            }
        }

    }

    fun get(position: Int) = mDatas.getOrNull(position)

    fun removeAll() {
        mDatas.clear()

        notifyDataSetChanged()
    }

    fun isSelected(position: Int) {
        if(activeSong != null) {
            mDatas.getOrNull(activeSong ?: 0)?.isPlay = false
        }

        mDatas.getOrNull(position)?.isPlay = true
        activeSong = position

        notifyDataSetChanged()
    }

}