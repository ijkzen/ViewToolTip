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
        } else {
            val fragment = ParentFragment()
            transaction.add(R.id.content, fragment, ParentFragment::class.java.name)
        }
        transaction.commit()
    }
}