package app.meal_planner.presentation.view.states

import app.meal_planner.data.models.MealWithItems

sealed class MealState {
    object Loading: MealState()
    data class Success(val meals: List<MealWithItems>): MealState()
    data class Error(val message: String): MealState()
}