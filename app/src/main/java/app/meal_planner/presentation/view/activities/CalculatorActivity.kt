package app.meal_planner.presentation.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import app.meal_planner.R
import app.meal_planner.data.models.RawData
import app.meal_planner.data.models.RemainingData
import app.meal_planner.modules.dailyReportModule
import app.meal_planner.presentation.contract.DailyReportContract
import app.meal_planner.presentation.contract.KcalDataContract
import app.meal_planner.presentation.contract.MealsContract
import app.meal_planner.presentation.contract.UserDataContract
import app.meal_planner.presentation.viewmodel.DailyReportViewModel
import app.meal_planner.presentation.viewmodel.KcalDataViewModel
import app.meal_planner.presentation.viewmodel.MealsViewModel
import app.meal_planner.presentation.viewmodel.UserDataViewModel
import kotlinx.android.synthetic.main.activity_calculator.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalculatorActivity : AppCompatActivity(R.layout.activity_calculator) {

    private val kcalDataViewModel: KcalDataContract.ViewModel by viewModel<KcalDataViewModel>()
    private val userDataViewModel: UserDataContract.ViewModel by viewModel<UserDataViewModel>()
    private val mealsViewModel: MealsContract.ViewModel by viewModel<MealsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        initListeners()
        initFields()
    }

    private fun initFields() {
        updateHints()

        val adapterActivity = ArrayAdapter(this, R.layout.spinner_item, arrayOf("No exercise", "3 days a week", "3 - 5 days a week", "5 - 7 days a week"))
        adapterActivity.setDropDownViewResource(R.layout.spinner_item)
        activity.adapter = adapterActivity

        val adapterGoal = ArrayAdapter(this, R.layout.spinner_item, arrayOf("Maintain weight", "Lose weight", "Gain weight"))
        adapterGoal.setDropDownViewResource(R.layout.spinner_item)
        goal.adapter = adapterGoal

        val adapterDiet = ArrayAdapter(this, R.layout.spinner_item, arrayOf("Unsure", "Low fat", "Low carb"))
        adapterDiet.setDropDownViewResource(R.layout.spinner_item)
        diet.adapter = adapterDiet
    }

    private fun initListeners() {
        metric_or_not.setOnClickListener {
            updateHints()
        }
        male_or_female.setOnClickListener {
            updateHints()
        }
        calculate.setOnClickListener {
            if(checkAllFields()){
                var metric = true
                var male = true
                val activity = getActivityLevel()
                val goal = getGoal()
                val diet = getDietType()
                val ageClean = age.text.toString().trim().replace("[^\\d.]", "").toInt()
                val heightClean = height.text.toString().trim().replace("[^\\d.]", "").toDouble()
                val weightClean = weight.text.toString().trim().replace("[^\\d.]", "").toInt()

                if(metric_or_not.isChecked){ metric = false }
                if(male_or_female.isChecked){ male = false }

                val rawData = RawData(metric, male, ageClean, heightClean, weightClean, activity, goal, diet)
                kcalDataViewModel.calculateUserData(rawData)

                userDataViewModel.setExistingData(kcalDataViewModel.userData.value!!)
                val remainingData = RemainingData(kcalDataViewModel.userData.value!!.calories, kcalDataViewModel.userData.value!!.carbohydrates, kcalDataViewModel.userData.value!!.protein, kcalDataViewModel.userData.value!!.fat)
                userDataViewModel.setRemainingData(remainingData)

                //Delete everything
                //mealsViewModel.deleteAllMeals()
//                dailyReportViewModel.deleteAll()
                mealsViewModel.dailyReset()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Please make sure all fields are filled.", Toast.LENGTH_LONG).show()
            }
        }
        cancel.setOnClickListener {
            finish()
        }
    }

    private fun getDietType(): Int {
        return when(diet.selectedItem) {
            "Unsure" -> 1
            "Low fat" -> 2
            "Low carb" -> 3
            else -> 0
        }
    }

    private fun getGoal(): Int {
        return when(goal.selectedItem) {
            "Maintain weight" -> 1
            "Lose weight" -> 2
            "Gain weight" -> 3
            else -> 0
        }
    }

    private fun getActivityLevel(): Int {
        return when(activity.selectedItem){
            "No exercise" -> return 1
            "3 days a week" -> return 2
            "3 - 5 days a week" -> return 3
            "5 - 7 days a week" -> return 4
            else -> 0
        }
    }

    private fun checkAllFields(): Boolean {
        return !(age.text.trim().isEmpty() || height.text.trim().isEmpty() || weight.text.trim().isEmpty() || activity == null || goal == null || diet == null)
    }

    private fun updateHints(){
        if (metric_or_not.isChecked){
            height.hint = "eg. 5.6ft"
            weight.hint = "eg. 165lbs"
        }else{
            height.hint = "eg. 160cm"
            weight.hint = "eg. 75kg"
        }
    }
}
