package com.ess.core.adapter.inner

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ess.core.adapter.delegate.ViewType
import com.ess.core.adapter.holder.ParentViewHolder
import com.ess.core.adapter.inner.delegate.InnerAdapterDelegate
import com.ess.core.adapter.inner.delegate.InnerViewTypeManager
import com.ess.core.extension.weak

class InnerRecyclerAdapter(
        parent: ParentViewHolder,
        viewTypeManager: InnerViewTypeManager? = null,
        provider: InnerProvider,
        private vararg val delegates: InnerAdapterDelegate<*>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val parent = parent.weak()
    private val viewTypeManager = viewTypeManager?.weak()
    private val provider = provider.weak()

    private fun delegateAtIndex(index: Int) = delegates.elementAtOrNull(index)

    private fun delegateIndexForType(viewType: ViewType) =
            delegates.indexOfFirst { it.viewType == viewType }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            delegateAtIndex(viewType)?.onCreateViewHolder(parent)
                    ?: throw IllegalArgumentException("No AdapterDelegates registered for view type: $viewType.")

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAtIndex(getItemViewType(position))?.apply {
            onBindViewHolder(parent.get()?.parentAdapterPosition ?: return@apply, position, holder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val viewTypeManager = viewTypeManager?.get()
        val parentPosition = parent.get()?.parentAdapterPosition
        if (viewTypeManager != null && parentPosition != null) {
            val viewType = viewTypeManager.getItemViewType(parentPosition, position)
            return delegateIndexForType(viewType)
        }
        if (delegates.size == 1) {
            return 0
        }
        throw IllegalArgumentException("No AdapterDelegates registered for item view at position: $position.")
    }

    override fun getItemCount(): Int {
        val provider = provider.get()
        val parentPosition = parent.get()?.parentAdapterPosition
        if (provider != null && parentPosition != null) {
            return provider.getItemCount(parentPosition)
        }
        return 0
    }

    fun getScrollPosition(): Int {
        val provider = provider.get()
        val parentPosition = parent.get()?.parentAdapterPosition
        if (provider != null && parentPosition != null) {
            return provider.getItemTop(parentPosition)
        }
        return 0
    }
}
