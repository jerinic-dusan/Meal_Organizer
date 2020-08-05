package app.meal_planner.presentation.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.meal_planner.R
import app.meal_planner.presentation.view.adapters.TabAdapter
import kotlinx.android.synthetic.main.fragment_progress.*

class ProgressFragment : Fragment(R.layout.fragment_progress) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initInnerFragment()
    }

    private fun initInnerFragment() {
        viewPager.adapter = TabAdapter(childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_week)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_today)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_month)
        tabLayout.getTabAt(1)?.select()
    }
}