package app.meal_planner.data.repositories


import app.meal_planner.data.datasources.local.DailyReportDao
import app.meal_planner.data.models.DailyReport
import app.meal_planner.data.models.DailyReportEntity
import app.meal_planner.utilities.CalendarUtils
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.*

class DailyReportRepositoryImpl(private val dailyReportDao: DailyReportDao): DailyReportRepository, CalendarUtils {

    override fun saveDailyReport(dailyReportEntity: DailyReportEntity): Completable {
        return dailyReportDao.saveDailyReport(dailyReportEntity)
    }

    override fun getLastSevenDays(): Observable<List<DailyReport>> {
        return dailyReportDao.getDaysReport(calculateDate(-7), calculateDate(0)).map { it.map { DailyReport(it.id, it.kcal, it.carbs, it.protein, it.fat, it.date) } }
    }

    override fun getLastThirtyDays(): Observable<List<DailyReport>> {
        return dailyReportDao.getDaysReport(calculateDate(-30), calculateDate(0)).map { it.map { DailyReport(it.id, it.kcal, it.carbs, it.protein, it.fat, it.date) } }
    }

    override fun deleteOlderReports(): Completable {
        return dailyReportDao.deleteOlderReports(calculateDate(-31),calculateDate(-30))
    }

    override fun getTodaysReport(): Observable<DailyReportEntity> {
        return Observable.fromCallable {
            val list = dailyReportDao.getTodaysReport(calculateDate(-2),calculateDate(0))
            if (list.isEmpty()){
                val dailyReportEntity = DailyReportEntity(-1,0,0,0,0)
                dailyReportEntity
            }else{
                val dailyReportEntity = list.last()
                dailyReportEntity
            }
        }
    }

    override fun updateTodaysReport(dailyReport: DailyReportEntity): Completable {
        return dailyReportDao.updateTodaysReport(dailyReport)
    }

    override fun deleteAll(): Completable {
        return dailyReportDao.deleteAll()
    }

//    private fun calculateDate(days: Int): Date{
//        val calendar = Calendar.getInstance()
//        calendar.add(Calendar.DAY_OF_YEAR, days)
//        return calendar.time
//    }
}