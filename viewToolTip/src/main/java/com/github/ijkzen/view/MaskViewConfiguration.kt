package com.github.ijkzen.view

import android.os.IBinder

interface MaskViewConfiguration {

    fun isShowBackground(show: Boolean)

    fun token(token: IBinder)

    fun show()

    fun dismiss()
}