package com.github.ijkzen

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup

fun convertDp2Px(dp: Int, context: Context): Int {
    return dp * context.resources
        .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
}

fun screenWidth(context: Context): Int {
    return context.resources.displayMetrics.widthPixels
}

fun screenHeight(context: Context): Int {
    return context.resources.displayMetrics.heightPixels
}

fun statusBarHeight(context: Context): Int {
    var result = 0
    val resourceId: Int =
        context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = context.resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun setViewMatchScreen(root: View){
    val layout  = ViewGroup.LayoutParams(screenWidth(root.context) - convertDp2Px(20, root.context), ViewGroup.LayoutParams.WRAP_CONTENT)
    root.layoutParams = layout
}