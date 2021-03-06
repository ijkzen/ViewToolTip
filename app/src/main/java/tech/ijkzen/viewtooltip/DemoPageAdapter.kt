package tech.ijkzen.viewtooltip

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DemoPageAdapter :
    FragmentStateAdapter {

    constructor(fragment: Fragment) : super(fragment)

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return DemoFragment()
    }

}