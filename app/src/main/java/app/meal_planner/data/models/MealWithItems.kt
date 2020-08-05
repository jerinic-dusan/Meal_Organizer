package app.meal_planner.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MealWithItems(
    val meal: Meal,
    val items: List<Item>
): Parcelable