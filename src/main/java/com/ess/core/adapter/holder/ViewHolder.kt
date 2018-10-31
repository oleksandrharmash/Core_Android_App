package com.ess.core.adapter.holder

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.lang.ref.WeakReference

abstract class ViewHolder<L>(
        parent: ViewGroup,
        private val listener: WeakReference<L>? = null,
        @LayoutRes layoutResId: Int,
        clickable: Boolean = false
) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(
                layoutResId,
                parent,
                false
        )
) {
    interface OnClickListener {
        fun onViewHolderClick(holder: RecyclerView.ViewHolder, position: Int)
    }

    val context: Context get() = itemView.context

    init {
        if (clickable && listener?.get() is OnClickListener) {
            itemView.setOnClickListener {
                val listener = listener.get() as? OnClickListener
                        ?: return@setOnClickListener
                listener.onViewHolderClick(this, adapterPosition)
            }
        }
    }
}