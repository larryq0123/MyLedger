package com.larrystudio.myledger.mvp.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.larry.larrylibrary.extension.hideLoading
import com.larry.larrylibrary.extension.showLoading
import com.larrystudio.myledger.R
import com.larrystudio.myledger.mvp.BaseFragment
import com.larrystudio.myledger.mvp.main.day.DayLedgerFragment
import com.larrystudio.myledger.util.LogUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.concurrent.TimeUnit

class MainFragment: BaseFragment() {

    companion object{
        private const val ARG_OBJECT = "args"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager.adapter = TempPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position){
                0 -> tab.text = "日"
                1 -> tab.text = "月"
                2 -> tab.text = "年"
            }
        }.attach()
    }

    class TempPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment){
        override fun getItemCount(): Int { return 3 }
        override fun createFragment(position: Int): Fragment {
            return if(position == 0){
                DayLedgerFragment()
            }else{
                val fragment = TempFragment()
                val args = Bundle().apply { putInt(ARG_OBJECT, position+1) }
                fragment.arguments = args
                fragment
            }
        }
    }

    class TempFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            return inflater.inflate(R.layout.fragment_temp, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
                val textTemp: TextView = view.findViewById(R.id.textTemp)
                textTemp.text = getInt(ARG_OBJECT).toString()
            }
        }
    }
}