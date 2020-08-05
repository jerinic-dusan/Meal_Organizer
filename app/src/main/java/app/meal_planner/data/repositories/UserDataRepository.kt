package app.meal_planner.data.repositories

import app.meal_planner.data.models.UserData
import app.meal_planner.data.models.RemainingData
import io.reactivex.Completable
import io.reactivex.Observable

interface UserDataRepository {

    fun getExistingData(): Observable<UserData>
    fun setExistingData(data: UserData): Completable
    fun getRemainingData(): Observable<RemainingData>
    fun setRemainingData(data: RemainingData): Completable

}