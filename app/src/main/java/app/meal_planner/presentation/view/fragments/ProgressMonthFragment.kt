package app.meal_planner.presentation.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import app.meal_planner.R
import kotlinx.android.synthetic.main.fragment_progress_month.*

class ProgressMonthFragment: Fragment(R.layout.fragment_progress_month) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initFields()
    }

    private fun initFields() {
        val adapterProgressMonth = ArrayAdapter(requireContext(), R.layout.spinner_item, arrayOf("Calories", "Carbohydrates", "Protein", "Fat"))
        adapterProgressMonth.setDropDownViewResource(R.layout.spinner_item)
        progress_month.adapter = adapterProgressMonth
    }
}