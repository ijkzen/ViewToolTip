package com.github.ijkzen.control

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.github.ijkzen.*
import com.github.ijkzen.view.AnimationType
import com.github.ijkzen.view.MaskView
import com.github.ijkzen.view.ToolTipView

open class ViewToolTip(private val context: Context, protected val mTargetView: View) :
    ToolTipConfiguration, LifecycleObserver {
    private var mTipView = ToolTipView(context)

    @SuppressLint("NewApi")
    private var mMaskView = MaskView(context)
    private var mTargetRect = Rect()

    private var mTipGravity = TipGravity.AUTO
    private var mIsWidthMatchParent = false

    private var isShowing = false
    private var isAllowHideByClickMask = true
    private var isAllowHideByClickTip = true
    private var isAutoHide = true
    private var mDuration = DEFAULT_DURATION
    private var mTag: String? = null

    private val screenWidth = screenWidth(context)
    private val screenHeight = screenHeight(context)
    private val padding = convertDp2Px(10, context)
    private val mHandler = Handler {
        val what = it.what
        if (what == AUTO_HIDE_MESSAGE) {
            dismiss()
            return@Handler true
        } else {
            return@Handler false
        }
    }

    companion object {
        const val DEFAULT_DURATION: Long = 15000
        const val AUTO_HIDE_MESSAGE = 0XFFF8

        fun on(target: View): ViewToolTip {
            return ViewToolTip(target.context, target)
        }
    }

    private fun initTargetRect() {
        if (!mTargetView.getGlobalVisibleRect(Rect())) {
            mTargetView.post {
                mTargetView.getGlobalVisibleRect(mTargetRect)
                mTipView.setTargetRect(mTargetRect)
            }
        } else {
            mTargetView.getGlobalVisibleRect(mTargetRect)
            mTipView.setTargetRect(mTargetRect)
        }
    }

    @SuppressLint("NewApi")
    override fun show() {
        if (isShowing) {
            return
        }
        isShowing = true
        initTargetRect()

        mMaskView.token(mTargetView.windowToken)
        mMaskView.show()
        mTipView.token(mTargetView.windowToken)
        if (isValidGravity(mTipGravity, mIsWidthMatchParent)) {
            showGravity(mTipGravity)
        } else {
            showAuto()
        }

        setMaskClick()
        setTipClick()
        setAutoHide()
    }

    private fun isValidGravity(tipGravity: TipGravity, matchParent: Boolean): Boolean {
        if (tipGravity == TipGravity.AUTO) {
            return true
        }

        when (tipGravity) {
            TipGravity.LEFT -> {
                return mTargetRect.left > 6 * padding
                        && mTargetRect.top > padding
                        && screenHeight - mTargetRect.bottom > padding
                        && !matchParent
            }
            TipGravity.TOP -> {
                return if (matchParent) {
                    val widthSpec = generateMatchWidthSpec(context)
                    val heightSpec = generateWrapHeightSpec(context)
                    mTipView.measure(widthSpec, heightSpec)
                    mIsWidthMatchParent = mTargetRect.top > mTipView.measuredHeight
                    mIsWidthMatchParent
                } else {
                    measureUnspecifiedView(mTipView)
                    if (mTargetRect.top > mTipView.measuredHeight) {
                        true
                    } else {
                        isValidGravity(tipGravity, !matchParent)
                    }
                }
            }
            TipGravity.RIGHT -> {
                return screenWidth - mTargetRect.right > 6 * padding
                        && mTargetRect.top > padding
                        && screenHeight - mTargetRect.bottom > padding
                        && !matchParent
            }
            TipGravity.BOTTOM -> {
                return if (matchParent) {
                    val widthSpec =
                        generateMatchWidthSpec(context)
                    val heightSpec =
                        generateWrapHeightSpec(context)
                    mTipView.measure(widthSpec, heightSpec)
                    mIsWidthMatchParent =
                        screenHeight - mTargetRect.bottom > mTipView.measuredHeight
                    mIsWidthMatchParent
                } else {
                    measureUnspecifiedView(mTipView)
                    if (screenHeight - mTargetRect.bottom > mTipView.measuredHeight) {
                        true
                    } else {
                        isValidGravity(tipGravity, !matchParent)
                    }
                }
            }
            else -> {
                return false
            }
        }
    }

    private fun getValidGravity(): TipGravity {
        if (isValidGravity(TipGravity.LEFT, mIsWidthMatchParent)) {
            return TipGravity.LEFT
        }

        if (isValidGravity(TipGravity.TOP, mIsWidthMatchParent)) {
            return TipGravity.TOP
        }

        if (isValidGravity(TipGravity.RIGHT, mIsWidthMatchParent)) {
            return TipGravity.RIGHT
        }

        return TipGravity.BOTTOM
    }

    private fun showAuto() {
        val gravity = getValidGravity()
        showGravity(gravity)
    }

    private fun showGravity(tipGravity: TipGravity) {
        mTipView.gravity(tipGravity)
        mTipView.show()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun dismiss() {
        isShowing = false
        mTipView.dismiss()
        mMaskView.dismiss()
        removeAutoHideMessage()
    }

    override fun observeLifecycle(owner: LifecycleOwner): ViewToolTip {
        owner.lifecycle.addObserver(this)
        return this
    }

    override fun customView(contentView: View): ViewToolTip {
        mTipView.customView(contentView)
        return this
    }

    override fun widthMatchParent(match: Boolean): ViewToolTip {
        mIsWidthMatchParent = match
        mTipView.isWidthMatchParent(match)
        return this
    }

    override fun gravity(gravity: TipGravity): ViewToolTip {
        mTipGravity = gravity
        return this
    }

    override fun animation(type: AnimationType): ToolTipConfiguration {
        mTipView.animationType(type)
        return this
    }

    override fun arrowWidth(width: Int): ViewToolTip {
        mTipView.arrowWidth(width)
        return this
    }

    override fun arrowHeight(height: Int): ViewToolTip {
        mTipView.arrowHeight(height)
        return this
    }

    override fun arrowColor(color: Int): ViewToolTip {
        mTipView.arrowColor(color)
        return this
    }

    override fun background(background: GradientDrawable): ViewToolTip {
        mTipView.background(background)
        return this
    }

    override fun backgroundColor(color: Int): ViewToolTip {
        mTipView.backgroundColor(color)
        return this
    }

    override fun backgroundRadius(radius: Int): ViewToolTip {
        mTipView.backgroundRadius(radius)
        return this
    }

    override fun text(text: CharSequence): ViewToolTip {
        mTipView.text(text)
        return this
    }

    override fun textColor(color: Int): ViewToolTip {
        mTipView.textColor(color)
        return this
    }

    override fun textSize(size: Float): ViewToolTip {
        mTipView.textSize(size)
        return this
    }

    override fun textAlign(align: Int): ViewToolTip {
        mTipView.textAlign(align)
        return this
    }

    override fun isShowMaskBackground(show: Boolean): ViewToolTip {
        mMaskView.isShowBackground(show)
        return this
    }

    override fun isAllowHideByClickMask(allow: Boolean): ViewToolTip {
        isAllowHideByClickMask = allow
        return this
    }

    override fun isAllowHideByClickTip(allow: Boolean): ViewToolTip {
        isAllowHideByClickTip = allow
        return this
    }

    override fun isAutoHide(auto: Boolean): ViewToolTip {
        isAutoHide = auto
        return this
    }

    override fun displayTime(duration: Long): ViewToolTip {
        mDuration = duration
        return this
    }

    override fun setTag(tag: String): ViewToolTip {
        mTag = tag
        return this
    }

    private fun setMaskClick() {
        if (isAllowHideByClickMask) {
            mMaskView.setOnClickListener { dismiss() }
        }
    }

    private fun setTipClick() {
        if (isAllowHideByClickTip) {
            mTipView.setOnClickListener { dismiss() }
        }
    }

    private fun setAutoHide() {
        if (isAutoHide) {
            mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MESSAGE, mDuration)
        }
    }

    private fun removeAutoHideMessage() {
        mHandler.removeMessages(AUTO_HIDE_MESSAGE)
    }

    override fun notify(tag: String?) {
        if (tag == mTag) {
            if (isShowing) {
                return
            }

            show()
        } else {
            dismiss()
        }
    }
}
