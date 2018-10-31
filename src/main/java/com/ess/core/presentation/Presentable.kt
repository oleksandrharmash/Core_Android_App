package com.ess.core.presentation

interface Presentable<out V, P : GenericPresenter<V>> {
    var presenter: P
}