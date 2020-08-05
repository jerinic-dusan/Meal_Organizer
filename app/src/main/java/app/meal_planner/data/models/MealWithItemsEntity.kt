package app.meal_planner.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class MealWithItemsEntity (
    @Embedded val mealEntity: MealEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "meal_id"
    )
    val items: List<ItemEntity>
)