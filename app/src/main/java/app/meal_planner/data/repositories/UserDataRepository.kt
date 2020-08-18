package app.meal_planner.data.repositories

import app.meal_planner.data.models.Data
import app.meal_planner.data.models.UserData
import app.meal_planner.data.models.RemainingData
import io.reactivex.Completable
import io.reactivex.Observable

interface UserDataRepository {

    fun setExistingData(data: UserData): Completable
    fun setRemainingData(data: RemainingData): Completable
    fun getData(): Observable<Data>

}