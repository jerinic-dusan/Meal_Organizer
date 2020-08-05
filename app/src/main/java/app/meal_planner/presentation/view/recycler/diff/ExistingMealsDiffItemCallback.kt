package app.meal_planner.presentation.view.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import app.meal_planner.data.models.MealWithItems

class ExistingMealsDiffItemCallback: DiffUtil.ItemCallback<MealWithItems>() {

    override fun areItemsTheSame(oldItem: MealWithItems, newItem: MealWithItems): Boolean {
        return oldItem.meal.id == newItem.meal.id
    }

    override fun areContentsTheSame(oldItem: MealWithItems, newItem: MealWithItems): Boolean {
        return oldItem.meal.name == newItem.meal.name
                && oldItem.meal.kcal == newItem.meal.kcal
                && oldItem.meal.carbs == newItem.meal.carbs
                && oldItem.meal.protein == newItem.meal.protein
                && oldItem.meal.fat == newItem.meal.fat
    }
}