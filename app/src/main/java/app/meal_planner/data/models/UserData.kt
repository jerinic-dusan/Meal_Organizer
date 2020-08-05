package app.meal_planner.data.models

data class UserData(
    val metric: Boolean,
    val age: Int,
    val height: Double,
    val weight: Int,
    val calories: Int,
    val carbohydrates: Int,
    val protein: Int,
    val fat: Int
)