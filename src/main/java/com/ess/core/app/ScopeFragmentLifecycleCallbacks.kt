package com.ess.core.app

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.ess.core.di.ModulesInstallable
import toothpick.Toothpick

class ScopeFragmentLifecycleCallbacks : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        if (f is ModulesInstallable) {
            val scope = Toothpick.openScopes(f.activity, f)

            f.installModules(scope)

            Toothpick.inject(f, scope)
        }
    }


    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        if (f is ModulesInstallable) {
            Toothpick.closeScope(f)
        }
    }
}