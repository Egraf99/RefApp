package com.egraf.refapp.utils

import android.content.Context

val dp = { context: Context, dp: Int ->
    val scale = context.resources.displayMetrics.density
    (dp * scale + 0.5f).toInt() }
