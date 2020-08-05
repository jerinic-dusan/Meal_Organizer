package app.meal_planner.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.meal_planner.data.models.ItemEntity
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
abstract class ItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertItems(itemEntity: List<ItemEntity>): Completable

    @Query("DELETE FROM items")
    abstract fun deleteAll(): Completable

    @Query("DELETE FROM items WHERE meal_id == :id")
    abstract fun deleteById(id: Long): Completable

}