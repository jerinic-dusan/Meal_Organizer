package app.meal_planner.data.models

data class RawData(
    val metric: Boolean,
    val male: Boolean,
    val age: Int,
    val height: Double,
    val weight: Int,
    val activity: Int,
    val goal: Int,
    val diet: Int
)