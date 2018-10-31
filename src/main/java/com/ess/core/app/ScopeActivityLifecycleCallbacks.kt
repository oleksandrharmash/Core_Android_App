package com.ess.core.app

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ess.core.di.ModulesInstallable
import toothpick.Toothpick

class ScopeActivityLifecycleCallbacks : AbsActivityLifecycleCallbacks() {
    private val fragmentCallbacks = ScopeFragmentLifecycleCallbacks()

    override fun onActivityDestroyed(activity: Activity) {
        if (activity is ModulesInstallable) {
            Toothpick.closeScope(activity)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is ModulesInstallable) {
            val scope = Toothpick.openScopes(activity.application, activity)

            activity.installModules(scope)

            Toothpick.inject(activity, scope)
        }

        if (activity is AppCompatActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, true)
        }
    }
}