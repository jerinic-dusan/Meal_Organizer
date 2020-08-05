package app.meal_planner.presentation.view.activities

import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.meal_planner.R
import app.meal_planner.presentation.view.adapters.PagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init(){
        initMainFragment()
    }

    private fun initMainFragment() {
        mainFragment.adapter = PagerAdapter(supportFragmentManager)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_1 -> { mainFragment.setCurrentItem(PagerAdapter.FRAGMENT_1, false) }
                R.id.nav_2 -> { mainFragment.setCurrentItem(PagerAdapter.FRAGMENT_2, false) }
                R.id.nav_3 -> { mainFragment.setCurrentItem(PagerAdapter.FRAGMENT_3, false) }
                R.id.nav_4 -> { mainFragment.setCurrentItem(PagerAdapter.FRAGMENT_4, false) }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}
