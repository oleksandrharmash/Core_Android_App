package com.ess.core.adapter.inner.delegate

import com.ess.core.adapter.delegate.ViewType

interface InnerViewTypeManager {
    fun getItemViewType(parentPosition: Int, position: Int): ViewType
}