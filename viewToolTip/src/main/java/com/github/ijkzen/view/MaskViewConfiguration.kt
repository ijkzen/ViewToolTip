package com.github.ijkzen.view

import android.os.IBinder
import android.view.animation.Interpolator

interface MaskViewConfiguration {

    fun isShowBackground(show: Boolean)

    fun token(token: IBinder)

    fun animationTime(duration: Long)

    fun interpolator(interpolator: Interpolator)

    fun show()

    fun dismiss()
}