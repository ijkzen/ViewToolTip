package com.github.ijkzen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView

@SuppressLint("ViewConstructor")
open class ToolTipView : FrameLayout, ToolTipViewConfiguration {
    private var targetViewRect: Rect = Rect()
    private val mBubblePaint = Paint()
    private val mBubblePath = Path()
    private var mContentView: View = AppCompatTextView(context)
    private var mGravity: TipGravity = TipGravity.BOTTOM
    private val padding = convertDp2Px(10, context)
    private var arrowWidth = (padding * 1.2).toInt()
    private var arrowHeight = (padding * 0.8).toInt()
    private var arrowLocation: Int = 0
    private var mBackground = GradientDrawable()
    private var mWindowX: Int = 0
    private var mWindowY: Int = 0
    private var start = 0
    private var end = 0
    private val mLocationPaint = Paint()

    constructor(context: Context) : super(context)

    init {
        setWillNotDraw(false)
        setPadding(padding, padding, padding, padding)
        mContentView.setPadding(padding, padding, padding, padding)
        addView(mContentView)
        mBackground.cornerRadius = convertDp2Px(10, context).toFloat()
        mBackground.setColor(Color.WHITE)
        mContentView.background = mBackground

        mBubblePaint.color = Color.WHITE
        mBubblePaint.isAntiAlias = true
        mBubblePaint.style = Paint.Style.FILL
        mBubblePath.fillType = Path.FillType.EVEN_ODD

        mLocationPaint.color = Color.RED
        mLocationPaint.strokeWidth = 16F
    }

    fun setTargetRect(targetRect: Rect) {
        targetViewRect = targetRect
    }

    fun setWindowLocation(x: Int, y: Int) {
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
        arrowWidth = width
    }

    override fun arrowHeight(height: Int) {
        arrowHeight = height
    }

    override fun arrowColor(color: Int) {
        mBubblePaint.color = color
    }

    override fun background(background: GradientDrawable) {
        mBackground = background
        mContentView.background = mBackground
    }

    override fun backgroundColor(color: Int) {
        mBackground.setColor(color)
        mContentView.background = mBackground
    }

    override fun backgroundRadius(radius: Int) {
        mBackground.cornerRadius = radius.toFloat()
        mContentView.background = mBackground
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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawBubble(canvas)
    }

    private fun drawBubble(canvas: Canvas?) {
        when (mGravity) {
            TipGravity.LEFT -> {
                drawLeftBubble(canvas)
            }
            TipGravity.TOP -> {
                drawTopBubble(canvas)
            }
            TipGravity.RIGHT -> {
                drawRightBubble(canvas)
            }
            TipGravity.BOTTOM -> {
                drawBottomBubble(canvas)
            }
            else -> {
            }
        }
    }

    private fun drawLeftBubble(canvas: Canvas?) {
        updateArrowLocation()
        val a = PointF()
        val b = PointF()
        val c = PointF()

        val arrowMiddle = arrowLocation
        a.x = padding.toFloat()
        a.y = arrowMiddle - arrowWidth / 2.toFloat()
        b.x = 0F
        b.y = arrowMiddle.toFloat()
        c.x = padding.toFloat()
        c.y = arrowMiddle + arrowWidth / 2.toFloat()

        drawBubble(canvas, a, b, c)
    }

    private fun drawTopBubble(canvas: Canvas?) {
        updateArrowLocation()

        val a = PointF()
        val b = PointF()
        val c = PointF()

        val arrowMiddle = arrowLocation
        a.x = arrowMiddle - arrowWidth / 2.toFloat()
        a.y = padding.toFloat()
        b.x = arrowMiddle.toFloat()
        b.y = (padding - arrowHeight).toFloat()
        c.x = arrowMiddle + arrowHeight / 2.toFloat()
        c.y = padding.toFloat()

        drawBubble(canvas, a, b, c)
    }

    private fun drawRightBubble(canvas: Canvas?) {
        updateArrowLocation()

        val a = PointF()
        val b = PointF()
        val c = PointF()

        val arrowMiddle = arrowLocation
        a.x = (width - padding).toFloat()
        a.y = arrowMiddle - arrowWidth / 2.toFloat()
        b.x = (width - padding + arrowHeight).toFloat()
        b.y = arrowMiddle.toFloat()
        c.x = a.x
        c.y = arrowMiddle + arrowWidth / 2.toFloat()

        drawBubble(canvas, a, b, c)
    }

    private fun drawBottomBubble(canvas: Canvas?) {
        updateArrowLocation()

        val a = PointF()
        val b = PointF()
        val c = PointF()

        val arrowMiddle = arrowLocation.toFloat()
        a.x = arrowMiddle - arrowWidth / 2
        a.y = (height - padding).toFloat()
        b.x = arrowMiddle
        b.y = (height - padding + arrowHeight).toFloat()
        c.x = arrowMiddle + arrowWidth / 2
        c.y = a.y


        drawBubble(canvas, a, b, c)
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
        updateLocationRange()
        setValidArrowLocation()
    }

    fun getArrowLocation(): Float {
        return 0.5F
    }

    private fun updateLocationRange() {
        val windowRect = Rect(
            mWindowX,
            mWindowY,
            mWindowX + width,
            mWindowY + height
        )
        when (mGravity) {
            TipGravity.TOP, TipGravity.BOTTOM -> {
                start = Math.max(targetViewRect.left, windowRect.left) - mWindowX
                end = Math.min(targetViewRect.right, windowRect.right) - mWindowX
            }
            else -> {
                start = Math.max(targetViewRect.top, windowRect.top) - mWindowY
                end = Math.min(targetViewRect.bottom, windowRect.bottom) - mWindowY
            }
        }
    }

    private fun setValidArrowLocation() {
        arrowLocation = (start + end) / 2
    }
}