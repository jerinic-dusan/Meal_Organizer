package app.meal_planner.presentation.contract

import androidx.lifecycle.LiveData
import app.meal_planner.data.models.RawData
import app.meal_planner.data.models.UserData

interface KcalDataContract {

    interface ViewModel{

        val userData: LiveData<UserData>
        fun calculateUserData(data: RawData)

    }
}