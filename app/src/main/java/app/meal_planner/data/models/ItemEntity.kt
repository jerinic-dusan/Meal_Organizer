package app.meal_planner.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "meal_id") var mealId: Long,
    val name: String,
    val carbs: Int,
    val protein: Int,
    val fat: Int
)