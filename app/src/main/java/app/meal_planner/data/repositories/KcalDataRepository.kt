package app.meal_planner.data.repositories

import app.meal_planner.data.models.RawData
import app.meal_planner.data.models.UserData

interface KcalDataRepository {

    fun calculateUserData(data: RawData): UserData

}