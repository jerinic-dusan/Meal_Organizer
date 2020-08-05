package app.meal_planner.data.datasources.local

import io.reactivex.Observable
import app.meal_planner.data.models.UserData
import app.meal_planner.data.models.RemainingData
import io.reactivex.Completable

interface SharedPrefInterface {

    fun getExistingData(): Observable<UserData>
    fun setExistingData(data: UserData): Completable
    fun getRemainingData(): Observable<RemainingData>
    fun setRemainingData(data: RemainingData): Completable

}