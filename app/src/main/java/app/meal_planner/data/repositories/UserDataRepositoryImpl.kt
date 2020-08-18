package app.meal_planner.data.repositories

import app.meal_planner.data.datasources.local.SharedPrefInterface
import app.meal_planner.data.models.Data
import app.meal_planner.data.models.UserData
import app.meal_planner.data.models.RemainingData
import io.reactivex.Completable
import io.reactivex.Observable

class UserDataRepositoryImpl(private val sharedPrefInterface: SharedPrefInterface): UserDataRepository {

    override fun setExistingData(data: UserData): Completable {
        return sharedPrefInterface.setExistingData(data)
    }

    override fun setRemainingData(data: RemainingData): Completable {
        return sharedPrefInterface.setRemainingData(data)
    }

    override fun getData(): Observable<Data> {
        return sharedPrefInterface.getData()
    }

}