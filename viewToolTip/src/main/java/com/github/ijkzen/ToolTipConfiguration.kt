package com.github.ijkzen

import android.graphics.drawable.GradientDrawable
import android.view.View

interface ToolTipConfiguration {
    fun customView(contentView: View): ViewToolTip

    fun widthMatchParent(match: Boolean): ViewToolTip

    fun gravity(gravity: TipGravity): ViewToolTip

    fun arrowWidth(width: Int): ViewToolTip

    fun arrowHeight(height: Int): ViewToolTip

    fun arrowColor(color: Int): ViewToolTip

    fun background(background: GradientDrawable): ViewToolTip

    fun backgroundColor(color: Int): ViewToolTip

    fun backgroundRadius(radius: Int): ViewToolTip

    fun text(text: CharSequence): ViewToolTip

    fun textColor(color: Int): ViewToolTip

    fun textSize(size: Float): ViewToolTip

    fun textAlign(align: Int): ViewToolTip

    fun isShowMaskBackground(show: Boolean): ViewToolTip

    fun isAllowHideByClickMask(allow: Boolean): ViewToolTip

    fun isAllowHideByClickTip(allow: Boolean): ViewToolTip

    fun isAutoHide(auto: Boolean): ViewToolTip

    fun displayTime(duration: Long): ViewToolTip

    fun setTag(tag: String): ViewToolTip

    fun notify(tag: String?)
}