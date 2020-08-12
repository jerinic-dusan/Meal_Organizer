package app.meal_planner.presentation.view.recycler.viewholder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.meal_planner.R
import app.meal_planner.data.models.MealWithItems
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.existing_meals_item.*
import kotlinx.android.synthetic.main.existing_meals_item.parent_linear_layout
import kotlinx.android.synthetic.main.ingredient_item_card.view.*

class ExistingMealsViewHolder(override val containerView: View,
                              swipeMeal: (Int) -> Unit,
                              deleteMeal: (Int) -> Unit,
                              editMeal: (Int) -> Unit): RecyclerView.ViewHolder(containerView), LayoutContainer {

    init {
        bring_to_todays_meals.setOnClickListener { swipeMeal(adapterPosition) }
        delete_meal.setOnClickListener { deleteMeal(adapterPosition) }
        edit_meal.setOnClickListener { editMeal(adapterPosition) }
    }

    @SuppressLint("InflateParams")
    fun bind(mealWithItems: MealWithItems){
        meal_title.text = mealWithItems.meal.name
        parent_linear_layout!!.removeAllViews()
        mealWithItems.items.forEach {
            val inflater = LayoutInflater.from(containerView.context)
            val rowView: View = inflater.inflate(R.layout.ingredient_item_card, null)
            rowView.ingredient.text = it.name
            parent_linear_layout!!.addView(rowView, parent_linear_layout!!.childCount)
        }
    }
}