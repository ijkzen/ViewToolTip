package tech.ijkzen.viewtooltip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.ijkzen.control.TipGravity
import com.github.ijkzen.control.ViewToolTip
import tech.ijkzen.viewtooltip.databinding.ActivityMainBinding
import tech.ijkzen.viewtooltip.databinding.DialogShowBinding

class DemoFragment : Fragment() {
    private lateinit var mBinding: ActivityMainBinding

    private val list = arrayListOf<TipGravity>(
        TipGravity.LEFT,
        TipGravity.TOP,
        TipGravity.RIGHT,
        TipGravity.BOTTOM
    )
    private var count = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = ActivityMainBinding.inflate(inflater, container, false)
        mBinding = ActivityMainBinding.inflate(inflater, container, false)

        val binding1 = DialogShowBinding.inflate(layoutInflater, null, false)
        val binding2 = DialogShowBinding.inflate(inflater, null, false)
        val binding3 = DialogShowBinding.inflate(inflater, null, false)
        val binding4 = DialogShowBinding.inflate(inflater, null, false)
        val binding5 = DialogShowBinding.inflate(inflater, null, false)
        val binding6 = DialogShowBinding.inflate(inflater, null, false)
        val binding7 = DialogShowBinding.inflate(inflater, null, false)
        val binding8 = DialogShowBinding.inflate(inflater, null, false)
        val binding9 = DialogShowBinding.inflate(inflater, null, false)

        val tip1 = ViewToolTip.on(mBinding.text1)
            .customView(binding1.root)
            .gravity(TipGravity.BOTTOM)

        mBinding.text1.setOnClickListener {
            tip1.gravity(list[count++ % 4])
            tip1.show()
        }

        val tip2 = ViewToolTip.on(mBinding.text2)
            .customView(binding2.root)
            .isAllowHideByClickMask(false)
            .gravity(TipGravity.BOTTOM)

        mBinding.text2.setOnClickListener {
            tip2.gravity(list[count++ % 4])
            tip2.show()
        }

        val tip3 = ViewToolTip.on(mBinding.text3)
            .customView(binding3.root)
            .gravity(TipGravity.BOTTOM)

        mBinding.text3.setOnClickListener {
            tip3.gravity(list[count++ % 4])
            tip3.show()
        }

        val tip4 = ViewToolTip.on(mBinding.text4)
            .customView(binding4.root)
            .gravity(TipGravity.BOTTOM)

        mBinding.text4.setOnClickListener {
            tip4.gravity(list[count++ % 4])
            tip4.show()
        }

        val tip5 = ViewToolTip.on(mBinding.text5)
            .customView(binding5.root)
            .gravity(TipGravity.TOP)

        mBinding.text5.setOnClickListener {
            tip5.gravity(list[count++ % 4])
            tip5.show()
        }

        val tip6 = ViewToolTip.on(mBinding.text6)
            .customView(binding6.root)
            .gravity(TipGravity.BOTTOM)

        mBinding.text6.setOnClickListener {
            tip6.gravity(list[count++ % 4])
            tip6.show()
        }

        val tip7 = ViewToolTip.on(mBinding.text7)
            .customView(binding7.root)
            .gravity(TipGravity.BOTTOM)

        mBinding.text7.setOnClickListener {
            tip7.gravity(list[count++ % 4])
            tip7.show()
        }

        val tip8 = ViewToolTip.on(mBinding.text8)
            .customView(binding8.root)
            .gravity(TipGravity.BOTTOM)

        mBinding.text8.setOnClickListener {
            tip8.gravity(list[count++ % 4])
            tip8.show()
        }

        val tip9 = ViewToolTip.on(mBinding.text9)
            .customView(binding9.root)
            .gravity(TipGravity.BOTTOM)

        mBinding.text9.setOnClickListener {
            tip9.gravity(list[count++ % 4])
            tip9.show()
        }

        return mBinding.root

    }
}