package app.meal_planner.data.repositories

import app.meal_planner.data.datasources.local.ItemsDao
import app.meal_planner.data.models.ItemEntity
import io.reactivex.Completable
import io.reactivex.Observable

class ItemRepositoryImpl(private val itemsDao: ItemsDao): ItemRepository {

    override fun insertItems(itemEntity: List<ItemEntity>): Completable {
        return itemsDao.insertItems(itemEntity)
    }

    override fun deleteAll(): Completable {
        return itemsDao.deleteAll()
    }

    override fun deleteById(id: Long): Completable {
        return itemsDao.deleteById(id)
    }

}