package app.meal_planner.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Meal(
    val id: Long,
    val name: String,
    val kcal: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val today: Boolean = false,
    val existing: Boolean = false,
    val date: Date
): Parcelable