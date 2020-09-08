package com.github.ijkzen

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout

fun convertDp2Px(dp: Int, context: Context): Int {
    return dp * context.resources
        .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
}

fun screenWidth(context: Context): Int {
    return context.resources.displayMetrics.widthPixels
}

fun screenHeight(context: Context): Int {
    val dm = DisplayMetrics()
    (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getRealMetrics(dm)
    return dm.heightPixels
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

fun generateMatchWidthLayout(): FrameLayout.LayoutParams{
    return FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
}

fun isWidthMatchSpace(view: View):Boolean{
    val layout = view.layoutParams
    return layout != null && layout.width == ViewGroup.LayoutParams.MATCH_PARENT
}

fun measureUnspecifiedView(view: View) {
    view.measure(generateWrapWidthSpec(view.context), generateWrapHeightSpec(view.context))
}