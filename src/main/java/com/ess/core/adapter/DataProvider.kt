package com.ess.core.adapter

interface DataProvider<T> : Provider {
    val data: MutableList<T>

    override val itemCount: Int get() = data.size

    fun setData(data: List<T>?) {
        this.data.clear()

        if (data != null && data.isNotEmpty()) {
            this.data.addAll(data)
        }
    }
}