package com.techpopup.musicpopup.application.base

import android.content.Context
import android.view.View

abstract class AmItemViewHolder<Data>(
    protected var mContext: Context?,
    itemView: View,
    private val mItemClickListener: AmRecyclerAdapter.OnItemClickListener?,
    private val mLongItemClickListener: AmRecyclerAdapter.OnLongItemClickListener?
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener,
    View.OnLongClickListener {

    var isHasHeader = false

    init {
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    abstract fun bind(data: Data)

    override fun onClick(v: View) {
        mItemClickListener?.onItemClick(
            v,
            if (isHasHeader) absoluteAdapterPosition - 1 else absoluteAdapterPosition
        )
    }

    override fun onLongClick(v: View): Boolean {
        if (mLongItemClickListener != null) {
            mLongItemClickListener.onLongItemClick(
                v,
                if (isHasHeader) absoluteAdapterPosition - 1 else absoluteAdapterPosition
            )
            return true
        }
        return false
    }
}