package app.meal_planner.data.repositories

import app.meal_planner.data.datasources.local.MealsDao
import app.meal_planner.data.models.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class MealRepositoryImpl(private val mealsDataSource: MealsDao): MealRepository {

    override fun getMeals(): Observable<List<MealWithItems>> {
        return mealsDataSource
            .getMealsWithItems()
            .map {
                it.map {
                    val mutableList = mutableListOf<Item>()
                    it.items.forEach { mutableList.add(Item(it.id, it.mealId, it.name, it.carbs, it.protein, it.fat)) }
                    MealWithItems(
                        Meal(it.mealEntity.id, it.mealEntity.name, it.mealEntity.kcal, it.mealEntity.carbs, it.mealEntity.protein, it.mealEntity.fat, it.mealEntity.today, it.mealEntity.existing, it.mealEntity.date),
                        mutableList.toList()
                    )
                }
            }
    }

    override fun insertMeal(mealEntity: MealEntity): Single<Long> {
        return mealsDataSource.insertMeal(mealEntity)
    }

    override fun deleteMeal(meal: MealEntity, items: List<ItemEntity>): Completable {
        return mealsDataSource.deleteMeal(meal, items)
    }

    override fun deleteAll(): Completable {
        return mealsDataSource.deleteAll()
    }

    override fun delete(meal: MealEntity): Completable {
        return mealsDataSource.updateMeal(meal)
    }

    override fun update(meal: MealEntity): Completable {
        return mealsDataSource.updateMeal(meal)
    }
}

//Timber.e("$it")