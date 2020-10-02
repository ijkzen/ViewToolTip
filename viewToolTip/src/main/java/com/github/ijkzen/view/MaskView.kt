package com.github.ijkzen.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.github.ijkzen.R
import com.github.ijkzen.getAnimationDuration
import com.github.ijkzen.getFlags
import com.github.ijkzen.getWindowGravity

@SuppressLint("NewApi")
class MaskView(context: Context) : View(context), MaskViewConfiguration {

    private val mLayoutParam = WindowManager.LayoutParams()

    private var mIsShow = true

    private val mBackgroundColor = ContextCompat.getColor(context, R.color.mask_color)

    private val mStartColor = Color.argb(
        0,
        Color.red(mBackgroundColor),
        Color.green(mBackgroundColor),
        Color.blue(mBackgroundColor)
    )

    private val mEndColor = Color.argb(
        125,
        Color.red(mBackgroundColor),
        Color.green(mBackgroundColor),
        Color.blue(mBackgroundColor)
    )

    private val mWindowManager by lazy { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }

    private val mStartValueAnimator by lazy {
        ValueAnimator.ofArgb(mStartColor, mEndColor)
            .apply {
                duration = getAnimationDuration()
                addUpdateListener {
                    setBackgroundColor(it.animatedValue as Int)
                }
            }
    }

    private val mEndValueAnimator by lazy {
        ValueAnimator.ofArgb(mEndColor, mStartColor)
            .apply {
                duration = getAnimationDuration()
                addUpdateListener {
                    val color = it.animatedValue as Int
                    setBackgroundColor(color)
                    if (color == mStartColor && isAttachedToWindow) {
                        mWindowManager.removeView(this@MaskView)
                    }
                }
            }
    }

    init {
        mLayoutParam.width = WindowManager.LayoutParams.MATCH_PARENT
        mLayoutParam.height = WindowManager.LayoutParams.MATCH_PARENT
        mLayoutParam.format = PixelFormat.TRANSLUCENT
        mLayoutParam.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
        mLayoutParam.gravity = getWindowGravity()
        mLayoutParam.flags = getFlags()
        fitsSystemWindows = true
    }

    override fun isShowBackground(show: Boolean) {
        mIsShow = show
    }

    override fun token(token: IBinder) {
        mLayoutParam.token = token
    }

    override fun show() {
        if (!mIsShow || isAttachedToWindow) {
            return
        }

        mWindowManager.addView(this, mLayoutParam)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewTreeObserver.addOnGlobalLayoutListener {
                mStartValueAnimator.start()
            }
        }
    }

    override fun dismiss() {
        isClickable = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mEndValueAnimator.start()
        } else {
            mWindowManager.removeView(this)
        }
    }
}