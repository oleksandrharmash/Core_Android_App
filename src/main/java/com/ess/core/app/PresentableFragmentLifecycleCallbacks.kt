package com.ess.core.app

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import com.ess.core.presentation.Presentable

class PresentableFragmentLifecycleCallbacks : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        if (f is Presentable<*, *>) {
            f.presenter.start()
        }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        if (f is Presentable<*, *>) {
            f.presenter.stop()
        }
    }

}