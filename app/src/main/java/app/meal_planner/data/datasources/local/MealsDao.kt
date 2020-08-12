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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMeal(meal: MealEntity): Single<Long>

    @Transaction
    @Delete
    abstract fun deleteMeal(meal: MealEntity, items: List<ItemEntity>): Completable

    @Query("DELETE FROM meals")
    abstract fun deleteAllMeals(): Completable

    @Update
    abstract fun updateMeal(meal: MealEntity): Completable

    @Transaction
    @Query("SELECT * FROM meals WHERE existing == 0")
    abstract fun getByTodayAndExisting(): List<MealWithItemsEntity>

    @Query("UPDATE meals SET today = 0")
    abstract fun resetTodayToZero(): Completable

    @Query("DELETE FROM items")
    abstract fun deleteAllItems(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertItems(items: List<ItemEntity>): Completable

    @Query("DELETE FROM items WHERE meal_id == :id")
    abstract fun deleteById(id: Long): Completable

    @Transaction
    open fun update(mealWithItemsEntity: MealWithItemsEntity){
        updateMeal(mealWithItemsEntity.mealEntity).blockingAwait()
        deleteById(mealWithItemsEntity.mealEntity.id).blockingAwait()
        insertItems(mealWithItemsEntity.items).blockingAwait()
        getMealsWithItems()
    }

    @Transaction
    open fun insert(mealWithItemsEntity: MealWithItemsEntity){
        val id = insertMeal(mealWithItemsEntity.mealEntity).blockingGet()
        mealWithItemsEntity.items.forEach { it.mealId = id }
        insertItems(mealWithItemsEntity.items).blockingAwait()
    }

    @Transaction
    open fun resetDB(){
        deleteAllMeals().blockingAwait()
        deleteAllItems().blockingAwait()
    }

    @Transaction
    open fun dailyReset(){
        resetTodayToZero().blockingAwait()
        val meals = getByTodayAndExisting()
        meals.forEach { deleteMeal(it.mealEntity, it.items).blockingAwait() }
    }
}