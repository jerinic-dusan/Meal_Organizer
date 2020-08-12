package app.meal_planner.data.repositories

import app.meal_planner.data.models.DailyReport
import app.meal_planner.data.models.DailyReportEntity
import io.reactivex.Completable
import io.reactivex.Observable

interface DailyReportRepository {

    fun saveDailyReport(dailyReportEntity: DailyReportEntity): Completable
    fun getLastSevenDays(): Observable<List<DailyReport>>
    fun getLastThirtyDays(): Observable<List<DailyReport>>
    fun deleteOlderReports(): Completable
    fun getTodaysReport(): Observable<DailyReportEntity>
    fun updateTodaysReport(dailyReport: DailyReportEntity): Completable
}