package com.ess.core.adapter

import com.ess.core.presentation.ItemView

interface ItemViewPresenter<V : ItemView> {
    fun present(view: V, position: Int)
}