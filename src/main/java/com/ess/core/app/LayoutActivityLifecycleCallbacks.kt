package com.ess.core.app

import android.app.Activity
import android.os.Bundle
import com.ess.core.annotation.ActivityLayout
import com.ess.core.extension.getAnnotationOrNull

class LayoutActivityLifecycleCallbacks : AbsActivityLifecycleCallbacks() {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val activityLayout = activity::class.java.getAnnotationOrNull(ActivityLayout::class.java)
        if (activityLayout != null) {
            activity.setContentView(activityLayout.layoutId)
        }
    }
}