package tech.ijkzen.viewtooltip

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.widget.RelativeLayout

class CustomRelativeLayout : RelativeLayout {
    private val paint = Paint()

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)

}