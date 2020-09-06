package tech.ijkzen.viewtooltip

import android.content.res.TypedArray
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.View.combineMeasuredStates
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.github.ijkzen.*
import tech.ijkzen.viewtooltip.databinding.DialogShowBinding
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    private val list = arrayListOf<TipGravity>(
        TipGravity.LEFT,
        TipGravity.TOP,
        TipGravity.RIGHT,
        TipGravity.BOTTOM
    )
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text = findViewById<TextView>(R.id.text)

        val binding = DialogShowBinding.inflate(LayoutInflater.from(this), null, false)
//        setViewMatchScreen(binding.root)

        val tip = ViewToolTip.on(this, text)
            .customView(binding.root)
            .isAllowHideByClickMask(false)
            .gravity(TipGravity.TOP)

        text.setOnClickListener {
            tip.gravity(list[count++ % 4])
            tip.show()
        }

        val styledAttributes: TypedArray =
            theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val mActionBarSize = styledAttributes.getDimension(0, 0f).toInt()
        styledAttributes.recycle()
    }
}