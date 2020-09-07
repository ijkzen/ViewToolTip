package tech.ijkzen.viewtooltip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tech.ijkzen.viewtooltip.databinding.ActivityViewPagerBinding

class ViewPagerFragment : Fragment() {

    private lateinit var mBinding: ActivityViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = ActivityViewPagerBinding.inflate(inflater, container, false)
        mBinding.page.adapter = DemoPageAdapter(this)
        return mBinding.root
    }
}