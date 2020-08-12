package app.meal_planner.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class DailyReport(
    val id: Long,
    val kcal: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val date: Date
):Parcelable