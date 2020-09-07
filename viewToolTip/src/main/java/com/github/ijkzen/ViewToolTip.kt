package com.github.ijkzen

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.transition.Fade
import android.view.*
import android.widget.PopupWindow

open class ViewToolTip(private val context: Context, protected val mTargetView: View) :
    PopupWindow(context), ToolTipConfiguration {
    private var mWindowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var mTipView: ToolTipView = ToolTipView(context)
    private val mTargetRect = Rect()
    private lateinit var mMaskView: View
    private val mMaskValueAnimator = ValueAnimator.ofInt(0, 0X4D)
    private lateinit var mMaskLayoutParam: WindowManager.LayoutParams

    private var mTipGravity = TipGravity.AUTO
    private var isShowMaskBackground = true
    private var isAllowHideByClickMask = true
    private var isAllowHideByClickTip = true
    private var isAutoHide = true
    private var isWidthMatchParent = false
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
        const val ANIMATION_DURATION: Long = 500
        const val DEFAULT_DURATION: Long = 15000
        const val AUTO_HIDE_MESSAGE = 0XFFF8

        fun on(context: Context, target: View): ViewToolTip {
            return ViewToolTip(context, target)
        }
    }

    init {
        initTargetRect()
        setBackgroundDrawable(null)
        isClippingEnabled = false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            enterTransition = Fade()
            exitTransition = Fade()
        }
        initValueAnimator()
        initMaskView()
    }

    private fun initTargetRect() {
        if (!mTargetView.getGlobalVisibleRect(Rect())) {
            val layoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    mTargetView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    mTargetView.getGlobalVisibleRect(mTargetRect)
                    mTipView.setTargetRect(mTargetRect)
                }
            }
            mTargetView.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
        } else {
            mTargetView.getGlobalVisibleRect(mTargetRect)
            mTipView.setTargetRect(mTargetRect)
        }
    }

    private fun initValueAnimator() {
        val valueUpdateListener = ValueAnimator.AnimatorUpdateListener {
            if (isShowing) {
                if (isMaskAttach2Window()) {
                    mMaskView.setBackgroundColor(Color.argb(it.animatedValue as Int, 41, 36, 33))
                }
            } else {
                if (isMaskAttach2Window()) {
                    val value = it.animatedValue as Int
                    mMaskView.setBackgroundColor(Color.argb(0X4D - value, 41, 36, 33))
                    if (value == 0X4D) {
                        mWindowManager.removeView(mMaskView)
                    }
                }
            }
        }
        mMaskValueAnimator.duration = ANIMATION_DURATION
        mMaskValueAnimator.addUpdateListener(valueUpdateListener)
    }

    private fun initMaskView() {
        mMaskLayoutParam = WindowManager.LayoutParams()
        mMaskLayoutParam.width = WindowManager.LayoutParams.MATCH_PARENT
        mMaskLayoutParam.height = WindowManager.LayoutParams.MATCH_PARENT
        mMaskLayoutParam.format = PixelFormat.TRANSLUCENT
        mMaskLayoutParam.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
        mMaskLayoutParam.token = mTargetView.windowToken
        mMaskLayoutParam.flags.and(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.inv())

        mMaskView = View(context)
        mMaskView.fitsSystemWindows = false
    }

    fun show() {
        if (isShowing) {
            return
        }
        startMaskAnimation()

        if (isValidGravity(mTipGravity, isWidthMatchParent)) {
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
                        && !matchParent
            }
            TipGravity.TOP -> {
                return if (matchParent) {
                    val widthSpec = generateMatchWidthSpec(context)
                    val heightSpec = generateWrapHeightSpec(context)
                    mTipView.measure(widthSpec, heightSpec)
                    isWidthMatchParent = mTargetRect.top > mTipView.measuredHeight
                    isWidthMatchParent
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
                        && !matchParent
            }
            TipGravity.BOTTOM -> {
                return if (matchParent) {
                    val widthSpec =
                        generateMatchWidthSpec(context)
                    val heightSpec =
                        generateWrapHeightSpec(context)
                    mTipView.measure(widthSpec, heightSpec)
                    isWidthMatchParent = screenHeight - mTargetRect.bottom > mTipView.measuredHeight
                    isWidthMatchParent
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
        if (isValidGravity(TipGravity.LEFT, isWidthMatchParent)) {
            return TipGravity.LEFT
        }

        if (isValidGravity(TipGravity.TOP, isWidthMatchParent)) {
            return TipGravity.TOP
        }

        if (isValidGravity(TipGravity.RIGHT, isWidthMatchParent)) {
            return TipGravity.RIGHT
        }

        return TipGravity.BOTTOM
    }

    private fun showAuto() {
        val gravity = getValidGravity()
        showGravity(gravity)
    }

    private fun showLeft() {
        mTipView.gravity(TipGravity.RIGHT)
        measureUnspecifiedView(mTipView)
        val widthSpec: Int
        val heightSpec: Int
        width = if (mTipView.measuredWidth > mTargetRect.left) {
            widthSpec = View.MeasureSpec.makeMeasureSpec(mTargetRect.left, View.MeasureSpec.EXACTLY)
            mTargetRect.left
        } else {
            widthSpec =
                View.MeasureSpec.makeMeasureSpec(mTipView.measuredWidth, View.MeasureSpec.EXACTLY)
            mTipView.measuredWidth
        }

        height = if (mTipView.measuredHeight > screenHeight) {
            heightSpec = generateMatchHeightSpec(context)
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            heightSpec = generateWrapHeightSpec(context)
            ViewGroup.LayoutParams.WRAP_CONTENT
        }
        mTipView.measure(widthSpec, heightSpec)
        contentView = mTipView
        val middle = (mTargetRect.top + mTargetRect.bottom) / 2
        val location = mTipView.getArrowLocation()

        var x: Int = mTargetRect.left - mTipView.measuredWidth
        var y: Int = (middle - mTipView.measuredHeight * location).toInt()

        x = Math.max(0, x)
        y = Math.min(screenHeight - mTipView.measuredHeight, Math.max(0, y))

        val top = Math.max(mTargetRect.top, y)
        val bottom = Math.min(y + mTipView.measuredHeight, mTargetRect.bottom)
        val delta = bottom - top
        if (delta < 4.2 * padding) {
            y += (4.2 * padding).toInt() - delta
        }

        showAtLocation(x, y)
    }

    private fun showTop() {
        mTipView.gravity(TipGravity.BOTTOM)
        if (isWidthMatchParent) {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            val widthSpec = generateMatchWidthSpec(context)
            val heightSpec =
                View.MeasureSpec.makeMeasureSpec(mTargetRect.top, View.MeasureSpec.AT_MOST)
            mTipView.measure(widthSpec, heightSpec)
            contentView = mTipView
            val x = 0
            val y = mTargetRect.top - mTipView.measuredHeight
            showAtLocation(x, y)
            return
        }

        measureUnspecifiedView(mTipView)
        val widthSpec: Int
        val heightSpec: Int
        width = if (mTipView.measuredWidth > screenWidth) {
            widthSpec = generateMatchWidthSpec(context)
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            widthSpec = generateWrapWidthSpec(context)
            ViewGroup.LayoutParams.WRAP_CONTENT
        }

        height = if (mTipView.measuredHeight > screenHeight) {
            heightSpec = generateMatchHeightSpec(context)
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            heightSpec = generateWrapHeightSpec(context)
            ViewGroup.LayoutParams.WRAP_CONTENT
        }

        mTipView.measure(widthSpec, heightSpec)

        contentView = mTipView

        val middle = (mTargetRect.left + mTargetRect.right) / 2
        val location = mTipView.getArrowLocation()
        var x: Int = (middle - mTipView.measuredWidth * location).toInt()
        var y: Int = mTargetRect.top - mTipView.measuredHeight

        x = Math.min(Math.max(0, x), screenWidth - mTipView.measuredWidth)
        y = Math.max(0, y)

        showAtLocation(x, y)
    }

    private fun showRight() {
        mTipView.gravity(TipGravity.LEFT)
        measureUnspecifiedView(mTipView)
        val widthSpec: Int
        val heightSpec: Int
        val maxWidth = screenWidth - mTargetRect.right
        width = if (mTipView.measuredWidth > maxWidth) {
            widthSpec = View.MeasureSpec.makeMeasureSpec(maxWidth, View.MeasureSpec.EXACTLY)
            maxWidth
        } else {
            widthSpec =
                View.MeasureSpec.makeMeasureSpec(mTipView.measuredWidth, View.MeasureSpec.EXACTLY)
            mTipView.measuredWidth
        }

        height = if (mTipView.measuredHeight > screenHeight) {
            heightSpec = generateMatchHeightSpec(context)
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            heightSpec = generateWrapHeightSpec(context)
            ViewGroup.LayoutParams.WRAP_CONTENT
        }
        mTipView.measure(widthSpec, heightSpec)
        contentView = mTipView
        val middle = (mTargetRect.top + mTargetRect.bottom) / 2
        val location = mTipView.getArrowLocation()

        var x: Int = mTargetRect.right
        var y: Int = (middle - mTipView.measuredHeight * location).toInt()

        x = Math.max(0, x)
        y = Math.min(screenHeight - mTipView.measuredHeight, Math.max(0, y))

        val top = Math.max(mTargetRect.top, y)
        val bottom = Math.min(y + mTipView.measuredHeight, mTargetRect.bottom)
        val delta = bottom - top
        if (delta < 4.2 * padding) {
            y += (4.2 * padding).toInt() - delta
        }

        showAtLocation(x, y)
    }

    private fun showBottom() {
        mTipView.gravity(TipGravity.TOP)
        if (isWidthMatchParent) {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            contentView = mTipView
            val x = 0
            val y = mTargetRect.bottom
            showAtLocation(x, y)
            return
        }

        measureUnspecifiedView(mTipView)
        val widthSpec: Int
        val heightSpec: Int
        width = if (mTipView.measuredWidth > screenWidth) {
            widthSpec = generateMatchWidthSpec(context)
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            widthSpec = generateWrapWidthSpec(context)
            ViewGroup.LayoutParams.WRAP_CONTENT
        }

        height = if (mTipView.measuredHeight > screenHeight) {
            heightSpec = generateMatchHeightSpec(context)
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            heightSpec = generateWrapHeightSpec(context)
            ViewGroup.LayoutParams.WRAP_CONTENT
        }

        mTipView.measure(widthSpec, heightSpec)

        contentView = mTipView

        val middle = (mTargetRect.left + mTargetRect.right) / 2
        val location = mTipView.getArrowLocation()
        var x: Int = (middle - mTipView.measuredWidth * location).toInt()
        var y: Int = mTargetRect.bottom

        x = Math.min(Math.max(0, x), screenWidth - mTipView.measuredWidth)
        y = Math.max(0, y)
        showAtLocation(x, y)
    }

    private fun showAtLocation(x: Int, y: Int) {
        mTipView.setWindowLocation(x, y)
        showAtLocation(mTargetView, Gravity.NO_GRAVITY, x, y)
    }

    private fun showGravity(tipGravity: TipGravity) {
        when (tipGravity) {
            TipGravity.LEFT -> {
                showLeft()
            }
            TipGravity.TOP -> {
                showTop()
            }
            TipGravity.RIGHT -> {
                showRight()
            }
            TipGravity.BOTTOM -> {
                showBottom()
            }
            else -> {
                showAuto()
            }
        }
    }

    private fun isMaskAttach2Window(): Boolean {
        return mMaskView.isAttachedToWindow
    }

    private fun startMaskAnimation() {
        if (!isShowMaskBackground) {
            return
        }

        if (isMaskAttach2Window()) {
            mWindowManager.removeView(mMaskView)
        }

        mWindowManager.addView(mMaskView, mMaskLayoutParam)
        mMaskValueAnimator.start()
    }

    private fun endMaskAnimation() {
        if (!isShowMaskBackground) {
            return
        }
        mMaskValueAnimator.start()
    }

    override fun dismiss() {
        super.dismiss()
        endMaskAnimation()
        removeAutoHideMessage()
    }

    override fun customView(contentView: View): ViewToolTip {
        mTipView.customView(contentView)
        return this
    }

    override fun widthMatchParent(match: Boolean): ViewToolTip {
        isWidthMatchParent = match
        return this
    }

    override fun showOnDialogFragment(dialog: Boolean): ViewToolTip {
        if (dialog) {
            mMaskLayoutParam.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
        } else {
            mMaskLayoutParam.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
        }
        return this
    }

    override fun gravity(gravity: TipGravity): ViewToolTip {
        mTipGravity = gravity
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
        isShowMaskBackground = show
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