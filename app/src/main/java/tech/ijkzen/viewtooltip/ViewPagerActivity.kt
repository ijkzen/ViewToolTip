package tech.ijkzen.viewtooltip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import tech.ijkzen.viewtooltip.databinding.ActivityViewPagerBinding

class ViewPagerActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityViewPagerBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(mBinding.root)
        mBinding.page.adapter = DemoPageAdapter(this)
    }
}