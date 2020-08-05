package app.meal_planner.presentation.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import app.meal_planner.R
import app.meal_planner.presentation.contract.MealsContract
import app.meal_planner.presentation.view.activities.AddMealActivity
import app.meal_planner.presentation.viewmodel.MealsViewModel
import kotlinx.android.synthetic.main.fragment_existing_meals.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import app.meal_planner.data.models.Meal
import app.meal_planner.data.models.MealEntity
import app.meal_planner.data.models.MealWithItems
import app.meal_planner.presentation.view.recycler.adapter.ExistingMealsAdapter
import app.meal_planner.presentation.view.recycler.diff.ExistingMealsDiffItemCallback
import app.meal_planner.presentation.view.states.MealState

class ExistingMealsFragment : Fragment(R.layout.fragment_existing_meals) {

    private val mealViewModel: MealsContract.ViewModel by sharedViewModel<MealsViewModel>()
    private lateinit var existingMealsAdapter: ExistingMealsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initListeners()
        initObservers()
        initRecycler()
    }

    private fun initRecycler() {
        recycler_existing_meals.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        existingMealsAdapter = ExistingMealsAdapter(ExistingMealsDiffItemCallback(),
            {
                mealViewModel.updateMeal(MealEntity(it.meal.id, it.meal.name, it.meal.kcal, it.meal.carbs, it.meal.protein, it.meal.fat, true, it.meal.existing, it.meal.date))
                Toast.makeText(context, "Added ${it.meal.name} to today's meals.", Toast.LENGTH_SHORT).show()
            },
            {
                mealViewModel.updateMeal(MealEntity(it.meal.id, it.meal.name, it.meal.kcal, it.meal.carbs, it.meal.protein, it.meal.fat, it.meal.today, false, it.meal.date))
                Toast.makeText(context, "Removed ${it.meal.name} from existing meals.", Toast.LENGTH_SHORT).show()
            },
            {
                //TODO Edit
            })
        recycler_existing_meals.adapter = existingMealsAdapter
    }

    private fun initObservers() {
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
                    if (mealWithItems.meal.existing){
                        list.add(mealWithItems)
                    }
                }
                existingMealsAdapter.submitList(list.toList())
            }
            is MealState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is MealState.Loading -> {
                Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initListeners() {
        add_meal_existing.setOnClickListener {
            val intent = Intent(context, AddMealActivity::class.java)
            intent.putExtra(TodaysMealsFragment.MESSAGE_ADD_MEAL, "Existing_Add")
            startActivity(intent)
        }
    }
}