package com.ess.core.presentation

import android.support.annotation.CallSuper
import com.ess.core.extension.weak
import java.lang.ref.WeakReference

abstract class ViewPresenter<out V>(view: V) : GenericPresenter<V> {
    private var viewReference: WeakReference<V>? = view.weak()

    private var isAttached = false

    override val view get() = if (isAttached) viewReference?.get() else null

    @CallSuper
    override fun start() {
        isAttached = true
    }

    @CallSuper
    override fun stop() {
        isAttached = false
    }

}