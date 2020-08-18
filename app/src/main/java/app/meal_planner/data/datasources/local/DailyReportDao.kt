package app.meal_planner.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import app.meal_planner.data.models.DailyReportEntity
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.*

@Dao
abstract class DailyReportDao {

    @Insert
    abstract fun saveDailyReport(dailyReport: DailyReportEntity): Completable

    @Query("DELETE FROM daily_report WHERE date BETWEEN :from AND :to")
    abstract fun deleteOlderReports(from: Date, to: Date): Completable

    @Query("DELETE FROM daily_report")
    abstract fun deleteAll(): Completable

    @Query("SELECT * FROM daily_report WHERE date BETWEEN :from AND :to ")
    abstract fun getDaysReport(from: Date, to: Date): Observable<List<DailyReportEntity>>

    @Query("SELECT * FROM daily_report WHERE date BETWEEN :from AND :to ")
    abstract fun getTodaysReport(from: Date, to: Date): List<DailyReportEntity>

    @Update
    abstract fun updateTodaysReport(dailyReport: DailyReportEntity): Completable

}