package app.meal_planner.data.repositories

import app.meal_planner.data.datasources.local.KcalInterface
import app.meal_planner.data.models.RawData
import app.meal_planner.data.models.UserData
import io.reactivex.Observable

class KcalDataRepositoryImpl(private val kcalInterface: KcalInterface): KcalDataRepository {

    override fun calculateUserData(data: RawData): UserData {
        return kcalInterface.calculateUserData(data)
    }

}