package app.meal_planner.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val kcal: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val today: Boolean = false,
    val existing: Boolean = false,
    val date: Date = Date()
)