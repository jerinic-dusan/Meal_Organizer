package app.meal_planner.presentation.view.states

import app.meal_planner.data.models.DailyReport

sealed class DailyReportState {
    object Loading: DailyReportState()
    data class Success(val lastXDays: List<DailyReport>): DailyReportState()
    data class Error(val message: String): DailyReportState()
}