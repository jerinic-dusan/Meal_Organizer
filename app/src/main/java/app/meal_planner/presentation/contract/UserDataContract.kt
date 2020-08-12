package app.meal_planner.presentation.contract

import androidx.lifecycle.LiveData
import app.meal_planner.data.models.UserData
import app.meal_planner.data.models.RemainingData

interface UserDataContract {
    interface ViewModel{
        val exists: LiveData<UserData>
        val remainingData: LiveData<RemainingData>
        val dailyData: LiveData<RemainingData>

        fun getExistingData()
        fun setExistingData(data: UserData)
        fun getRemainingData()
        fun setRemainingData(data: RemainingData)
        fun saveDailyProgress()
        fun setDailyData(data: RemainingData)
        fun getDailyData()
        fun calculateDailyData()
    }
}