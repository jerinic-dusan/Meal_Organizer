package app.meal_planner.data.repositories

import app.meal_planner.data.models.ItemEntity
import app.meal_planner.data.models.MealEntity
import app.meal_planner.data.models.MealWithItems
import app.meal_planner.data.models.MealWithItemsEntity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface MealRepository {

    fun getMeals(): Observable<List<MealWithItems>>
    fun insertMeal(mealEntity: MealEntity): Single<Long>
    fun deleteMeal(meal: MealEntity, items: List<ItemEntity>): Completable
    fun deleteAll(): Completable
    fun delete(meal: MealEntity): Completable
    fun update(meal: MealEntity): Completable

}