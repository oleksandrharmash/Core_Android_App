package com.ess.core.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

interface AdapterDelegate<H : RecyclerView.ViewHolder> {
    val viewType: ViewType?

    fun onCreateViewHolder(parent: ViewGroup) = onCreateTypedViewHolder(parent)

    fun onBindViewHolder(position: Int, holder: RecyclerView.ViewHolder) {
        @Suppress("UNCHECKED_CAST")
        onBindTypedViewHolder(position, holder as? H ?: return)
    }

    fun onCreateTypedViewHolder(parent: ViewGroup): H

    fun onBindTypedViewHolder(position: Int, holder: H)
}