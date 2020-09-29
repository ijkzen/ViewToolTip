package com.github.ijkzen.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.github.ijkzen.*
import com.github.ijkzen.control.TipGravity

@SuppressLint("ViewConstructor")
open class ToolTipView : FrameLayout, ToolTipViewConfiguration {

    private val mWindowManager by lazy { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }

    private var mTargetRect: Rect = Rect()
    private val mBubblePaint = Paint()
    private val mBubblePath = Path()
    private var mContentView: View = AppCompatTextView(context)
    private var mGravity = TipGravity.AUTO
    private val mPadding = convertDp2Px(10, context)
    private var mArrowWidth = (mPadding * 1.2).toInt()
    private var mArrowHeight = (mPadding * 0.8).toInt()
    private var mArrowLocation: Int = 0
    private var mBackground = GradientDrawable()
    private var mWindowX: Int = 0
    private var mWindowY: Int = 0

    private var mIsWidthMatchParent = false
    private val screenWidth = screenWidth(context)
    private val screenHeight = screenHeight(context)

    private val mLayoutParam = WindowManager.LayoutParams()

    private var mAnimationType: AnimationType = AnimationType.FADE

    private val mFadeStartAnimator = ValueAnimator.ofFloat(0F, 1F)
        .apply {
            duration = getAnimationDuration()
            addUpdateListener {
                if (isAttachedToWindow) {
                    val alpha = it.animatedValue as Float
                    this@ToolTipView.alpha = alpha
                    mContentView.alpha = alpha
                }
            }
        }

    private val mFadeEndAnimator = ValueAnimator.ofFloat(1F, 0F)
        .apply {
            duration = getAnimationDuration()
            addUpdateListener {
                if (isAttachedToWindow) {
                    val alpha = it.animatedValue as Float
                    this@ToolTipView.alpha = alpha
                    mContentView.alpha = alpha

                    if (alpha == 0F) {
                        mWindowManager.removeView(this@ToolTipView)
                    }
                }
            }
        }

    private val mScaleStartAnimator = ValueAnimator.ofFloat(0F, 1F)
        .apply {
            duration = getAnimationDuration()
            addUpdateListener {
                if (isAttachedToWindow) {
                    val scale = it.animatedValue as Float
                    scaleX = scale
                    scaleY = scale
                    mContentView.scaleX = scale
                    mContentView.scaleY = scale
                    this@ToolTipView.alpha = scale
                    mContentView.alpha = scale
                }
            }
        }

    private val mScaleEndAnimator = ValueAnimator.ofFloat(1F, 0F)
        .apply {
            duration = getAnimationDuration()
            addUpdateListener {
                if (isAttachedToWindow) {
                    val scale = it.animatedValue as Float
                    scaleX = scale
                    scaleY = scale
                    mContentView.scaleX = scale
                    mContentView.scaleY = scale
                    this@ToolTipView.alpha = scale
                    mContentView.alpha = scale

                    if (alpha == 0F) {
                        mWindowManager.removeView(this@ToolTipView)
                    }
                }
            }
        }

    private var mStartAnimator: Animator = mFadeStartAnimator
    private var mEndAnimator: Animator = mFadeEndAnimator


    constructor(context: Context) : super(context)

    init {
        setPadding(mPadding, mPadding, mPadding, mPadding)
        mContentView.setPadding(mPadding, mPadding, mPadding, mPadding)
        addView(
            mContentView,
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        )
        mBackground.cornerRadius = convertDp2Px(10, context).toFloat()
        mBackground.setColor(Color.WHITE)
        mContentView.background = mBackground

        mBubblePaint.color = Color.WHITE
        mBubblePaint.isAntiAlias = true
        mBubblePaint.style = Paint.Style.FILL
        mBubblePath.fillType = Path.FillType.EVEN_ODD

        mLayoutParam.format = PixelFormat.TRANSLUCENT
        mLayoutParam.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
        mLayoutParam.gravity = getWindowGravity()
        mLayoutParam.flags = getFlags()
        fitsSystemWindows = false
    }

    fun setTargetRect(targetRect: Rect) {
        mTargetRect = targetRect
    }

    private fun setWindowLocation(x: Int, y: Int) {
        mWindowX = if (x < 0) 0 else x
        mWindowY = if (y < 0) 0 else y
    }

    override fun customView(contentView: View) {
        removeView(mContentView)
        mContentView = contentView
        addView(mContentView)
    }

    override fun gravity(gravity: TipGravity) {
        mGravity = gravity
    }

    override fun arrowWidth(width: Int) {
        mArrowWidth = width
    }

    override fun arrowHeight(height: Int) {
        mArrowHeight = height
    }

    override fun arrowColor(color: Int) {
        mBubblePaint.color = color
    }

    override fun background(background: GradientDrawable) {
        mBackground = background
        if (mContentView is AppCompatTextView) {
            mContentView.background = mBackground
        }
    }

    override fun backgroundColor(color: Int) {
        mBackground.setColor(color)
        if (mContentView is AppCompatTextView) {
            mContentView.background = mBackground
        }
    }

    override fun backgroundRadius(radius: Int) {
        mBackground.cornerRadius = radius.toFloat()
        if (mContentView is AppCompatTextView) {
            mContentView.background = mBackground
        }
    }

    override fun text(text: CharSequence) {
        (mContentView as? AppCompatTextView)?.text = text
    }

    override fun textColor(color: Int) {
        (mContentView as? AppCompatTextView)?.setTextColor(color)
    }

    override fun textSize(size: Float) {
        (mContentView as? AppCompatTextView)?.textSize = size
    }

    override fun textAlign(align: Int) {
        (mContentView as? AppCompatTextView)?.textAlignment = align
    }

    override fun animationType(animationType: AnimationType) {
        mAnimationType = animationType
        when (animationType) {
            AnimationType.FADE -> {
                mStartAnimator = mFadeStartAnimator
                mEndAnimator = mFadeEndAnimator
            }
            AnimationType.SCALE -> {
                mStartAnimator = mScaleStartAnimator
                mEndAnimator = mScaleEndAnimator
            }
            AnimationType.SLIDE -> {

            }
            AnimationType.REVEAL -> {

            }
        }
    }

    override fun isWidthMatchParent(match: Boolean) {
        mIsWidthMatchParent = match
    }

    override fun show() {
        when (mGravity) {
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
            }
        }
    }

    private fun showLeft() {
        val widthSpec: Int
        val heightSpec: Int
        if (isWidthMatchSpace(mContentView)) {
            mLayoutParam.width = mTargetRect.left
            mLayoutParam.height = ViewGroup.LayoutParams.WRAP_CONTENT
            widthSpec = MeasureSpec.makeMeasureSpec(mTargetRect.left, MeasureSpec.EXACTLY)
            heightSpec = generateWrapHeightSpec(context)
        } else {
            measureUnspecifiedView(this)
            mLayoutParam.width = measuredWidth
            mLayoutParam.height = ViewGroup.LayoutParams.WRAP_CONTENT
            widthSpec =
                MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY)
            heightSpec = generateWrapHeightSpec(context)
        }

        measure(widthSpec, heightSpec)
        val middle = (mTargetRect.top + mTargetRect.bottom) / 2
        val x: Int = mTargetRect.left - measuredWidth
        val y: Int = (middle - measuredHeight * 0.5).toInt()

        showAtLocation(x, y)
    }

    private fun showTop() {
        if (mIsWidthMatchParent) {
            mLayoutParam.width = ViewGroup.LayoutParams.MATCH_PARENT
            mLayoutParam.height = ViewGroup.LayoutParams.WRAP_CONTENT
            val widthSpec = generateMatchWidthSpec(context)
            val heightSpec =
                MeasureSpec.makeMeasureSpec(mTargetRect.top, MeasureSpec.AT_MOST)
            measure(widthSpec, heightSpec)
            val x = 0
            val y = mTargetRect.top - measuredHeight
            showAtLocation(x, y)
            return
        }

        measureUnspecifiedView(this)
        val widthSpec: Int
        val heightSpec: Int
        mLayoutParam.width = if (measuredWidth >= screenWidth) {
            widthSpec = generateMatchWidthSpec(context)
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            widthSpec = generateWrapWidthSpec(context)
            ViewGroup.LayoutParams.WRAP_CONTENT
        }

        mLayoutParam.height = if (measuredHeight > screenHeight) {
            heightSpec = generateMatchHeightSpec(context)
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            heightSpec = generateWrapHeightSpec(context)
            ViewGroup.LayoutParams.WRAP_CONTENT
        }

        measure(widthSpec, heightSpec)
        val middle = (mTargetRect.left + mTargetRect.right) / 2
        val x: Int = (middle - measuredWidth * 0.5).toInt()
        val y: Int = mTargetRect.top - measuredHeight

        showAtLocation(x, y)
    }

    private fun showRight() {
        val widthSpec: Int
        val heightSpec: Int
        val maxWidth = screenWidth - mTargetRect.right

        if (isWidthMatchSpace(mContentView)) {
            mLayoutParam.width = maxWidth
            mLayoutParam.height = ViewGroup.LayoutParams.WRAP_CONTENT
            widthSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY)
            heightSpec = generateWrapHeightSpec(context)
        } else {
            measureUnspecifiedView(this)
            mLayoutParam.width = measuredWidth
            mLayoutParam.height = ViewGroup.LayoutParams.WRAP_CONTENT
            widthSpec =
                MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY)
            heightSpec = generateWrapHeightSpec(context)
        }

        layoutParams = generateMatchWidthLayout()
        measure(widthSpec, heightSpec)
        val middle = (mTargetRect.top + mTargetRect.bottom) / 2

        val x: Int = mTargetRect.right
        val y: Int = (middle - measuredHeight * 0.5).toInt()

        showAtLocation(x, y)
    }

    private fun showBottom() {
        if (mIsWidthMatchParent) {
            mLayoutParam.width = ViewGroup.LayoutParams.MATCH_PARENT
            mLayoutParam.height = ViewGroup.LayoutParams.WRAP_CONTENT
            val x = 0
            val y = mTargetRect.bottom
            showAtLocation(x, y)
            return
        }

        measureUnspecifiedView(this)
        val widthSpec: Int
        val heightSpec: Int
        mLayoutParam.width = if (measuredWidth > screenWidth) {
            widthSpec = generateMatchWidthSpec(context)
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            widthSpec = generateWrapWidthSpec(context)
            ViewGroup.LayoutParams.WRAP_CONTENT
        }

        mLayoutParam.height = if (measuredHeight > screenHeight) {
            heightSpec = generateMatchHeightSpec(context)
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            heightSpec = generateWrapHeightSpec(context)
            ViewGroup.LayoutParams.WRAP_CONTENT
        }

        measure(widthSpec, heightSpec)
        val middle = (mTargetRect.left + mTargetRect.right) / 2
        val x: Int = (middle - measuredWidth * 0.5).toInt()
        val y: Int = mTargetRect.bottom

        showAtLocation(x, y)
    }

    private fun showAtLocation(x: Int, y: Int) {
        val finalX = resetX(x)
        val finalY = resetY(y)
        setWindowLocation(finalX, finalY)
        mLayoutParam.x = finalX
        mLayoutParam.y = finalY
        setScaleCenter()
        mWindowManager.addView(this, mLayoutParam)
        post {
            if (mAnimationType == AnimationType.REVEAL) {
                mContentView.visibility = INVISIBLE
            }

            setRevealAnimator()
            mStartAnimator.start()

            if (mAnimationType == AnimationType.REVEAL) {
                mContentView.visibility = VISIBLE
            }
        }
    }

    private fun resetX(x: Int): Int {
        var result = Math.max(0, x)
        if (result + measuredWidth > screenWidth) {
            result = screenWidth - measuredWidth
        }
        return result
    }

    private fun resetY(y: Int): Int {
        var result = Math.max(0, y)
        if (result + measuredHeight > screenHeight) {
            result = screenHeight - measuredHeight
        }
        return result
    }

    private fun setScaleCenter() {
        if (mAnimationType == AnimationType.SCALE) {
            getBubblePoints()?.let {
                mContentView.pivotX = it[1].x - mPadding
                mContentView.pivotY = it[1].y - mPadding
            }
        }
    }

    @SuppressLint("NewApi")
    private fun setRevealAnimator() {
        if (mAnimationType == AnimationType.REVEAL) {
            val pointF =
                getBubblePoints()?.get(1) ?: PointF(measuredWidth / 2f, measuredHeight / 2f)
            val maxRadius =
                Math.hypot(measuredWidth.toDouble(), measuredHeight.toDouble()).toFloat()
            mStartAnimator = ViewAnimationUtils.createCircularReveal(
                mContentView,
                pointF.x.toInt(),
                pointF.y.toInt(),
                0F,
                maxRadius
            )

            mEndAnimator = ViewAnimationUtils.createCircularReveal(
                mContentView,
                pointF.x.toInt(),
                pointF.y.toInt(),
                maxRadius,
                0F
            )
            mEndAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    Log.e("test", "animator end")
                    mWindowManager.removeView(this@ToolTipView)
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

            })
        }
    }

    override fun dismiss() {
        if (isAttachedToWindow) {
            isClickable = false
            mEndAnimator.start()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawBubble(canvas)
    }

    private fun drawBubble(canvas: Canvas?) {
        val list = getBubblePoints()
        list?.let { drawBubble(canvas, it[0], it[1], it[2]) }
    }

    private fun getBubblePoints(): List<PointF>? {
        updateArrowLocation()
        return when (mGravity) {
            TipGravity.RIGHT -> {
                val a = PointF()
                val b = PointF()
                val c = PointF()

                val arrowMiddle = mArrowLocation
                a.x = mPadding.toFloat()
                a.y = arrowMiddle - mArrowWidth / 2.toFloat()
                b.x = mPadding.toFloat() - mArrowHeight
                b.y = arrowMiddle.toFloat()
                c.x = mPadding.toFloat()
                c.y = arrowMiddle + mArrowWidth / 2.toFloat()
                listOf(a, b, c)
            }
            TipGravity.BOTTOM -> {
                val a = PointF()
                val b = PointF()
                val c = PointF()

                val arrowMiddle = mArrowLocation
                a.x = arrowMiddle - mArrowWidth / 2.toFloat()
                a.y = mPadding.toFloat()
                b.x = arrowMiddle.toFloat()
                b.y = (mPadding - mArrowHeight).toFloat()
                c.x = arrowMiddle + mArrowWidth / 2.toFloat()
                c.y = mPadding.toFloat()
                listOf(a, b, c)
            }
            TipGravity.LEFT -> {
                val a = PointF()
                val b = PointF()
                val c = PointF()

                val arrowMiddle = mArrowLocation
                a.x = (measuredWidth - mPadding).toFloat()
                a.y = arrowMiddle - mArrowWidth / 2.toFloat()
                b.x = (measuredWidth - mPadding + mArrowHeight).toFloat()
                b.y = arrowMiddle.toFloat()
                c.x = a.x
                c.y = arrowMiddle + mArrowWidth / 2.toFloat()
                listOf(a, b, c)
            }
            TipGravity.TOP -> {
                val a = PointF()
                val b = PointF()
                val c = PointF()

                val arrowMiddle = mArrowLocation.toFloat()
                a.x = arrowMiddle - mArrowWidth / 2
                a.y = (measuredHeight - mPadding).toFloat()
                b.x = arrowMiddle
                b.y = (measuredHeight - mPadding + mArrowHeight).toFloat()
                c.x = arrowMiddle + mArrowWidth / 2
                c.y = a.y
                listOf(a, b, c)
            }
            else -> null
        }
    }

    private fun drawBubble(canvas: Canvas?, a: PointF, b: PointF, c: PointF) {
        mBubblePath.reset()
        mBubblePath.moveTo(a.x, a.y)
        mBubblePath.lineTo(b.x, b.y)
        mBubblePath.lineTo(c.x, c.y)
        mBubblePath.close()

        canvas?.drawPath(mBubblePath, mBubblePaint)
    }

    private fun updateArrowLocation() {
        val windowRect = Rect(
            mWindowX,
            mWindowY,
            mWindowX + measuredWidth,
            mWindowY + measuredHeight
        )

        val start: Int
        val end: Int

        when (mGravity) {
            TipGravity.TOP, TipGravity.BOTTOM -> {
                start = Math.max(mTargetRect.left, windowRect.left) - mWindowX
                end = Math.min(mTargetRect.right, windowRect.right) - mWindowX
            }
            else -> {
                start = Math.max(mTargetRect.top, windowRect.top) - mWindowY
                end = Math.min(mTargetRect.bottom, windowRect.bottom) - mWindowY
            }
        }
        mArrowLocation = (start + end) / 2
    }
}
