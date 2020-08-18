package app.meal_planner.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "daily_report")
data class DailyReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val kcal: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    var date: Date = Date()
)