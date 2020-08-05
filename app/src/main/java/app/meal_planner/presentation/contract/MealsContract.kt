package app.meal_planner.presentation.contract

import androidx.lifecycle.LiveData
import app.meal_planner.data.models.ItemEntity
import app.meal_planner.data.models.MealEntity
import app.meal_planner.data.models.MealWithItems
import app.meal_planner.data.models.MealWithItemsEntity
import app.meal_planner.presentation.view.states.MealState

interface MealsContract {

    interface ViewModel{

        val mealState: LiveData<MealState>
        val mealId: LiveData<Long>

        fun getMeals()
        fun insertMeal(mealWithItemsEntity: MealWithItemsEntity)
        fun deleteMeal(mealWithItemsEntity: MealWithItemsEntity)
        fun insertItems(items: List<ItemEntity>)
        fun deleteAllMeals()
        fun deleteAllItems()
        fun delete(meal: MealEntity)
        fun deleteItemsById(id: Long)
        fun updateMeal(meal: MealEntity)
    }
}