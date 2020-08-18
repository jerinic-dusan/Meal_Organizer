package app.meal_planner.presentation.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import app.meal_planner.R
import app.meal_planner.data.models.RemainingData
import app.meal_planner.data.models.UserData
import app.meal_planner.presentation.contract.UserDataContract
import app.meal_planner.presentation.viewmodel.UserDataViewModel
import app.meal_planner.utilities.CalendarUtils
import kotlinx.android.synthetic.main.fragment_progress_today.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class ProgressTodayFragment: Fragment(R.layout.fragment_progress_today), CalendarUtils {

    private val userDataViewModel: UserDataContract.ViewModel by sharedViewModel<UserDataViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        initFields()
        initObservers()
        initListeners()
    }

    private fun initListeners() {
        progress_today.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { return }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val data = userDataViewModel.dailyData.value
                val totalData = userDataViewModel.exists.value
                setProgressValueBySpinner(data!!, totalData!!)
            }
        }
    }

    private fun initObservers() {
        userDataViewModel.getData()
        userDataViewModel.dailyData.observe(viewLifecycleOwner, Observer {
            setProgressValueBySpinner(it, userDataViewModel.exists.value!!)
        })

    }

    private fun initFields() {
        val adapterProgressToday = ArrayAdapter(requireContext(), R.layout.spinner_item, arrayOf("Calories", "Carbohydrates", "Protein", "Fat"))
        adapterProgressToday.setDropDownViewResource(R.layout.spinner_item)
        progress_today.adapter = adapterProgressToday
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setProgressValueBySpinner(data: RemainingData, totalData: UserData) {
        val df = DecimalFormat("#")
        val dateFormatter = SimpleDateFormat("EEE MMM yyyy")
        val dateFormatter2 = SimpleDateFormat("hh:mm aaa")
        value_date_t.text = dateFormatter.format(calculateDate(0))
        time_now.text = dateFormatter2.format(calculateDate(0))
        df.roundingMode = RoundingMode.HALF_EVEN

        when(progress_today.selectedItem){
            "Calories" -> {
                val increase = df.format((data.calories / totalData.calories.toFloat()) * 100).toInt()
                textView2.text = "$increase%"
                max_value_t.text = data.calories.toString() + "kcal"
                min_value_t.text = totalData.calories.toString() + "kcal"
                resetProgressValue()
                setProgressValue(data.calories, totalData.calories)
            }
            "Carbohydrates" -> {
                textView2.text = df.format((data.carbohydrates / totalData.carbohydrates.toFloat()) * 100).toString() + "%"
                max_value_t.text = data.carbohydrates.toString() + "g"
                min_value_t.text = totalData.carbohydrates.toString() + "g"
                resetProgressValue()
                setProgressValue(data.carbohydrates, totalData.carbohydrates)
            }
            "Protein" -> {
                textView2.text = df.format((data.protein / totalData.protein.toFloat()) * 100).toString() + "%"
                max_value_t.text = data.protein.toString() + "g"
                min_value_t.text = totalData.protein.toString() + "g"
                resetProgressValue()
                setProgressValue(data.protein, totalData.protein)
            }
            "Fat" -> {
                textView2.text = df.format((data.fat / totalData.fat.toFloat()) * 100).toString() + "%"
                max_value_t.text = data.fat.toString() + "g"
                min_value_t.text = totalData.fat.toString() + "g"
                resetProgressValue()
                setProgressValue(data.fat, totalData.fat)
            }
        }
    }

    private fun resetProgressValue() {
        circularProgressBar.apply {
            progressMax = 0.0F
            progress = 0.0F
        }
    }

    private fun setProgressValue(value: Int, total: Int) {
        circularProgressBar.apply {
            progressMax = total.toFloat()
            progress = 0.0F
            setProgressWithAnimation(value.toFloat(), 1000)
        }
    }
}