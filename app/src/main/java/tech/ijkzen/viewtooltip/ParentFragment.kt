package tech.ijkzen.viewtooltip

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tech.ijkzen.viewtooltip.databinding.ActivityFragmentBinding

class ParentFragment : Fragment() {

    private lateinit var mBinding: ActivityFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = ActivityFragmentBinding.inflate(inflater, container, false)

        val transaction = childFragmentManager.beginTransaction()
        val fragment = DemoFragment()
        transaction.add(R.id.content, fragment, DemoFragment::class.java.name)
        transaction.commit()
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        Log.e("test", "on parentFragment start")
    }
}