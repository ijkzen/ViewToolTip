package com.github.ijkzen.control

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.github.ijkzen.view.AnimationType

interface ToolTipConfiguration {

    fun observeLifecycle(owner: LifecycleOwner): ToolTipConfiguration

    fun customView(contentView: View): ToolTipConfiguration

    fun widthMatchParent(match: Boolean): ToolTipConfiguration

    fun gravity(gravity: TipGravity): ToolTipConfiguration

    fun animation(type: AnimationType): ToolTipConfiguration

    fun arrowWidth(width: Int): ToolTipConfiguration

    fun arrowHeight(height: Int): ToolTipConfiguration

    fun arrowColor(color: Int): ToolTipConfiguration

    fun background(background: GradientDrawable): ToolTipConfiguration

    fun backgroundColor(color: Int): ToolTipConfiguration

    fun backgroundRadius(radius: Int): ToolTipConfiguration

    fun text(text: CharSequence): ToolTipConfiguration

    fun textColor(color: Int): ToolTipConfiguration

    fun textSize(size: Float): ToolTipConfiguration

    fun textAlign(align: Int): ToolTipConfiguration

    fun isShowMaskBackground(show: Boolean): ToolTipConfiguration

    fun isAllowHideByClickMask(allow: Boolean): ToolTipConfiguration

    fun isAllowHideByClickTip(allow: Boolean): ToolTipConfiguration

    fun isAutoHide(auto: Boolean): ToolTipConfiguration

    fun displayTime(duration: Long): ToolTipConfiguration

    fun setTag(tag: String): ToolTipConfiguration

    fun notify(tag: String?)

    fun show()

    fun dismiss()
}