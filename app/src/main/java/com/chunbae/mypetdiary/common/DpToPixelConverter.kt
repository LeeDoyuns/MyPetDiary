package com.chunbae.mypetdiary.common

import android.content.res.Resources
import android.util.TypedValue

class DpToPixelConverter {

    open fun convert(resources: Resources, dp: Int): Int{
        val pixels: Int = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
            resources.displayMetrics
        ).toInt()
        return pixels
    }
}