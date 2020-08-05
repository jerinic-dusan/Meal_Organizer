package app.meal_planner.data.datasources.local

import app.meal_planner.data.models.RawData
import app.meal_planner.data.models.UserData
import io.reactivex.Observable

interface KcalInterface {

    fun calculateUserData(data: RawData): UserData

}