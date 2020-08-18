package app.meal_planner.data.datasources.local

import app.meal_planner.data.models.Data
import io.reactivex.Observable
import app.meal_planner.data.models.UserData
import app.meal_planner.data.models.RemainingData
import io.reactivex.Completable

interface SharedPrefInterface {

    fun setExistingData(data: UserData): Completable
    fun setRemainingData(data: RemainingData): Completable
    fun getData(): Observable<Data>

}