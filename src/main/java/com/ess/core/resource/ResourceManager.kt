package com.ess.core.resource

import android.support.annotation.StringRes

interface ResourceManager {
    fun string(@StringRes resId: Int): String
}