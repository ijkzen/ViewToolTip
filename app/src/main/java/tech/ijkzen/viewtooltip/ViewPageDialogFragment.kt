package tech.ijkzen.viewtooltip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tech.ijkzen.viewtooltip.databinding.ActivityViewPagerBinding

class ViewPageDialogFragment : DemoDialogFragment() {

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