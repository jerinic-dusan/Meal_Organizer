package app.meal_planner.data.repositories

import app.meal_planner.data.models.RawData
import app.meal_planner.data.models.UserData
import io.reactivex.Observable

interface KcalDataRepository {

    fun calculateUserData(data: RawData): UserData

}