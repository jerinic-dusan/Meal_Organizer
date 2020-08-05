package app.meal_planner.presentation.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import app.meal_planner.presentation.view.fragments.ExistingMealsFragment
import app.meal_planner.presentation.view.fragments.ProfileFragment
import app.meal_planner.presentation.view.fragments.ProgressFragment
import app.meal_planner.presentation.view.fragments.TodaysMealsFragment

class PagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)  {

    companion object{
        private const val ITEM_COUNT = 4
        const val FRAGMENT_1 = 0
        const val FRAGMENT_2 = 1
        const val FRAGMENT_3 = 2
        const val FRAGMENT_4 = 3
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            FRAGMENT_1 -> TodaysMealsFragment()
            FRAGMENT_2 -> ExistingMealsFragment()
            FRAGMENT_3 -> ProgressFragment()
            else -> ProfileFragment()
        }
    }

    override fun getCount(): Int {
        return ITEM_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            FRAGMENT_1 -> "Today's Meals"
            FRAGMENT_2 -> "Existing Meals"
            FRAGMENT_3 -> "Your Progress"
            else -> "Profile & Calorie Calculator"
        }
    }

}