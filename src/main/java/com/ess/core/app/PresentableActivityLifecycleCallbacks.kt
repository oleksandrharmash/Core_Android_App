package com.ess.core.app

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ess.core.presentation.Presentable


class PresentableActivityLifecycleCallbacks : AbsActivityLifecycleCallbacks() {
    private val fragmentCallbacks = PresentableFragmentLifecycleCallbacks()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is Presentable<*, *>) {
            activity.presenter.start()
        }

        if (activity is AppCompatActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, true)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity is Presentable<*, *>) {
            activity.presenter.stop()
        }
    }
}
