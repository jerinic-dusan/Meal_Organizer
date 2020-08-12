package app.meal_planner.data.datasources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.meal_planner.data.datasources.local.converters.DateConverter
import app.meal_planner.data.models.DailyReportEntity
import app.meal_planner.data.models.ItemEntity
import app.meal_planner.data.models.MealEntity

@Database(
    entities = [MealEntity::class, ItemEntity::class, DailyReportEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class MainDataBase: RoomDatabase(){
    abstract fun getMealsDao(): MealsDao
    abstract fun getDailyReportDao(): DailyReportDao
}