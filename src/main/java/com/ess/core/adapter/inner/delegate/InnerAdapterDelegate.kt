package com.ess.core.adapter.inner.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ess.core.adapter.delegate.ViewType

interface InnerAdapterDelegate<H : RecyclerView.ViewHolder> {
    val viewType: ViewType?

    fun onCreateViewHolder(parent: ViewGroup) = onCreateTypedViewHolder(parent)

    fun onBindViewHolder(parentPosition: Int, position: Int, holder: RecyclerView.ViewHolder) {
        @Suppress("UNCHECKED_CAST")
        onBindTypedViewHolder(parentPosition, position, holder as? H ?: return)
    }

    fun onCreateTypedViewHolder(parent: ViewGroup): H

    fun onBindTypedViewHolder(parentPosition: Int, position: Int, holder: H)
}