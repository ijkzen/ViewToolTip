package com.github.ijkzen

import android.graphics.drawable.GradientDrawable
import android.view.View

interface ToolTipViewConfiguration {

    fun customView(contentView: View)

    fun gravity(gravity: TipGravity)

    fun arrowWidth(width: Int)

    fun arrowHeight(height: Int)

    fun arrowColor(color: Int)

    fun background(background: GradientDrawable)

    fun backgroundColor(color: Int)

    fun backgroundRadius(radius: Int)

    fun text(text: CharSequence)

    fun textColor(color: Int)

    fun textSize(size: Float)

    fun textAlign(align: Int)

    fun animationType(animationType: AnimationType)
}