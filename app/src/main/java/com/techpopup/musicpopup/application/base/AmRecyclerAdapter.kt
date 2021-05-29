package com.techpopup.musicpopup.application.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Created by Dimas Prakoso on 07/12/2019.
 */

abstract class AmRecyclerAdapter<Data, Holder : AmItemViewHolder<Data>> :
    RecyclerView.Adapter<Holder?> {

    protected var mContext: Context

    var mDatas: MutableList<Data>

    protected var mItemClickListener: OnItemClickListener? = null

    protected var mLongItemClickListener: OnLongItemClickListener? = null

    constructor(context: Context) {
        mContext = context
        mDatas = ArrayList()
    }

    constructor(context: Context, data: MutableList<Data>) {
        mContext = context
        mDatas = data
    }

    protected fun getView(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(mContext).inflate(getItemResourceLayout(viewType), parent, false)
    }

    protected abstract fun getItemResourceLayout(viewType: Int): Int

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mDatas[position])
    }

    override fun getItemCount(): Int {
        return try {
            mDatas.size
        } catch (e: Exception) {
            0
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        mItemClickListener = itemClickListener
    }

    fun setOnLongItemClickListener(longItemClickListener: OnLongItemClickListener?) {
        mLongItemClickListener = longItemClickListener
    }

    fun getDatas(): List<Data> {
        return mDatas
    }

    fun add(item: Data) {
        mDatas.add(item)
        notifyItemInserted(mDatas.size - 1)
    }

    fun addAll(items: List<Data>) {
        add(items)
    }

    fun add(item: Data, position: Int) {
        mDatas.add(position, item)
        notifyItemInserted(position)
    }

    open fun add(items: List<Data>) {
        val size = items.size
        for (i in 0 until size) {
            mDatas.add(items[i])
        }
        notifyDataSetChanged()
    }

    fun addOrUpdate(item: Data) {
        val i = mDatas.indexOf(item)
        if (i >= 0) {
            mDatas[i] = item
            notifyItemChanged(i)
        } else {
            add(item)
        }
    }

    fun addOrUpdate(items: List<Data>) {
        val size = items.size
        for (i in 0 until size) {
            val item = items[i]
            val x = mDatas.indexOf(item)
            if (x >= 0) {
                mDatas[x] = item
            } else {
                add(item)
            }
        }
        notifyDataSetChanged()
    }

    fun addOrUpdateToFirst(items: List<Data>) {
        val size = items.size
        for (i in 0 until size) {
            val item = items[i]
            val x = mDatas.indexOf(item)
            if (x >= 0) {
                mDatas[x] = item
            } else {
                add(item, 0)
            }
        }
        notifyDataSetChanged()
    }

    fun addToFirst(item: Data) {
        mDatas.add(0, item)
        notifyDataSetChanged()
    }

    fun addToFirst(items: List<Data>?) {
        mDatas.addAll(0, items!!)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        if (position >= 0 && position < mDatas.size) {
            mDatas.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun remove(item: Data) {
        val position = mDatas.indexOf(item)
        remove(position)
    }

    fun clear() {
        mDatas.clear()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    interface OnLongItemClickListener {
        fun onLongItemClick(view: View?, position: Int)
    }
}
