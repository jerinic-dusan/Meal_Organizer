package app.meal_planner.presentation.view.fragments

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.meal_planner.R
import app.meal_planner.data.models.Meal
import app.meal_planner.data.models.MealEntity
import app.meal_planner.data.models.MealWithItems
import app.meal_planner.data.models.RemainingData
import app.meal_planner.presentation.contract.MealsContract
import app.meal_planner.presentation.contract.UserDataContract
import app.meal_planner.presentation.view.activities.AddMealActivity
import app.meal_planner.presentation.view.activities.MainActivity
import app.meal_planner.presentation.view.alarms.ResetDailyMeals
import app.meal_planner.presentation.view.recycler.adapter.TodaysMealsAdapter
import app.meal_planner.presentation.view.recycler.diff.TodaysMealsDiffItemCallback
import app.meal_planner.presentation.view.states.MealState
import app.meal_planner.presentation.viewmodel.MealsViewModel
import app.meal_planner.presentation.viewmodel.UserDataViewModel
import kotlinx.android.synthetic.main.fragment_todays_meals.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.io.Serializable
import java.util.*


class TodaysMealsFragment : Fragment(R.layout.fragment_todays_meals) {

    private val userDataViewModel: UserDataContract.ViewModel by sharedViewModel<UserDataViewModel>()
    private val mealViewModel: MealsContract.ViewModel by sharedViewModel<MealsViewModel>()
    private lateinit var todaysMealsAdapter: TodaysMealsAdapter
    private var alarmMgr: AlarmManager? = null

    companion object{
        const val MESSAGE_ADD_MEAL = "add_meal"
        const val MESSAGE_EDIT_MEAL = "edit_meal"
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun init() {
        initFields()
        initRecycler()
        initObservers()
        initListeners()
        initAlarm()
    }

    private fun initRecycler() {
        recycler_todays_meals.layoutManager = LinearLayoutManager(activity)
        todaysMealsAdapter = TodaysMealsAdapter(TodaysMealsDiffItemCallback(),
            {
                mealViewModel.updateMeal(MealEntity(it.meal.id, it.meal.name, it.meal.kcal, it.meal.carbs, it.meal.protein, it.meal.fat, false, it.meal.existing, it.meal.date))
                Toast.makeText(context, "Removed ${it.meal.name} from today's meals.", Toast.LENGTH_SHORT).show()
            },{
                val intent = Intent(context, AddMealActivity::class.java)
                intent.putExtra(MESSAGE_ADD_MEAL, "Today_Edit")
                intent.putExtra(MESSAGE_EDIT_MEAL, it)
                startActivity(intent)
            })
        recycler_todays_meals.adapter = todaysMealsAdapter
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initAlarm() {
        alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ResetDailyMeals::class.java)
        intent.action = "RESET_DAILY_MEALS"
        intent.putExtra("KEY_RESET_DAILY_MEALS", "Resetting daily meals right now!")
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 5)
            //TODO possible bugs
        }

        Timber.e("Alarm sent")
        alarmMgr?.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    private fun initListeners() {
        add_meal.setOnClickListener {
            val intent = Intent(context, AddMealActivity::class.java)
            intent.putExtra(MESSAGE_ADD_MEAL, "Today_Add")
            startActivity(intent)
        }
    }

    private fun initFields() {
        userDataViewModel.getRemainingData()
        userDataViewModel.getExistingData()
    }

    @SuppressLint("SetTextI18n")
    private fun initObservers() {
        userDataViewModel.remainingData.observe(viewLifecycleOwner, Observer {
            leftToGo.text = it.calories.toString() + " kcal to go!"
            carbs.text = it.carbohydrates.toString() + "g"
            protein.text = it.protein.toString() + "g"
            fat.text = it.fat.toString() + "g"
        })
        mealViewModel.mealState.observe(viewLifecycleOwner, Observer {
            renderState(it)
        })
        mealViewModel.getMeals()
    }

    private fun renderState(state: MealState) {
        when(state){
            is MealState.Success -> {
                var list = mutableListOf<MealWithItems>()
                state.meals.forEach { mealWithItems ->
                    if (mealWithItems.meal.today){
                        list.add(mealWithItems)
                    }
                }
                todaysMealsAdapter.submitList(list.toList())
                calculateRemainingData(list.toList())
            }
            is MealState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is MealState.Loading -> {
                Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateRemainingData(list: List<MealWithItems>) {
        val kcalOld = userDataViewModel.exists.value?.calories
        val carbsOld = userDataViewModel.exists.value?.carbohydrates
        val proteinOld = userDataViewModel.exists.value?.protein
        val fatOld = userDataViewModel.exists.value?.fat

        if(kcalOld == null || carbsOld == null || proteinOld == null || fatOld == null){
            return
        }

        var kcal = 0
        var carbs = 0
        var protein = 0
        var fat = 0

        list.forEach {
            kcal += it.meal.kcal
            carbs += it.meal.carbs
            protein += it.meal.protein
            fat += it.meal.fat
        }

        Timber.e("KcalOld $kcalOld, CarbsOld $carbsOld, ProteinOld $proteinOld, FatOld $fatOld")
        Timber.e("KcalNew $kcal, CarbsNew $carbs, ProteinNew $protein, FatNew $fat")

        userDataViewModel.setRemainingData(RemainingData(kcalOld - kcal, carbsOld - carbs, proteinOld - protein, fatOld - fat))
        userDataViewModel.getRemainingData()
    }

}