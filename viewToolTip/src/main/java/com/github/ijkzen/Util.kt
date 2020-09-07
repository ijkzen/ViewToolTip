package com.github.ijkzen

import android.content.Context
import android.util.DisplayMetrics
import android.view.View

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

fun generateMatchWidthSpec(context: Context): Int {
    return View.MeasureSpec.makeMeasureSpec(screenWidth(context), View.MeasureSpec.EXACTLY)
}

fun generateWrapWidthSpec(context: Context): Int {
    return View.MeasureSpec.makeMeasureSpec(screenWidth(context), View.MeasureSpec.AT_MOST)
}

fun generateMatchHeightSpec(context: Context): Int {
    return View.MeasureSpec.makeMeasureSpec(screenHeight(context), View.MeasureSpec.EXACTLY)
}

fun generateWrapHeightSpec(context: Context): Int {
    return View.MeasureSpec.makeMeasureSpec(screenHeight(context), View.MeasureSpec.AT_MOST)
}

fun measureUnspecifiedView(view: View) {
    view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
}