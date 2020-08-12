package app.meal_planner.data.repositories

import app.meal_planner.data.models.MealEntity
import app.meal_planner.data.models.MealWithItems
import app.meal_planner.data.models.MealWithItemsEntity
import io.reactivex.Completable
import io.reactivex.Observable

interface MealRepository {

    fun getMeals(): Observable<List<MealWithItems>>
    fun insert(mealWithItemsEntity: MealWithItemsEntity): Completable
    fun deleteAll(): Completable
    fun dailyReset(): Completable
    fun update(meal: MealEntity): Completable
    fun updateMeal(mealWithItemsEntity: MealWithItemsEntity): Completable

}