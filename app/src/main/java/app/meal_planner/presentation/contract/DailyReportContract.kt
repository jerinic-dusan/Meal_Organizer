package app.meal_planner.presentation.contract

import androidx.lifecycle.LiveData
import app.meal_planner.data.models.DailyReportEntity
import app.meal_planner.presentation.view.states.DailyReportState

interface DailyReportContract {

    interface ViewModel{
        val lastSevenDays: LiveData<DailyReportState>
        val lastThirtyDays: LiveData<DailyReportState>

        fun saveDailyReport(dailyReport: DailyReportEntity)
        fun getLastSevenDays()
        fun getLastThirtyDays()
        fun deleteOlderReports()
        fun getTodaysReport(dailyReport: DailyReportEntity)
        fun updateTodaysReport(dailyReport: DailyReportEntity)
        fun deleteAll()
    }
}