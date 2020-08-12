package app.meal_planner.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import app.meal_planner.data.models.DailyReportEntity
import io.reactivex.Completable

@Dao
abstract class DailyReportDao {

    @Insert
    abstract fun saveDailyReport(dailyReport: DailyReportEntity): Completable

    @Query("SELECT * FROM daily_report WHERE date > date('now','-7 day')")
    abstract fun getLastSevenDays(): List<DailyReportEntity>

    @Query("SELECT * FROM daily_report WHERE date > date('now','-30 day')")
    abstract fun getThirtySevenDays(): List<DailyReportEntity>

    @Query("DELETE FROM daily_report WHERE date <= date('now','-30 day')")
    abstract fun deleteOlderReports(): Completable

    @Query("DELETE FROM daily_report")
    abstract fun deleteAll(): Completable

    @Query("SELECT * FROM daily_report WHERE date = date('now')")
    abstract fun getTodaysReport(): List<DailyReportEntity>

    @Update
    abstract fun updateTodaysReport(dailyReport: DailyReportEntity): Completable

}