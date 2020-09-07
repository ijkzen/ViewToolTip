package tech.ijkzen.viewtooltip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tech.ijkzen.viewtooltip.databinding.ActivityFragmentBinding

class FragmentActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFragmentBinding.inflate(layoutInflater, null, false)
        setContentView(mBinding.root)

        val action = intent.action
        val transaction = supportFragmentManager.beginTransaction()

        if ("fragment" == action) {
            val fragment = DemoFragment()
            transaction.add(R.id.content, fragment, DemoFragment::class.java.name)
        } else if ("child_fragment" == action) {
            val fragment = ParentFragment()
            transaction.add(R.id.content, fragment, ParentFragment::class.java.name)
        } else if ("fragment_view_pager" == action) {
            val fragment = ViewPagerFragment()
            transaction.add(R.id.content, fragment, ParentFragment::class.java.name)
        }
        transaction.commit()
    }
}