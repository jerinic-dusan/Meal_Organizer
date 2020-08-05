package app.meal_planner.presentation.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import app.meal_planner.presentation.view.fragments.ProgressMonthFragment
import app.meal_planner.presentation.view.fragments.ProgressTodayFragment
import app.meal_planner.presentation.view.fragments.ProgressWeekFragment

class TabAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object{
        private const val ITEM_COUNT = 3
        const val FRAGMENT_1 = 0
        const val FRAGMENT_2 = 1
        const val FRAGMENT_3 = 2
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            FRAGMENT_1 -> ProgressWeekFragment()
            FRAGMENT_2 -> ProgressTodayFragment()
            else -> ProgressMonthFragment()
        }
    }

    override fun getCount(): Int {
        return ITEM_COUNT
    }


}