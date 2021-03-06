package com.github.ijkzen.view

import android.graphics.drawable.GradientDrawable
import android.os.IBinder
import android.view.View
import android.view.animation.Interpolator
import com.github.ijkzen.control.TipGravity

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

    fun isWidthMatchParent(match: Boolean)

    fun token(token: IBinder)

    fun animationTime(duration: Long)

    fun interpolator(interpolator: Interpolator)

    fun show()

    fun dismiss()
}