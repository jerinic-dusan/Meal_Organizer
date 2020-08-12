package app.meal_planner.presentation.view.recycler.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.meal_planner.data.models.MealWithItems
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.todays_meals_item.*

class TodaysMealsViewHolder(override val containerView: View, deleteMeal: (Int) -> Unit, editMeal: (Int) -> Unit): RecyclerView.ViewHolder(containerView), LayoutContainer {

    init {
        delete_meal.setOnClickListener { deleteMeal(adapterPosition) }
        edit_meal.setOnClickListener { editMeal(adapterPosition) }
    }

    @SuppressLint("SetTextI18n")
    fun bind(meal: MealWithItems){
        meal_title.text = meal.meal.name
        meal_carbs.text = meal.meal.carbs.toString() + "g"
        meal_protein.text = meal.meal.protein.toString() + "g"
        meal_fat.text = meal.meal.fat.toString() + "g"
    }

}