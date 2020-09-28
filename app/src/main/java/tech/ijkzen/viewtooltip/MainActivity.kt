package tech.ijkzen.viewtooltip

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.ijkzen.AnimationType
import com.github.ijkzen.TipGravity
import com.github.ijkzen.ViewToolTip
import com.github.ijkzen.convertDp2Px
import tech.ijkzen.viewtooltip.databinding.ActivityMainBinding
import tech.ijkzen.viewtooltip.databinding.DialogShowBinding


class MainActivity : AppCompatActivity() {

    private val list = arrayListOf<TipGravity>(
        TipGravity.LEFT,
        TipGravity.TOP,
        TipGravity.RIGHT,
        TipGravity.BOTTOM
    )
    private var count = 0

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val binding1 = DialogShowBinding.inflate(LayoutInflater.from(this), null, false)
        val binding2 = DialogShowBinding.inflate(LayoutInflater.from(this), null, false)
        val binding3 = DialogShowBinding.inflate(LayoutInflater.from(this), null, false)
        val binding4 = DialogShowBinding.inflate(LayoutInflater.from(this), null, false)
        val binding5 = DialogShowBinding.inflate(LayoutInflater.from(this), null, false)
        val binding6 = DialogShowBinding.inflate(LayoutInflater.from(this), null, false)
        val binding7 = DialogShowBinding.inflate(LayoutInflater.from(this), null, false)
        val binding8 = DialogShowBinding.inflate(LayoutInflater.from(this), null, false)
        val binding9 = DialogShowBinding.inflate(LayoutInflater.from(this), mBinding.root, false)

        val tip1 = ViewToolTip.on(mBinding.text1)
            .customView(binding1.root)
            .gravity(TipGravity.BOTTOM)

        mBinding.text1.setOnClickListener {
            tip1.gravity(list[count++ % 4])
            tip1.show()
        }

        val tip2 = ViewToolTip.on(mBinding.text2)
            .customView(binding2.root)
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
//            .text("Are you Ok?")
            .customView(binding5.root)
            .arrowHeight(convertDp2Px(5, this))
            .animation(AnimationType.REVEAL)
            .gravity(TipGravity.LEFT)

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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.more, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.dialog) {
            DemoDialogFragment().show(supportFragmentManager, DemoDialogFragment::class.java.name)
        } else if (item.itemId == R.id.fragment) {
            val intent = Intent(this, FragmentActivity::class.java)
            intent.action = "fragment"
            startActivity(intent)
        } else if (item.itemId == R.id.child_fragment) {
            val intent = Intent(this, FragmentActivity::class.java)
            intent.action = "child_fragment"
            startActivity(intent)
        } else if (item.itemId == R.id.view_page_activity) {
            startActivity(Intent(this, ViewPagerActivity::class.java))
        } else if (item.itemId == R.id.view_page_fragment) {
            val intent = Intent(this, FragmentActivity::class.java)
            intent.action = "fragment_view_pager"
            startActivity(intent)
        } else if (item.itemId == R.id.view_page_dialogfragmeng) {
            ViewPageDialogFragment().show(
                supportFragmentManager,
                ViewPageDialogFragment::class.java.name
            )
        }

        return super.onOptionsItemSelected(item)
    }
}