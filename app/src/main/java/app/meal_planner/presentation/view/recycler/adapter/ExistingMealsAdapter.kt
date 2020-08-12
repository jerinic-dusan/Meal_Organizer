package app.meal_planner.presentation.view.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import app.meal_planner.R
import app.meal_planner.data.models.MealWithItems
import app.meal_planner.presentation.view.recycler.diff.ExistingMealsDiffItemCallback
import app.meal_planner.presentation.view.recycler.viewholder.ExistingMealsViewHolder

class ExistingMealsAdapter(existingMealsDiffItemCallback: ExistingMealsDiffItemCallback,
                           private val swipeMeal: (MealWithItems) -> Unit,
                           private val deleteMeal: (MealWithItems) -> Unit,
                           private val editMeal: (MealWithItems) -> Unit): ListAdapter<MealWithItems, ExistingMealsViewHolder>(existingMealsDiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExistingMealsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val containerView = layoutInflater.inflate(R.layout.existing_meals_item, parent, false)
        return ExistingMealsViewHolder(containerView, {swipeMeal(getItem(it))}, {deleteMeal(getItem(it))}, {editMeal(getItem(it))})
    }

    override fun onBindViewHolder(holder: ExistingMealsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}