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
import app.meal_planner.data.models.MealWithItems
import app.meal_planner.presentation.contract.DailyReportContract
import app.meal_planner.presentation.view.states.DailyReportState
import app.meal_planner.presentation.view.states.MealState
import app.meal_planner.presentation.viewmodel.DailyReportViewModel
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.fragment_progress_today.*
import kotlinx.android.synthetic.main.fragment_progress_week.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ProgressWeekFragment: Fragment(R.layout.fragment_progress_week) {

    private val dailyReportViewModel: DailyReportContract.ViewModel by sharedViewModel<DailyReportViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initFields()
        initObservers()
    }

    private fun initListeners(days: List<DailyReport>) {
        progress_week.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { return }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setupChart(days)
            }
        }
    }

    private fun initObservers() {
        dailyReportViewModel.lastSevenDays.observe(viewLifecycleOwner, Observer {
            renderState(it)
        })
        dailyReportViewModel.getLastSevenDays()
    }

    private fun renderState(state: DailyReportState) {
        when(state){
            is DailyReportState.Success -> {
                val days = state.lastXDays
                setupChart(days)
                initListeners(days)
                Timber.e("Success $days")
            }
            is DailyReportState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is DailyReportState.Loading -> {
                Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initFields() {
        val adapterProgressWeek = ArrayAdapter(requireContext(), R.layout.spinner_item, arrayOf("Calories", "Carbohydrates", "Protein", "Fat"))
        adapterProgressWeek.setDropDownViewResource(R.layout.spinner_item)
        progress_week.adapter = adapterProgressWeek
    }

    private fun setupChart(days: List<DailyReport>){
        when(progress_week.selectedItem){
            "Calories" -> {
                weekly_stats.type = "Calories"
                weekly_stats.values = prepareData(days.map{it.kcal})
                fillFields(days.map{it.kcal}, days.map{it.date}, true)
//                weekly_stats.values = prepareData(listOf(10,5,2,3,7,8))
            }
            "Carbohydrates" -> {
                weekly_stats.type = "Carbohydrates"
                weekly_stats.values = prepareData(days.map{it.carbs})
                fillFields(days.map{it.carbs}, days.map{it.date})
            }
            "Protein" -> {
                weekly_stats.type = "Protein"
                weekly_stats.values = prepareData(days.map{it.protein})
                fillFields(days.map{it.protein}, days.map{it.date})
            }
            "Fat" -> {
                weekly_stats.type = "Fat"
                weekly_stats.values = prepareData(days.map{it.fat})
                fillFields(days.map{it.fat}, days.map{it.date})
            }
        }
        weekly_stats.invalidate()
    }

    private fun prepareData(input: List<Int>): List<Int>{
        if(input.size == 7) { return input }
        val list = mutableListOf(0,0,0,0,0,0,0)
        if(input.size < 7){
            input.forEachIndexed { index, i ->
                list[index] = i
            }
        }else{
            for(i in 0..6){
                list[i] = input[i]
            }
        }
        return list.toList().subList(0,7)
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
            max_value.text = max.toString() + "kcal"
            min_value.text = min.toString() + "kcal"
        }else{
            max_value.text = max.toString() + "g"
            min_value.text = min.toString() + "g"
        }
        max_value_date.text = dateFormatter.format(maxDate)
        min_value_date.text = dateFormatter.format(minDate)
    }
}