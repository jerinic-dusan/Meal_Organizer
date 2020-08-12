package app.meal_planner.presentation.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import app.meal_planner.R
import kotlinx.android.synthetic.main.fragment_progress_week.*

class ProgressWeekFragment: Fragment(R.layout.fragment_progress_week) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initFields()
    }

    private fun initFields() {
        val adapterProgressWeek = ArrayAdapter(requireContext(), R.layout.spinner_item, arrayOf("Calories", "Carbohydrates", "Protein", "Fat"))
        adapterProgressWeek.setDropDownViewResource(R.layout.spinner_item)
        progress_week.adapter = adapterProgressWeek
    }
}