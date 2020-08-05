package app.meal_planner.data.datasources.local

import androidx.room.*
import app.meal_planner.data.models.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
abstract class MealsDao {

    @Transaction
    @Query("SELECT * FROM meals")
    abstract fun getMealsWithItems(): Observable<List<MealWithItemsEntity>>

    @Transaction
    @Query("SELECT * FROM meals WHERE today == 1")
    abstract fun getTodaysMeals(): Observable<List<MealWithItemsEntity>>

    @Transaction
    @Query("SELECT * FROM meals WHERE existing == 1")
    abstract fun getExistingMeals(): Observable<List<MealWithItemsEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMeal(meal: MealEntity): Single<Long>


    @Transaction
    @Delete
    abstract fun deleteMeal(meal: MealEntity, items: List<ItemEntity>): Completable

    @Query("DELETE FROM meals")
    abstract fun deleteAll(): Completable

    @Delete
    abstract fun delete(mealEntity: MealEntity): Completable


    @Update
    abstract fun updateMeal(meal: MealEntity): Completable

}