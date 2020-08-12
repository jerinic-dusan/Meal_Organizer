package app.meal_planner.presentation.view.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import app.meal_planner.R
import app.meal_planner.data.models.MealWithItems
import app.meal_planner.presentation.view.recycler.diff.TodaysMealsDiffItemCallback
import app.meal_planner.presentation.view.recycler.viewholder.TodaysMealsViewHolder

class TodaysMealsAdapter(todaysMealsDiffItemCallback: TodaysMealsDiffItemCallback,
                         private val deleteMeal: (MealWithItems) -> Unit,
                         private val editMeal: (MealWithItems) -> Unit): ListAdapter<MealWithItems, TodaysMealsViewHolder>(todaysMealsDiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodaysMealsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val containerView = layoutInflater.inflate(R.layout.todays_meals_item, parent, false)
        return TodaysMealsViewHolder(containerView, {deleteMeal(getItem(it))}, {editMeal(getItem(it))})
    }

    override fun onBindViewHolder(holder: TodaysMealsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}