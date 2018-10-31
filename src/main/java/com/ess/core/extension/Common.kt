package com.ess.core.extension

import java.lang.ref.WeakReference

fun <T> T.weak() = WeakReference(this)