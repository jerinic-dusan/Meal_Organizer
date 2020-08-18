package app.meal_planner.presentation.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import app.meal_planner.R
import app.meal_planner.data.models.DailyReport
import app.meal_planner.presentation.contract.DailyReportContract
import app.meal_planner.presentation.view.states.DailyReportState
import app.meal_planner.presentation.viewmodel.DailyReportViewModel
import kotlinx.android.synthetic.main.fragment_progress_month.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ProgressMonthFragment: Fragment(R.layout.fragment_progress_month) {

    private val dailyReportViewModel: DailyReportContract.ViewModel by sharedViewModel<DailyReportViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initFields()
        initObservers()
    }

    private fun initObservers() {
        dailyReportViewModel.lastThirtyDays.observe(viewLifecycleOwner, Observer {
            renderState(it)
        })
        dailyReportViewModel.getLastThirtyDays()
    }

    private fun renderState(state: DailyReportState) {
        when(state){
            is DailyReportState.Success -> {
                val days = state.lastXDays
                setupChart(days)
                initListeners(days)
                Timber.e(" $days")
            }
            is DailyReportState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is DailyReportState.Loading -> {
                Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupChart(days: List<DailyReport>){
        when(progress_month.selectedItem){
            "Calories" -> {
                monthly_stats.type = "Calories"
                monthly_stats.values = prepareData(days.map{it.kcal})
                fillFields(days.map{it.kcal}, days.map{it.date}, true)
//                monthly_stats.values = prepareData(listOf(10,5,2,3,7,8,4,3,9,1,10,5,2,3,7,8,4,3,9,1,10,5,2,3,7,8,4,3,9))
            }
            "Carbohydrates" -> {
                monthly_stats.type = "Carbohydrates"
                monthly_stats.values = prepareData(days.map{it.carbs})
                fillFields(days.map{it.carbs}, days.map{it.date})
            }
            "Protein" -> {
                monthly_stats.type = "Protein"
                monthly_stats.values = prepareData(days.map{it.protein})
                fillFields(days.map{it.protein}, days.map{it.date})
            }
            "Fat" -> {
                monthly_stats.type = "Fat"
                monthly_stats.values = prepareData(days.map{it.fat})
                fillFields(days.map{it.fat}, days.map{it.date})
            }
        }
        monthly_stats.invalidate()
    }

    private fun prepareData(input: List<Int>): List<Int>{
        if(input.size == 30) { return input }
        val list = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        if(input.size < 30){
            input.forEachIndexed { index, i ->
                list[index] = i
            }
        }else{
            for(i in 0..29){
                list[i] = input[i]
            }
        }
        return list.toList().subList(0,30)
    }

    private fun initListeners(days: List<DailyReport>) {
        progress_month.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { return }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setupChart(days)
            }
        }
    }

    private fun initFields() {
        val adapterProgressMonth = ArrayAdapter(requireContext(), R.layout.spinner_item, arrayOf("Calories", "Carbohydrates", "Protein", "Fat"))
        adapterProgressMonth.setDropDownViewResource(R.layout.spinner_item)
        progress_month.adapter = adapterProgressMonth
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun fillFields(input: List<Int>, dates: List<Date>, kcal: Boolean = false){
        var max = 0
        var min = 10000000000
        var maxDate = Date()
        var minDate = Date()
        val dateFormatter = SimpleDateFormat("EEE MMM yyyy")
        input.forEachIndexed { index, i ->
            if(i>max){
                max = i
                maxDate = dates[index]
            }
            if(i < min && i != 0){
                min = i.toLong()
                minDate = dates[index]
            }
        }
        if(kcal){
            max_value_m.text = max.toString() + "kcal"
            min_value_m.text = min.toString() + "kcal"
        }else{
            max_value_m.text = max.toString() + "g"
            min_value_m.text = min.toString() + "g"
        }
        max_value_date_m.text = dateFormatter.format(maxDate)
        min_value_date_m.text = dateFormatter.format(minDate)
    }
}