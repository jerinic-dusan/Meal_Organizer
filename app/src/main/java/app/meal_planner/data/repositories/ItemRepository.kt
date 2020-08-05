package app.meal_planner.data.repositories

import app.meal_planner.data.models.ItemEntity
import io.reactivex.Completable
import io.reactivex.Observable

interface ItemRepository {

    fun insertItems(itemEntity: List<ItemEntity>): Completable
    fun deleteAll(): Completable
    fun deleteById(id: Long): Completable

}