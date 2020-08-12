package app.meal_planner.presentation.contract

import androidx.lifecycle.LiveData
import app.meal_planner.data.models.MealEntity
import app.meal_planner.data.models.MealWithItemsEntity
import app.meal_planner.presentation.view.states.MealState

interface MealsContract {

    interface ViewModel{

        val mealState: LiveData<MealState>
        val mealId: LiveData<Long>

        fun getMeals()
        fun insertMeal(mealWithItemsEntity: MealWithItemsEntity)
        fun deleteAll()
        fun updateMeal(meal: MealEntity)
        fun updateMealAndItems(mealWithItemsEntity: MealWithItemsEntity)
        fun dailyReset()

    }
}