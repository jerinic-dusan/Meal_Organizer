package app.meal_planner.presentation.view.fragments

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.meal_planner.R
import app.meal_planner.data.models.DailyReportEntity
import app.meal_planner.data.models.MealEntity
import app.meal_planner.data.models.MealWithItems
import app.meal_planner.data.models.RemainingData
import app.meal_planner.presentation.contract.DailyReportContract
import app.meal_planner.presentation.contract.MealsContract
import app.meal_planner.presentation.contract.UserDataContract
import app.meal_planner.presentation.view.activities.AddMealActivity
import app.meal_planner.presentation.view.recycler.adapter.TodaysMealsAdapter
import app.meal_planner.presentation.view.recycler.diff.TodaysMealsDiffItemCallback
import app.meal_planner.presentation.view.states.MealState
import app.meal_planner.presentation.viewmodel.DailyReportViewModel
import app.meal_planner.presentation.viewmodel.MealsViewModel
import app.meal_planner.presentation.viewmodel.UserDataViewModel
import kotlinx.android.synthetic.main.fragment_todays_meals.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class TodaysMealsFragment : Fragment(R.layout.fragment_todays_meals) {

    private val userDataViewModel: UserDataContract.ViewModel by sharedViewModel<UserDataViewModel>()
    private val mealViewModel: MealsContract.ViewModel by sharedViewModel<MealsViewModel>()
    private val dailyReportViewModel: DailyReportContract.ViewModel by sharedViewModel<DailyReportViewModel>()
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
    }

    private fun initRecycler() {
        recycler_todays_meals.layoutManager = LinearLayoutManager(activity)
        todaysMealsAdapter = TodaysMealsAdapter(TodaysMealsDiffItemCallback(),
            {
                mealViewModel.updateMeal(MealEntity(it.meal.id, it.meal.name, it.meal.kcal, it.meal.carbs, it.meal.protein, it.meal.fat, false, it.meal.existing, it.meal.date))
                Toast.makeText(context, "Removed ${it.meal.name} from today's meals.", Toast.LENGTH_SHORT).show()
                Timber.e("${it.meal}")

                val linearLayoutManager = recycler_todays_meals.layoutManager as LinearLayoutManager
                if (linearLayoutManager.getChildAt(0)!!.isVisible && !add_meal.isShown && !reset_meals.isShown) {
                    add_meal.show(false)
                    reset_meals.show(true)
                }

            },{
                val intent = Intent(context, AddMealActivity::class.java)
                intent.putExtra(MESSAGE_ADD_MEAL, "Today_Edit")
                intent.putExtra(MESSAGE_EDIT_MEAL, it)
                startActivity(intent)
            })
        recycler_todays_meals.adapter = todaysMealsAdapter
    }

    private fun initListeners() {
        add_meal.setOnClickListener {
            val intent = Intent(context, AddMealActivity::class.java)
            intent.putExtra(MESSAGE_ADD_MEAL, "Today_Add")
            startActivity(intent)
        }
        recycler_todays_meals.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(dy > 0){
                    reset_meals.hide(false)
                    add_meal.hide(true)
                }else if(dy < 0){
                    add_meal.show(false)
                    reset_meals.show(true)
                }
            }
        })
        reset_meals.setOnClickListener {
            val builder = AlertDialog.Builder(context, R.style.AlertDialogCustom)
            builder.setTitle("Daily Reset.").setMessage("This action will result in a reset of remaining calories and deletion of today's meals. Use when new day arrives and you are ready to eat again.")
                .setCancelable(false).setIcon(R.drawable.ic_warning).setPositiveButton("Reset"){_, _ -> resetMeals()}.setNegativeButton("Cancel"){dialog, _ -> dialog.cancel()}
            val dialog: AlertDialog? = builder.create()
            dialog!!.show()
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

        userDataViewModel.setRemainingData(RemainingData(kcalOld - kcal, carbsOld - carbs, proteinOld - protein, fatOld - fat))
        userDataViewModel.getRemainingData()
    }

    private fun resetMeals() {
        mealViewModel.dailyReset()
        val data = userDataViewModel.dailyData.value
        dailyReportViewModel.getTodaysReport(DailyReportEntity(0, data!!.calories, data.carbohydrates, data.protein, data.fat))
        dailyReportViewModel.deleteOlderReports()
    }

}