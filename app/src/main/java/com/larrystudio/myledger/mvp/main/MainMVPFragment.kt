package com.larrystudio.myledger.mvp.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.larrystudio.myledger.R
import com.larrystudio.myledger.mvp.BaseMVPFragment
import com.larrystudio.myledger.mvp.main.day.DayLedgerMVPFragment
import com.larrystudio.myledger.mvp.main.monthyear.MonthYearLedgerMVPFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainMVPFragment: BaseMVPFragment() {

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
            return when(position){
                0 -> DayLedgerMVPFragment()
                1 -> MonthYearLedgerMVPFragment.getInstance(MonthYearLedgerMVPFragment.TYPE_MONTH)
                else -> MonthYearLedgerMVPFragment.getInstance(MonthYearLedgerMVPFragment.TYPE_YEAR)
            }
        }
    }
}