package app.meal_planner.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    val id: Long,
    val mealId: Long,
    val name: String,
    val carbs: Int,
    val protein: Int,
    val fat: Int
):Parcelable